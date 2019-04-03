import logging

from flask_ask import question, statement
from flask_socketio import emit
from . import ask

logging.getLogger('flask_ask').setLevel(logging.DEBUG)

@ask.launch
def launch():
	speech_text = 'Welcome to Memory Frame Automation.'
	return question(speech_text).reprompt(speech_text).simple_card(speech_text)

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
	emit('sleep', time.seconds, namespace='/', broadcast=True)
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
	emit('next_photo', namespace='/', broadcast=True)
	#do all photos have captions?
	return statement('Here is the next photo')

@ask.intent('LocationStillIntent')
def LocationStill_Intent(city):
	images = fb.filter('location', city)
	if len(images) != 0:
		emit('location_still', json.dumps(images), namespace='/', broadcast=True)
		return statement('Here is a photo from {}. By the way {} other photos from {} were also found' .format(city, len(images), city))
	else
		return statement('Sorry, no photos were found from {}' .format(city))

@ask.intent('LocationSlideIntent')
def LocationSlide_Intent(city):
	images = fb.filter('location', city)
	if len(images) != 0:
		emit('location_slide', json.dumps(images), namespace='/', broadcast=True)
		return statement('I\'ve found {} photos from {}' .format(len(images), city))
	else
		return statement('Sorry, no photos were found from {}' .format(city))

@ask.intent('PersonStillIntent')
def PersonStill_Intent(person):
	images = fb.filter('people', person)
	if len(images) != 0:
		emit('person_still', json.dumps(images), namespace='/', broadcast=True)
		return statement('Here is a photo of {}. By the way {} other photos with {} were also found' .format(person, len(images), person))
	else:
		return statement('Sorry, no photos were found of {}' .format(person))

@ask.intent('PersonSlideIntent')
def PersonSlide_Intent(person):
	images = fb.filter('people', person)
	if len(images) != 0:
		emit('person_slide', json.dumps(images), namespace='/', broadcast=True)
		return statement('I\'ve found {} photos of {}' .format(len(images), person))
	else
		return statement('Sorry, no photos were found of {}' .format(person))

@ask.intent('SlideIntent', default={'interval':'PT1M'}, convert={'interval': 'timedelta'})
def Slide_Intent(interval):
	#TODO: add code to start slideshow, switching photos at set interval
	#note: interval is of datatype timedelta
	return statement('Memory frame is starting slideshow')

@ask.intent('StillIntent')
def Still_Intent():
	#TODO: add code to pause slideshow on an image
	return statement('Memory frame is pausing slideshow')

@ask.intent('HelloIntent')
def Hello_Intent():
	return statement('Hi, I\'m your memory frame and I\'m happy to show you photos of all your favorite memories')

@ask.intent('HelpIntent', default={'options':'general'})
def Help_Intent(options):
	if options == 'sleep':
		return statement('Going to bed or to work and don\'t want the memory frame on while you\'re not there to enjoy it? Just tell the frame to sleep for a set amount of time and it will turn off until that time has passed')
	elif options == 'commands':
		return statement('You can tell the memory frame to turn on and off, say hello to it, switch photos, go to sleep, filter photos by location or by person')
	elif options == 'filtering':
		return statement('Feeling like reliving some favorite memories? You can revisit them by asking the memory frame to show you photos from a specific person or city')
	else:
		return statement('Memory frame is a digital photo frame with voice control. You can tell it to turn on and off, change photos or even search for photos by location or person')

@ask.intent('AMAZON.HelpIntent')
def help():
	speech_text = 'You can say hello to me!'
	return question(speech_text).reprompt(speech_text).simple_card('HelloWorld', speech_text)

@ask.session_ended
def session_ended():
	return "{}", 200
