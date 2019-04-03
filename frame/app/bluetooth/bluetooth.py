from serial_comm import S

def bluetooth():
    ser = S()

    while 1:
        req = ser.port.readline().decode("utf-8")
        if(req == "connect")
            ser.port.write(str("password").encode())