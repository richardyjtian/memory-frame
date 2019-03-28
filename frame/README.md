## Frame App on Raspberry Pi
this folder contains the source code for the Alexa app running on Raspberry Pi

## Running Backend
### Installing python dependencies
`sudo pip3 install flask flask-ask flask-socketio pyrebase`

### Run python server locally  
`python3 app.py`

### Expose local python server
We need to then tunnel a working global URL into our local server, to do this download ngrok from https://ngrok.com/download, then unzip the file and execute it by:  

Linux: `./ngrok http 5000`  

Windows: `ngrok.exe http 5000`

Copy and paste the generated URL in the formate `https://abcdefg.ngrok.com` to the End-Point tab in Amazon Skill Skit Panel
