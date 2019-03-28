import logging
import os

from flask import Flask, render_template
from flask_ask import Ask, request, session, question, statement
from flask_socketio import SocketIO, emit

import json

app = Flask(__name__, static_url_path='/static')
# set up Alexa intent handler
ask = Ask(app, "/")
logging.getLogger('flask_ask').setLevel(logging.DEBUG)
# set up SocketIO
app.config['SECRET_KEY'] = 'secret!'
socketio = SocketIO(app)

STATUSON = ['on','high']
STATUSOFF = ['off','low']

############### Photoframe Web Server ###############
@app.route('/')
def frame():
	return render_template('index.html');

############### SocketIO ###############
@socketio.on('connect')
def socket_connect():
	images = ['pic_1.jpg', 'pic_2.jpg', 'pic_4.jpg']
	emit('initialize', json.dumps(images), namespace='/')
	print('connection established')

@socketio.on('test_print')
def socket_test_print(message):
	print(message)

############### Alexa Intent Handlers ############### 
@ask.launch
def launch():
	speech_text = 'Welcome to Raspberry Pi Automation.'
	return question(speech_text).reprompt(speech_text).simple_card(speech_text)

@ask.intent('GpioIntent', mapping = {'status':'status'})
def Gpio_Intent(status,room):
	if status in STATUSON:
		emit('test', 'turning LED on', namespace='/', broadcast=True)
		return statement('turning {} lights'.format(status))
	elif status in STATUSOFF:
		emit('test', 'turning LED off', namespace='/', broadcast=True)
		return statement('turning {} lights'.format(status))
	else:
		return statement('Sorry not possible.')
 
@ask.intent('AMAZON.HelpIntent')
def help():
	speech_text = 'You can say hello to me!'
	return question(speech_text).reprompt(speech_text).simple_card('HelloWorld', speech_text)


@ask.session_ended
def session_ended():
	return "{}", 200


if __name__ == '__main__':
	if 'ASK_VERIFY_REQUESTS' in os.environ:
		verify = str(os.environ.get('ASK_VERIFY_REQUESTS', '')).lower()
		if verify == 'false':
			app.config['ASK_VERIFY_REQUESTS'] = False
	# app.run(debug=True)
	socketio.run(app)
