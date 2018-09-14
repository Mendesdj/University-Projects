#include "ofMain.h"
#include "ofApp.h"

//========================================================================
int main( ){

	ofGLFWWindowSettings settings;

	// Window not resizable 
	settings.resizable = false;
	settings.height = 800;
	settings.width = 1280;
	ofCreateWindow(settings);
	ofSetWindowTitle("Timeline Generator");
	ofRunApp( new ofApp());

}
