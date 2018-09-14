#pragma once
//
//  VideoBox.hpp
//  VideoPlayer
//
//  Created by Ricardo Esteves on 27/04/18.
//

#include <stdio.h>
#include "ofMain.h"
#include "ofxDatGui.h"
#include "videoElement.h"
#include "ofEvents.h"
#include "EventManager.h"

class videoBox {

	const int NUMBER_OF_MOVIES_HORIZONTAL = 3;
	const int NUMBER_OF_MOVIES_VERTICAL = 3;
	const int MAX_MOVIES = NUMBER_OF_MOVIES_HORIZONTAL * NUMBER_OF_MOVIES_VERTICAL;

	const int VIDEO_BOX_HEIGHT = 400;
	const int VIDEO_BOX_WIDTH = 711;

	const int VIDEO_HEIGHT = 110;
	const int VIDEO_WIDTH = 195;
	//const int VIDEO_BOX_X = 350 + 189;
	const int VIDEO_BOX_X = 540;
	const int VIDEO_BOX_Y = 30;

public:
	void setup();
	void draw();
	void update();

	std::vector<std::string> getAllVideosUrls();

private:

	void keyPress(ofKeyEventArgs& eventArgs);
	void dealWithEvent(string & s);

	vector <videoElement> videoGrid;
	int nMovies;

	float hGap;
	float vGap;
	
};

