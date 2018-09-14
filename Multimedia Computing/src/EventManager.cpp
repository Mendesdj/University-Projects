
#include "EventManager.h"

namespace EventManager {

	// Event to trigger Video Adition to the VideoBox
	ofEvent <string> AddVideoEvent = ofEvent<string>();

	// Event to trigger Revert to The Initial Screen
	ofEvent <bool> goBackEvent = ofEvent<bool>();

	// Events to pass Mouse Releases from ofApp to other classes
	ofEvent <ofMouseEventArgs> mouseReleasedEvent = ofEvent <ofMouseEventArgs>();
	ofEvent <ofMouseEventArgs> mouseReleasedSV = ofEvent <ofMouseEventArgs>();

	// Events to enable reordering of the videos
	ofEvent <std::pair <int, int>> ReorderTimelineEvent = ofEvent <std::pair <int, int>>();
	ofEvent <std::pair <int, int>> ReorderGeneratedTimeline = ofEvent <std::pair <int, int>>();

	// Event to Enable the Single Video Reproduction
	ofEvent <string> playVideoEvent = ofEvent<string>();

	// Event to Remake Generated Timeline after deletes on timeline
	ofEvent <vector<string>> RemakeGeneratedTimeline = ofEvent <vector<string>>();
	
}


