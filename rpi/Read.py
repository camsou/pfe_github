#!/usr/bin/env python
# -*- coding: utf8 -*-
#
#    Copyright 2014,2018 Mario Gomez <mario.gomez@teubi.co>
#
#    This file is part of MFRC522-Python
#    MFRC522-Python is a simple Python implementation for
#    the MFRC522 NFC Card Reader for the Raspberry Pi.
#
#    MFRC522-Python is free software: you can redistribute it and/or modify
#    it under the terms of the GNU Lesser General Public License as published by
#    the Free Software Foundation, either version 3 of the License, or
#    (at your option) any later version.
#
#    MFRC522-Python is distributed in the hope that it will be useful,
#    but WITHOUT ANY WARRANTY; without even the implied warranty of
#    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
#    GNU Lesser General Public License for more details.
#
#    You should have received a copy of the GNU Lesser General Public License
#    along with MFRC522-Python.  If not, see <http://www.gnu.org/licenses/>.
#

import RPi.GPIO as GPIO
import MFRC522
import signal
import time
import socket

continue_reading = True

# Capture SIGINT for cleanup when the script is aborted
def end_read(signal,frame):
    global continue_reading
    print "Ctrl+C captured, ending read."
    continue_reading = False
    GPIO.cleanup()
    raise SystemExit

# Hook the SIGINT
signal.signal(signal.SIGINT, end_read)

# Create an object of the class MFRC522
MIFAREReader = MFRC522.MFRC522()

# Create a list that contain the address of the server
wifi_ip = ["172.20","192.168"] #Zone1, Zone2

# Welcome message
print "Welcome to the MFRC522 data read example"
print "Press Ctrl-C to stop."


# This loop keeps checking for chips. If one is near do nothing, if not, send a message to a phone
id_obj = 636620316212
t1 = time.time()
while continue_reading:
    
    # Scan for cards    
    (status,TagType) = MIFAREReader.MFRC522_Request(MIFAREReader.PICC_REQIDL)

    # If a card is found
    if (status == MIFAREReader.MI_OK) :
       print "Objet detectee"
       t1 = time.time()
       ip = socket.gethostbyname("raspberrypi.local")

    if ((status == MIFAREReader.MI_ERR) and (time.time()-t1>5)):
	for wifi in wifi_ip:
	    if ip[:3] == wifi[:3]:
		print "Objet de Jean Dupont perdu en zone %d" %(wifi_ip.index(wifi))
        	t1 = time.time() + 15



