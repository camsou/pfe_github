
#!/usr/bin/env python3

import  firebase_admin
from    firebase_admin import credentials
from    firebase_admin import messaging

# This registration token comes from the client FCM SDKs.
#registration_token = 'dTahG-Pwjf4:APA91bFMd7bSWwO0EBHrOl3nunkWNqrITzBUTY-FUOicWWouEO5BuvKSz-Y2haRG5GazGYCp6YPAA6ucs2RmBhW3WEPNlX2gwCbjBemghGD-SLpXfdly-yCFWPU-ObuXzBA21--xjeyC'

#Tel Camille
#registration_token = 'fqtRUhSOqUI:APA91bGSfdTkXsmiPlE1EGS99jLJZSj-IZMWrAX7oe4V3eCsjUZOwE0LryToG5-bqU2ECB7RUqL17xU_EwjhypvTE1Or679wzMG8W32EPqNB56SO0GpO7P4ABfdczTngxzkrFyi5BqIn'

#Tel Pierre
registration_token = 'ddi9ZoU1D9U:APA91bEdOneTLrmqLozYhzx1sHmbwgKp0WJRHWB5jlGHnVvHWdRXfY3BOyjgwj5EpUGRR8ETVnQv_b-7Qt6sK-6wVrd3YVxow8dQDotPwvDkzN_m6-LHK3GgQZ-TssL6zLgaFiY7835d'

##
# @brief    Connect to Firebase Admin API.
#           Necessary to send notifications.
def firebase_connect():
    if '[DEFAULT]' not in firebase_admin._apps:
        cred = credentials.Certificate('./firebase_admin_sdk.json')
        firebase_admin.initialize_app(cred,{'databaseURL': 'https://detect-n-alert.firebaseio.com/'})

##
# @brief        Send a Notification.
#
# @param title  The notification headline.
# @param body   The notification message.
def send_notification(notif_title='default_title', notif_body='default_body'):
    # See documentation on defining a message payload.
    message = messaging.Message(
        android=messaging.AndroidConfig(
            notification=messaging.AndroidNotification(
                title=notif_title,
                body=notif_body,
                click_action="OPEN_ACTIVITY_1"
            )
        ),
        token=registration_token
    )

    response = messaging.send(message)
    print('Successfully sent message:', response)

def send_notification_color(notif_color="00ff00"):
    message = messaging.Message(
        android=messaging.AndroidConfig(
            notification=messaging.AndroidNotification(
                title='Test',
                body='Test',
                color=notif_color,
                click_action="OPEN_ACTIVITY_1"
            ),
        ),
        token=registration_token
    )
    response = messaging.send(message)
    print('Successfully sent message:', response)
