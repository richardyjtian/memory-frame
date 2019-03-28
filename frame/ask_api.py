import logging

from flask import Blueprint, render_template
from flask_ask import Ask, question, statement

ask_blueprint = Blueprint('ask_api', __name__, url_prefix="/ask")
ask = Ask(blueprint=ask_blueprint)

logging.getLogger('flask_ask').setLevel(logging.DEBUG)

STATUSON = ['on','high']
STATUSOFF = ['off','low']

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
