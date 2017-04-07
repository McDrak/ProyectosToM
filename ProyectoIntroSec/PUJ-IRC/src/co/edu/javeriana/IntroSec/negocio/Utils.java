package co.edu.javeriana.IntroSec.negocio;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.security.DigestInputStream;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;
import java.util.StringTokenizer;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

import org.mindrot.jbcrypt.BCrypt;

public class Utils 
{
	public final static String KEY = "ZxyPwbz9E6Q=";
	
	public static String getHash(String input) {
		String hashed = BCrypt.hashpw(input, BCrypt.gensalt());
		System.out.println(input + " - " + hashed);
		return hashed;
	}

	public static boolean verifyUser(String username, String password) {
		password = getHash(password);
		return false;
		// return true;
	}

	public static HashSet<User> cargarDatos(HashSet<User> ServerUsers) {
		try {
			boolean flag = true;
			Scanner scan = new Scanner(new File("./data/Users.txt"));
			while (scan.hasNextLine()) {
				if(ServerUsers.size() == 0)
					flag = true;
				else
					flag = false;
				String line = scan.nextLine();
				ServerUsers.add(addUser(line, false, flag));
			}
			scan.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return ServerUsers;
	}

	public static User addUser(String datos, boolean choice,boolean admin) {
		StringTokenizer st = new StringTokenizer(datos, "|");
		String nombre = st.nextToken();
		String apellido = st.nextToken();
		String temp = st.nextToken();
		int edad = Integer.parseInt(temp);
		String direccion = st.nextToken();
		String pais = st.nextToken();
		String email = st.nextToken();
		String username = st.nextToken();
		String nickname = st.nextToken();
		temp = st.nextToken();
		String contrasena = BCrypt.hashpw(temp, BCrypt.gensalt());
		User newUser = new User(username, nickname, nombre, apellido, edad, direccion, pais, email, contrasena);
		if(admin)
			newUser.setLevel("Admin");
		String data = nombre + "|" + apellido + "|" + edad + "|" + direccion + "|" + pais + "|" + email + "|"
				+ username + "|" + nickname + "|" + newUser.getLevel();
		System.out.println(data+"|"+contrasena);
		String status = BCrypt.hashpw(data, BCrypt.gensalt());
		
		if (choice) {
			try (PrintWriter output = new PrintWriter(new FileWriter("./data/Users.txt", true))) {
				output.println(data+"|"+contrasena);
			} catch (Exception e) {
				e.printStackTrace();
			}
			try (PrintWriter output = new PrintWriter(new FileWriter("./data/4S0dP5wRs.txt", true))) {
				output.println(status);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return newUser;
	}

	public static User searchUser(String username, HashSet<User> usuarios) {
		Object[] array = usuarios.toArray();
		for (int i = 0; i < array.length; i++) {
			User o = (User) array[i];
			System.out.println(o.toString());
			if (o.getUsername().equals(username)) {
				return o;
			}
		}
		return null;
	}

	public static User checkLogin(String readLine, HashSet<User> serverUsers) {
		StringTokenizer st = new StringTokenizer(readLine, "|");
		String username = st.nextToken();
		String contrasena = st.nextToken();

		User tempo = searchUser(username, serverUsers);
		if (tempo != null) {
			BCrypt.checkpw(contrasena, tempo.getContrasena());
		}
		return tempo;
	}


	public static void displayUserInfo(HashMap<String, PrintWriter> activeUsers, HashSet<User> serverUsers, String name, String level, PrintWriter out) 
	{
		Object[] array = serverUsers.toArray();
		for (int i = 0; i < array.length; i++) {
			User o = (User)array[i];
			if(level.equals("Usuario"))
			{
				out.println(o.getNombre()+" "+o.getApellido()+" "+o.getNickname()+" "+o.getEmail());
			}
			if(level.equals("Custodio"))
			{
				out.println(o.getNombre()+" "+o.getApellido()+" "+o.getEdad()+" "+o.getUsername()+" "+
											  o.getCondidcionMedica()+" "+o.getWallet()+" "+o.getNickname()+" "+o.getEmail());
			}
			if(level.equals("Admin"))
			{
				out.println(o.getNombre()+" "+o.getApellido()+" "+o.getEdad()+" "+o.getDireccion()+" "+o.getPais()+" "+
						o.getUsername()+" "+o.getContrasena()+" "+o.getCondidcionMedica()+" "+o.getWallet()+" "+o.getNickname()+" "+o.getEmail()+" "+o.getLevel());
			}
		}
	}

	public static void displayMenuOptions(String level) {
		// TODO Auto-generated method stub

	}

	public static byte[] encryptFile(String path, SecretKey encryptKey)
	{
		byte[] dataEncrypted = null;

		try 
		{
			Path pathh = Paths.get(path);
			byte[] data = Files.readAllBytes(pathh);
			Cipher cipher = Cipher.getInstance("DES");

			cipher.init(Cipher.ENCRYPT_MODE, encryptKey);
			dataEncrypted = cipher.doFinal(data);

			Files.delete(pathh);
			Files.write(pathh, dataEncrypted, StandardOpenOption.CREATE_NEW);
		} 
		catch (NoSuchAlgorithmException e) 
		{
			e.printStackTrace();
		} 
		catch (NoSuchPaddingException e) 
		{
			e.printStackTrace();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		} 
		catch (InvalidKeyException e) 
		{
			e.printStackTrace();
		} 
		catch (IllegalBlockSizeException e) 
		{
			e.printStackTrace();
		} 
		catch (BadPaddingException e) 
		{
			e.printStackTrace();
		}

		return dataEncrypted;
	}

	public static void decryptFile(String path, SecretKey encryptKey)
	{
		try 
		{
			Path pathh = Paths.get(path);
			byte[] data = Files.readAllBytes(pathh);
			Cipher cipher = Cipher.getInstance("DES");

			cipher.init(Cipher.DECRYPT_MODE, encryptKey);
			byte[] dataDecrypted = cipher.doFinal(data);
			
			Files.delete(pathh);
			Files.write(pathh, dataDecrypted, StandardOpenOption.CREATE_NEW);
		}
		catch (IOException e) 
		{
			e.printStackTrace();
		} 
		catch (NoSuchAlgorithmException e) 
		{
			e.printStackTrace();
		} 
		catch (NoSuchPaddingException e) 
		{
			e.printStackTrace();
		} 
		catch (InvalidKeyException e) 
		{
			e.printStackTrace();
		}
		catch (IllegalBlockSizeException e) 
		{
			e.printStackTrace();
		}
		catch (BadPaddingException e) 
		{
			e.printStackTrace();
		}
	}

	public static byte[] hashFromFile(String path)
	{
		byte[] hash = null;
		try 
		{
			MessageDigest sha = MessageDigest.getInstance("SHA-512");
			InputStream is = Files.newInputStream(Paths.get(path));
			DigestInputStream dis = new DigestInputStream(is, sha);
			boolean eof = false;

			while(eof == false)
			{
				int cond = dis.read();

				if(cond == -1)
				{
					eof = true;
				}
			}

			hash = sha.digest();
		}
		catch (NoSuchAlgorithmException e) 
		{
			e.printStackTrace();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}

		return hash;
	}
	
	public static void writeToLog(String string) {
		try (PrintWriter output = new PrintWriter(new FileWriter("./data/serverLog.txt", true))) {
			output.println(string);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static boolean dataWasModified(User U) 
	{
		try {
			Scanner scan = new Scanner(new File("./data/4S0dP5wRs.txt"));
			while (scan.hasNextLine()) {
				String line = scan.nextLine();
				String data = U.getNombre() + "|" + U.getApellido() + "|" + U.getEdad() + "|" + U.getDireccion() + "|" + U.getPais() + "|" + U.getEmail() + "|"
						+ U.getUsername() + "|" + U.getNickname() + "|" + U.getLevel();
				System.out.println(data);
				if(BCrypt.checkpw(data, line))
					return true;
			}
			scan.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return false;
	}
}
