from serial_comm import S

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