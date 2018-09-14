# Multimedia Computing <img align="right" width="100" height="50" src="https://camo.githubusercontent.com/b2029ffe76b249d5bdd72d48611937651db6a96a/68747470733a2f2f692e696d6775722e636f6d2f4c304e4c616a582e706e67">

    - In this course, we had to develop one projects in groups of two students.
    - The language used was C++.

## (Semi) Automatic Timeline (Project)

In this project, the goal is to semi-automatically build a video timeline that can be used for playing or further editing. 
The criteria for building the timeline is based on similarity for several parameters.
The order of the videos in the timeline is according to the defined criteria in settings mode. 
A settings interface allows selecting the criteria. Removing videos and changing the order of the timeline should be possible. 
Transitions between videos (e.g., fade and dissolve) should also be included.

### Video Archive

This mode shows the collection of videos available in the archive. 
It should include the following features: 
* Representative frame and moving icons all available videos. 
* Moving icons should be implemented based on the cut detection. 
* It should also be possible to see the metadata for each of the videos.

### Player

The player plays a clip or the entire timeline. Play/stop/pause/resume control should be included.

### Settings Mode

The settings interface should allow selecting the criteria that are in place at a given time. 
There should be a way to present the metadata for a given video.

### Metadata

The metadata for each item is the following and it is used to compare the videos:

* Date
* Luminance
* Color-based on the first moment 
* Edge distribution
* Number of times a specific object (input as an image and/or video capture) appears in the video frame
* Number of faces appearing in the video
* Texture characteristics (only for the first frame)
* Rhythm 
* This metadata can be stored in an XML file, so that it is only necessary to process each video once.

Development and Library Support
In order to support access to media content, we use openFrameworks as we were suggested. 
