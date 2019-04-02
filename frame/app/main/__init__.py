from flask import Blueprint
from flask_ask import Ask
from . import firebase
from . import config

main = Blueprint('main', __name__, url_prefix="/")
ask = Ask(blueprint=main)
fb = firebase.Firebase(config.firebase_config)
fb.sign_in(config.user_config['email'], config.user_config['password'])

from . import routes, events, intents
