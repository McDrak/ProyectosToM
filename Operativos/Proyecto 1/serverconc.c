#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <time.h>
#include <sys/time.h>
#include <pthread.h>

struct StuffEntrada
{
  char *consulta;
  char *tipo;
  char *valor;
  int tam;
  int **datosTP;
  int *datosSNP;
  int x;
  int pos;
};

struct StuffSalida
{
  char *resp;
  char *consulta;
  int hora;
  int min;
  int sec;
};

struct StuffEntrada *params;
struct StuffSalida *terminados;


void *tiempo(void *struc)
{
  int indice;
  struct StuffEntrada s;
  struct StuffSalida *ss;
  struct tm * timeinfo;
  time_t rawtime;
  int **datos, tam, i, comp, tiempo, pos;
  char *valor, *resp;

  indice = *((int *) struc);
  s = params[indice];
  tam = s.tam;
  resp = (char*) malloc(sizeof(char) * 20);
  datos = (int**) malloc(sizeof(int) * 2);
  valor = s.valor;
  ss = (struct StuffSalida*) malloc(sizeof(struct StuffSalida));

  for(i = 0; i < 2; i++)
  {
    datos[i] = (int*) malloc(sizeof(int) * tam);
  }
  datos = s.datosTP;

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
    comp = -1;
  }

  for(i = 0; i < tam; i++)
  {
    if(strcmp(valor, "min") == 0)
    {
      if(comp > datos[1][i] && datos[1][i] != -1)
      {
        tiempo = datos[1][i];
        comp = tiempo;
        pos = datos[0][i];
      }
    }
    else if(strcmp(valor, "max") == 0)
    {
      if(comp < datos[1][i] && datos[1][i] != -1)
      {
        tiempo = datos[1][i];
        comp = tiempo;
        pos = datos[0][i];
      }
    }
  }

  if(comp != -1)
  {
    sprintf(resp, "%d %d", tiempo, pos);
  }
  else
  {
    resp = (char*) "Error en la consulta";
  }

  time(&rawtime);
  timeinfo = localtime(&rawtime);

  ss->resp = resp;
  ss->consulta = s.consulta;
  ss->hora = timeinfo->tm_hour;
  ss->min = timeinfo->tm_min;
  ss->sec = timeinfo->tm_sec;

  terminados[indice] = *ss;

  pthread_exit(NULL);
}

void *procesos(void *struc)
{
  int indice = *((int *) struc);
  struct StuffEntrada s = params[indice];
  struct StuffSalida *ss;
  struct tm * timeinfo;
  time_t rawtime;
  int **datos, tam, i, proc;
  char *resp;

  tam = s.tam;
  resp = (char*) malloc(sizeof(char) * 20);
  datos = (int**) malloc(sizeof(int) * 2);
  ss = (struct StuffSalida*) malloc(sizeof(struct StuffSalida));

  for(i = 0; i < 2; i++)
  {
    datos[i] = (int*) malloc(sizeof(int) * tam);
  }
  datos = s.datosTP;

  for(i = 0; i < tam; i++)
  {
    if(datos[0][i] == s.x)
    {
      proc = datos[1][i];
    }
  }

  sprintf(resp, "%d", proc);

  time(&rawtime);
  timeinfo = localtime(&rawtime);

  ss->resp = resp;
  ss->consulta = s.consulta;
  ss->hora = timeinfo->tm_hour;
  ss->min = timeinfo->tm_min;
  ss->sec = timeinfo->tm_sec;

  terminados[indice] = *ss;

  pthread_exit(NULL);
}

void *status(void *struc)
{
  int indice = *((int *) struc);
  struct StuffEntrada s = params[indice];
  struct StuffSalida *ss;
  struct tm * timeinfo;
  time_t rawtime;
  int *datos, tam, comp, i, cont;
  char *resp;

  tam = s.tam;
  resp = (char*) malloc(sizeof(char) * 20);
  ss = (struct StuffSalida*) malloc(sizeof(struct StuffSalida));

  datos = (int*) malloc(sizeof(int) * tam);
  datos = s.datosSNP;

  if(strcmp(s.tipo, "bien") == 0)
  {
    comp = 1;
  }
  else if(strcmp(s.tipo, "cancelado") == 0)
  {
    comp = 5;
  }
  else if(strcmp(s.tipo, "falla") == 0)
  {
    comp = 0;
  }
  else
  {
    comp = -1;
  }

  for(i = 0; i < tam; i++)
  {
    if(datos[i] == comp)
    {
      cont++;
    }
  }

  if(comp != -1)
  {
    sprintf(resp, "%d", cont);
  }
  else
  {
    resp = (char*) "Error en la consulta";
  }

  time(&rawtime);
  timeinfo = localtime(&rawtime);

  ss->resp = resp;
  ss->consulta = s.consulta;
  ss->hora = timeinfo->tm_hour;
  ss->min = timeinfo->tm_min;
  ss->sec = timeinfo->tm_sec;

  terminados[indice] = *ss;

  pthread_exit(NULL);
}

void *numProcesos(void *struc)
{
  int indice = *((int *) struc);
  struct StuffEntrada s = params[indice];
  struct StuffSalida *ss;
  struct tm * timeinfo;
  time_t rawtime;
  int *datos, tam, i, cont;
  char *resp;

  tam = s.tam;
  resp = (char*) malloc(sizeof(char) * 20);
  ss = (struct StuffSalida*) malloc(sizeof(struct StuffSalida));

  datos = (int*) malloc(sizeof(int) * tam);
  datos = s.datosSNP;

  for(i = 0; i < tam; i++)
  {
    if(datos[i] == s.x)
    {
      cont++;
    }
  }

  sprintf(resp, "%d", cont);

  time(&rawtime);
  timeinfo = localtime(&rawtime);

  ss->resp = resp;
  ss->consulta = s.consulta;
  ss->hora = timeinfo->tm_hour;
  ss->min = timeinfo->tm_min;
  ss->sec = timeinfo->tm_sec;

  terminados[indice] = *ss;

  pthread_exit(NULL);
}

int main(int argc, char *argv[])
{
  struct timeval tval_before, tval_after, tval_result;
  int tam, i, j, t, contHechos, contCorrectos;
  FILE *consulta, *logfile, *salida;
  char *cons, *tipo, *valor, *temp;

  cons = (char*) malloc(sizeof(char) * 3);
  tipo = (char*) malloc(sizeof(char) * 12);
  valor = (char*) malloc(sizeof(char) * 8);
  temp = (char*) malloc(sizeof(char) * 20);
  contHechos = 0;
  contCorrectos = 0;
  gettimeofday(&tval_before, NULL);

  if(argc != 5)
  {
    printf("Error numero de parametros.\nUso - ./serverconc [archivo consulta] [numero consultas] [archivo datos] [archivo salida]");
    exit(0);
  }

  logfile = fopen(argv[3], "r");

  if(logfile == NULL)
  {
    printf("Error al abrir el log.\n");
    exit(1);
  }
  else
  {
    tam = 0;
    while(!feof(logfile))
    {
      if(fgetc(logfile) == '\n')
      {
        tam++;
      }
    }
    fclose(logfile);
  }

  int indices[tam];
  params = (struct StuffEntrada*) malloc(sizeof(struct StuffEntrada) * tam);
  terminados = (struct StuffSalida*) malloc(sizeof(struct StuffSalida) * tam);
  pthread_t threads[tam];

  consulta = fopen(argv[1], "r");
  logfile = fopen(argv[3], "r");
  salida = fopen(argv[4], "w");

  if(consulta == NULL || salida == NULL || logfile == NULL)
  {
    printf("Error al abrir algun archivo\n");
    exit(1);
  }
  else
  {
    double datos[tam][18];
    i = 0;
    j = 0;
    t = 0;

    while(!feof(logfile))
    {
      fscanf(logfile, "%s", temp);
      datos[i][j] = strtod(temp, NULL);
      j++;
      if(j == 18)
      {
        i++;
        j = 0;
      }
    }

    fclose(logfile);

    for(t = 0; t < atoi(argv[2]) && !feof(consulta); t++)
    {
	  struct StuffEntrada s;
	  s.consulta = (char*) malloc(sizeof(char) * 25);
	  s.tipo = (char*) malloc(sizeof(char) * 12);
	  s.valor = (char*) malloc(sizeof(char) * 8);

      indices[t] = t;

      if(fscanf(consulta, "%s %s %s", cons, tipo, valor) != 3)
      {
        printf("Consulta con mal formato\n");
        break;
      }

      if(strcmp(cons, "T") == 0)
      {
        int **dat;
        dat = (int**) malloc(sizeof(int) * 2);
        dat[0] = (int*) malloc(sizeof(int) * tam);
        dat[1] = (int*) malloc(sizeof(int) * tam);

        if(strcmp(tipo, "ejecucion") == 0)
        {
          for(i = 0; i < tam; i++)
          {
            dat[0][i] = (int) datos[i][0];
            dat[1][i] = (int) datos[i][3];
          }


          sprintf(s.consulta, "%s %s %s", cons, tipo, valor);
          strcpy(s.tipo, tipo);
          strcpy(s.valor, valor);
          s.tam = tam;
          s.datosTP = dat;

          params[t] = s;

          pthread_create(&threads[t], NULL, tiempo, (void *) &indices[t]);
        }
        else if(strcmp(tipo, "espera") == 0)
        {
          for(i = 0; i < tam; i++)
          {
            dat[0][i] = (int) datos[i][0];
            dat[1][i] = (int) datos[i][2];
          }

          sprintf(s.consulta, "%s %s %s", cons, tipo, valor);;
          strcpy(s.tipo, tipo);
          strcpy(s.valor, valor);
          s.tam = tam;
          s.datosTP = dat;

          params[t] = s;

          pthread_create(&threads[t], NULL, tiempo, (void *) &indices[t]);
        }
        else if(strcmp(tipo, "cpu") == 0)
        {
          for(i = 0; i < tam; i++)
          {
            dat[0][i] = (int) datos[i][0];
            dat[1][i] = (int) datos[i][5];
          }

          sprintf(s.consulta, "%s %s %s", cons, tipo, valor);;
          strcpy(s.tipo, tipo);
          strcpy(s.valor, valor);
          s.tam = tam;
          s.datosTP = dat;

          params[t] = s;

          pthread_create(&threads[t], NULL, tiempo, (void *) &indices[t]);
        }
      }
      else if(strcmp(cons, "P") == 0)
      {
        int **dat;
        dat = (int**) malloc(sizeof(int) * 2);
        dat[0] = (int*) malloc(sizeof(int) * tam);
        dat[1] = (int*) malloc(sizeof(int) * tam);

        for(i = 0; i < tam; i++)
        {
          dat[0][i] = (int) datos[i][0];
          dat[1][i] = (int) datos[i][4];
        }

        sprintf(s.consulta, "%s %s %s", cons, tipo, valor);
        strcpy(s.tipo, tipo);
        strcpy(s.valor, valor);
        s.tam = tam;
        s.datosTP = dat;
        s.x = atoi(tipo);

        params[t] = s;

        pthread_create(&threads[t], NULL, procesos, (void *) &indices[t]);
      }
      else if(strcmp(cons, "S") == 0)
      {
        int *dat;
        dat = (int*) malloc(sizeof(int) * tam);

        for(i = 0; i < tam; i++)
        {
          dat[i] = (int) datos[i][10];
        }

        sprintf(s.consulta, "%s %s %s", cons, tipo, valor);
        strcpy(s.tipo, tipo);
        strcpy(s.valor, valor);
        s.tam = tam;
        s.datosSNP = dat;

        params[t] = s;

        pthread_create(&threads[t], NULL, status, (void *) &indices[t]);
      }
      else if(strcmp(cons, "NP") == 0)
      {
        int *dat;
        dat = (int*) malloc(sizeof(int) * tam);

        for(i = 0; i < tam; i++)
        {
          dat[i] = (int) datos[i][4];
        }

        sprintf(s.consulta, "%s %s %s", cons, tipo, valor);
        strcpy(s.tipo, tipo);
        strcpy(s.valor, valor);
        s.tam = tam;
        s.datosSNP = dat;
        s.x = atoi(tipo);

        params[t] = s;

        pthread_create(&threads[t], NULL, numProcesos, (void *) &indices[t]);
      }
      else
      {
        printf("Consulta con mal formato.\n");
      }

      contHechos++;

      //printf("Afuera: %s %d\n", params[t].consulta, indices[t]);
    }

    for(t = 0; t < atoi(argv[2]); t++)
    {
      pthread_join(threads[t], NULL);
    }

    for(i = 0; i < atoi(argv[2]); i++)
    {
	  if(strcmp(terminados[i].resp, "Error en la consulta") != 0)
	  {
		contCorrectos++;
	  }
      fprintf(salida, "%d:%d:%d [%s] %s\n", terminados[i].hora, terminados[i].min, terminados[i].sec, terminados[i].consulta, terminados[i].resp);
    }

    gettimeofday(&tval_after, NULL);
    timersub(&tval_after, &tval_before, &tval_result);
    printf("Consultas Realizadas: %d\nConsultas Correctas: %d\nTiempo de Ejecucion: %ld.%06ld segundos", contHechos, contCorrectos, (long int)tval_result.tv_sec, (long int)tval_result.tv_usec);
  }

  pthread_exit(0);
}
