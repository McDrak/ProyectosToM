package co.edu.javeriana.IntroSec.cliente;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import co.edu.javeriana.IntroSec.interfazCliente.ClientInterface;

public class Client 
{
	public final static int PORT = 9401;
	BufferedReader in;
	PrintWriter out;
	private Socket socket;
	ClientInterface interfaz;
	
	public Client(ClientInterface interfazeP)
	{
		this.interfaz = interfazeP;
		this.run();
	}
	
	public String getServerAddress()
	{
		return "192.168.43.144";
	}
	
	private void run()
	{
		String serverAddress = getServerAddress();
		try 
		{
			socket = new Socket(serverAddress, PORT);
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new PrintWriter(socket.getOutputStream(), true);
		} 
		catch (UnknownHostException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		catch (IOException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String register(String nombre, String apellido, int edad, String direccion, String pais, String correo, String username, String nickname, String password)
	{
		String mensaje =  nombre + "|" + apellido + "|" + edad + "|" + direccion + "|" + pais + "|" + correo + "|" + username + "|" + nickname + "|" + password;
		String resp = "";
		boolean flag = true;
		out.println("newUser");
		
		while(flag == true)
		{
			try 
			{
				String line = in.readLine();
				
				if(line.startsWith("Start"))
				{
					out.println(mensaje);
				}
				else if(line.startsWith("Ya Existe el usuario"))
				{
					resp = "User";
					flag = false;
				}
				else if(line.startsWith("Exito"))
				{
					resp = "Exito";
					flag = false;
				}
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
			}
		}
		return resp;
	}
	
	public boolean login(String user, String pass)
	{
		String mensaje = user + "|" + pass;
		boolean resp = false;
		boolean flag = true;
		out.println("login");
		
		while(flag == true)
		{
			try 
			{
				String line = in.readLine();
				
				if(line.startsWith("Ready"))
				{
					out.println(mensaje);
				}
				else if(line.startsWith("Exito"))
				{
					int dias = Integer.parseInt(in.readLine());
					interfaz.showDiasPane(dias);
					resp = true;
					flag = false;
				}
				else if(line.startsWith("Fail"))
				{
					flag = false;
				}
			}
			catch (IOException e) 
			{
				e.printStackTrace();
			}
		}
		
		return resp;
	}
	
	public String commandManegement(String command)
	{
		boolean flag = true;
		String resp = "";
		out.println("command");
		
		while(flag == true)
		{
			try 
			{
				String prep = in.readLine();
				if(prep.equals("YA"))
				{
					out.println(command);
					resp = in.readLine() + ": ";
					int num = Integer.parseInt(in.readLine());
					
					for(int i = 0; i < num; i++)
					{
						resp += in.readLine();
					}
					
					flag = false;
				}
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
			}
		}
		
		return resp;
	}
	
	public void endConnection()
	{
		try 
		{
			socket.close();
		}
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
}
