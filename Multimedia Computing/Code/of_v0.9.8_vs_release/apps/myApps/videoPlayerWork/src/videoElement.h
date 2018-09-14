#pragma once
#include <stdio.h>
#include "ofMain.h"
#include "ofxXmlSettings.h"
#include "EventManager.h"
#include "ofxDatGui.h"
#include "XmlUtils.h"

class videoElement {

public:

	void setup(string url);
	void draw();
	void update();
	string getUrl();
	bool isToggled();
	void newCoordinates(float x, float y, float width, float height);
	void deleteMetadataOverlay();

private:

	float x;
	float y;
	float height;
	float width;

	int playWidth;
	int playHeight;
	int playX;
	int playY;

	ofVideoPlayer movie;
	ofImage repImage;
	string url;

	vector<string> images;

	ofxDatGui* metadataGui;

	clock_t clock;
	clock_t clockDelete;

	int currentImage;
	ofImage playButton;
	
	bool toggle;
};

