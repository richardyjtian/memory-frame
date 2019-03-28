import logging
import os

from flask import Flask
from flask_socketio import SocketIO

socketio = SocketIO()

logging.getLogger('flask_ask').setLevel(logging.DEBUG)

def create_app(debug=False):
    """Create an application."""
    app = Flask(__name__)
    app.debug = debug
    app.config['SECRET_KEY'] = 'sdkasndask12w'

    from .main import main as main_blueprint
    from .intents import intents as intents_blueprint
    app.register_blueprint(main_blueprint)
    app.register_blueprint(intents_blueprint)

    if 'ASK_VERIFY_REQUESTS' in os.environ:
        verify = str(os.environ.get('ASK_VERIFY_REQUESTS', '')).lower()
        if verify == 'false':
            app.config['ASK_VERIFY_REQUESTS'] = False

    socketio.init_app(app)
    return app
