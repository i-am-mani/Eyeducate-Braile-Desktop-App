import win32com.client as wincl
import sys
import os
import time
import subprocess
import serial

# cmd = "arduino --upload C:\\Users\\Home\\Documents\\Arduino\\sketch_dec08a\\sketch_dec08a.ino"

# returns output as byte string

# using decode() function to convert byte string to string

lst = []

file = open("alphnum.txt", "r", encoding="utf8")
# os.chdir("E:\\arduino-nightly-windows\\arduino-nightly")
# returned_output = subprocess.check_output(cmd)

# time.sleep(1)
# print(returned_output)
speak = wincl.Dispatch("SAPI.SpVoice")
arduino = serial.Serial('COM8', 9600, timeout=0)
time.sleep(2)
# arduino.write(23)
# print(arduino.readlines())
# fileLines=file.readlines()
delay = sys.argv[1]
i = 0
while (i < len(fileLines)):
    arduino.write(bytes(fileLines[i], "utf8"))
    speak.Speak(fileLines[i])
    i += 1
    time.sleep(int(delay))
