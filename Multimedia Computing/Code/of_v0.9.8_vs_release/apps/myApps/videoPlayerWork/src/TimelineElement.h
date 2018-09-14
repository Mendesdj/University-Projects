#pragma once
#include <stdio.h>
#include "ofMain.h"
#include "ofxXmlSettings.h"
#include "EventManager.h"
#include "ofxDatGui.h"
#include "XmlUtils.h"

class TimelineElement {

public:
	void setup(float x, float y, float width, float height, string url);
	void draw();
	void update();
	std::pair <float, float> getXandY();
	void setXandY(std::pair<float, float> p);
	bool isToggled();
	string getUrl();
	void setModifiable(bool b);
	
private:
	float x;
	float y;
	float height;
	float width;

	ofImage repImage;
	string url;
	ofVideoPlayer movie;

	bool toggle;
	clock_t clock;

	bool isModifiable;

};

