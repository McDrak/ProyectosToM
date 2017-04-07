package co.edu.javeriana.IntroSec.negocio;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.HashMap;
import java.util.HashSet;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Date;
public class Server {

    private static final int PORT = 9401;
    private static boolean admin = false;
    private static String level;

    private static HashSet<User> ServerUsers = new HashSet<User>();
    private static HashMap<String, PrintWriter> activeUsers = new HashMap<String, PrintWriter>();

    public static void main(String[] args) throws Exception
    {
    	startupCeremony();
        System.out.println("The server is running.");
        Utils.writeToLog("["+new Date()+"] - Server Started");
        Utils.writeToLog("["+new Date()+"] - Cargando Base de Datos...");
        Utils.cargarDatos(ServerUsers);
        if(ServerUsers.size()==0)
        {
        	Utils.writeToLog("["+new Date()+"] - Base de datos vacia, asignando primer registro como due�o");
        	admin = true;
        }
        else
        	Utils.writeToLog("["+new Date()+"] - Base de datos cargada, "+ServerUsers.size()+" Usuarios registrados");
        ServerSocket listener = new ServerSocket(PORT);
        try {
            while (true) {
                new Handler(listener.accept()).start();
            }
        } finally {
            listener.close();
        }
    }

	private static class Handler extends Thread {
		private String name;
		private Socket socket;
		private BufferedReader in;
		private PrintWriter out;

		public Handler(Socket socket) {
			this.socket = socket;
		}

		public void run() {
			try {

				Utils.writeToLog("[" + new Date() + "] - new conection with client");
				in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				out = new PrintWriter(socket.getOutputStream(), true);
				while (true) {
					String line = in.readLine();
					System.out.println(line);
					if (line.equals("newUser")) {
						out.println("Start");
						String data = in.readLine();
						User newUser = Utils.addUser(data, false, admin);
						if (Utils.searchUser(newUser.getUsername(), ServerUsers) == null) {
							newUser = Utils.addUser(data, true, admin);
							if (admin)
								admin = false;
							System.out.println("entre");
							ServerUsers.add(newUser);
							out.println("Exito");
							out.println(newUser.getNickname());
							Utils.writeToLog("[" + new Date() + "] - Added new user to database");							
						} else {
							System.out.println("Ya Existe el usuario");
							out.println("Ya Existe el usuario");
							Utils.writeToLog("[" + new Date() + "] - Failed to create a user, disconnect client");
							socket.close();							
						}
					}
					if (line.equals("login")) {
						Utils.writeToLog("[" + new Date() + "] - Login attempt started");
						out.println("Ready");
						User newUser = Utils.checkLogin(in.readLine(), ServerUsers);
						if (newUser != null) {
							synchronized (activeUsers) {
								activeUsers.put(newUser.getNickname(), out);
								name = newUser.getNickname();
								level = newUser.getLevel();
							}
							out.println("Exito");
							out.println(30 - newUser.diasHastaCambioContrasena());
							if (Utils.dataWasModified(newUser)) {
								System.out.println("SISAS");
							}
							Utils.writeToLog("[" + new Date() + "] - Successful login!");
							break;
						} else {
							Utils.writeToLog("[" + new Date() + "] - Failed login attempt");
							out.println("Fail");
						}

					} 						
					if(line.equals("command"))
					{
						System.out.println("Entre");
						out.println("YA");
						line = in.readLine();
						System.out.println(line);
						if(line.equals("/usuarios"))
						{
							out.println("/usuarios");
							System.out.println(level);
							Utils.writeToLog("[" + new Date() + "] - " + level + "-user searched user information.");
							out.println(activeUsers.size());
							Utils.displayUserInfo(activeUsers, ServerUsers,name, level,out);
						}
						else if(line.equals("/exit"))
						{
							out.println("EXIT");
							shutdownCeremony();							
						}
					}
					else 
					{
						Utils.writeToLog("[" + new Date() + "] - invalid input");
						break;
					}									
				}				
				Utils.writeToLog("[" + new Date() + "] - Verified conection between client-server");
				while (true) {
					String input = in.readLine();
					System.out.println(input);
					if (input == null) {
						return;
					}
					if (input.equals("/usuarios")) {
						out.println("usuarios");
						Utils.writeToLog("[" + new Date() + "] - " + level + "-user searched user information.");
						// Utils.displayUserInfo(activeUsers, ServerUsers,
						// name, level);
					}
					/*
					 * if (input.equals("/cambiar_datos_usuario")) {
					 * out.println("modData"); Utils.writeToLog("[" + new
					 * Date() + "] - " + level +
					 * "-user wants to modify data."); if
					 * (level.equals("Usuario")) {
					 * 
					 * } if (level.equals("Custodio")) {
					 * 
					 * } if (level.equals("Dueno")) {
					 * 
					 * }
					 * 
					 * }
					 */
					if (input.equals("/exit")) {
						activeUsers.remove(name);
						try {
							socket.close();
						} catch (IOException e) {
						}
						break;
					}
				}
			} catch (IOException e) {
				System.out.println(e);
			}
		}
    }
    
    public static void startupCeremony()
    {
    	byte[] decodedKey = Base64.getDecoder().decode(Utils.KEY);
    	SecretKey sKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, "DES");
    	
    	File folder = new File("./data");
    	File[] files = folder.listFiles();
    	Utils.decryptFile("./data/Hashes.txt", sKey);
    	Path hashesPath = Paths.get("./data/Hashes.txt");
    	try 
    	{
			byte[] hashesData = Files.readAllBytes(hashesPath);
			String hashesString = new String(hashesData);
			String[] arrayHashes = hashesString.split("-");
			boolean flagExterior = false;
			
			for(int i = 0; i < arrayHashes.length && flagExterior == false; i++)
			{
				boolean flagInterior = false;
				
				for(int j = 0; j < files.length && flagInterior == false; j++)
				{
					File f = files[j];
					String hash = new String(Utils.hashFromFile(f.getPath()));
					String compHash = arrayHashes[i];
					
					if(compHash.equals("") == false && f.getName().equals("Hashes.txt") == false)
					{
						if(compHash.contains(hash) == true)
						{
							flagInterior = true;
						}
					}
				}
				
				if(flagInterior == false)
				{
					flagExterior = true;
				}
			}
			
			if(flagExterior == true)
			{
				//System.out.println("Fallo verificacion de Hash en archivos");
				//System.exit(0);
			}
			
			for(int i = 0; i < files.length; i++)
			{
				File f = files[i];
				
				if(f.getName().equals("Hashes.txt") == false)
				{
					Utils.decryptFile(f.getPath(), sKey);
				}
			}
		}
    	catch (IOException e) 
    	{
			e.printStackTrace();
		}
    }
    
    public static void shutdownCeremony()
    {
    	byte[] decodedKey = Base64.getDecoder().decode(Utils.KEY);
    	SecretKey sKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, "DES");
    	
    	File folder = new File("./data");
    	File[] files =  folder.listFiles();
    	PrintWriter hashesFile = null;
    	try 
    	{
			hashesFile = new PrintWriter("./data/Hashes.txt", "UTF-8");
		}
    	catch (FileNotFoundException e) 
    	{
			e.printStackTrace();
		}
    	catch (UnsupportedEncodingException e) 
    	{
			e.printStackTrace();
		}
    	
    	for(int i = 0; i < files.length; i++)
    	{
    		File f = files[i];
    		
    		if(f.isFile())
    		{
    			if(f.getName().equals("Hashes.txt") == false)
    			{
    				Utils.encryptFile(f.getPath(), sKey);
    			}
    		}
    	}
    	
    	files = folder.listFiles();
    	
    	for(int i = 0; i < files.length; i++)
    	{
    		File f = files[i];
    		
    		if(f.isFile())
    		{
    			if(f.getName().equals("Hashes.txt") == false)
    			{
    				hashesFile.println(f.getName() + "_Hash: " + new String(Utils.hashFromFile(f.getPath())) + "-");
    			}
    		}
    	}
    	
    	hashesFile.close();
    	Utils.encryptFile("./data/Hashes.txt", sKey);
    	
    	System.exit(0);
    }
}