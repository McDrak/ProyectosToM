/*
  Archivo: structs.h
  Autores: Alejandro Guayara y Luis Urdaneta
  Contiene: Estructuras utilizadas para guardar datos obtenidos de los archivos.
  Fecha de ultima modificacion:
*/

#ifndef STRUCTS__H
#define STRUCTS__H

/*
  Estructura: Cliente
  Contenido: idCliente hace referencia al entero que identifica al cliente. nomPipe hace referencia al
  nombre del pipe de escritura que creara el servidor para comunicarse con el cliente.
*/
typedef struct Cliente
{
  int idCliente;
  char nomPipe[20];
} Cliente;

/*
  Estructura: Archivo
  Contenido: nomArchivo hace referencia al nombre del archivo de log. tam hace referencia al numero de
  lineas que tiene el archivo. datos es la matriz que contiene todos los valores del log.
*/
typedef struct Archivo
{
  char nomArchivo[10];
  int tam;
  double **datos;
} Archivo;

/*
  Estructura: Consulta
  Contenido: idCliente hace referencia al entero que identifica al cliente y el cual realizo la consulta.
  cons hace referencia a la consulta que se va a realizar (T, P, S, NP). tipo hace referencia al tipo de la
  consulta que se va a realizar. valor hace referencia al valor deseado de la consulta. nomLog es el nombre
  del archivo de log sobre el que se realizara la consulta. resulta hace referencia al resultado final de la
  consulta. vecesConsultada es la cantidad de veces que se a realizado la consulta.
*/
typedef struct Consulta
{
  int idCliente;
  char cons[2];
  char tipo[10];
  char valor[7];
  char nomLog[10];
  char resultado[50];
  int vecesConsultada;
} Consulta;

#endif //STRUCTS__H
