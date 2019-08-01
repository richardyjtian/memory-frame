this folder contains the source code for the Alexa app as the digital photo frame.  
  
*Note: this code is developed to be ran on Raspberry Pi 3*

## Requirements
- Python3 
- [ngrok](https://ngrok.com/download)
- [Alexa Voice Service (AVS)](https://github.com/alexa/avs-device-sdk/wiki)

## Explaination
1. a simple Web front-end to display photos
2. Alexa Voice Service (AVS) listens for users commands and send a recorded voice sample to the Amazon’s servers for parsing
3. a local python Flask server that handles web hook requests from [Alexa Skill](https://developer.amazon.com/docs/custom-skills/steps-to-build-a-custom-skill.html)

## Installation
1. to install dependencies to run the python app:  
`pip3 -r requirements.txt`  
2. start a new AVS product, [here](https://developer.amazon.com/alexa/console/avs/products)
3. start a new [Alexa Skill](https://developer.amazon.com/alexa/console/ask), copy and paste the settings in `alexa/skill-model.json` to build the model

## How to start

### 1. Set up Firebase config file
enter your Firebase credentials in [/app/main/config.py](./app/main/config.py)

### 2. AVS
start AVS in the background, on the first time starting it'll ask you to enter the credentials of your Alexa product.  
You can find this on the AVS developer page, [here](https://developer.amazon.com/alexa/console/avs/products)

### 3. Run python server locally  
`python3 app.py`

### 4. Expose local python server
We need to then tunnel a working global URL into our local server using ngrok

Linux: `./ngrok http 5000`  

Windows: `ngrok.exe http 5000`

Copy and paste the generated URL in the formate `https://abcdefg.ngrok.com` to the End-Point tab in Amazon Skill Skit Panel

### 5. Opening web app
`bash ./start.sh`