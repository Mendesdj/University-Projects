#pragma once

#include <stdio.h>
#include "ofxDatGui.h"
#include "EventManager.h"


class switchElement {


public:
	void setup(int index1, int index2, int x, int y, int width, int height);
	void draw();
	void update();
	void changeActiveState(bool b);

private:

	int index1;
	int index2;
	int x;
	int y;
	int width;
	int height;
	ofImage arrow;
	bool active;
	clock_t clock;

};

