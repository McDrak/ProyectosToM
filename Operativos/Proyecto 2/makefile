# PROYECTO #1 - Makefile para generar 2 ejecutables
# Alejandro Guayara - Luis Urdaneta

all: logserver client

logserver: logserver.o
	gcc logserver.o -o logserver -lpthread

logserver.o: logserver.c
	gcc -c logserver.c -lpthread

client: client.o
	gcc client.o -o client

client.o: client.c
	gcc -c client.c

clean:
	rm *.o *.h.gch client logserver
