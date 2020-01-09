import win32com.client as wincl
import sys

speak = wincl.Dispatch("SAPI.SpVoice")
speak.Volume = 100
speak.Rate = -2
print(help(speak))
speak.Voice = speak.GetVoices()[1]  # print(voices)
speak.speak("Data descriptors inherited from return self")

