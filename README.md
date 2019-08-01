## What is Memory Frame?
Memory Frame is a voice controlled digital photo frame.  
The photos are first uploaded via our android app. This let’s the user control what is shown in the frame anywhere they go.
User’s can then tell Memory Frame to display any photos they want to see in any given moment.  
  
By saying phases such as, “Show me a photo of Johnny”  
  
Allowing for a personalized and familiar experience.

## Motivation
Our motivation for Memory Frame is to give people the ability to share their everyday memories with their loved ones who they may not see often. And we want to do this in an organic and natural way.

## Demo
A demo video of the product can be found [here](https://drive.google.com/file/d/1QpM32Y1tHgK384IYS2WcRnolTynRTqS4/view?usp=sharing)

## Components
There are 2 main components to the product
1) an android app
2) a digital photoframe

### Android App
The app allows the user to upload the photos they want to be shown by their Memory Frame, and configure their account and frame.

### Digital Photoframe
The purpose of the digital photoframe is to listen and process user voices commands then display the queried photos on the screen.  
- The photo display is done via a web app (served via python with Flask framework)
- and the voice processing is done with the [Alexa Voice Service](https://developer.amazon.com/docs/alexa-voice-service/get-started-with-alexa-voice-service.html) from Amazon

A more detailed report of the product can be found [here](/public/assets/memory-frame-report.pdf)

## Installation
### Android App
Open the `/PhotoFrame` directory in Android Studio and it should compile and run! 

### Digital Photoframe

## Conclusion and Suggested Future Work
The overall project was a great success and we accomplished everything we set out to do. For a continuation of the project, feedback could be sent from the photo frame user to be displayed on the Android app. Facial recognition could also be used to automatically determine the people in a picture.
