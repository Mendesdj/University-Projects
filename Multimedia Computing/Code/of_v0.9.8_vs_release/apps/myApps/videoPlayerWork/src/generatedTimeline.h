#pragma once
#include "ofMain.h"
#include "ofxDatGui.h"
#include "EventManager.h"
#include "ofEvents.h"
#include "XmlUtils.h"

class generatedTimeline {

	const int CIRCLE_X_CENTER = 187;
	const int CIRCLE_Y_CENTER = 93;
	const int CIRCLE_RADIUS = 30;

	// Icon Locations
	const string GO_BACK_BUTTON = "icons/GoBack.png";
	const string PAUSE_BUTTON = "icons/pause.png";
	const string PLAY_BUTTON = "icons/play.png";
	const string STOP_BUTTON = "icons/stop.png";
	const string AUDIO_BUTTON = "icons/audio.png";

	const int VIDEO_PLAYER_WIDTH = 533;
	const int VIDEO_PLAYER_HEIGHT = 300;
	const int VIDEO_PLAYER_X = 374;
	const int VIDEO_PLAYER_Y = 63;

	const int BUTTON_SIZE = 26;
	const int GAP = 2;

	const int PLAY_PAUSE_X = VIDEO_PLAYER_X;
	const int PLAY_PAUSE_Y = VIDEO_PLAYER_Y + VIDEO_PLAYER_HEIGHT ;

	const int STOP_X = PLAY_PAUSE_X + BUTTON_SIZE + GAP;

	const int SLIDER_Y = PLAY_PAUSE_Y;
	const int SLIDER_X = STOP_X + BUTTON_SIZE + GAP;

	const int SLIDER_WIDTH = 420;

	const int TIME_ELAPSED_X = SLIDER_X + SLIDER_WIDTH + GAP;
	const int TIME_ELAPSED_Y = PLAY_PAUSE_Y + 17;


public:

	void setup();
	void draw();
	void update();

	void feedTimeline(std::vector<string> urls);
	void resumePlaying();

private:

	void onDropdownEvent(ofxDatGuiDropdownEvent e);
	void dealWithMouseRelease(ofMouseEventArgs &mouse);
	void onSliderEvent(ofxDatGuiSliderEvent e);
	void reorderGTimeline(std::pair <int, int> & p);
	void RemakeGeneratedTimeline(vector<string> & p);

	ofVideoPlayer video;
	std::vector<string> videos;
	std::vector<int> durations;
	std::vector<int> accumulativeDurations;
	float totalFrames;
	int currentVideo;

	ofxDatGuiSlider* slider;
	ofxDatGuiDropdown* myDropdown;

	ofImage goBackButton;
	ofImage playPauseButton;
	ofImage stopButton;

	bool isPaused;
	bool isFading;

	float totalDuration;
	float currentFrame;
};
