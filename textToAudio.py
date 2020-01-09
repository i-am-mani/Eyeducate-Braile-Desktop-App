import win32com.client as wincl
import sys

speak = wincl.Dispatch("SAPI.SpVoice")
# speak.speak("hello world")
file = open("pdf.txt", "r", encoding="utf8")

file2 = open("prevPdf.txt", "r", encoding="utf8")

fileLines = file.readlines()
file2Lines = file2.readlines()

file.close()
file2.close()

i = 0
continueRun = True

if (len(file2Lines) > 0 and fileLines[0] == file2Lines[0]):
    i = len(file2Lines) - 1

file2 = open("prevPdf.txt", "a+", encoding="utf8")

if (i == len(fileLines) - 1):
    file2.truncate(0)
    i = 0

while (i < len(fileLines)):
    if (continueRun):
        file2.write(fileLines[i])
        speak.Speak(fileLines[i])
        file2.flush()
        print(fileLines[i])
        i += 1
