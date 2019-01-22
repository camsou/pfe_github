#!/usr/bin/env python
# -*- coding: utf8 -*-
"""
@author: mpoutet_plopez
"""

import RPi.GPIO as GPIO
import MFRC522
import signal
import time
import socket
import sys
import os
import firebase_admin
from   adxl345          import ADXL345
from   threading        import Thread
from   firebase_utils   import firebase_connect
from   firebase_utils   import send_notification
from   firebase_utils   import send_notification_color
from   firebase_admin   import db
from   firebase         import firebase

# Id of the raspberry
id_rasp = 1

# Connect to the database
firebase = firebase.FirebaseApplication('https://detect-n-alert.firebaseio.com', None)
firebase_connect()

# Initailisation de la variable notification
ref = db.reference('/')
ref.update({'Notification/Notification_%d/Etat' %id_rasp : 1})

# Welcome message
print "Welcome to Detect'N'Alert example"
print "Press Ctrl-C to stop."

# Create a list that contain the address of the server
wifi_ip = ["172.20","192.168"] #Zone1, Zone2

# Create a fonction to return an correct format for the time
minute = 0

def format_time(second):
    minute = second/60
    second %= 60
    return "%d:%d" %(minute, second)

# Create a fonction to detect if a personnal object is lost
def lost():
    id_obj = 636620316212
    t1 = time.time()
    i = 0
    while i<3:

        # Initialisation ip
        ip = socket.gethostbyname("raspberrypi.local")

        # Create a MFRC522 object
        MIFAREReader = MFRC522.MFRC522()

        # Scan for cards
        (status,TagType) = MIFAREReader.MFRC522_Request(MIFAREReader.PICC_REQIDL)

        # If a card is found
        if (status == MIFAREReader.MI_OK) :
            print "Objet detectee"
            t1 = time.time()

        if ((status == MIFAREReader.MI_ERR) and (time.time()-t1>5)):
            for wifi in wifi_ip:
                if ip[:3] == wifi[:3]:
                    print "Objet de Jean Dupont perdu en zone %d" %(wifi_ip.index(wifi)+1)
		    send_notification("Objet de Jean Dupont perdu", "en zone %d" %(wifi_ip.index(wifi)+1))
                    t1 = time.time() + 15
                    i+=1

thread1 = Thread(target=lost)
thread1.start()

# Create a fonction to detect the fall
def chute():
    while (firebase.get('/Notification/Notification_%d/Etat' %id_rasp, None) == 1):
        # Initialisation ip
        ip = socket.gethostbyname("raspberrypi.local")

        # Create a ADXL345 object
        adxl345 = ADXL345()

	# Take the acceleration from the 3 axes
        axes = adxl345.getAxes(True)

        # Detect if there is a movement
	if (abs(axes['x'])<2) and (abs(axes['y'])<2) and (abs(axes['z'])<2):
	    continue_reading = True
        if (abs(axes['x'])>2) or (abs(axes['y'])>2) or (abs(axes['z'])>2):
            t1 = time.time()
            print ("detection de mouvement")
            # Wait 0.5 s before detcting an other movement, in this case it is not a fall
            time.sleep(0.5)
            while (time.time()-t1<10):
                axes = adxl345.getAxes(True)
                # Detect any other movement
                if (abs(axes['x'])>1.3) or (abs(axes['y'])>1.3) or (abs(axes['z'])>1.3):
		    continue_reading = True
                    chute()
            # If no other movement during 10 secondes -> fall
            while ((time.time()-t1)>10) and (firebase.get('/Notification/Notification_%d/Etat' %id_rasp, None) == 1):
                # Send reminder every 5 secondes
                for wifi in wifi_ip:
                    if ip[:3] == wifi[:3]:
                       print("Monsieur Dupont est tombe il y a %s min en zone %d" %(format_time(time.time()-t1),wifi_ip.index(wifi)+1))
                       send_notification("Monsieur Dupont est tombe", "Il y a %s min en zone %d" %(format_time(time.time()-t1),wifi_ip.index(wifi)+1))
                       time.sleep(5)

chute()
thread1.join()
