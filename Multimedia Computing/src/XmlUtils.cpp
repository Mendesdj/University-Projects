#pragma once
#include "XmlUtils.h"


void XmlUtils::processVideo(string url)
{

	string xmlFileName = getXmlNameFromUrl(url);
	ofxXmlSettings settings;

	// If there already exists an xml dont't reprocess the video 
	if (!settings.loadFile(xmlFileName)) {

		// Process Date and store the value
		settings.setValue("metadata:date", ProcessDate(url)); 

		// Load url Video
		ofVideoPlayer movie;
		movie.load(url);
		movie.setPaused(true);

		ofImage firstFrame;
		movie.setFrame(0);
		firstFrame.setFromPixels(movie.getPixels());

		// Capture and save the first Frame
		string firstFramepath = "Thumbnails/" + getFileNameFromUrl(url) + "-FirstFrame.png";
		firstFrame.save(firstFramepath);

		// Store the first Frame location
		settings.setValue("metadata:imageUrl:firstFrame", firstFramepath);

		// Process textures and save the value
		settings.setValue("metadata:gaborFilter", ProcessGabor(firstFrame));


		//Edge Detection Variables
		double edgesX = 0;
		double totalEdges = 0;
		std::pair<double,double> p;

		//Luminance Variables
		float luminance = 0;
		float frameLuminance = 0;
		ofImage image;
		ofColor color;

		//Number of Faces Variables
		ofxCvHaarFinder finder;
		finder.setup("/Filtersfiles/haarcascade_frontalface_default.xml");
		int nFaces = 0;

		//Color Variables
		ofPixels pixels = movie.getPixels();
		int nChannels = movie.getPixelsRef().getNumChannels();
		int widthOfLine = movie.getWidth();
		double reds = 0;
		double greens = 0;
		double blues = 0;
		float meanReds = 0;
		float meanGreens = 0;
		float meanBlues = 0;

		//Rhythm Variables
		float rhythm = 0;
		float meanRhythm = 0;
		float rhythmFrame = 0;
		vector <float> rhythmPerFrame;
		vector <float> rhythmVariations;

		int max = movie.getTotalNumFrames();
		for (int i = 0; i < max; i++) {

			 // Cout progress
			 cout << i+1 << "/" << max << " ";

			 movie.setFrame(i);
			 image.setFromPixels(movie.getPixels());

			 // Edge detection for each frame
			 p = ProcessEdges(image);

			 // Add to Vertical Edges 
			 edgesX += p.first;

			 //Add to TotalEdges (Vertical Edges + Horizontal Edges)
			 totalEdges += p.second;

			 // Calculate number of faces of frame
			 int faces = finder.findHaarObjects(movie.getPixels());

			 // Get the new maximum
			 if (nFaces < faces) {
				 nFaces = faces;

			 }

			 // Set the rhythm of the frame to 0
			 rhythmFrame = 0;

			 frameLuminance = 0;

			 // Go through each pixel
			 for (int j = 0; j < image.getHeight(); j++) {

				 for (int y = 0; y < image.getWidth(); y++) {

					 // Calculate Luminance
					 color = image.getColor(y, j);
					 float light = color.getLightness();
					 frameLuminance += light;

					 // Calculate rhythm 
					 rhythmFrame += light;

					 // Calculate RGB colors 
					 reds    += color.r;
					 greens  += color.g;
					 blues   += color.b;

				 }
			 }
			 //Sum of rhythm of all frames
			 rhythmFrame = rhythmFrame / (image.getWidth() * image.getHeight());
			 rhythmPerFrame.push_back(rhythmFrame);
			 luminance += frameLuminance / (image.getWidth() * image.getHeight());
		}

		// Save average of Vertical Edges
		float ve = edgesX / totalEdges *100;
		settings.setValue("metadata:VerticalEdges", ve);
		
		// Save average luminance
		luminance = luminance / movie.getTotalNumFrames();
		settings.setValue("metadata:luminance", luminance);

		// Save maximum number of Faces detected
		settings.setValue("metadata:nFaces", nFaces);

		// Calculate mean of RGB colors of the Video
		meanReds    = reds  / (movie.getTotalNumFrames() * image.getWidth() * image.getHeight());
		meanGreens  = greens / (movie.getTotalNumFrames() * image.getWidth() * image.getHeight());
		meanBlues   = blues  / (movie.getTotalNumFrames() * image.getWidth() * image.getHeight());

		// Save Mean RGB Colors
		settings.setValue("metadata:meanReds",   meanReds);
		settings.setValue("metadata:meanGreens", meanGreens);
		settings.setValue("metadata:meanBlues",  meanBlues);

		// Calculate Rhythm Variations between each frame (based on luminance)
		for (int i = 1; i < rhythmPerFrame.size(); i++) {

			float variation = std::abs(rhythmPerFrame.at(i - 1) - rhythmPerFrame.at(i));
			rhythmVariations.push_back(variation);
			rhythm += variation;
		}

		// Calculate Mean Rhythm
		meanRhythm = rhythm / ( rhythmPerFrame.size() - 1 );

		settings.setValue("metadata:meanRhythm", meanRhythm);

		// CUT DETECTION
		// The greater the rhythm variation the better the frame is suited to become a "key" Frame
		// initialize original index locations
		vector<size_t> idx(rhythmVariations.size());
		iota(idx.begin(), idx.end(), 0);

		// sort indexes based on comparing values in rhythmVariations
		sort(idx.begin(), idx.end(),
			[&rhythmVariations](size_t i1, size_t i2) {return rhythmVariations[i1] < rhythmVariations[i2]; });


		// For the 5 frames with the greatest variations save the images for moving icons
		for (int i = idx.size()-1, c = 0; c < 5 ; c++, i--) {

			movie.setFrame(idx.at(i));
			image.setFromPixels(movie.getPixels());

			string imagePath = getPngNameFromUrl(url, c);

			image.save(imagePath);

			settings.setValue("metadata:imageUrl" + std::to_string(c) , imagePath);
		}
		

		settings.saveFile(xmlFileName);
	}
}

// Get the date of video with url
string XmlUtils::ProcessDate(string url)
{
	
	ofFile file(url);
	struct tm * timeinfo;
	time_t rawTime;
	rawTime = std::filesystem::last_write_time(file);
	timeinfo = localtime(&rawTime);

	string year, month, day, dateHeader;
	year = std::to_string(timeinfo->tm_year + 1900);
	month = std::to_string(timeinfo->tm_mon + 1);
	day = std::to_string(timeinfo->tm_mday);

	return day + "/" + month + "/" + year;
	
}

// Process Texture, return the avg
float XmlUtils::ProcessGabor(ofImage image) {

	//Gabor Variables
	float sigma = 20.0f;
	float gamma = 0.5f;
	float gaborValue = 0;

	cv::Mat createGabor;
	cv::Mat input_image;
	cv::Mat out_image;
	cv::Size ksize = cv::Size(5, 5);

	// Convert image to cv::Mat
	input_image = ofxCv::toCv(image);

	// get GaborValue with different wavelenghts and orientation
	int iterations = 0;
	for (float lambda = 5.0f; lambda <= 95.0f; lambda += 10.0f) {

		for (float theta = 0.0f; theta <= 2 * (float)CV_PI; theta += 2 * (float)CV_PI / 8.0f) {
			createGabor = cv::getGaborKernel(ksize, sigma, theta, lambda, gamma);
			cv::filter2D(input_image, out_image, -1, createGabor);
			gaborValue += cv::mean(out_image)[0];
			iterations++;

		}

	}

	// return the avg gabor Value
	float meanGaborValue = gaborValue / iterations;
	return meanGaborValue;

}

// return a pair of VerticalEdges Count and Total Edges Count, total edges count is the sum of the count of vertical edges and horizontal edges
std::pair<double,double> XmlUtils::ProcessEdges(ofImage toDetect) {

	cv::Mat src_gray;
	cv::Mat grad;
	int scale = 1;
	int delta = 0;
	int ddepth = CV_16S;

	// Treshold for a pixel to be considered an edge
	// Established 50 based on some testing and some research
	int threshold = 50;

	// convert toDetect Image to cv:Mat
	cv::Mat src = ofxCv::toCv(toDetect);

	// Applu gaussian blur to reduce noise 
	cv::GaussianBlur(src, src, cv::Size(3, 3), 0, 0, cv::BORDER_DEFAULT);

	// convert image to gray
	cv::cvtColor(src, src_gray, CV_BGR2GRAY);

	cv::Mat grad_x, grad_y;
	cv::Mat abs_grad_x, abs_grad_y;

	// Gradient X
	// Scharr Over Sobel due to better performance (almost neglible but still, studies show it's better in some cases)

	cv::Scharr(src_gray, grad_x, ddepth, 1, 0, scale, delta, cv::BORDER_DEFAULT);
	//cv::Sobel(src_gray, grad_x, ddepth, 1, 0, 3, scale, delta, cv::BORDER_DEFAULT);

	// Gradient Y
	cv::Scharr(src_gray, grad_y, ddepth, 0, 1, scale, delta, cv::BORDER_DEFAULT);
	//cv::Sobel(src_gray, grad_y, ddepth, 0, 1, 3, scale, delta, cv::BORDER_DEFAULT);

	cv::convertScaleAbs(grad_x, abs_grad_x);
	cv::convertScaleAbs(grad_y, abs_grad_y);

	//cv::addWeighted(abs_grad_x, 0.5, abs_grad_y, 0.5, 0, grad);

	ofImage newImg;
	ofxCv::toOf(grad_x, newImg);

	ofColor color;
	double vEdges = 0;
	double totalEdges = 0;

	for (int j = 0; j < newImg.getHeight(); j++) {

		for (int y = 0; y < newImg.getWidth(); y++) {

			color = newImg.getColor(y, j);

			// if color value exceeds the treshold it is considered a Vertical Edge
			if ((int)color.r > threshold) {
				vEdges++;
			}
			
		}
	}

	totalEdges += vEdges;

	ofxCv::toOf(grad_y, newImg);

	double hEdges = 0;

	for (int j = 0; j < newImg.getHeight(); j++) {

		for (int y = 0; y < newImg.getWidth(); y++) {

			color = newImg.getColor(y, j);

			if ((int)color.r > threshold) {
				hEdges++;
			}

		}
	}

	totalEdges += hEdges;

	std::pair <double, double> p;
	p = std::make_pair(vEdges, totalEdges);
	return p;
}

string XmlUtils::getTimeFromVideoURL(string url) {

	ofxXmlSettings settings;
	string xmlFileName = getXmlNameFromUrl(url);

	settings.loadFile(xmlFileName);
	return settings.getValue("metadata:date", "");
}

float XmlUtils::getLuminanceFromVideoURL(string url) {

	ofxXmlSettings settings;
	string xmlFileName = getXmlNameFromUrl(url);

	settings.loadFile(xmlFileName);
	return std::stof(settings.getValue("metadata:luminance", ""));
}

string XmlUtils::getFirstFrameImageFromVideoURL(string url) {

	ofxXmlSettings settings;
	string xmlFileName = getXmlNameFromUrl(url);

	settings.loadFile(xmlFileName);
	return (settings.getValue("metadata:imageUrl:firstFrame", ""));

}

vector<string> XmlUtils::getImagesFromVideoURL(string url) {

	ofxXmlSettings settings;
	string xmlFileName = getXmlNameFromUrl(url);

	settings.loadFile(xmlFileName);

	vector<string> toReturn;
	for (int i = 0; i < 5; i++) {
		toReturn.push_back(getPngNameFromUrl(url, i));
	}

	return toReturn;
}

string XmlUtils::getXmlNameFromUrl(string url) {
	ofFile file(url);

	size_t lastindex = file.getFileName().find_last_of(".");
	return "Xml/" + file.getFileName().substr(0, lastindex) + ".xml";
}

string XmlUtils::getFileNameFromUrl(string url) {
	ofFile file(url);

	size_t lastindex = file.getFileName().find_last_of(".");
	return file.getFileName().substr(0, lastindex);
}

string XmlUtils::getPngNameFromUrl(string url, int i) {
	ofFile file(url);

	size_t lastindex = file.getFileName().find_last_of(".");
	return "Thumbnails/" + file.getFileName().substr(0, lastindex) + std::to_string(i) +  ".png";
}

int XmlUtils::getNFacesFromVideoURL(string url) {

	ofxXmlSettings settings;
	string xmlFileName = getXmlNameFromUrl(url);

	settings.loadFile(xmlFileName);
	return std::stof(settings.getValue("metadata:nFaces", ""));
}

float XmlUtils::getVerticalEdges(string url)
{
	ofxXmlSettings settings;
	string xmlFileName = getXmlNameFromUrl(url);

	settings.loadFile(xmlFileName);
	return std::stof(settings.getValue("metadata:VerticalEdges", ""));

}

float XmlUtils::getColorRedBasedFromVideoURL(string url) {

	ofxXmlSettings settings;
	string xmlFileName = getXmlNameFromUrl(url);

	settings.loadFile(xmlFileName);
	return std::stof(settings.getValue("metadata:meanReds", ""));
}

float XmlUtils::getColorGreenBasedFromVideoURL(string url) {

	ofxXmlSettings settings;
	string xmlFileName = getXmlNameFromUrl(url);

	settings.loadFile(xmlFileName);
	return std::stof(settings.getValue("metadata:meanGreens", ""));
}

float XmlUtils::getColorBlueBasedFromVideoURL(string url) {

	ofxXmlSettings settings;
	string xmlFileName = getXmlNameFromUrl(url);

	settings.loadFile(xmlFileName);
	return std::stof(settings.getValue("metadata:meanBlues", ""));
}


float XmlUtils::getRhythmFromVideoURL(string url) {

	ofxXmlSettings settings;
	string xmlFileName = getXmlNameFromUrl(url);

	settings.loadFile(xmlFileName);
	return std::stof(settings.getValue("metadata:meanRhythm", ""));
}

float XmlUtils::getTexturesFromVideoURL(string url) {

	ofxXmlSettings settings;
	string xmlFileName = getXmlNameFromUrl(url);

	settings.loadFile(xmlFileName);
	return std::stof(settings.getValue("metadata:gaborFilter", ""));
}

// returns true if the date of the video in the url is between the parameters date 
bool XmlUtils::isBeetweenDate(string url, int startDay, int startMonth, int startYear, int endDay, int endMonth, int endYear)
{

	string xmlFileName = getXmlNameFromUrl(url);
	ofxXmlSettings settings;
	settings.loadFile(xmlFileName);

	string videoDateString = getTimeFromVideoURL(url);
	vector<int> vec = getDateFormatted(videoDateString);

	int videoDate = (vec.at(2) * 10000) + (vec.at(1) * 100) + vec.at(0);
	int startDate = (startYear * 10000) + (startMonth * 100) + startDay;
	int endDate = (endYear * 10000) + (endMonth * 100) + endDay;

	if (videoDate >= startDate && videoDate <= endDate) {
		return true;
	}
	else {
		return false;
	}

}

// return a vector of ints from a date string ex: "12-8/2015" ==> [12,8,2015]
// string Date can be formed using '-' or '/'
vector<int> XmlUtils::getDateFormatted(string Date) {

	std::string date = Date;
	std::replace(date.begin(), date.end(), '-', ' ');
	std::replace(date.begin(), date.end(), '/', ' ');

	std::stringstream iss(date);

	std::vector<int> dates;

	int n;

	while (iss >> n)
		dates.push_back(n);

	return dates;
}

// get Time string in minutes:seconds given some total seconds
string XmlUtils::getTime(int seconds) {
	
	int outMinutes = seconds / 60;
	int outSeconds = seconds % 60;
		
	char buffer[10];
	snprintf(buffer, sizeof(buffer), "%02d", outMinutes);
	const std::string minutesToString = buffer;

	char buff[10];
	snprintf(buff, sizeof(buff), "%02d", outSeconds);
	const std::string secondsToString = buff;

	return minutesToString + " : " + secondsToString;
}

// Given a queryImg and a trainImage, detect the keypoints matching and save the picture of the matches for visualization
// Return the amount of "good matches between the pictures"
int XmlUtils::ProcessObject(ofImage queryImg, ofImage trainImage, string savePath)
{
	// transform both images in grayscale
	queryImg.setImageType(OF_IMAGE_GRAYSCALE);
	trainImage.setImageType(OF_IMAGE_GRAYSCALE);

	// transform into cv:Mat
	cv::Mat img_1 = ofxCv::toCv(queryImg);
	cv::Mat img_2 = ofxCv::toCv(trainImage);

	// Detect the keypoints with SURF
	int minHessian = 400;
	cv::SurfFeatureDetector detector(minHessian);
	std::vector<cv::KeyPoint> keypoints_1, keypoints_2;
	detector.detect(img_1, keypoints_1);
	detector.detect(img_2, keypoints_2);

	// Compute the descriptors
	cv::SurfDescriptorExtractor extractor;
	cv::Mat descriptors_1, descriptors_2;
	extractor.compute(img_1, keypoints_1, descriptors_1);
	extractor.compute(img_2, keypoints_2, descriptors_2);

	// if one of the images has no descriptors return 0
	if (descriptors_1.empty() || descriptors_2.empty()) {
		return 0;
	}

	// Use a FlannBasedMatcher with K-nearest-neighbours = 2 to determine the 2 best matches for each descriptor point 
	cv::FlannBasedMatcher matcher;
	vector< vector< cv::DMatch> > matches;
	matcher.knnMatch(descriptors_1, descriptors_2, matches, 2);

	// Only "good matches" will be saved 
	// According to Dr.Loe Ratio Test a value around 0.7 should be used 
	// With the images we used, we reduced to 0.6 because 0.7 was giving too many false positives

	std::vector< cv::DMatch > good_matches;

	for (int k = 0; k < std::min(descriptors_2.rows - 1, (int)matches.size()); k++)
	{
		// According to Dr.Loe Ratio Test
		if ((matches[k][0].distance < 0.6*(matches[k][1].distance)) &&
			((int)matches[k].size() <= 2 && (int)matches[k].size()>0))
		{
			good_matches.push_back(matches[k][0]);
		}
	}

	cv::Mat img_matches;
	cv::drawMatches(img_1, keypoints_1, img_2, keypoints_2,
	good_matches, img_matches, cv::Scalar::all(-1), cv::Scalar::all(-1),
	vector<char>(), cv::DrawMatchesFlags::NOT_DRAW_SINGLE_POINTS);

	ofImage newImg;
	ofxCv::toOf(img_matches, newImg);
	// Save the image 
	newImg.save(savePath);

	// Return the amount of "good matches"
	return good_matches.size();

}

// For the query Image compare, with all the representative frames of the video with said url
// and return the maximum number of matches found
int XmlUtils::getMaxMatches(ofImage query, string url)
{
	ofImage scene;
	vector<string> images = getImagesFromVideoURL(url);
	int maxMatches = 0;
	int nMatches;
	string savePath;

	for (int i = 0; i < images.size(); i++) {
		scene.load(images.at(i));
		savePath = "Matches/" + getFileNameFromUrl(images.at(i)) + "Matches" + std::to_string(i) + ".png";

		nMatches = ProcessObject(query, scene, savePath);
		if (nMatches > maxMatches) {
			maxMatches = nMatches;
		}

	}

	return maxMatches;

}


