# Pascal2016-compiler, INF2100
This is a compiler for the language Pascal2016, written during the fall of 2016 for the INF2100 course held at Institutt for informatikk (Ifi), Universitet i Oslo. Pascal2016 is a subset of Pascal, containing most of the original language, except for I/O-handling, floating point math. A specification for Pascal2016 (and additional information about the course) can be found in kompendium.pdf (in Norwegian).

Most of this code is written by me (Torkel Rogstad), some of it is written by the course lecturer Dag Langmyhr.

##### Building
```
gradle build
```
`gradle` is a build tool for Java applications. You can find it at gradle.org. After building, the runnable `.jar` is found at `build/libs/pascal2016.jar`

##### Usage
```
java -jar pascal2016.jar programName
```
Some test programs are located in the folder `programs`.
