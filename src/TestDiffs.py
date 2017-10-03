#! /usr/bin/env python3

import subprocess
import sys
import os


def usage():
    print("Usage:")

    print("actual:  ", end=' ')
    for arg in sys.argv:
        print(arg, end=' ')
    print()

    print("expected: ./TestDiffs.py filename.pas -flag")
    quit()

flags = list()
filename = None

try:
    for s in sys.argv:
        if s[-4:] == ".pas":
            if filename is not None:
                print("Error! Please specify just one .pas-filename")
                usage()

            filename = s
            name = filename[:-4]

        if s[:1] == "-":
            flags.append(s)

except Exception as e:
    print("Error!")
    usage()

if filename is None:
    print("Please specify a filename")
    usage()

subprocess.call("clear && printf '\e[3J'", shell=True)

# build
subprocess.call("ant")

compilator_location = "pascal2016.jar"
if not os.path.isfile(compilator_location):
    print(compilator_location + " does not exist!")
    quit()

compile_command = "java -jar " + compilator_location

test_path = "programmer/test/"
if not os.path.isfile(test_path + filename):
    print(test_path + filename + " does not exist!")

command = compile_command + " " + test_path + filename
for flag in flags:
    command += " " + flag
print(command)

# compile
ret_code = subprocess.call(command, shell=True)

if "-testparser" in flags:
    extension = ".log"
    part = "2"

elif "-testbinder" in flags:
    extension = ".log"
    part = "3"

elif "-testscanner" in flags:
    extension = ".log"
    part = "1"

else:
    extension = ".s"
    part = "4"

test = ("programmer/test/" + name + extension)

ref = ("programmer/fasit/del-" + part + "/" + name + extension)

# test diffs
subprocess.call("colordiff -ywd " + test + " " + ref, shell=True)#+ " > temp.txt", shell=True)
# subprocess.call("cat -n temp.txt", shell=True)
