#pragma once
#include "ofMain.h"
#include "ofxDatGui.h"
#include "EventManager.h"
#include "filtersEnum.h"
#include "XmlUtils.h"

class filters {

	const int FILTERS_WIDTH = 400;
	const int FILTERS_HEIGHT = 26;
	const int FILTERS_TOGGLE_WIDTH = 25;
	const int FILTERS_X = 70;
	const int FILTERS_Y = 30;
	const float LABEL_RATIO = 0.45;
	const float SLIDER_LABEL_RATIO = 0.52;
	const int SLIDER_WIDTH = FILTERS_WIDTH - FILTERS_TOGGLE_WIDTH - 50;

	const string FILTERS_LABEL = "Filters                                              Range";

	const string ADD_FILE = "Add a File                                               +-    ";
	const string ADD_FILE_MSG = "Select a movie clip";
	const int OPEN_FILE_Y = 56;

	const string LUMINANCE = "Luminance";
	const int LUMINANCE_Y = 82;
	const int LUMINANCE_MIN = 0;
	const int LUMINANCE_MAX = 255;
	const int LUMINANCE_VALUE = (LUMINANCE_MAX + LUMINANCE_MIN) / 2;

	const string FACES = "Number of Faces";
	const int FACES_Y = 108;
	const int FACES_MIN = 0;
	const int FACES_MAX = 10;
	const int FACES_VALUE = (FACES_MIN + FACES_MAX) / 2;

	const string EDGES = "Vertical Edges %";
	const int EDGES_Y = 134;
	const int EDGES_MIN = 0;
	const int EDGES_MAX = 100;
	const int EDGES_VALUE = (EDGES_MIN + EDGES_MAX) / 2;

	const string RHYTHM = "Rhythim";
	const int RHYTHM_Y = 160;
	const int RHYTHM_MIN = 0;
	const int RHYTHM_MAX = 4;
	const int RHYTHM_VALUE = (RHYTHM_MIN + RHYTHM_MAX) / 2;

	const string TEXTURE = "Texture (Gabor)";
	const int TEXTURE_MIN = 0;
	const int TEXTURE_MAX = 20;
	const int TEXTURE_VALUE = (TEXTURE_MIN + TEXTURE_MAX) / 2;
	const int TEXTURE_Y = 186;

	const string OBJECTS = "Max Keypoint Matches";
	const string OBJECTS_SAMPLE = "Object.png";
	const string OBJECTS_MSG = "Select an image file";
	const int OBJECTS_Y = 212;
	const int OBJECTS_LABEL_WIDTH = 168;
	const int OBJECTS_BUTTON_WIDTH = 112;
	const int OBJECTS_NUMBER_WIDTH = 45;

	const string COLOR = "Predominant Color";
	const int COLOR_Y = 238;

	const string DATE = "Date";
	const int DATE_Y = 264;

	const string START_DATE = "Start Date: ";
	const string START_DATE_VALUE = "1 - 1 - 2000";

	const string END_DATE = "End Date: ";
	const string END_DATE_VALUE = "31 - 12 - 2018";

	const int LUMINANCE_RANGE = 10;
	const int FACES_RANGE = 1;
	const int EDGES_RANGE = 5;
	const int RHYTHM_RANGE = 1;
	const int TEXTURE_RANGE = 2;
	const int KEYPOINTS_NUMBER = 15;
	const int OBJECTS_RANGE = 5;

public:
	void setup();
	void draw();
	void update();

	void setGuiAutoDraw(bool b);

	bool getToggleValue(Filters de);
	float getLuminanceValue();
	int getFacesNumber();
	float getRhythmValue();
	ofColor getColorValue();
	float getTextureValue();
	float getVerticalEdges();
	bool isObjectLoaded();
	ofImage getObjectImage();
	int getKeypointsNumber();
	vector<int> getDateValues();

	float getLuminanceRange();
	float getFaceRange();
	float getEdgesRange();
	float getRhythmRange();
	float getTextureRange();
	float getObjectsRange();

private:

	void onButtonOpenEvent(ofxDatGuiButtonEvent e);
	void onButtonOpenPictureEvent(ofxDatGuiButtonEvent e);

	ofVideoPlayer movie;
	ofImage objectImage;
	bool loaded;
	
	ofxDatGuiLabel* filtersLabel;
	ofxDatGuiButton* openFileButton;

	ofxDatGuiToggle* dateToggle;
	ofxDatGuiFolder* dateFolder;

	ofxDatGuiToggle* luminanceToggle;
	ofxDatGuiSlider* luminanceSlider;
	ofxDatGuiTextInput* luminanceTinput;

	ofxDatGuiToggle* colorToggle;
	ofxDatGuiColorPicker* colorPicker;

	ofxDatGuiToggle* facesToggle;
	ofxDatGuiSlider* facesSlider;
	ofxDatGuiTextInput* facesTinput;

	ofxDatGuiToggle* edgesToggle;
	ofxDatGuiSlider* edgesSlider;
	ofxDatGuiTextInput* edgesTinput;

	ofxDatGuiToggle* objectsToggle;
	ofxDatGuiLabel* objectsLabel;
	ofxDatGuiButton* objectsButton;
	ofxDatGuiTextInput* objectsNumberInput;
	ofxDatGuiTextInput* objectsTinput;

	ofxDatGuiToggle* textureToggle;
	ofxDatGuiSlider* textureSlider;
	ofxDatGuiTextInput* textureTinput;

	ofxDatGuiToggle* rhythmToggle;
	ofxDatGuiSlider* rhythmSlider;
	ofxDatGuiTextInput* rhythmTinput;

	ofxDatGuiTextInput* startDateInput;
	ofxDatGuiTextInput* endDateInput;

	ofxDatGui* gui;
	
};
