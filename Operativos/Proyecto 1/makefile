# PROYECTO #1 - Makefile para generar 2 ejecutables
# Alejandro Guayara - Luis Urdaneta

all: serversec serverconc

serverconc: serverconc.o
	gcc serverconc.o -o serverconc -lpthread

serverconc.o: serverconc.c
	gcc -c serverconc.c -lpthread

serversec: serversec.o
	gcc serversec.o -o serversec

serversec.o: serversec.c
	gcc -c serversec.c

clean:
	rm *.o serversec serverconc
