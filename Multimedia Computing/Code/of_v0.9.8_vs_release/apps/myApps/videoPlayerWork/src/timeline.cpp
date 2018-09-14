#include "timeline.h"


void timeline::setup()
{
	isModifiable = false;
	ofAddListener(EventManager::ReorderTimelineEvent, this, &timeline::reorder);
	ofAddListener(ofGetWindowPtr()->events().keyPressed, this, &timeline::keyPress);
}

// Detect presses of the Delete Key
void timeline::keyPress(ofKeyEventArgs& eventArgs) {

	if (isModifiable) {
		if (eventArgs.keycode == 261) {

			vector<int> videosToDelete;

			// See if the videos are toggled 
			for (int i = 0; i < videos.size(); i++) {
				if (videos.at(i).isToggled()) {
					videosToDelete.push_back(i);
				}
			}

			if (videosToDelete.empty()) {
				return;
			}

			// Delete all toggled videos
			for (int i = 0; i < videosToDelete.size(); i++) {

				videos.erase(videos.begin() + videosToDelete.at(videosToDelete.size() - 1 - i));
				
			}

			vector<string> newVideos;
			for (int i = 0; i < videos.size(); i++) {
				newVideos.push_back(videos.at(i).getUrl());
			}

			// Remake the timeline with the remaining videos
			feedTimeline(newVideos);

			// Remake the videoReproduction class with the remaining videos
			ofNotifyEvent(EventManager::RemakeGeneratedTimeline, newVideos);

			// Allow modifications on remaining videos
			for (int i = 0; i < videos.size(); i++) {
				videos.at(i).setModifiable(true);
			}
		}
		
	}
	
}

// Reorder 2 videos (with the indexes stored in the pair p)
void timeline::reorder(std::pair <int, int> & p) {
	int index1 = p.first;
	int index2 = p.second;

	// swap their positions
	std::pair <float, float> pair1 = videos.at(index1).getXandY();
	std::pair <float, float> pair2 = videos.at(index2).getXandY();
	videos.at(index1).setXandY(pair2);
	videos.at(index2).setXandY(pair1);
	
	// swap them in the vector
	std::swap(videos.at(index1), videos.at(index2));

}

// When the timeline is modifiable, switches are visible and videos are deletable
void timeline::modifiable(bool b) {
	isModifiable = b;

	for (int i = 0; i < switches.size(); i++) {
		switches.at(i).changeActiveState(b);
	}

	for (int i = 0; i < videos.size(); i++) {
		videos.at(i).setModifiable(b);
	}

}

// Create the timeline with the videos in the input
void timeline::feedTimeline(std::vector<std::string> input) {

	videos.clear();
	switches.clear();

	// Start with a fixed size
	int videoWidth = MAX_VIDEO_WIDTH;
	int videoHeight = MAX_VIDEO_HEIGHT;
	int hGap = videoWidth / 10;
	int totalWidth = input.size() * videoWidth + (input.size() + 1) * (hGap);

	// Shrink by 20% until it fits the timeline 
	while (totalWidth >= TIMELINE_WIDTH) {
		videoWidth = videoWidth * 0.8;
		videoHeight = videoHeight * 0.8;
		hGap = videoWidth / 10;
		totalWidth = input.size() * videoWidth + (input.size() + 1) * (hGap);
	}

	// Recalculate the gaps between videos and borders
	hGap = (TIMELINE_WIDTH - (input.size()*videoWidth)) / (input.size() + 1);
	int vGap = (TIMELINE_HEIGHT - videoHeight) / 2;

	// Get Switches size based on video size
	int switchHeight = videoHeight / 3;
	int switchWidth = hGap / 2;
	int switchHGap = switchWidth / 2;
	int switchVGap = switchHeight / 2;

	// Get the position of each element, instantiate it and add it to the vector 
	for (int i = 0; i < input.size(); i++) {
		int newX = TIMELINE_X + (i+1)*hGap + (i*videoWidth);
		int newY = TIMELINE_Y + vGap;
		TimelineElement newVideo = TimelineElement();
		newVideo.setup(newX, newY, videoWidth, videoHeight, input[i]);
		videos.push_back(newVideo);

		// Create a switch in between each video for reordering purposes
		if (i != input.size() - 1) {
			switchElement sw = switchElement();

			int switchX = newX + videoWidth + switchHGap;
			int switchY = newY + switchHeight;
			sw.setup(i, i + 1, switchX, switchY, switchWidth, switchHeight);

			switches.push_back(sw);
		}

	}


}

void timeline::draw()
{

	ofSetHexColor(0x60b9ed);
	ofNoFill();
	ofDrawRectangle(TIMELINE_X, TIMELINE_Y, TIMELINE_WIDTH, TIMELINE_HEIGHT);

	// Draw the switches
	for (int i = 0; i < switches.size(); i++) {
		switches.at(i).draw();
	}

	// Draw the videos on the timeline
	for (int i = 0; i < videos.size(); i++) {
		videos.at(i).draw();

		// if modifiable
		if (isModifiable) {
			bool isAnyToggle = false;

			// if any video is toggled show Delete message
			if (isAnyToggle || videos.at(i).isToggled()) {
				isAnyToggle = true;

				ofSetHexColor(0x60b9ed);
				ofDrawBitmapString("Press Delete on Keyboard to delete selected videos", 443, 505);
			}
		}
	
	}

}

void timeline::update() {

	
	for (int i = 0; i < videos.size(); i++) {

		videos.at(i).update();

	}

	for (int i = 0; i < switches.size(); i++) {
		switches.at(i).update();
	}

}


