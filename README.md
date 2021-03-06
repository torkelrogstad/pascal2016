# Pascal2016-compiler, INF2100
This is a compiler for the language Pascal2016, written during the fall of 2016 for the INF2100 course held at Institutt for informatikk (Ifi), Universitet i Oslo. Pascal2016 is a subset of Pascal, containing most of the original language, except for I/O-handling, floating point math. A specification for Pascal2016 (and additional information about the course) can be found in kompendium.pdf (in Norwegian).

Most of this code is written by me (Torkel Rogstad), some of it is written by the course lecturer Dag Langmyhr.

##### Compatability
At the moment, both building the compiler and the compiler itself (and the binaries generated by it) only works on Linux. I will port it to macOS in the near future, but probably not to Windows.

##### Building
You ned `ant` to build this project. Install it with your package manager of choice, if you don't already have it. On Debian based systems (Ubuntu, Linux Mint, etc.) you should run `apt-get install ant`. To build the project, simply run
```
ant
```
After building, the runnable `.jar` is called `pascal2016.jar`. If you can't or won't build the project yourself, `pascal2016-PREBUILT.jar` is provided. 
##### Usage
```
java -jar pascal2016.jar program.pas
```
An example run would be `java -jar pascal2016.jar programs/10star.pas`. Some test programs are located in the folder `programs`. Binaries are placed in the folder `binaries`. If a file with that name exists, the binaries are placed from wherever the compiler was started.
