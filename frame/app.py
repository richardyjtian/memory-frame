import logging
import os

from flask import Flask, render_template
from flask_ask import Ask, request, session, question, statement
from flask_socketio import SocketIO, emit

from ask_api import ask_blueprint

import json

app = Flask(__name__, static_url_path='/static')
# set up Alexa intent handler
app.register_blueprint(ask_blueprint);

# set up SocketIO
app.config['SECRET_KEY'] = 'secret!'
socketio = SocketIO(app)

logging.getLogger('flask_ask').setLevel(logging.DEBUG)

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

if __name__ == '__main__':
	if 'ASK_VERIFY_REQUESTS' in os.environ:
		verify = str(os.environ.get('ASK_VERIFY_REQUESTS', '')).lower()
		if verify == 'false':
			app.config['ASK_VERIFY_REQUESTS'] = False
	# app.run(debug=True)
	socketio.run(app)
