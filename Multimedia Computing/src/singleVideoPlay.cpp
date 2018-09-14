#include "singleVideoPlay.h"

// Very similar to GeneratedTimeline.cpp but with reduced complexity as there is only one Video to reproduce
void singleVideoPlay::setup()
{
	goBackButton.load(GO_BACK_BUTTON);
	playPauseButton.load(PAUSE_BUTTON);
	stopButton.load(STOP_BUTTON);

	ofAddListener(EventManager::mouseReleasedSV, this, &singleVideoPlay::dealWithMouseRelease);
	isPaused = false;
}


void singleVideoPlay::dealWithMouseRelease(ofMouseEventArgs &mouse) {

	int x = mouse.x;
	int y = mouse.y;


	if (ofDist(x, y, CIRCLE_X_CENTER, CIRCLE_Y_CENTER) <= CIRCLE_RADIUS) {
		bool b = true;
		video.setPaused(true);
		isPaused = true;
		ofNotifyEvent(EventManager::goBackEvent, b);
	}

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
	else if ((x>STOP_X && x<STOP_X + BUTTON_SIZE) && (y>PLAY_PAUSE_Y && y<PLAY_PAUSE_Y + BUTTON_SIZE)) {
		video.setFrame(0);
		video.play();
		video.setPaused(true);
		playPauseButton.load(PLAY_BUTTON);
		isPaused = true;

	}


}

void singleVideoPlay::feedVideo(string url) {

	video.load(url);
	totalFrames = video.getTotalNumFrames();
	duration = video.getDuration();

	slider = new ofxDatGuiSlider("", 0, totalFrames, 0);
	slider->setTheme(new ofxDatGuiThemeSmoke());
	slider->setPosition(SLIDER_X, SLIDER_Y);
	slider->setWidth(SLIDER_WIDTH, 0);
	slider->setStripeVisible(false);
	slider->onSliderEvent(this, &singleVideoPlay::onSliderEvent);

	video.play();
	isPaused = false;

}

void singleVideoPlay::onSliderEvent(ofxDatGuiSliderEvent e) {


	video.setFrame(e.target->getValue());
	video.play();

}


void singleVideoPlay::draw()
{
	XmlUtils utils;

	ofSetColor(255, 255, 255);
	goBackButton.draw(CIRCLE_X_CENTER - CIRCLE_RADIUS, CIRCLE_Y_CENTER - CIRCLE_RADIUS, CIRCLE_RADIUS * 2, CIRCLE_RADIUS * 2);

	if (totalFrames > 0) {
		video.draw(VIDEO_PLAYER_X, VIDEO_PLAYER_Y, VIDEO_PLAYER_WIDTH, VIDEO_PLAYER_HEIGHT);
		playPauseButton.draw(PLAY_PAUSE_X, PLAY_PAUSE_Y, BUTTON_SIZE, BUTTON_SIZE);
		stopButton.draw(STOP_X, PLAY_PAUSE_Y, BUTTON_SIZE, BUTTON_SIZE);
		slider->draw();

		ofSetHexColor(0x60b9ed);
		float position = video.getCurrentFrame() / totalFrames;
		string time = utils.getTime(duration * position);
		ofDrawBitmapString(time, TIME_ELAPSED_X, TIME_ELAPSED_Y);

	}

}

void singleVideoPlay::update()
{

	goBackButton.update();
	playPauseButton.update();

		if (totalFrames > 0) {

			video.update();
			slider->setValue(video.getCurrentFrame());

		}

		slider->update();
		stopButton.update();

	}



