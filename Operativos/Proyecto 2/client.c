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
#include "structs.h"

int main(int argc, char *argv[])
{
  int fd, fdEscritura, fdLectura, idCliente, creado = 0;
  char* nombrePipe, *temp, pipe[10];
  Cliente c;

  nombrePipe = (char*) malloc(sizeof(char) * 10);
  temp = (char*) malloc(sizeof(char) * 10);

  if(argc != 3)
  {
    printf("Error en la cantidad de parametros.\nUso: ./client <id> <nombrePipe>\n");
    exit(0);
  }

  mode_t fifo_mode = S_IRUSR | S_IWUSR;

  do
  {
    fd = open(argv[1], O_WRONLY);
    if(fd == -1)
    {
      perror("Pipe");
      printf("Se volvera a intentar mas tarde\n");
      sleep(5);
    }
    else
    {
      printf("Comunicacion iniciada\n");
      creado = 1;
    }
  } while(creado == 0);

  idCliente = atoi(argv[2]);
  strcpy(nombrePipe, "cts"); //Client To Server
  sprintf(temp, "%d", idCliente);
  strcat(nombrePipe, temp);
  strcpy(c.nomPipe, nombrePipe);
  c.idCliente = idCliente;

  if(mkfifo(nombrePipe, fifo_mode) == -1)
  {
    perror("mk Pipe 2");
    exit(1);
  }

  printf("%s\n", c.nomPipe);

  write(fd, &c, sizeof(c));

  creado = 0;
  do
  {
    fdLectura = open(nombrePipe, O_RDONLY);
    if(fdLectura == -1)
    {
      perror("Pipe Lectura");
      sleep(5);
    }
    else
    {
      creado = 1;
    }
  } while(creado == 0);

  read(fdLectura, pipe, sizeof(pipe));
  printf("%s %d\n", pipe, (int) sizeof(pipe));

  creado = 0;
  do
  {
    fdEscritura = open(pipe, O_WRONLY);
    if(fdEscritura == -1)
    {
      perror("Pipe Escritura");
      sleep(5);
    }
    else
    {
      creado = 1;
    }
  } while(creado == 0);

  write(fdEscritura, "Inicio?", 10);
  read(fdLectura, temp, sizeof(temp));

  if(strcmp(temp, "ACK") == 0)
  {
    int opcion = 0, opcion2 = 0, opcion3 = 0, flag = 1;
    char dia[10];

    do
    {
      Consulta consulta;

    	printf("------------------------------------------------------------------\n");
    	printf("    ¿Que tipo de consulta desea realizar?\n");
    	printf("------------------------------------------------------------------\n");
    	printf("1.Tiempos de ejecucion del cpu\n");
    	printf("2.Cantidad de procesadores asignados a un proceso\n");
    	printf("3.Cantidad de procesos Terminados segun parametro\n");
    	printf("4.Cantidad de procesos ejecutados en una cantidad de procesadores\n");
    	printf("5.Salir del sistema\n");
    	printf("------------------------------------------------------------------\n");
    	scanf("%d", &opcion);

    	if((opcion < 1) == 0 || (opcion > 5) == 0)
    	{
    		flag = 0;
    	}

    	if(opcion==1)
    	{
        strcpy(consulta.cons,"T"); //Consulta de tipo T

    		printf("\n\n------------------------------------------------------------------\n");
    		printf("    ¿Que tipo de consulta desea realizar?\n");
    		printf("------------------------------------------------------------------\n");
    		printf("1.Tiempos de ejecucion del cpu\n");
    		printf("2.Tiempos de espera del cpu\n");
    		printf("3.Tiempo promedio del cpu\n");
    		printf("------------------------------------------------------------------\n");
    		scanf("%d", &opcion2);

        //Tipo de la Consulta
        if(opcion2 == 1)
    		{
          strcpy(consulta.tipo,"ejecucion");
    		}
        else if(opcion2 == 2)
        {
          strcpy(consulta.tipo, "espera");
        }
        else if(opcion2 == 3)
        {
          strcpy(consulta.tipo, "cpu");
        }
    		else
    		{
          flag = 0;
    		}

    		printf("Desea ver el valor minimo (1) o el valor maximo (2) \n");
    		scanf("%d", &opcion3);

        //Valor de la Consulta
        if(opcion3 == 1)
        {
          strcpy(consulta.valor, "min");
        }
        else if(opcion3 == 2)
        {
          strcpy(consulta.valor, "max");
        }
        else
        {
          flag = 0;
        }

    		printf("Que dia de la semana desea consultar?\n");
    		scanf("%10s", dia);

        strcpy(consulta.nomLog, dia);

    		//$ T estado_proceso_a_buscar, min_o_max, dia_de_la_semana
    		//$ T <opcion2> min_o_max, dia_de_la_semana
    	}

    	if(opcion == 2)
    	{
        strcpy(consulta.cons, "P");

    		printf("Ingrese el identificador del proceso: \n");
    		scanf("%d", &opcion2);

        sprintf(consulta.tipo, "%d", opcion2);

        strcpy(consulta.valor, "cuantos");

    		printf("Que dia de la semana desea consultar?\n");
    		scanf("%10s", dia);

        strcpy(consulta.nomLog, dia);

    		//$ P id_proceso, cantidad, dia_de_la_semana
    		//$ P <opcion2> cuantos, dia
    	}

    	if(opcion==3)
    	{
        strcpy(consulta.cons, "S");

    		printf("------------------------------------------------------------------\n");
    		printf("    ¿Que estado de finalizacion del procesos desea buscar?\n");
    		printf("------------------------------------------------------------------\n");
    		printf("1.Procesos finalizados correctamente				(Bien)\n");
    		printf("2.Procesos finalizados con problemas				(Falla)\n");
    		printf("3.Procesos finalizados cancelados					(Cancelados)\n");
    		printf("------------------------------------------------------------------\n");
    		scanf("%d", &opcion2);

        if(opcion2 == 1)
    		{
          strcpy(consulta.tipo, "bien");
    		}
    		else if (opcion2 == 2)
    		{
          strcpy(consulta.tipo, "falla");
    		}
        else if(opcion2 == 3)
        {
          strcpy(consulta.tipo, "cancelado");
        }
        else
        {
          flag = 0;
        }

        strcpy(consulta.valor, "cuantos");

    		printf("Que dia de la semana desea consultar?\n");
    		scanf("%10s", dia);

        strcpy(consulta.nomLog, dia);

    		//$ S estado_proceso_a_buscar, cantidad, dia_de_la_semana
    		//$ S	<opcion2> cuantos dia
    	}

    	if(opcion==4)
    	{
        strcpy(consulta.cons, "NP");

    		printf("Ingrese la cantidad de Procesos \n");
    		scanf("%d", &opcion2);

        sprintf(consulta.tipo, "%d", opcion2);

        strcpy(consulta.valor, "cuantos");

    		printf("Que dia de la semana desea consultar?\n");
    		scanf("%10s", dia);

        strcpy(consulta.nomLog, dia);

    		//$ NP <cant_procesos>, cantidad, dia_de_la_semana
    		//$ NP <opcion2> cuantos[] dia
    	}

    	if(flag != 1)
    	{
    		printf("Input incorrecto, por favor seleccione alguna de las opciones\n");
    		flag = 1;
    	}
      else
      {
        write(fdEscritura, &consulta, sizeof(consulta));
        read(fdLectura, temp, sizeof(temp));
        printf("El resultado de la consulta es: %s\n", temp);
      }
    }
    while(opcion != 5);
  }

  write(fdEscritura, "Termina", 7);

  //TODO: Hacer esto
}
