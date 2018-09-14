
#include "videoElement.h"

const string PLAY_BUTTON = "icons/playvideo.png";
const int METADATA_X = 543;
const int METADATA_Y = 400;

void videoElement::setup(string url)
{
	this->url = url;
	playButton.load(PLAY_BUTTON);

	toggle = false;
	
	XmlUtils utils;
	// Process Video
	utils.processVideo(url);
	
	// Get representative Frames
	images = utils.getImagesFromVideoURL(url);
	repImage.load(images.at(0));
	currentImage = 0;
	
	metadataGui = new ofxDatGui(0,0);

	string videoNameString = utils.getFileNameFromUrl(url);
	ofxDatGuiLabel * NameLabel = metadataGui->addLabel("Name:  " + videoNameString);
	NameLabel->setStripeVisible(false);
	NameLabel->setTheme(new ofxDatGuiThemeSmoke());
	NameLabel->setLabelAlignment(ofxDatGuiAlignment::CENTER);

	string timeString = utils.getTimeFromVideoURL(url);

	ofxDatGuiLabel * dateLabel = metadataGui->addLabel("Date: " + timeString);
	dateLabel->setStripeVisible(false);
	dateLabel->setTheme(new ofxDatGuiThemeSmoke());
	dateLabel->setLabelAlignment(ofxDatGuiAlignment::CENTER);

	float luminanceString = utils.getLuminanceFromVideoURL(url);
	ofxDatGuiLabel * luminanceLabel = metadataGui->addLabel("Luminance:  " + std::to_string(luminanceString));
	luminanceLabel->setStripeVisible(false);
	luminanceLabel->setTheme(new ofxDatGuiThemeSmoke());
	luminanceLabel->setLabelAlignment(ofxDatGuiAlignment::CENTER);

	int facesString = utils.getNFacesFromVideoURL(url);
	ofxDatGuiLabel * NFacesLabel = metadataGui->addLabel("Faces:  " + std::to_string(facesString));
	NFacesLabel->setStripeVisible(false);
	NFacesLabel->setTheme(new ofxDatGuiThemeSmoke());
	NFacesLabel->setLabelAlignment(ofxDatGuiAlignment::CENTER);

	int redString = utils.getColorRedBasedFromVideoURL(url);
	int GreenString = utils.getColorGreenBasedFromVideoURL(url);
	int blueString = utils.getColorBlueBasedFromVideoURL(url);
	ofxDatGuiLabel * colorRGBLabel = metadataGui->addLabel("RGB Color:  (" + std::to_string(redString) + " , " + std::to_string(GreenString) + " , " + std::to_string(blueString) + " )");
	colorRGBLabel->setStripeVisible(false);
	colorRGBLabel->setTheme(new ofxDatGuiThemeSmoke());
	colorRGBLabel->setLabelAlignment(ofxDatGuiAlignment::CENTER);

	float rhythmString = utils.getRhythmFromVideoURL(url);
	ofxDatGuiLabel * rhythmLabel = metadataGui->addLabel("Rhythm:  " + std::to_string(rhythmString));
	rhythmLabel->setStripeVisible(false);
	rhythmLabel->setTheme(new ofxDatGuiThemeSmoke());
	rhythmLabel->setLabelAlignment(ofxDatGuiAlignment::CENTER);

	float textureString = utils.getTexturesFromVideoURL(url);
	ofxDatGuiLabel * textureLabel = metadataGui->addLabel("Texture:  " + std::to_string(textureString));
	textureLabel->setStripeVisible(false);
	textureLabel->setTheme(new ofxDatGuiThemeSmoke());
	textureLabel->setLabelAlignment(ofxDatGuiAlignment::CENTER);

	float edgesString = utils.getVerticalEdges(url);
	ofxDatGuiLabel * edgesLabel = metadataGui->addLabel("Vertical Edges:  " + std::to_string(edgesString)+ "%");
	edgesLabel->setStripeVisible(false);
	edgesLabel->setTheme(new ofxDatGuiThemeSmoke());
	edgesLabel->setLabelAlignment(ofxDatGuiAlignment::CENTER);

	clock = std::clock();
	clockDelete = std::clock();
}

void videoElement::deleteMetadataOverlay() {
	metadataGui->setAutoDraw(false);
}

// Give new coordinates to videoElement, useful to change the position after deleting on videobox, removes the need for creating a new object
void videoElement::newCoordinates(float x, float y, float width, float height) {
	this->x = x;
	this->y = y;
	this->height = height;
	this->width = width;

	playWidth = width * 0.3;
	playHeight = height * 0.4;
	playX = x + (width - playWidth) / 2;
	playY = y + (height - playHeight) / 2;

	metadataGui->setPosition(x, height + y + 2);
	metadataGui->setWidth(width);

}

bool videoElement::isToggled() {
	return toggle;
}

string videoElement::getUrl() {
	return url;
}

void videoElement::draw()
{
	ofSetColor(255, 255, 255);
	repImage.draw(x, y, width, height);
	
	int mouseX = ofGetMouseX();
	int mouseY = ofGetMouseY();

	// If hovering the videoElement display the PlayButton
	if ((mouseX>x && mouseX<x + width) && (mouseY>y && mouseY<y + height)) {
		playButton.draw(playX, playY, playWidth, playHeight);
	}

	// If toggled highlight
	if (toggle) {
		ofSetHexColor(0xcd0000);
		ofNoFill();
		ofDrawRectangle(x, y, width, height);
		ofDrawRectangle(x - 1, y - 1, width + 2, height + 2);
	}
	
}

void videoElement::update() {

	clock_t actual = std::clock();

	double elapsed_secs = double( actual - clock ) / CLOCKS_PER_SEC;

	// Change representative frame every 2 seconds
	if (elapsed_secs > 2) {
		if (currentImage + 1 >= 5) {
			currentImage = 0;
		}
		else {
			currentImage = currentImage + 1;
		}
		
		repImage.load(images.at(currentImage));
		clock = std::clock();
	}
	

	repImage.update();
	int mouseX = ofGetMouseX();
	int mouseY = ofGetMouseY();

	if ((mouseX>x && mouseX<x + width) && (mouseY>y && mouseY<y + height)) {

		metadataGui->setPosition(x, height + y + 2);

		// Show metadata on hover
		metadataGui->setVisible(true);
		
		elapsed_secs = double(actual - clockDelete) / CLOCKS_PER_SEC;

		// If Play Button of video is pressed trigger the event to reproduce video
		if (ofGetMousePressed() && elapsed_secs > 0.5) {
			if ((mouseX > playX && mouseX < playX + playWidth) && (mouseY > playY && mouseY < playY + playHeight)) {
				metadataGui->setVisible(true);

				// Metadata changes position to be displayed alongside the video playback
				metadataGui->setPosition(METADATA_X, METADATA_Y);
				ofNotifyEvent(EventManager::playVideoEvent, url);

			}
			else {
				toggle = !toggle;
				clockDelete = std::clock();
			}
		}

	}
	// Hide when not hovering
	else {
		metadataGui->setVisible(false);
	}


	

}
