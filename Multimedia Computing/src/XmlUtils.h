#pragma once
#include "ofMain.h"
#include "ofxDatGui.h"
#include "EventManager.h"
#include "ofxXmlSettings.h"
#include "ofxCvHaarFinder.h"
#include <opencv2/imgproc/imgproc.hpp>
#include <opencv2/highgui/highgui.hpp>
#include <opencv2/nonfree/features2d.hpp>
#include "ofxCv.h"

class XmlUtils {

public:

	static void processVideo(string url);
	static string getTimeFromVideoURL(string url);
	static float getLuminanceFromVideoURL(string url);
	static vector<string> getImagesFromVideoURL(string url);
	static string getFileNameFromUrl(string url);
	static string getXmlNameFromUrl(string url);
	static string getPngNameFromUrl(string url, int i);
	static int getNFacesFromVideoURL(string url);
	static float getVerticalEdges(string url);
	static float getColorRedBasedFromVideoURL(string url);
	static float getColorGreenBasedFromVideoURL(string url);
	static float getColorBlueBasedFromVideoURL(string url);
	static float getRhythmFromVideoURL(string url);
	static float getTexturesFromVideoURL(string url);
	static bool isBeetweenDate(string url, int startDay, int startMonth, int startYear, int endDay, int endMonth, int endYear);
	static string getTime(int seconds);
	static int getMaxMatches(ofImage query, string url);
	
	static string getFirstFrameImageFromVideoURL(string url);
	static int ProcessObject(ofImage queryImg, ofImage trainImage, string savePath);
	static vector<int> getDateFormatted(string Date);

private:

	static string ProcessDate(string url);
	static float ProcessGabor(ofImage image);
	static std::pair<double,double> ProcessEdges(ofImage toDetect);
	
};
