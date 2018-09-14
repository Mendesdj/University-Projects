#include "videoBox.h"

void videoBox::setup()
{
	// Calculate gaps based on defined video sizes and number
	hGap = (VIDEO_BOX_WIDTH - NUMBER_OF_MOVIES_HORIZONTAL *  VIDEO_WIDTH) / (NUMBER_OF_MOVIES_HORIZONTAL + 1);
	vGap = (VIDEO_BOX_HEIGHT - NUMBER_OF_MOVIES_VERTICAL *  VIDEO_HEIGHT) / (NUMBER_OF_MOVIES_VERTICAL + 1);

	nMovies = 0;
	ofAddListener(EventManager::AddVideoEvent, this, &videoBox::dealWithEvent);
	ofAddListener(ofGetWindowPtr()->events().keyPressed, this, &videoBox::keyPress);
}

// Listener for Deletes on VideoBox
void videoBox::keyPress(ofKeyEventArgs& eventArgs) {
	if (eventArgs.keycode == 261) {

		vector<int> videosToDelete;

		// Collect the toggled videos 
		for (int i = 0; i < videoGrid.size(); i++) {
			if (videoGrid.at(i).isToggled()) {
				videosToDelete.push_back(i);
			}
		}

		if (videosToDelete.empty()) {
			return;
		}

		// Remove toggled from videoGrid
		for (int i = 0; i < videosToDelete.size(); i++) {
		
			videoGrid.at(videosToDelete.at(videosToDelete.size() - 1 - i)).deleteMetadataOverlay();
			videoGrid.erase(videoGrid.begin() + videosToDelete.at(videosToDelete.size()-1-i));
			nMovies--;
		}

		// Readjust remaining videos positions
		for (int i = 0; i < videoGrid.size(); i++) {

			int line = i / NUMBER_OF_MOVIES_HORIZONTAL;
			int column = i % NUMBER_OF_MOVIES_HORIZONTAL;
			int newX = VIDEO_BOX_X + ((column + 1) * hGap + column * VIDEO_WIDTH);
			int newY = VIDEO_BOX_Y + ((line + 1) * vGap + line * VIDEO_HEIGHT);

			videoGrid.at(i).newCoordinates(newX, newY, VIDEO_WIDTH, VIDEO_HEIGHT);

		}

	}
	
}

// Handler for adding a new Video (Triggered by the Add File Button)
void videoBox::dealWithEvent(string & s) {

	if (nMovies < MAX_MOVIES) {

		int line = nMovies / NUMBER_OF_MOVIES_HORIZONTAL;
		int column = nMovies % NUMBER_OF_MOVIES_HORIZONTAL;

		int newX = VIDEO_BOX_X + ((column + 1) * hGap + column * VIDEO_WIDTH);
		int newY = VIDEO_BOX_Y + ((line + 1) * vGap + line * VIDEO_HEIGHT);
		videoElement newVideo = videoElement();
		newVideo.setup(s);
		newVideo.newCoordinates(newX, newY, VIDEO_WIDTH, VIDEO_HEIGHT);
		videoGrid.push_back(newVideo);
			
		nMovies ++;
	}
	
}

// Get allVideos in the videoBox (used for timeline Creation)
std::vector<std::string> videoBox::getAllVideosUrls() {

	std::vector<std::string> allVideos;

	for (int i = 0; i < nMovies; i++) {
		allVideos.push_back(videoGrid.at(i).getUrl());
	}

	return allVideos;
}


void videoBox::draw()
{

	ofSetHexColor(0x60b9ed);
	ofNoFill();
	ofDrawRectangle(VIDEO_BOX_X, VIDEO_BOX_Y, VIDEO_BOX_WIDTH, VIDEO_BOX_HEIGHT);

	bool isAnyToggle = false;
	for (int i = 0; i < nMovies; i++) {

		videoGrid.at(i).draw();
		if (isAnyToggle || videoGrid.at(i).isToggled()) {
			isAnyToggle = true;

			ofSetHexColor(0x60b9ed);
			ofDrawBitmapString("Press Delete on Keyboard to delete selected videos", 700, 20);
		}
	}

}

void videoBox::update() {

	for (int i = 0; i < nMovies; i++) {

		videoGrid.at(i).update();
	}
}
