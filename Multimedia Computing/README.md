<img src="https://i.imgur.com/L0NLajX.png" alt="FCT Logo" align="right" height="50" />

# Multimedia Computing

## (Semi) Automatic Timeline (Project)  (Demanded Functionality)

In this project the goal is to semi-automatically build a video timeline that can be used for playing or further editing. 
The criteria for building the timeline in based on similarity for several parameters.
The order of the videos in the timeline is according to the defined criteria in settings mode. 
A settings interface allows to select the criteria. Removing videos and changing the order of the timeline should be possible. 
Transitions between videos (e.g., fade and dissolve) should also be included.

### Video Archive

This mode shows the collection of videos available in the archive. 
It should include the following features: 
* Representative frame and moving icons all available videos. 
* Moving icons should be implemented based on cut detection. 
* It should also be possible to see the metadata for each of the videos.

### Player

The player plays a clip or the entire timeline. Play/stop/pause/resume control should be included.

### Settings Mode

The settings interface should allow to select the criteria that are in place at a given time. 
There should be a way to present the metadata for a given video.

### Metadata

The metadata for each item is the following and it is used to compare the videos:

* Date
* Luminance
* Color - based on first moment 
* Edge distribution
* Number of times a specific object (input as an image and/or video capture) appears in the video frame
* Number of faces appearing in the video
* Texture characteristics (only for first frame)
* Rhythm 
* This metadata can be stored in an XML file, so that it is only necessary to process each video once.

Development and Library Support
In order to support access to media content, we use openFrameworks.

