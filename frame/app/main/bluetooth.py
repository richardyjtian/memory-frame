import time
import serial
import sys

class S(object):
    def __init__(self):
        self.port = serial.Serial(
            port = '/dev/serial0',
            baudrate = 115200,
            parity = serial.PARITY_NONE,
            stopbits = serial.STOPBITS_ONE,
            bytesize = serial.EIGHTBITS,
            timeout = 1
        )

def bluetooth():
    ser = S()

    while 1:
        req = ser.port.readline().decode("utf-8")
        if not req:
            userpass = req.split('*')
            user = userpass[0]
            pwd = userpass[1]
            return user, pwd
            #ser.port.write(str("password").encode())