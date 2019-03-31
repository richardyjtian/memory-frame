from flask import Blueprint
from flask_ask import Ask

main = Blueprint('main', __name__, url_prefix="/")
ask = Ask(blueprint=main)

from . import routes, events, intents
