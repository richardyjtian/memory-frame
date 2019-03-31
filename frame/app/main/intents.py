import logging

from flask_ask import question, statement
from flask_socketio import emit
from . import ask

logging.getLogger('flask_ask').setLevel(logging.DEBUG)

STATUSON = ['on','high']
STATUSOFF = ['off','low']

@ask.launch
def launch():
	speech_text = 'Welcome to Raspberry Pi Automation.'
	return question(speech_text).reprompt(speech_text).simple_card(speech_text)

@ask.intent('GpioIntent', mapping = {'status':'status'})
def Gpio_Intent(status,room):
	print('gpiotent!!')
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
