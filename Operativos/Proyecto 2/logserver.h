/*
  Archivo: logserver.h
  Autores: Alejandro Guayara y Luis Urdaneta
  Contenido: Prototipos de funciones que maneja el servidor de logs.
  Fecha ultima modificacion:
*/

#ifndef LOGSERVER__H
#define LOGSERVER__H

#include "structs.h"
typedef void (*sighandler_t)(int);

void *alarmManagement(void*);

sighandler_t clockHandler(void);

void impresionArchivoCheckpoint();

/*
  Funcion: clientManagement
  Parametros de entrada: Apuntador al indice que contiene la posicion del cliente en el arreglo de clientes.
  Valor de Salida: no tiene.
  Descripcion: Realiza el manejo de clientes en thread por aparte. Realiza la creacion y apertura los pipes personales
  entre cliente y servidor. Encargado del manejo de la logica de las consultas enviadas por el cliente.
*/
void *clientManagement(void* indice);

/*
  Funcion: tiempo
  Parametros de entrada: tipo hace referencia al tipo de la consulta, valor hace referencia al valor de la consulta,
  arch hace referencia al objeto de la estructura Archvio que representa los datos del log en el que se realizara la consulta.
  Valor de Salida: Resultado de la consulta.
  Descripcion: Realiza la consulta dentro del log seleccionado y obtiene el resultado del mismo.
*/
int tiempo(char tipo[10], char valor[7], Archivo arch);

/*
  Funcion: procesos
  Parametros de entrada: tipo hace referencia al tipo de la consulta, valor hace referencia al valor de la consulta,
  arch hace referencia al objeto de la estructura Archvio que representa los datos del log en el que se realizara la consulta.
  Valor de Salida: Resultado de la consulta.
  Descripcion: Realiza la consulta dentro del log seleccionado y obtiene el resultado del mismo.
*/
int procesos(char tipo[10], char valor[7], Archivo arch);

/*
  Funcion: status
  Parametros de entrada: tipo hace referencia al tipo de la consulta, valor hace referencia al valor de la consulta,
  arch hace referencia al objeto de la estructura Archvio que representa los datos del log en el que se realizara la consulta.
  Valor de Salida: Resultado de la consulta.
  Descripcion: Realiza la consulta dentro del log seleccionado y obtiene el resultado del mismo.
*/
int status(char tipo[10], char valor[7], Archivo arch);

/*
  Funcion: numProceso
  Parametros de entrada: tipo hace referencia al tipo de la consulta, valor hace referencia al valor de la consulta,
  arch hace referencia al objeto de la estructura Archvio que representa los datos del log en el que se realizara la consulta.
  Valor de Salida: Resultado de la consulta.
  Descripcion: Realiza la consulta dentro del log seleccionado y obtiene el resultado del mismo.
*/
int numProceso(char tipo[10], char valor[7], Archivo arch);

#endif //LOGSERVER__H
