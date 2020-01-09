import win32com.client as wincl
import sys
import os
import time
import subprocess
import serial

speak = wincl.Dispatch("SAPI.SpVoice")
arduino = serial.Serial('COM8', 9600, timeout=0)

time.sleep(1)
letter = "x"
arduino.write(bytes(letter, "utf8"))
arduino.readlines()[0].encode("utf8")
speak.Speak(letter)
