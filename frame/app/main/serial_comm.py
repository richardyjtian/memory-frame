"""
Read json format user request from the serial port then query
    the database according to the request type. Return the result
    via the same serial port. 

Expects requests to include fields below seperated by coma:
    search_type: "n"/"c",
    keyword: the keyword to search for

"""
import time
import serial
import sys

#from serial import Serial

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



        
