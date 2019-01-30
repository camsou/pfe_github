#!/usr/bin/env python3
# -*- coding: utf8 -*-
"""
@author: mpoutet_plopez
"""

import RPi.GPIO as GPIO
import time
import socket
import board
import busio
import adafruit_adxl34x
from   adxl345_py3      import ADXL345

def chute():

    # Connection with the bus
    i2c = busio.I2C(board.SCL, board.SDA)

    # Create an accelerometer object
    accelerometer = adafruit_adxl34x.ADXL345(i2c)
    accelerometer.enable_freefall_detection(time=70)
    # alternatively you can specify attributes when you enable freefall detection for more contr$
    # accelerometer.enable_freefall_detection(threshold=10,time=25)

    fichier_name = input("Nom du fichier test : \n")
    f = open('./%s.txt' %fichier_name, 'w')
    t = time.time()

    while (time.time()-t<50):
        print(time.time()-t)
        print("Dropped: %s\n"%accelerometer.events["freefall"])
        f.write("Dropped: %s\n"%accelerometer.events["freefall"])
        time.sleep(0.5)

chute()

