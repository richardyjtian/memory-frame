from serial_comm import S

def bluetooth():
    ser = S()

    while 1:
        req = ser.port.readline().decode("utf-8")
        if not req:
            json_string = json.load(req)
            return json_string['email'], json_string['password']
            #ser.port.write(str("password").encode())