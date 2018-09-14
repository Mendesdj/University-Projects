#include "switchElement.h"

const string SWITCH_BUTTON = "icons/switch.png";

// Switch element represents the arrows that allow for timeline reordering 
void switchElement::setup(int index1, int index2, int x, int y, int width, int height)
{
	// index1 and index2 hold the indexes of the videos that are outside the switch
	//				0 / 1
	//   Videos[0]  <--->  Videos[1] 
	// this way when the switch is clicked we can propagate the event with the pair of indexes to change the position of the correct videos 

	this->index1 = index1;
	this->index2 = index2;
	this->x = x;
	this->y = y;
	this->width = width;
	this->height = height;

	active = false;
	arrow.load(SWITCH_BUTTON);
	
}

// activate or deactivate the switch
void switchElement::changeActiveState(bool b) {
	active = b;
}

// Only draw the arrow if active
void switchElement::draw()
{
	if (active) {
		ofSetColor(255, 255, 255);
		arrow.draw(x, y, width, height);
	}

}

// Update only when active
void switchElement::update()
{
	if (active) {
		
		arrow.update();

		clock_t actual = std::clock();
		double elapsed_secs = double(actual - clock) / CLOCKS_PER_SEC;

		// Because there is no ofGetMouseReleased, we simulate the functionality by clocking the time between the last click
		// This way we only allow 1 click per 0.5 seconds and avoid one mouse Click to be interpreted as multiple mouse presses 
		if (ofGetMousePressed() && elapsed_secs > 0.5) {
			int mouseX = ofGetMouseX();
			int mouseY = ofGetMouseY();

			if ((mouseX>x && mouseX<x + width) && (mouseY>y && mouseY<y + height)) {
				std::pair <int, int> p;
				p = std::make_pair(index1, index2);

				ofNotifyEvent(EventManager::ReorderTimelineEvent, p);
				ofNotifyEvent(EventManager::ReorderGeneratedTimeline, p);

				clock = std::clock();
			}
		}

	}
}
