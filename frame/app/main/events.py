import logging
import json

from flask import Blueprint, render_template
from flask_socketio import SocketIO, emit
from .. import socketio

@socketio.on('connect')
def socket_connect():
	images = ['pic_1.jpg', 'pic_2.jpg', 'pic_4.jpg', 'small_pic_1.jpg', 'small_pic_2.jpg', 'small_pic_3.jpg']
	emit('initialize', json.dumps(images), namespace='/')
	print('connection established')

@socketio.on('test_print')
def socket_test_print(message):
	print(message)
