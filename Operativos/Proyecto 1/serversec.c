#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <time.h>
#include <sys/time.h>

char *tiempo(char *lugar, int tam, int datos[2][tam])
{
  int i, pos, tiempo, comp;
  char *resp;

  resp = malloc(sizeof(char) * 50);

  if(strcmp(lugar, "min") == 0)
  {
    comp = 99999999;
  }
  else if(strcmp(lugar, "max") == 0)
  {
    comp = 0;
  }
  else
  {
    return "Error en la consulta";
  }

  for(i = 0; i < tam; i++)
  {
    if(strcmp(lugar, "min") == 0)
    {
      if(comp > datos[1][i] && datos[1][i] != -1)
      {
        tiempo = datos[1][i];
        comp = tiempo;
        pos = datos[0][i];
      }
    }
    else if(strcmp(lugar, "max") == 0)
    {
      if(comp < datos[1][i] && datos[1][i] != -1)
      {
        tiempo = datos[1][i];
        comp = tiempo;
        pos = datos[0][i];
      }
    }
  }

  sprintf(resp, "%d %d", tiempo, pos);

  return resp;
}

char *procesos(int nProc, int tam, int datos[2][tam])
{
  char *resp;
  int i, proc;

  proc = 0;
  resp = malloc(sizeof(char) * 20);

  for(i = 0; i < tam && proc == 0; i++)
  {
    if(datos[0][i] == nProc)
    {
      proc = datos[1][i];
    }
  }

  sprintf(resp, "%d", proc);

  return resp;
}

char *status(char *tipo, int tam, int datos[tam])
{
  char *resp;
  int i, cont, comp;

  resp = malloc(sizeof(char) * 20);
  cont = 0;

  if(strcmp(tipo, "bien") == 0)
  {
    comp = 1;
  }
  else if(strcmp(tipo, "cancelado") == 0)
  {
    comp = 5;
  }
  else if(strcmp(tipo, "falla") == 0)
  {
    comp = 0;
  }
  else
  {
    return "Error en la consulta";
  }

  for(i = 0; i < tam; i++)
  {
    if(datos[i] == comp)
    {
      cont++;
    }
  }

  sprintf(resp, "%d", cont);

  return resp;
}

char *numProcesos(int x, int tam, int datos[tam])
{
  char *resp;
  int i, cont;

  resp = malloc(sizeof(char) * 20);
  cont = 0;

  for(i = 0; i < tam; i++)
  {
    if(datos[i] == x)
    {
      cont++;
    }
  }

  sprintf(resp, "%d", cont);

  return resp;
}

int main(int argc, char *argv[])
{
  struct timeval tval_before, tval_after, tval_result;
  time_t rawtime;
  int tam, i, j, nProc, contHechos, contCorrectos;
  struct tm * timeinfo;
  FILE *consulta, *logfile, *salida;
  char *cons, *tipo, *valor, *resp, *temp;

  cons = malloc(sizeof(char) * 3);
  tipo = malloc(sizeof(char) * 12);
  valor = malloc(sizeof(char) * 8);
  resp = malloc (sizeof(char) * 20);
  temp = malloc(sizeof(char) * 20);
  contHechos = 0;
  contCorrectos = 0;
  gettimeofday(&tval_before, NULL);

  if(argc != 5)
  {
    printf("Error numero de parametros.\nUso - ./serversec [archivo consulta] [numero consultas] [archivo datos] [archivo salida]");
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

    while(!feof(consulta))
    {
      if(fscanf(consulta, "%s %s %s", cons, tipo, valor) != 3)
      {
        printf("Consulta con mal formato\n");
        break;
      }

      if(strcmp(cons, "T") == 0)
      {
        int dat[2][tam];

        if(strcmp(tipo, "ejecucion") == 0)
        {
          for(i = 0; i < tam; i++)
          {
            dat[0][i] = (int) datos[i][0];
            dat[1][i] = (int) datos[i][3];
          }

          resp = tiempo(valor, tam, dat);
        }
        else if(strcmp(tipo, "espera") == 0)
        {
          for(i = 0; i < tam; i++)
          {
            dat[0][i] = (int) datos[i][0];
            dat[1][i] = (int) datos[i][2];
          }

          resp = tiempo(valor, tam, dat);
        }
        else if(strcmp(tipo, "cpu") == 0)
        {
          for(i = 0; i < tam; i++)
          {
            dat[0][i] = (int) datos[i][0];
            dat[1][i] = (int) datos[i][5];
          }

          resp = tiempo(valor, tam, dat);
        }
        else
        {
          resp = "Error en la consulta";
        }
      }
      else if(strcmp(cons, "P") == 0)
      {
        int dat[2][tam];

        for(i = 0; i < tam; i++)
        {
          dat[0][i] = datos[i][0];
          dat[1][i] = datos[i][4];
        }

        nProc = atoi(tipo);

        resp = procesos(nProc, tam, dat);
      }
      else if(strcmp(cons, "S") == 0)
      {
        int dat[tam];

        for(i = 0; i < tam; i++)
        {
          dat[i] = datos[i][10];
        }

        resp = status(tipo, tam, dat);
      }
      else if(strcmp(cons, "NP") == 0)
      {
        int dat[tam];
        long proc;

        for(i = 0; i < tam; i++)
        {
          dat[i] = datos[i][4];
        }

        proc = strtol(tipo, NULL, 0);

        if(proc == 0L)
        {
          resp = "Error en la consulta";
        }
        else
        {
          resp = numProcesos((int) proc, tam, dat);
        }
      }
      else
      {
        printf("Consulta con mal formato\n");
      }
      
      contHechos++;

      time(&rawtime);
      timeinfo = localtime(&rawtime);
      int hora = timeinfo->tm_hour;
      int min = timeinfo->tm_min;
      int sec = timeinfo->tm_sec;
      
      if(strcmp(resp, "Error en la consulta") != 0)
      {
		contCorrectos++;
	  }

      fprintf(salida, "%d:%d:%d  [%s %s %s]  %s\n", hora, min, sec, cons, tipo, valor, resp);
    }

    fclose(salida);
    fclose(consulta);
  }
  
  gettimeofday(&tval_after, NULL);
  timersub(&tval_after, &tval_before, &tval_result);
  printf("Consultas Realizadas: %d\nConsultas Correctas: %d\nTiempo de Ejecucion: %ld.%06ld segundos\n", contHechos, contCorrectos, (long int)tval_result.tv_sec, (long int)tval_result.tv_usec);

  return 0;
}
