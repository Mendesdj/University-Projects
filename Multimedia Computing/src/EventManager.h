#pragma once
#include <stdio.h>
#include "ofMain.h"
#include "ofEvents.h"

namespace EventManager {

	extern ofEvent <string> AddVideoEvent;
	extern ofEvent <bool> goBackEvent;
	extern ofEvent <ofMouseEventArgs> mouseReleasedEvent;
	extern ofEvent <std::pair <int, int>> ReorderTimelineEvent;
	extern ofEvent <std::pair <int, int>> ReorderGeneratedTimeline;
	extern ofEvent <string> playVideoEvent;
	extern ofEvent <ofMouseEventArgs> mouseReleasedSV;
	extern ofEvent <vector<string>> RemakeGeneratedTimeline;
}


