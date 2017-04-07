#include <sys/stat.h>
#include <sys/types.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <time.h>
#include <sys/time.h>
#include <pthread.h>
#include <fcntl.h>
#include <unistd.h>
#include <signal.h>
#include "structs.h"
#include "logserver.h"

typedef void (*sighandler_t)(int);

Cliente clientes[100];
Consulta *consultasArchivo;
int numConsultas, numArchivos, numCheckpoints = 0, numTiempo, flagCheckpoint = 1;
mode_t fifo_mode = S_IRUSR | S_IWUSR;
Archivo *archivos;

int main(int argc, char *argv[])
{
  int fdLec, indicePipe, nClientes = 0, resultadoLec, i = 0, indices[100];
  pthread_t threads[100];
  pthread_t threadCheckpoints;

  numConsultas = 0;
  consultasArchivo = (Consulta*) malloc(sizeof(Consulta));

  //Verificacion numero de parametros
  if(argc < 5 || argc > 11)
  {
    printf("Error en la cantidad de parametros.\nUso: ./logserver <cantidad logs> <log0> ... <logN> <nombrePipe> <periodo>");
    exit(0);
  }

  //Verificacion numero de logs
  if(atoi(argv[1]) > 7)
  {
    printf("Error en el numero de logs ingresados.\n");
    exit(1);
  }

  numTiempo = atoi(argv[argc - 1]);

  if(pthread_create(&threadCheckpoints, NULL, alarmManagement, NULL) != 0)
  {
    perror("Thread Checkpoint:");
    exit(1);
  }

  //Archivos
  numArchivos = atoi(argv[1]);
  archivos = (Archivo*) malloc(sizeof(Archivo) * numArchivos);

  for(i = 0; i < numArchivos; i++)
  {
    Archivo archivo;
    FILE* arch;
    int tam = 0, cont, cont2;
    char temp[20];

    strcpy(archivo.nomArchivo, argv[i + 2]);
    arch = fopen(argv[i + 2], "r");

    if(arch == NULL)
    {
      perror("Error Archivo");
      exit(0);
    }
    else
    {
      while(!feof(arch))
      {
        if(fgetc(arch) == '\n')
        {
          tam++;
        }
      }

      fclose(arch);
    }

    archivo.tam = tam;

    arch = fopen(argv[i + 2], "r");
    double **datos;

    datos = (double**) malloc(sizeof(double*) * tam);
    for(cont = 0; cont < tam+1; cont++)
    {
      datos[cont] = (double*) malloc(sizeof(double) * 18);
    }

    if(arch == NULL)
    {
      perror("Error Archivo");
      exit(0);
    }
    else
    {
      cont = 0, cont2 = 0;

      while(!feof(arch))
      {
        fscanf(arch, "%s", temp);
        datos[cont][cont2] = strtod(temp, NULL);
        cont2++;
        if(cont2 == 18)
        {
          cont++;
          cont2 = 0;
        }
      }

      fclose(arch);
    }

    archivo.datos = datos;

    archivos[i] = archivo;
  }

  //Creacion del pipe de lectura
  indicePipe = argc - 2;
  if(mkfifo(argv[indicePipe], fifo_mode) == -1)
  {
    perror("mkfifo 1");
    exit(2);
  }

  //Apertura de pipe
  if((fdLec = open(argv[indicePipe], O_RDONLY)) == -1)
  {
    perror("pipeLec");
    exit(3);
  }
  else
  {
    //Ciclo de recepcion
    while(i == 0)
    {
      Cliente c;
      int j, pasaCliente = 1;

      resultadoLec = read(fdLec, &c, sizeof(c));
      if(resultadoLec == -1)
      {
        perror("Error lectura:");
      }
      else
      {
        printf("Lei esto: %d %s\n", c.idCliente, c.nomPipe);

        j = 0;
        for(j = 0; j < nClientes && pasaCliente == 1; j++)
        {
          if(clientes[j].idCliente == c.idCliente)
          {
            pasaCliente = 0;
          }
        }

        //Creacion del Thread
        if(pasaCliente == 1)
        {
          clientes[nClientes] = c;
          indices[nClientes] = nClientes;

          pthread_create(&threads[nClientes], NULL, clientManagement, (void *) &indices[nClientes]);

          nClientes++;
        }
      }

      if(nClientes == 100)
      {
        printf("Numero maximo de clientes alcanzado. Gracias por usar la version de prueba.\n");
        i = 1;
      }
    }

    int t;
    for(t = 0; t < nClientes; t++)
    {
      pthread_join(threads[t], NULL);
    }
  }

  return 0;
}

int tiempo(char tipo[10], char valor[7], Archivo arch)
{
  int comp, col, i;

  if(strcmp(valor, "min") == 0)
  {
    comp = 99999999;
  }
  else if(strcmp(valor, "max") == 0)
  {
    comp = 0;
  }
  else
  {
    return -99999;
  }

  if(strcmp(tipo, "ejecucion") == 0)
  {
    col = 3;

    for(i = 0; i < arch.tam; i++)
    {
      if(strcmp(valor, "min") == 0)
      {
        if(comp > arch.datos[i][col] && arch.datos[i][col] != -1)
        {
          comp = arch.datos[i][col];
        }
      }
      else if(strcmp(valor, "max") == 0)
      {
        if(comp < arch.datos[i][col] && arch.datos[i][col] != -1)
        {
          comp = arch.datos[i][col];
        }
      }
    }

    return comp;
  }
  else if(strcmp(tipo, "espera") == 0)
  {
    col = 2;

    for(i = 0; i < arch.tam; i++)
    {
      if(strcmp(valor, "min") == 0)
      {
        if(comp > arch.datos[i][col] && arch.datos[i][col] != -1)
        {
          comp = arch.datos[i][col];
        }
      }
      else if(strcmp(valor, "max") == 0)
      {
        if(comp < arch.datos[i][col] && arch.datos[i][col] != -1)
        {
          comp = arch.datos[i][col];
        }
      }
    }

    return comp;
  }
  else if(strcmp(tipo, "cpu") == 0)
  {
    col = 5;

    for(i = 0; i < arch.tam; i++)
    {
      if(strcmp(valor, "min") == 0)
      {
        if(comp > arch.datos[i][col] && arch.datos[i][col] != -1)
        {
          comp = arch.datos[i][col];
        }
      }
      else if(strcmp(valor, "max") == 0)
      {
        if(comp < arch.datos[i][col] && arch.datos[i][col] != -1)
        {
          comp = arch.datos[i][col];
        }
      }
    }

    return comp;
  }
  else
  {
    return -99999;
  }
}

int procesos(char tipo[10], char valor[7], Archivo arch)
{
  int col = 4, existe = 0, proceso = atoi(tipo), i, resp;

  if(strcmp(valor, "cuantos") == 0)
  {
    for(i = 0; i < arch.tam && existe == 0; i++)
    {
      if(arch.datos[i][0] == proceso)
      {
        existe = 1;
        resp = arch.datos[i][col];
      }
    }

    if(existe == 1)
    {
      return resp;
    }
    else
    {
      return -88888;
    }
  }
  else
  {
    return -99999;
  }
}

int status(char tipo[10], char valor[7], Archivo arch)
{
  int comp, col = 10, i, cont = 0;

  if(strcmp(valor, "cuantos") != 0)
  {
    return -99999;
  }

  if(strcmp(tipo, "bien") == 0)
  {
    comp = 1;

    for(i = 0; i < arch.tam; i++)
    {
      if(arch.datos[i][col] == comp)
      {
        cont++;
      }
    }

    return cont;
  }
  else if(strcmp(tipo, "cancelado") == 0)
  {
    comp = 5;

    for(i = 0; i < arch.tam; i++)
    {
      if(arch.datos[i][col] == comp)
      {
        cont++;
      }
    }

    return cont;
  }
  else if(strcmp(tipo, "falla") == 0)
  {
    comp = 0;

    for(i = 0; i < arch.tam; i++)
    {
      if(arch.datos[i][col] == comp)
      {
        cont++;
      }
    }

    return cont;
  }
  else
  {
    return -99999;
  }
}

int numProceso(char tipo[10], char valor[7], Archivo arch)
{
  int col = 4, i, comp = atoi(tipo), cont = 0;

  if(strcmp(valor, "cuantos") == 0)
  {
    for(i = 0; i < arch.tam; i++)
    {
      if(arch.datos[i][col] == comp)
      {
        cont++;
      }
    }

    return cont;
  }
  else
  {
    return -99999;
  }
}

void *clientManagement(void* indice)
{
  int fdLectura, fdEscritura, creado = 0, salida = 0;
  int pos = *((int*) indice);
  Cliente c = clientes[pos];
  char nPipeLectura[20], temp[10], strPerror[30], primerMensaje[30];

  //Creacion de los pipes de comunciacion
  do
  {
    fdEscritura = open(c.nomPipe, O_WRONLY);
    if(fdEscritura == -1)
    {
      strcpy(strPerror, "Pipe Escritura ");
      sprintf(temp, "%d", c.idCliente);
      strcat(strPerror, temp);
      perror(strPerror);
      sleep(5);
    }
    else
    {
      creado = 1;
    }
  } while(creado == 0);

  strcpy(nPipeLectura, "stc"); //Server to Client
  sprintf(temp, "%d", c.idCliente);
  strcat(nPipeLectura, temp);
  printf("%s %d\n", nPipeLectura , (int) sizeof(nPipeLectura));

  if(mkfifo(nPipeLectura, fifo_mode) == -1)
  {
    strcpy(strPerror, "Mk Fifo ");
    sprintf(temp, "%d", c.idCliente);
    strcat(strPerror, temp);
    perror(strPerror);
    exit(0);
  }

  write(fdEscritura, nPipeLectura, sizeof(nPipeLectura));

  creado = 0;
  do
  {
    fdLectura = open(c.nomPipe, O_RDONLY);
    if(fdLectura == -1)
    {
      strcpy(strPerror, "Pipe Lectura ");
      sprintf(temp, "%d", c.idCliente);
      strcat(strPerror, temp);
      perror(strPerror);
      sleep(5);
    }
    else
    {
      creado = 1;
    }
  } while(creado == 0);

  read(fdLectura, primerMensaje, sizeof(primerMensaje));

  if(strcmp(primerMensaje, "Inicio?") == 0)
  {
    write(fdEscritura, "ACK", 3);

    while(salida == 0)
    {
      Consulta cons;
      int i, valid = 0, existe = 0, pos = 0;

      read(fdLectura, &cons, sizeof(cons));

      if((int) sizeof(cons) != 3)
      {
        if(numConsultas > 0)
        {
          for(i = 0; i < numConsultas; i++)
          {
            if(strcmp(cons.cons, consultasArchivo[i].cons) == 0 && strcmp(cons.tipo, consultasArchivo[i].tipo) == 0 && strcmp(cons.valor, consultasArchivo[i].valor) == 0)
            {
              existe = 1;
              pos = i;
            }
          }
        }

        Archivo arch;
        for(i = 0; i < numArchivos && valid == 0; i++)
        {
          if(strcmp(archivos[i].nomArchivo, cons.nomLog) == 0)
          {
            arch = archivos[i];
            valid = 1;
          }
        }

        if(valid == 1)
        {
          int resp;

          if(strcmp(cons.cons, "T") == 0)
          {
            resp = tiempo(cons.tipo, cons.valor, arch);
          }
          else if(strcmp(cons.cons, "P") == 0)
          {
            resp = procesos(cons.tipo, cons.valor, arch);
          }
          else if(strcmp(cons.cons, "S") == 0)
          {
            resp = status(cons.tipo, cons.valor, arch);
          }
          else if(strcmp(cons.cons, "NP") == 0)
          {
            resp = numProceso(cons.tipo, cons.valor, arch);
          }
          else
          {
            strcpy(cons.resultado, "Consulta con mal Formato");
            write(fdEscritura, "Consulta con mal Formato", 24);
          }

          if(resp == -99999)
          {
            strcpy(cons.resultado, "Consulta con mal Formato");
            write(fdEscritura, "Consulta con mal Formato", 24);
          }
          else if(resp == -88888)
          {
            strcpy(cons.resultado, "Error en la consulta");
            write(fdEscritura, "Error en la Consulta", 20);
          }
          else
          {
            sprintf(cons.resultado, "%d", resp);
            write(fdEscritura, &resp, sizeof(resp));
          }
        }

        if(existe == 1)
        {
          cons.vecesConsultada = cons.vecesConsultada + 1;
          consultasArchivo[pos] = cons;
        }
        else
        {
          consultasArchivo = (Consulta*) realloc(consultasArchivo, sizeof(Consulta) * (numConsultas + 1));
          consultasArchivo[numConsultas] = cons;
          numConsultas++;
        }
      }
      else
      {
        salida = 1;
      }
    }
  }

  close(fdEscritura);
  close(fdLectura);

  pthread_exit(NULL);
}

sighandler_t clockHandler(void)
{
  flagCheckpoint = 1;
}

void impresionArchivoCheckpoint()
{
  FILE *f;
  int i;
  char nombre[20], numero[5];

  printf("El archivo de checkpoint sera creado en este momento.\n");

  sprintf(numero, "%d", numCheckpoints);
  strcpy(nombre, "CheckPoint_");
  strcat(nombre, numero);

  f = fopen(nombre, "w");

  for(i = 0; i < numConsultas; i++)
  {
    fprintf(f, "%d %s %s %s %s %s %d\n", consultasArchivo[i].idCliente, consultasArchivo[i].nomLog, consultasArchivo[i].cons, consultasArchivo[i].tipo, consultasArchivo[i].valor, consultasArchivo[i].resultado, consultasArchivo[i].vecesConsultada);
  }

  fclose(f);
}

void *alarmManagement(void* x)
{
  signal(SIGALRM, (sighandler_t) clockHandler);

  alarm(numTiempo);

  while(1)
  {
    if(flagCheckpoint == 1)
    {
      impresionArchivoCheckpoint();
      flagCheckpoint = 0;
      numCheckpoints++;
      alarm(numTiempo);
    }
  }

  pthread_exit(NULL);
}
