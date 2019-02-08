#!/usr/bin/env python3
# -*- coding: utf8 -*-
"""
@author: mpoutet_plopez
"""

import RPi.GPIO as GPIO
import MFRC522_py3 as MFRC522
#import signal
import time
import socket
#import sys
#import os
import board
import busio
import adafruit_adxl34x
import firebase_admin
#from   adxl345_py3      import ADXL345
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
ref = db.reference('/Notification')
ref.update({'/Notification_%d/Etat' %id_rasp : 1})

# Welcome message
print("Welcome to Detect'N'Alert example")
print("Press Ctrl-C to stop.")

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

    #id_obj = [148, 57, 135, 30, 52]
    id_obj = [172, 132, 233, 12, 205]
    t1 = time.time()
    i = 0

    while i<3 :

        print(i)

        # Initialisation ip
        ip = socket.gethostbyname("raspberrypi.local")

        # Create a MFRC522 object
        MIFAREReader = MFRC522.MFRC522()

        # Scan for cards
        (status,TagType) = MIFAREReader.MFRC522_Request(MIFAREReader.PICC_REQIDL)

        # If a card is found
        if (status == MIFAREReader.MI_OK):

            # Scan for cards and get its uid
            uid = MIFAREReader.MFRC522_Anticoll()[1]

            # If good card
            if uid == id_obj :
                print("Objet detectee")
                t1 = time.time()
                i =  0

            time.sleep(4)

        if (time.time()-t1>5):
            for wifi in wifi_ip:
                if ip[:3] == wifi[:3]:
                    print("Objet de Jean Dupont perdu en zone %d" %(wifi_ip.index(wifi)+1))
                    send_notification("Objet de Jean Dupont perdu", "en zone %d" %(wifi_ip.index(wifi)+1))
                    t1 = time.time() + 11
                    i+=1
                    time.sleep(4)
        else :
            time.sleep(4)

#lost()
thread1 = Thread(target=lost)
thread1.start()

# Create a function to detect the fall
def chute():
    while (firebase.get('/Notification/Notification_%d/Etat' %id_rasp, None) == 1):
        # Initialisation ip
        ip = socket.gethostbyname("raspberrypi.local")

        # Connection with the bus
        i2c = busio.I2C(board.SCL, board.SDA)

        # Create an accelerometer object
        accelerometer = adafruit_adxl34x.ADXL345(i2c)
        # accelerometer.enable_freefall_detection(time=70)
        # alternatively you can specify attributes when you enable freefall detection for more control:
        # accelerometer.enable_freefall_detection(threshold=10,time=25)

        while True:
           print("%f %f %f"%accelerometer.acceleration)
           time.sleep(0.5)
           if (abs(accelerometer.acceleration[0])>19 and abs(accelerometer.acceleration[1])>19) or (abs(accelerometer.acceleration[0])>19 and abs(accelerometer.acceleration[2])>19) or (abs(accelerometer.$
                t1 = time.time()
                time.sleep(5)
                for wifi in wifi_ip:
                    if ip[:3] == wifi[:3]:
                        while (time.time()-t1<10):
                            if (abs(accelerometer.acceleration[0])<12 and abs(accelerometer.acceleration[1])<12 and abs(accelerometer.acceleration[2])<12):
                                bool = 1
                            else :
                                bool = 0
                        if (bool == 1):
                            print("Monsieur Dupont est tombe il y a %s min en zone %d" %(format_time(time.time()-t1),wifi_ip.index(wifi)+1))
                            send_notification("Monsieur Dupont est tombe", "Il y a %s min en zone %d" %(format_time(time.time()-t1),wifi_ip.index(wifi)+1))
                            time.sleep(5)
                # Send remember every 5 seconds
                while (firebase.get('/Notification/Notification_%d/Etat' %id_rasp, None) == 1) and (bool == 1):
                    print("Monsieur Dupont est tombe il y a %s min en zone %d" %(format_time(time.time()-t1),wifi_ip.index(wifi)+1))
                    send_notification("Monsieur Dupont est tombe", "Il y a %s min en zone %d" %(format_time(time.time()-t1),wifi_ip.index(wifi)+1))
                    time.sleep(5)


chute()
thread1.join()

