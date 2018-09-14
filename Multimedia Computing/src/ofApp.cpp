#include "ofApp.h"

//--------------------------------------------------------------
void ofApp::setup() {

	// two booleans control the Screen to show
	// There are three possible screens
	// MainScreen (with Filters / VideoBox / Timeline) ==> initialScreen = true; singleVideo = false;
	// SingleVideo reproduction ==> initialScreen = false; singleVideo = true;
	// Generated Timeline reproduction ==> initialScreen = false; singleVideo = false;

	initialScreen = true;
	singleVideo = false;

	//------------- Areas Setup -----------------

	// Filter UI
	f.setup();

	// Timeline 
	t.setup();

	// VideoBox 
	vb.setup();

	// Second Screen
	gt.setup();

	// Single Video Screen
	sv.setup();

	//------------- Areas Setup -----------------

	// Setup for the button that feeds the Timeline from the videos in the videoBox
	generateTimeline = new ofxDatGuiButton("Generate Timeline");
	generateTimeline->setTheme(new ofxDatGuiThemeSmoke());
	generateTimeline->setPosition(GENERATE_TIMELINE_X, GENERATE_TIMELINE_Y);
	generateTimeline->setWidth(BUTTON_WIDTH);
	generateTimeline->setStripeVisible(false);
	generateTimeline->setLabelAlignment(ofxDatGuiAlignment::CENTER);

	// Setup for the button that changes to the GeneratedTimeline Reproduction screen
	modifyTimeline = new ofxDatGuiButton("Modify Timeline");
	modifyTimeline->setTheme(new ofxDatGuiThemeSmoke());
	modifyTimeline->setPosition(MODIFY_TIMELINE_X, MODIFY_TIMELINE_Y);
	modifyTimeline->setWidth(BUTTON_WIDTH);
	modifyTimeline->setStripeVisible(false);
	modifyTimeline->setLabelAlignment(ofxDatGuiAlignment::CENTER);

	// Listener so the ofApp knows when to change screens 
	ofAddListener(EventManager::goBackEvent, this, &ofApp::goBackEvent);
	ofAddListener(EventManager::playVideoEvent, this, &ofApp::playVideo);

	// Assigning the handler functions to the buttons
	generateTimeline->onButtonEvent(this, &ofApp::onGenerateTimelineEvent);
	modifyTimeline->onButtonEvent(this, &ofApp::onModifyTimelineEvent);

}

// SingleVideo Reproduction Listener
void ofApp::playVideo(string & url) {

	initialScreen = false;
	// hide filters that are auto drawn
	f.setGuiAutoDraw(false);
	// feed the url to Single Video Reproduction
	sv.feedVideo(url);
	singleVideo = true;

}

// Pass the MouseRelease Events
void ofApp::mouseReleased(ofMouseEventArgs &mouse) {

	if (!initialScreen) {
		if (singleVideo) {
			ofNotifyEvent(EventManager::mouseReleasedSV, mouse);
		}
		else {
			ofNotifyEvent(EventManager::mouseReleasedEvent, mouse);
		}
		
	}

}


// Event to redraw the main screen
void ofApp::goBackEvent(bool & b) {

	initialScreen = true;
	f.setGuiAutoDraw(true);
	t.modifiable(false);
	singleVideo = false;

}


// Event to handle the clicking of the Generate Timeline Button
void ofApp::onGenerateTimelineEvent(ofxDatGuiButtonEvent e)
{

	XmlUtils utils;
	// Get the videos from the videoBox
	std::vector<std::string> allVideos = vb.getAllVideosUrls();
	std::vector<std::string> toDelete;
	
	float lowLimit, highLimit;
	float videoLuminance, sliderLuminance, rangeLuminance;
	int videoNFaces, sliderNFaces, rangeNFaces;
	float videoVEdges, sliderVEdges, rangeVEdges;
	float videoRhythm, sliderRhythm, rangeRhythm;
	float videoTexture, sliderTexture, rangeTexture;

	// Apply all active Filters to create the timeline

	// Luminance Filter
	// If active -> apply filter
	if (f.getToggleValue(luminance)) {

		for (int i = 0; i < allVideos.size(); i++) {

			// Get luminance from currentVideo
			videoLuminance = utils.getLuminanceFromVideoURL(allVideos[i]);

			// Get Value of Luminance in the slider
			sliderLuminance = f.getLuminanceValue();

			// Get Value of the Luminance Range
			rangeLuminance = f.getLuminanceRange();
			lowLimit = sliderLuminance - rangeLuminance;
			highLimit = sliderLuminance + rangeLuminance;

			// If luminance of video is within limits keep it, otherwise add toDelete
			if (videoLuminance < lowLimit || videoLuminance > highLimit) {
				toDelete.push_back(allVideos[i]);
			}

		}

		// After every filter, delete the videos that were excluded by said filter to avoid recomputation on deleted videos on the following filters
		for (int i = 0; i < toDelete.size(); i++) {
			allVideos.erase(std::remove(allVideos.begin(), allVideos.end(), std::string(toDelete[i])), allVideos.end());
		}
		toDelete.clear();

	}

	// Number of Faces Filter
	if (f.getToggleValue(faces)) {

		for (int i = 0; i < allVideos.size(); i++) {

			videoNFaces = utils.getNFacesFromVideoURL(allVideos[i]);
			sliderNFaces = f.getFacesNumber();
			rangeNFaces = (int) f.getFaceRange();
			lowLimit = sliderNFaces - rangeNFaces;
			highLimit = sliderNFaces + rangeNFaces;

			if (videoNFaces < lowLimit || videoNFaces > highLimit) {
				toDelete.push_back(allVideos[i]);
			}
		}

		for (int i = 0; i < toDelete.size(); i++) {
			allVideos.erase(std::remove(allVideos.begin(), allVideos.end(), std::string(toDelete[i])), allVideos.end());
		}
		toDelete.clear();

	}

	// Edge Distribution Filter

	if (f.getToggleValue(edges)) {

		for (int i = 0; i < allVideos.size(); i++) {

			videoVEdges = utils.getVerticalEdges(allVideos[i]);
			sliderVEdges = f.getVerticalEdges();
			rangeVEdges = f.getEdgesRange();
			lowLimit = sliderVEdges - rangeVEdges;
			highLimit = sliderVEdges + rangeVEdges;

			if (videoVEdges < lowLimit || videoVEdges > highLimit) {
				toDelete.push_back(allVideos[i]);
			}
			
		}

		for (int i = 0; i < toDelete.size(); i++) {
			allVideos.erase(std::remove(allVideos.begin(), allVideos.end(), std::string(toDelete[i])), allVideos.end());
		}
		toDelete.clear();

	}

	// Rhythm Filter
	if (f.getToggleValue(rhythm)) {

		for (int i = 0; i < allVideos.size(); i++) {

			videoRhythm = utils.getRhythmFromVideoURL(allVideos[i]);
			sliderRhythm = f.getRhythmValue();
			rangeRhythm = f.getRhythmRange();
			lowLimit = sliderRhythm - rangeRhythm;
			highLimit = sliderRhythm + rangeRhythm;

			if (videoRhythm < lowLimit || videoRhythm > highLimit) {
				toDelete.push_back(allVideos[i]);
			}
		}

		for (int i = 0; i < toDelete.size(); i++) {
			allVideos.erase(std::remove(allVideos.begin(), allVideos.end(), std::string(toDelete[i])), allVideos.end());
		}
		toDelete.clear();

	}

	// Gabor - Texture Filter
	if (f.getToggleValue(texture)) {

		for (int i = 0; i < allVideos.size(); i++) {

			videoTexture = utils.getTexturesFromVideoURL(allVideos[i]);
			sliderTexture = f.getTextureValue();
			rangeTexture = f.getTextureRange();
			lowLimit = sliderTexture - rangeTexture;
			highLimit = sliderTexture + rangeTexture;

			if (videoTexture < lowLimit || videoTexture > highLimit) {
			toDelete.push_back(allVideos[i]);
			}
			
		}

		for (int i = 0; i < toDelete.size(); i++) {
			allVideos.erase(std::remove(allVideos.begin(), allVideos.end(), std::string(toDelete[i])), allVideos.end());
		}
		toDelete.clear();

	}

	// Color Based Filter
	if (f.getToggleValue(color)) {

		for (int i = 0; i < allVideos.size(); i++) {

			int colorBasedRed = utils.getColorRedBasedFromVideoURL(allVideos[i]);
			int colorBasedGreen = utils.getColorGreenBasedFromVideoURL(allVideos[i]);
			int colorBasedBlue = utils.getColorBlueBasedFromVideoURL(allVideos[i]);
			ofColor colorPicker = f.getColorValue();
			int red, green, blue;
			red = (int)colorPicker.r;
			green = (int)colorPicker.g;
			blue = (int)colorPicker.b;

			int lowRed = red - COLOR_RANGE;
			int highRed = red + COLOR_RANGE;
			int lowGreen = green - COLOR_RANGE;
			int highGreen = green + COLOR_RANGE;
			int lowBlue = blue - COLOR_RANGE;
			int highBlue = blue + COLOR_RANGE;

			if (colorBasedRed < lowRed || colorBasedRed > highRed || 
				colorBasedGreen < lowGreen || colorBasedGreen > highGreen ||
				colorBasedBlue < lowBlue || colorBasedBlue > highBlue) 
			{
				
				toDelete.push_back(allVideos[i]);	
				
			}
		}

		for (int i = 0; i < toDelete.size(); i++) {
			allVideos.erase(std::remove(allVideos.begin(), allVideos.end(), std::string(toDelete[i])), allVideos.end());
		}
		toDelete.clear();

	}

	// Date Filtering
	if (f.getToggleValue(date)) {

		vector<int> dateInts = f.getDateValues();

		if (dateInts.size() == 6) {

			for (int i = 0; i < allVideos.size(); i++) {

				if (!utils.isBeetweenDate(allVideos[i], dateInts.at(0), dateInts.at(1), dateInts.at(2), dateInts.at(3), dateInts.at(4), dateInts.at(5))) {
					toDelete.push_back(allVideos[i]);
				}
			}

			for (int i = 0; i < toDelete.size(); i++) {
				allVideos.erase(std::remove(allVideos.begin(), allVideos.end(), std::string(toDelete[i])), allVideos.end());
			}
			toDelete.clear();

		}

	}

	// If object filtering is toggled
	if (f.getToggleValue(objects)) {

		// If there is a valid object loaded
		if (f.isObjectLoaded()) {

			ofImage query = f.getObjectImage();
			int objectsInput = f.getKeypointsNumber();
			int objectsRange = f.getObjectsRange();
			lowLimit = objectsInput - objectsRange;
			highLimit = objectsInput + objectsRange;

				for (int i = 0; i < allVideos.size(); i++) {

					// get max number of keypoint matching on video i with the query object
					int m = utils.getMaxMatches(query, allVideos.at(i));
					if (m < lowLimit || m > highLimit) {
						toDelete.push_back(allVideos[i]);
					}

				}

		for (int i = 0; i < toDelete.size(); i++) {
			allVideos.erase(std::remove(allVideos.begin(), allVideos.end(), std::string(toDelete[i])), allVideos.end());
		}
		toDelete.clear();

		}

	}
	
	// Feed the timeline and the generatedTimeline after the filter application
	t.feedTimeline(allVideos);
	gt.feedTimeline(allVideos);

}


void ofApp::onModifyTimelineEvent(ofxDatGuiButtonEvent e)
{
	initialScreen = false;
	f.setGuiAutoDraw(false);
	gt.resumePlaying();
	t.modifiable(true);
}

// Draw according to the boolean variables that dictate the screen to show
void ofApp::draw() {

	if (initialScreen) {
		ofBackground(83, 83, 83);
		modifyTimeline->draw();
		generateTimeline->draw();
		vb.draw();
		f.draw();
		t.draw();
	}
	else if (singleVideo){

		sv.draw();

	}
	else {
		gt.draw();
		t.draw();
	}

	

}

void ofApp::update() {

	if (initialScreen) {
		modifyTimeline->update();
		generateTimeline->update();
		f.update();
		vb.update();
		t.update();
	}
	else if (singleVideo) {

		sv.update();

	}
	else {
		gt.update();
		t.update();
	}
}

