
#include "TimelineElement.h"

void TimelineElement::setup(float x, float y, float width, float height, string url)
{

	this->x = x;
	this->y = y;
	this->height = height;
	this->width = width;
	this->url = url;

	XmlUtils utils;

	// getFirstFrame of Video to display
	repImage.load(utils.getFirstFrameImageFromVideoURL(url));

	toggle = false;
	clock = std::clock();
	isModifiable = false;

}

void TimelineElement::setModifiable(bool b) {
	isModifiable = b;
}

bool TimelineElement::isToggled() {
	return toggle;
}

string TimelineElement::getUrl() {
	return url;
}

std::pair <float, float> TimelineElement::getXandY() {
	std::pair <float, float> p;
	p = std::make_pair(x, y);
	return p;
}

void TimelineElement::setXandY(std::pair <float, float> p) {
	x = p.first;
	y = p.second;
}

void TimelineElement::draw()
{
	ofSetColor(255, 255, 255);
	repImage.draw(x, y, width, height);

	// only draw the highlight if is in modifiable status and toggled
	if (isModifiable) {
		if (toggle) {
			ofSetHexColor(0xcd0000);
			ofNoFill();
			ofDrawRectangle(x, y, width, height);
			ofDrawRectangle(x - 1, y - 1, width + 2, height + 2);
		}
	}

}

void TimelineElement::update() {

	repImage.update();

	clock_t actual = std::clock();

	if (isModifiable) {
		int mouseX = ofGetMouseX();
		int mouseY = ofGetMouseY();
		double elapsed_secs;

		// if mouse is Pressed while hovering the video, toggle for delete
		if ((mouseX > x && mouseX < x + width) && (mouseY > y && mouseY < y + height)) {

			elapsed_secs = double(actual - clock) / CLOCKS_PER_SEC;

			// Only allow 1 click per 0.5 seconds, otherwise toggle would switch back and forth according to the update "refresh rate"
			if (ofGetMousePressed() && elapsed_secs > 0.5) {

				toggle = !toggle;
				clock = std::clock();
			}
		}
	}
	
}

