#pragma once

#include <stdio.h>
#include "ofxDatGui.h"
#include "EventManager.h"
#include "TimelineElement.h"
#include "switchElement.h"


class timeline {

	const int TIMELINE_X = 30;
	const int TIMELINE_Y = 520;
	const int TIMELINE_HEIGHT = 160;
	const int TIMELINE_WIDTH = 1220;
	const int SAMPLE_HEIGHT = 100;
	const int ELEMENT_Y = 600;

	const int MAX_VIDEO_HEIGHT = 110;
	const int MAX_VIDEO_WIDTH = 195;

public:
	void setup();
	void draw();
	void update();
	void feedTimeline(std::vector<std::string> input);
	void modifiable(bool b);

private:

	void keyPress(ofKeyEventArgs& eventArgs);
	void reorder(std::pair <int, int> & p);
	std::vector<TimelineElement> videos;
	bool isModifiable;
	vector<switchElement> switches;
};

