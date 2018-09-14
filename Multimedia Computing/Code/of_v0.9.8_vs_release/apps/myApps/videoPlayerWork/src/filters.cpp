#include "filters.h"

void filters::setup()
{
	ofxDatGuiLog::quiet();

	// Filters Setup

	filtersLabel = new ofxDatGuiLabel(FILTERS_LABEL);
	filtersLabel->setTheme(new ofxDatGuiThemeSmoke());
	filtersLabel->setPosition(FILTERS_X, FILTERS_Y);
	filtersLabel->setWidth(FILTERS_WIDTH);
	filtersLabel->setStripeVisible(false);
	filtersLabel->setLabelAlignment(ofxDatGuiAlignment::RIGHT);

	openFileButton = new ofxDatGuiButton(ADD_FILE);
	openFileButton->setTheme(new ofxDatGuiThemeSmoke());
	openFileButton->setPosition(FILTERS_X, OPEN_FILE_Y);
	openFileButton->setWidth(FILTERS_WIDTH);
	openFileButton->setStripeVisible(false);
	openFileButton->setLabelAlignment(ofxDatGuiAlignment::RIGHT);

	luminanceToggle = new ofxDatGuiToggle("");
	luminanceToggle->setTheme(new ofxDatGuiThemeSmoke());
	luminanceToggle->setPosition(FILTERS_X, LUMINANCE_Y);
	luminanceToggle->setWidth(FILTERS_TOGGLE_WIDTH);
	luminanceToggle->setStripeVisible(false);

	luminanceSlider = new ofxDatGuiSlider(LUMINANCE, LUMINANCE_MIN, LUMINANCE_MAX, LUMINANCE_VALUE);
	luminanceSlider->setTheme(new ofxDatGuiThemeSmoke());
	luminanceSlider->setPosition(FILTERS_X + FILTERS_TOGGLE_WIDTH, LUMINANCE_Y);
	luminanceSlider->setWidth(SLIDER_WIDTH , SLIDER_LABEL_RATIO);
	luminanceSlider->setStripeVisible(false);

	luminanceTinput = new ofxDatGuiTextInput("");
	luminanceTinput->setTheme(new ofxDatGuiThemeSmoke());
	luminanceTinput->setPosition(FILTERS_X + FILTERS_TOGGLE_WIDTH + SLIDER_WIDTH, LUMINANCE_Y);
	luminanceTinput->setWidth(50, 0);
	luminanceTinput->setStripeVisible(false);
	luminanceTinput->setText(std::to_string(LUMINANCE_RANGE));

	facesToggle = new ofxDatGuiToggle("");
	facesToggle->setTheme(new ofxDatGuiThemeSmoke());
	facesToggle->setPosition(FILTERS_X, FACES_Y);
	facesToggle->setWidth(FILTERS_TOGGLE_WIDTH);
	facesToggle->setStripeVisible(false);

	facesSlider = new ofxDatGuiSlider(FACES, FACES_MIN, FACES_MAX, FACES_VALUE);
	facesSlider->setTheme(new ofxDatGuiThemeSmoke());
	facesSlider->setPosition(FILTERS_X + FILTERS_TOGGLE_WIDTH, FACES_Y);
	facesSlider->setWidth(SLIDER_WIDTH, SLIDER_LABEL_RATIO);
	facesSlider->setStripeVisible(false);
	facesSlider->setPrecision(0);

	facesTinput = new ofxDatGuiTextInput("");
	facesTinput->setTheme(new ofxDatGuiThemeSmoke());
	facesTinput->setPosition(FILTERS_X + FILTERS_TOGGLE_WIDTH + SLIDER_WIDTH, FACES_Y);
	facesTinput->setWidth(50, 0);
	facesTinput->setStripeVisible(false);
	facesTinput->setText(std::to_string(FACES_RANGE));

	edgesToggle = new ofxDatGuiToggle("");
	edgesToggle->setTheme(new ofxDatGuiThemeSmoke());
	edgesToggle->setPosition(FILTERS_X, EDGES_Y);
	edgesToggle->setWidth(FILTERS_TOGGLE_WIDTH);
	edgesToggle->setStripeVisible(false);

	edgesSlider = new ofxDatGuiSlider(EDGES, EDGES_MIN, EDGES_MAX, EDGES_VALUE);
	edgesSlider->setTheme(new ofxDatGuiThemeSmoke());
	edgesSlider->setPosition(FILTERS_X + FILTERS_TOGGLE_WIDTH, EDGES_Y);
	edgesSlider->setWidth(SLIDER_WIDTH, SLIDER_LABEL_RATIO);
	edgesSlider->setStripeVisible(false);

	edgesTinput = new ofxDatGuiTextInput("");
	edgesTinput->setTheme(new ofxDatGuiThemeSmoke());
	edgesTinput->setPosition(FILTERS_X + FILTERS_TOGGLE_WIDTH + SLIDER_WIDTH, EDGES_Y);
	edgesTinput->setWidth(50, 0);
	edgesTinput->setStripeVisible(false);	
	edgesTinput->setText(std::to_string(EDGES_RANGE));

	rhythmToggle = new ofxDatGuiToggle("");
	rhythmToggle->setTheme(new ofxDatGuiThemeSmoke());
	rhythmToggle->setPosition(FILTERS_X, RHYTHM_Y);
	rhythmToggle->setWidth(FILTERS_TOGGLE_WIDTH);
	rhythmToggle->setStripeVisible(false);

	rhythmSlider = new ofxDatGuiSlider(RHYTHM, RHYTHM_MIN, RHYTHM_MAX, RHYTHM_VALUE);
	rhythmSlider->setTheme(new ofxDatGuiThemeSmoke());
	rhythmSlider->setPosition(FILTERS_X + FILTERS_TOGGLE_WIDTH, RHYTHM_Y);
	rhythmSlider->setWidth(SLIDER_WIDTH, SLIDER_LABEL_RATIO);
	rhythmSlider->setStripeVisible(false);

	rhythmTinput = new ofxDatGuiTextInput("");
	rhythmTinput->setTheme(new ofxDatGuiThemeSmoke());
	rhythmTinput->setPosition(FILTERS_X + FILTERS_TOGGLE_WIDTH + SLIDER_WIDTH, RHYTHM_Y);
	rhythmTinput->setWidth(50, 0);
	rhythmTinput->setStripeVisible(false);
	rhythmTinput->setText(std::to_string(RHYTHM_RANGE));

	textureToggle = new ofxDatGuiToggle("");
	textureToggle->setTheme(new ofxDatGuiThemeSmoke());
	textureToggle->setPosition(FILTERS_X, TEXTURE_Y);
	textureToggle->setWidth(FILTERS_TOGGLE_WIDTH);
	textureToggle->setStripeVisible(false);

	textureSlider = new ofxDatGuiSlider(TEXTURE, TEXTURE_MIN, TEXTURE_MAX, TEXTURE_VALUE);
	textureSlider->setTheme(new ofxDatGuiThemeSmoke());
	textureSlider->setPosition(FILTERS_X + FILTERS_TOGGLE_WIDTH, TEXTURE_Y);
	textureSlider->setWidth(SLIDER_WIDTH, SLIDER_LABEL_RATIO);
	textureSlider->setStripeVisible(false);

	textureTinput = new ofxDatGuiTextInput("");
	textureTinput->setTheme(new ofxDatGuiThemeSmoke());
	textureTinput->setPosition(FILTERS_X + FILTERS_TOGGLE_WIDTH + SLIDER_WIDTH, TEXTURE_Y);
	textureTinput->setWidth(50, 0);
	textureTinput->setStripeVisible(false);
	textureTinput->setText(std::to_string(TEXTURE_RANGE));

	objectsToggle = new ofxDatGuiToggle("");
	objectsToggle->setTheme(new ofxDatGuiThemeSmoke());
	objectsToggle->setPosition(FILTERS_X, OBJECTS_Y);
	objectsToggle->setWidth(FILTERS_TOGGLE_WIDTH);
	objectsToggle->setStripeVisible(false);

	objectsLabel = new ofxDatGuiLabel(OBJECTS);
	objectsLabel->setTheme(new ofxDatGuiThemeSmoke());
	objectsLabel->setPosition(FILTERS_X + FILTERS_TOGGLE_WIDTH, OBJECTS_Y);
	objectsLabel->setWidth(OBJECTS_LABEL_WIDTH);
	objectsLabel->setStripeVisible(false);

	objectsButton = new ofxDatGuiButton(OBJECTS_SAMPLE);
	objectsButton->setTheme(new ofxDatGuiThemeSmoke());
	objectsButton->setPosition(FILTERS_X + FILTERS_TOGGLE_WIDTH + OBJECTS_LABEL_WIDTH, OBJECTS_Y);
	objectsButton->setWidth(OBJECTS_BUTTON_WIDTH);
	objectsButton->setStripeVisible(false);
	objectsButton->setLabelUpperCase(false);
	objectsButton->setLabelAlignment(ofxDatGuiAlignment::CENTER);

	objectsNumberInput = new ofxDatGuiTextInput("");
	objectsNumberInput->setTheme(new ofxDatGuiThemeSmoke());
	objectsNumberInput->setPosition(FILTERS_X + FILTERS_TOGGLE_WIDTH + OBJECTS_LABEL_WIDTH + OBJECTS_BUTTON_WIDTH, OBJECTS_Y);
	objectsNumberInput->setWidth(OBJECTS_NUMBER_WIDTH, 0);
	objectsNumberInput->setStripeVisible(false);
	objectsNumberInput->setText(std::to_string(KEYPOINTS_NUMBER));

	objectsTinput = new ofxDatGuiTextInput("");
	objectsTinput->setTheme(new ofxDatGuiThemeSmoke());
	objectsTinput->setPosition(FILTERS_X + FILTERS_TOGGLE_WIDTH + OBJECTS_LABEL_WIDTH + OBJECTS_BUTTON_WIDTH + OBJECTS_NUMBER_WIDTH, OBJECTS_Y);
	objectsTinput->setWidth(50, 0);
	objectsTinput->setStripeVisible(false);
	objectsTinput->setText(std::to_string(OBJECTS_RANGE));

	gui = new ofxDatGui(FILTERS_X + FILTERS_TOGGLE_WIDTH, COLOR_Y);

	colorToggle = new ofxDatGuiToggle("");
	colorToggle->setTheme(new ofxDatGuiThemeSmoke());
	colorToggle->setPosition(FILTERS_X, COLOR_Y);
	colorToggle->setWidth(FILTERS_TOGGLE_WIDTH);
	colorToggle->setStripeVisible(false);

	colorPicker = gui->addColorPicker(COLOR, ofColor::black);
	colorPicker->setTheme(new ofxDatGuiThemeSmoke());
	colorPicker->setWidth(FILTERS_WIDTH - FILTERS_TOGGLE_WIDTH, LABEL_RATIO);
	colorPicker->setStripeVisible(false);

	dateToggle = new ofxDatGuiToggle("");
	dateToggle->setTheme(new ofxDatGuiThemeSmoke());
	dateToggle->setPosition(FILTERS_X, DATE_Y);
	dateToggle->setWidth(FILTERS_TOGGLE_WIDTH);
	dateToggle->setStripeVisible(false);

	dateFolder = gui->addFolder(DATE);
	dateFolder->setTheme(new ofxDatGuiThemeSmoke());
	dateFolder->setWidth(FILTERS_WIDTH - FILTERS_TOGGLE_WIDTH, LABEL_RATIO);
	dateFolder->setStripeVisible(false);

	startDateInput = dateFolder->addTextInput(START_DATE, START_DATE_VALUE);
	startDateInput->setTheme(new ofxDatGuiThemeSmoke());
	startDateInput->setWidth(FILTERS_WIDTH - FILTERS_TOGGLE_WIDTH, LABEL_RATIO);
	startDateInput->setStripeVisible(false);

	endDateInput = dateFolder->addTextInput(END_DATE, END_DATE_VALUE);
	endDateInput->setTheme(new ofxDatGuiThemeSmoke());
	endDateInput->setWidth(FILTERS_WIDTH - FILTERS_TOGGLE_WIDTH, LABEL_RATIO);
	endDateInput->setStripeVisible(false);

	// Listeners for the buttons
	openFileButton->onButtonEvent(this, &filters::onButtonOpenEvent);
	objectsButton->onButtonEvent(this, &filters::onButtonOpenPictureEvent);

	// No image is loaded for keypoint matching against the video frames
	loaded = false;

}

// Open File Explorer to select a video to add and notify event
void filters::onButtonOpenEvent(ofxDatGuiButtonEvent e)
{

	ofFileDialogResult result = ofSystemLoadDialog(ADD_FILE_MSG);
	
	if (result.bSuccess) {

		string path = result.getPath();
		ofFile file(path);
		string fileExtension = ofToUpper(file.getExtension());

		if (fileExtension == "MP4" || fileExtension == "MOV") {
			movie.load(path);
			// Notify event to add a video with "path" to the videoBox
			ofNotifyEvent(EventManager::AddVideoEvent, path);
		}
	}

}

// Open File Explorer to select a picture 
void filters::onButtonOpenPictureEvent(ofxDatGuiButtonEvent e)
{

	ofFileDialogResult result = ofSystemLoadDialog(OBJECTS_MSG);

	if (result.bSuccess) {

		string path = result.getPath();
		ofFile file(path);
		string fileExtension = ofToUpper(file.getExtension());

		if (fileExtension == "PNG" || fileExtension == "JPG" ) {
			e.target->setLabel(file.getFileName());
			// Load the picture and set loaded to true
			objectImage.load(path);
			loaded = true;
		}
	}

}

// Due to the fact that the elements on the ofxDatGui get auto drawn if associated to a Gui we need to disable it manually
// in order to "hide" it when desired
void filters::setGuiAutoDraw(bool b) {
	gui->setAutoDraw(b);
}

// Returns if said filter is toggled or not
bool filters::getToggleValue(Filters e) {

	switch (e) {
	case luminance:
		return luminanceToggle->getChecked();
	case faces:
		return facesToggle->getChecked();
	case edges:
		return edgesToggle->getChecked();
	case objects:
		return objectsToggle->getChecked();
	case texture:
		return textureToggle->getChecked();
	case rhythm:
		return rhythmToggle->getChecked();
	case color:
		return colorToggle->getChecked();
	case date:
		return dateToggle->getChecked();
	default:
		return false;
	}
}

//----------- Getters for Filters values -----------------------------------

float filters::getLuminanceValue() {
	return luminanceSlider->getValue();
}

int filters::getFacesNumber() {
	return facesSlider->getValue();
}

float filters::getVerticalEdges() {
	return edgesSlider->getValue();
}

float filters::getRhythmValue() {

	return rhythmSlider->getValue();
}

float filters::getTextureValue() {

	return textureSlider->getValue();
}

ofColor filters::getColorValue() {
	
	return colorPicker->getColor();
}

bool filters::isObjectLoaded() {
	return loaded;
}

ofImage filters::getObjectImage() {
	return objectImage;
}

int filters::getKeypointsNumber() {

	try {
		return std::stoi(objectsNumberInput->getText());
	}
	catch (const std::invalid_argument) {
		return 0;
	}
}

vector<int> filters::getDateValues() {
	XmlUtils utils;
	vector<int> returnVector, addVector;

	returnVector = utils.getDateFormatted(startDateInput->getText());
	addVector = utils.getDateFormatted(endDateInput->getText());

	returnVector.insert(returnVector.end(), addVector.begin(), addVector.end());
	return returnVector;
}

float filters::getLuminanceRange() {

	try {
		return std::stof(luminanceTinput->getText());
	}
	catch (const std::invalid_argument) {
		return LUMINANCE_RANGE;
	
	}
}

float filters::getFaceRange() {

	try {
		return std::stof(facesTinput->getText());
	}
	catch (const std::invalid_argument) {
		return FACES_RANGE;
	}
}

float filters::getEdgesRange() {

	try {
		return std::stof(edgesTinput->getText());
	}
	catch (const std::invalid_argument) {
		return EDGES_RANGE;
	}
}

float filters::getRhythmRange() {

	try {
		return std::stof(rhythmTinput->getText());
	}
	catch (const std::invalid_argument) {
		return RHYTHM_RANGE;
	}
}

float filters::getTextureRange() {

	try {
		return std::stof(textureTinput->getText());
	}
	catch (const std::invalid_argument) {
		return TEXTURE_RANGE;
	}
}

float filters::getObjectsRange() {

	try {
		return std::stof(objectsTinput->getText());
	}
	catch (const std::invalid_argument) {
		return OBJECTS_RANGE;
	}
}

//----------- Getters for Filters values -----------------------------------

void filters::draw()
{
	filtersLabel->draw();
	openFileButton->draw();

	luminanceToggle->draw();
	facesToggle->draw();
	edgesToggle->draw();
	objectsToggle->draw();
	textureToggle->draw();
	rhythmToggle->draw();
	colorToggle->draw();
	dateToggle->draw();

	luminanceSlider->draw();
	facesSlider->draw();
	edgesSlider->draw();
	objectsLabel->draw();
	objectsButton->draw();
	objectsNumberInput->draw();
	rhythmSlider->draw();
	textureSlider->draw();
	
	luminanceTinput->draw();
	facesTinput->draw();
	edgesTinput->draw();
	textureTinput->draw();
	rhythmTinput->draw();
	objectsTinput->draw();
	// Remaining get auto Drawn by Gui
	
}

void filters::update()
{
	filtersLabel->update();
	openFileButton->update();

	luminanceToggle->update();
	facesToggle->update();
	edgesToggle->update();
	objectsToggle->update();
	textureToggle->update();
	rhythmToggle->update();
	colorToggle->update();
	dateToggle->update();

	luminanceSlider->update();
	facesSlider->update();
	edgesSlider->update();
	objectsLabel->update();
	objectsButton->update();
	objectsNumberInput->update();
	rhythmSlider->update();
	textureSlider->update();

	luminanceTinput->update();
	facesTinput->update();
	edgesTinput->update();
	textureTinput->update();
	rhythmTinput->update();
	objectsTinput->update();
	
	// Remaining get auto Updated by Gui
	
}


