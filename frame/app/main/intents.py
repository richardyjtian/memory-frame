import logging

from flask_ask import question, statement
from flask_socketio import emit
from . import ask

logging.getLogger('flask_ask').setLevel(logging.DEBUG)

STATUSON = ['on','high']
STATUSOFF = ['off','low']

@ask.launch
def launch():
	speech_text = 'Welcome to Memory Frame Automation.'
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

@ask.intent('PowerIntent', mapping = {'state':'state'})
def Power_Intent(state):
	if state in STATUSON:
		emit('power', 'on', namespace='/', broadcast=True)
		return statement('turning memory frame on')
	elif state in STATUSOFF:
		emit('power', 'off', namespace='/', broadcast=True)
		return statement('turning memory frame off')
	else:
		return statement('sorry not possible')

@ask.intent('SleepIntent', convert={'time': 'timedelta'})
def Sleep_Intent(time):
	emit('sleep', time, namespace='/')
	if time.seconds < 60:
		return statement('Memory frame is sleeping for {} seconds' .format(time.seconds))
	elif time.seconds < 3600:
		minutes = int(time.seconds/60)
		return statement('Memory frame is sleeping for {} minutes' .format(minutes))
	else:
		hours = int(time.seconds/3600)
		return statement('Memory frame is sleeping for {} hours' .format(hours))

@ask.intent('NextPhotoIntent')
def NextPhoto_Intent():
	emit('nextPhoto', namespace='/')
	#do all photos have captions?
	return statement('Here is the next photo')

@ask.intent('LocationStillIntent')
def LocationStill_Intent(city):
	#TODO: add code to search for photos matching location city
	#if photo(s) found matching location city
	#TODO: add # of photos found to statement
	return statement('Here is a photo from {}. By the way x other photos from {} were also found' .format(city, city))
	#else
	#return statement('Sorry, no photos were found from {}' .format(city))

@ask.intent('LocationSlideIntent')
def LocationSlide_Intent(city):
	#TODO: add code to search for photos matching location city
	#if photo(s) found matching location city
	#TODO: add # of photos found to statement
	return statement('I have found x photos from {}' .format(city))
	#else
	#return statement('Sorry, no photos were found from {}' .format(city))

@ask.intent('LabelStillIntent')
def LabelStill_Intent(label):
	#TODO: add code to search for photos matching label
	#if photo(s) found matching label
	#TODO: add # of photos found to statement
	return statement('Here is a photo with {}. By the way x other photos with {} were also found' .format(label, label))
	#else
	#return statement('Sorry, no photos were found from {}' .format(city))

@ask.intent('LabelSlideIntent')
def LabelSlide_Intent(label):
	#TODO: add code to search for photos matching label
	#if photo(s) found matching label
	#TODO: add # of photos found to statement
	return statement('I have found x photos with {}' .format(label))
	#else
	#return statement('Sorry, no photos were found from {}' .format(city))

@ask.intent('SlideIntent', default={'interval':'PT1M'}, convert={'interval': 'timedelta'})
def Slide_Intent(interval):
	#TODO: add code to start slideshow, switching photos at set interval
	#note: interval is of datatype timedelta
	return statement('Memory frame is starting slideshow')

@ask.intent('StillIntent')
def Still_Intent():
	#TODO: add code to pause slideshow on an image
	return statement('Memory frame is pausing slideshow')

@ask.intent('AMAZON.HelpIntent')
def help():
	speech_text = 'You can say hello to me!'
	return question(speech_text).reprompt(speech_text).simple_card('HelloWorld', speech_text)

@ask.session_ended
def session_ended():
	return "{}", 200
