
#pragma once

#include "ofMain.h"
#include "ofxDatGui.h"
#include "videoBox.h"
#include "timeline.h"
#include "filters.h"
#include "XmlUtils.h"
#include "generatedTimeline.h"
#include "ofEvents.h"
#include "singleVideoPlay.h"

class ofApp : public ofBaseApp {

	const int GENERATE_TIMELINE_X = 195;
	const int GENERATE_TIMELINE_Y = 347;

	const int MODIFY_TIMELINE_X = 565;
	const int MODIFY_TIMELINE_Y = 735;

	const int BUTTON_WIDTH = 150;
	const int COLOR_RANGE = 15;

public:

	void setup();
	void update();
	void draw();

private:

	// event Handlers
	void playVideo(string & url);
	void goBackEvent(bool & b);
	void onGenerateTimelineEvent(ofxDatGuiButtonEvent e);
	void onModifyTimelineEvent(ofxDatGuiButtonEvent e);

	// Pass the Mouse Released Event
	void mouseReleased(ofMouseEventArgs &mouse);

	// State boolean variable 
	bool initialScreen;
	bool singleVideo;

	// Buttons
	ofxDatGuiButton* generateTimeline;
	ofxDatGuiButton* modifyTimeline;

	// Complex Components (Areas)
	filters f;
	timeline t;
	videoBox vb;
	generatedTimeline gt;
	singleVideoPlay sv;

};

