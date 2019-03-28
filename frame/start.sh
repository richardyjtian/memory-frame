# reference: https://github.com/libbymiller/pi-frame/blob/master/frame.sh

HOST_NAME="localhost"
PORT=5000

# random is just to stop it caching
RAN=$RANDOM

# again to stop it caching
rm -rf /home/pi/.config/chromium/
# start chromium in full screen linking to the entry point
/usr/bin/chromium-browser --kiosk --disable-infobars --disable-session-crashed-bubble --disable-application-cache --no-first-run http://$HOST_NAME:$PORT#$RAN

