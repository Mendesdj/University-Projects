#include "generatedTimeline.h"

vector<string> options = { "No Transitions", "Fade" };

void generatedTimeline::setup()
{
	// Load the button icons
	goBackButton.load(GO_BACK_BUTTON);
	playPauseButton.load(PAUSE_BUTTON);
	stopButton.load(STOP_BUTTON);

	// Listener for Mouse Releases to control the video playback or go back to the initial screen
	ofAddListener(EventManager::mouseReleasedEvent, this, &generatedTimeline::dealWithMouseRelease);

	// Listeners for Timeline Reordering or Remaking based on order of the videos or deleted videos on the timeline
	ofAddListener(EventManager::ReorderGeneratedTimeline, this, &generatedTimeline::reorderGTimeline);
	ofAddListener(EventManager::RemakeGeneratedTimeline, this, &generatedTimeline::RemakeGeneratedTimeline);

	// boolean to control if fade is active
	isFading = false;

	myDropdown = new ofxDatGuiDropdown("Transitions", options);
	myDropdown->setTheme(new ofxDatGuiThemeSmoke());
	myDropdown->setPosition(30, 700);
	myDropdown->setWidth(200);
	myDropdown->setStripeVisible(false);

	myDropdown->onDropdownEvent(this, &generatedTimeline::onDropdownEvent);
	
}

// Remake the timeline after delete of videos on the timeline
void generatedTimeline::RemakeGeneratedTimeline(vector<string> & p) {

	playPauseButton.load(PLAY_BUTTON);
	feedTimeline(p);

}

// Control the fade
void generatedTimeline::onDropdownEvent(ofxDatGuiDropdownEvent e)
{
	if (e.child == 1) {
		isFading = true;
	}
	else {
		isFading = false;
	}
}

// Reorder the generatedVideo
void generatedTimeline::reorderGTimeline(std::pair <int, int> & p){

	int index1 = p.first;
	int index2 = p.second;

	// Swap the videos in the timeline and adjust the durations arrays

	std::swap(videos.at(index1), videos.at(index2));
	
	accumulativeDurations.at(index2) = accumulativeDurations.at(index2) + durations.at(index2) - durations.at(index1);

	std::swap(durations.at(index1), durations.at(index2));
	
	// Re-start the video playback (paused) with the new ordering
	video.load(videos[0]);
	currentVideo = 0;
	video.setFrame(0);
	video.play();
	playPauseButton.load(PAUSE_BUTTON);
	video.setPaused(false);

}


void generatedTimeline::dealWithMouseRelease(ofMouseEventArgs &mouse) {
	
	int x = mouse.x;
	int y = mouse.y;

	// Go Back Button
	if (ofDist(x, y, CIRCLE_X_CENTER, CIRCLE_Y_CENTER) <= CIRCLE_RADIUS) {
		bool b = true;
		video.setPaused(true);
		isPaused = true;
		ofNotifyEvent(EventManager::goBackEvent, b);
	}

	// Control Play and Pause
	else if ((x > PLAY_PAUSE_X && x < PLAY_PAUSE_X + BUTTON_SIZE) && (y > PLAY_PAUSE_Y && y < PLAY_PAUSE_Y + BUTTON_SIZE)) {
		if (isPaused) {
			video.setPaused(false);
			playPauseButton.load(PAUSE_BUTTON);
			isPaused = false;
		}
		else {
			video.setPaused(true);
			playPauseButton.load(PLAY_BUTTON);
			isPaused = true;
		}
	}

	// Control Stop 
	else if ((x>STOP_X && x<STOP_X + BUTTON_SIZE) && (y>PLAY_PAUSE_Y && y<PLAY_PAUSE_Y + BUTTON_SIZE)) {
		video.load(videos[0]);
		currentVideo = 0;
		video.setFrame(0);
		video.play();
		video.setPaused(true);
		playPauseButton.load(PLAY_BUTTON);
		isPaused = true;

	}
	

}

// Setup for reproduction of the complete timeline 
void generatedTimeline::feedTimeline(std::vector<string> urls) {

	videos = urls;
	totalFrames = 0;
	currentVideo = 0;
	accumulativeDurations.clear();
	durations.clear();
	totalDuration = 0;
	currentFrame = 0;

	if (videos.size() != 0) {

		// Gather information about each videoDuration and order
		// Fill the arrays that hold the cummulativeDuration and duration for playback slider control and video transitioning
		// Get the total of frames and duration for display of time 
		for (int i = 0; i < videos.size(); i++) {
			video.load(videos[i]);
			durations.push_back(video.getTotalNumFrames());

			accumulativeDurations.push_back(totalFrames);
			totalFrames += video.getTotalNumFrames();
			totalDuration += video.getDuration();

		}

		// Slider for seek functionality
		slider = new ofxDatGuiSlider("", 0, totalFrames, 0);
		slider->setTheme(new ofxDatGuiThemeSmoke());
		slider->setPosition(SLIDER_X, SLIDER_Y);
		slider->setWidth(SLIDER_WIDTH, 0);
		slider->setStripeVisible(false);
		slider->onSliderEvent(this, &generatedTimeline::onSliderEvent);

		video.load(videos[0]);
		video.play();
		video.setPaused(true);
		isPaused = true;

	}
}

// Set the correct video at the correct time based on click of the slider
void generatedTimeline::onSliderEvent(ofxDatGuiSliderEvent e) {

	
	if (e.target->getValue() < durations[0]) {
		video.load(videos[0]);
		currentVideo = 0;
		video.setFrame(e.target->getValue());
		video.play();
	}

	else {
		int i = 1;

		while (i < accumulativeDurations.size() && e.target->getValue() > accumulativeDurations[i] ) {
			i++;
		}

		video.load(videos[i - 1]);
		currentVideo = i - 1;
		video.setFrame(e.target->getValue() - accumulativeDurations[i - 1]);
		video.play();
	}
	
	
}

// Resume reproduction
void generatedTimeline::resumePlaying() {
	video.setPaused(false);
	isPaused = false;
}


void generatedTimeline::draw()
{
	XmlUtils utils;

	ofSetColor(255, 255, 255);
	goBackButton.draw(CIRCLE_X_CENTER - CIRCLE_RADIUS, CIRCLE_Y_CENTER - CIRCLE_RADIUS, CIRCLE_RADIUS * 2, CIRCLE_RADIUS * 2);

	// Only draw if timeline is not empty
	if (totalFrames > 0) {
		
		myDropdown->draw();
		playPauseButton.draw(PLAY_PAUSE_X, PLAY_PAUSE_Y, BUTTON_SIZE, BUTTON_SIZE);
		stopButton.draw(STOP_X, PLAY_PAUSE_Y, BUTTON_SIZE, BUTTON_SIZE);
		slider->draw(); 

		// Control the fade
		if (isFading) {
			int frameDiff = durations.at(currentVideo) - video.getCurrentFrame();
			if (frameDiff < 50) {
				ofSetColor(255, 255, 255, 255 * frameDiff / 50);
			}
			else {
				ofSetColor(255, 255, 255, 255);
			}
		}

		video.draw(VIDEO_PLAYER_X, VIDEO_PLAYER_Y, VIDEO_PLAYER_WIDTH, VIDEO_PLAYER_HEIGHT);

		ofSetHexColor(0x60b9ed);
		float position = currentFrame / totalFrames;
		string time = utils.getTime(totalDuration * position);
		ofDrawBitmapString(time, TIME_ELAPSED_X, TIME_ELAPSED_Y);

	}

}

void generatedTimeline::update()
{

	goBackButton.update();
	playPauseButton.update();

	if (totalFrames > 0) {

		myDropdown->update();
		video.update();

		// Determine if next video should be loaded based on currentFrame
		// Checks for last 2 frames as sometimes there is one frame skip
		if (video.getCurrentFrame()+1 == durations[currentVideo] || video.getCurrentFrame() + 1 == durations[currentVideo] - 1) {

			// if not the last video of the timeline, start currentVideo ++
			if (currentVideo != videos.size() - 1) {

				currentVideo++;

				video.load(videos[currentVideo]);

				// Slider movement with video playback
				slider->setValue(accumulativeDurations.at(currentVideo));
				currentFrame = accumulativeDurations.at(currentVideo);

				video.play();

			}
			// Restart because last video of timeline was played
			else {

				currentVideo = 0;
				video.load(videos[currentVideo]);

				slider->setValue(accumulativeDurations[currentVideo]);
				currentFrame = accumulativeDurations.at(currentVideo);

				video.play();
			}
		}

		// If in middle of video reproduction just adjust the currentFrame (for Time Info) and the slider Value
		else if (video.getCurrentFrame() <= durations[currentVideo]) {
			slider->setValue(accumulativeDurations[currentVideo] + video.getCurrentFrame());
			currentFrame = accumulativeDurations.at(currentVideo) + video.getCurrentFrame() ;
		}

		slider->update();
		stopButton.update();

	}

}
