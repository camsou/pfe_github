# ADXL345 Python library for Raspberry Pi 
#
# author:  Jonathan Williamson
# license: BSD, see LICENSE.txt included in this package
# 
# This is a Raspberry Pi Python implementation to help you get started with
# the Adafruit Triple Axis ADXL345 breakout board:
# http://shop.pimoroni.com/products/adafruit-triple-axis-accelerometer

import smbus
from time import sleep
from struct import unpack

# select the correct i2c bus for this revision of Raspberry Pi
revision = ([l[12:-1] for l in open('/proc/cpuinfo','r').readlines() if l[:8]=="Revision"]+['0000'])[0]
bus = smbus.SMBus(1 if int(revision, 16) >= 4 else 0)

# ADXL345 constants
EARTH_GRAVITY_MS2   = 9.80665
SCALE_MULTIPLIER    = 0.004

DATA_FORMAT         = 0x31
BW_RATE             = 0x2C
POWER_CTL           = 0x2D
DEVID               = 0x00
OFSX                = 0x1E
OFSY                = 0x1F
OFSZ                = 0x20
THRESH_ACT          = 0x24 # Activity threshold
THRESH_INACT        = 0x25 # Inactivity threshold
TIME_INACT          = 0x26 # Inactivity time
ACT_INACT_CTL       = 0x27 # Axis enable control for [in]activity detection
THRESH_FF           = 0x28 # Free-fall threshold
TIME_FF             = 0x29 # Free-fall time
INT_ENABLE          = 0x2E # Interrupt enable control
INT_MAP             = 0x2F # Interrupt mapping control
INT_SOURCE          = 0x30 # Source of interrupts
DATAX0              = 0x32 # X-axis data 0
DATAX1              = 0x33 # X-axis data 1
DATAY0              = 0x34 # Y-axis data 0
DATAY1              = 0x35 # Y-axis data 1
DATAZ0              = 0x36 # Z-axis data 0

BW_RATE_1600HZ      = 0x0F
BW_RATE_800HZ       = 0x0E
BW_RATE_400HZ       = 0x0D
BW_RATE_200HZ       = 0x0C
BW_RATE_100HZ       = 0x0B
BW_RATE_50HZ        = 0x0A
BW_RATE_25HZ        = 0x09

RANGE_2G            = 0x00
RANGE_4G            = 0x01
RANGE_8G            = 0x02
RANGE_16G           = 0x03

MEASURE             = 0x08
AXES_DATA           = 0x32



class ADXL345:

    address = None

    def __init__(self, address = 0x53):        
        self.address = address
        self.setBandwidthRate(BW_RATE_100HZ)
        self.setRange(RANGE_8G)
        self.enableMeasurement()

    def enableMeasurement(self):
        bus.write_byte_data(self.address, POWER_CTL, MEASURE)

    def setBandwidthRate(self, rate_flag):
        bus.write_byte_data(self.address, BW_RATE, rate_flag)

    # set the measurement range for 10-bit readings
    def setRange(self, range_flag):
        value = bus.read_byte_data(self.address, DATA_FORMAT)

        value &= ~0x0F;
        value |= range_flag;  
        value |= 0x08;

        bus.write_byte_data(self.address, DATA_FORMAT, value)
    
    # returns the current reading from the sensor for each axis
    #
    # parameter gforce:
    #    False (default): result is returned in m/s^2
    #    True           : result is returned in gs
    def getAxes(self, gforce = False):
        bytes = bus.read_i2c_block_data(self.address, AXES_DATA, 6)
        
        x = bytes[0] | (bytes[1] << 8)
        if(x & (1 << 16 - 1)):
            x = x - (1<<16)

        y = bytes[2] | (bytes[3] << 8)
        if(y & (1 << 16 - 1)):
            y = y - (1<<16)

        z = bytes[4] | (bytes[5] << 8)
        if(z & (1 << 16 - 1)):
            z = z - (1<<16)

        x = x * SCALE_MULTIPLIER 
        y = y * SCALE_MULTIPLIER
        z = z * SCALE_MULTIPLIER

        if gforce == False:
            x = x * EARTH_GRAVITY_MS2
            y = y * EARTH_GRAVITY_MS2
            z = z * EARTH_GRAVITY_MS2

        x = round(x, 4)
        y = round(y, 4)
        z = round(z, 4)

        return {"x": x, "y": y, "z": z}

    def acceleration(self):
        """The x, y, z acceleration values returned in a 3-tuple in m / s ^ 2."""
        x, y, z = unpack('<hhhhh',bus.read_i2c_block_data(self.address, AXES_DATA, 6))
        x = x * SCALE_MULTIPLIER * EARTH_GRAVITY_MS2
        y = y * SCALE_MULTIPLIER * EARTH_GRAVITY_MS2
        z = z * SCALE_MULTIPLIER * EARTH_GRAVITY_MS2
        return (x, y, z)

    

if __name__ == "__main__":
    # if run directly we'll just create an instance of the class and output 
    # the current readings
    adxl345 = ADXL345()
    
    axes = adxl345.getAxes(True)
    print("ADXL345 on address 0x%x:" % (adxl345.address))
    print("   x = %.3fG" % ( axes['x'] ))
    print("   y = %.3fG" % ( axes['y'] ))
    print("   z = %.3fG" % ( axes['z'] ))
