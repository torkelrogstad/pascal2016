OLIB=build/libs/pas/static/libpas2016.o
ALIB=build/libs/pas/static/libpas2016.a
CLIB=src/pas/c/pas2016.c

lib:
	./gradlew build
	gcc -m32 -c $(CLIB) -o $(OLIB)
	ar rcs build/libs/pas/static/libpas2016.a $(OLIB)
