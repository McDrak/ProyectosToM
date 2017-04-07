package co.edu.javeriana.IntroSec.interfazCliente;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.border.EmptyBorder;

import co.edu.javeriana.IntroSec.cliente.Client;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JButton;

public class ClientInterface extends JFrame 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JLabel lblUsuario;
	private JTextField txtUsuario;
	private JPasswordField txtContrasena;
	private Client cliente;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ClientInterface frame = new ClientInterface();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public ClientInterface() 
	{
		setTitle("PUJ-IRC");
		cliente = new Client(this);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 245);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		lblUsuario = new JLabel("Usuario");
		lblUsuario.setBounds(100, 25, 66, 15);
		contentPane.add(lblUsuario);
		
		txtUsuario = new JTextField();
		txtUsuario.setBounds(215, 23, 124, 19);
		contentPane.add(txtUsuario);
		txtUsuario.setColumns(10);
		
		JLabel lblContrasena = new JLabel("Contraseña");
		lblContrasena.setBounds(100, 75, 78, 15);
		contentPane.add(lblContrasena);
		
		txtContrasena = new JPasswordField();
		txtContrasena.setBounds(215, 75, 124, 19);
		contentPane.add(txtContrasena);
		txtContrasena.setColumns(10);
		
		JButton btnEnviar = new JButton("Enviar");
		btnEnviar.addActionListener(new ActionListener() 
		{			
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				loginUsuario();
			}
		});
		btnEnviar.setBounds(164, 125, 114, 25);
		contentPane.add(btnEnviar);
		
		JButton btnRegistro = new JButton("Registro");
		btnRegistro.setBounds(12, 174, 114, 25);
		btnRegistro.addActionListener(new ActionListener() 
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				mostrarRegisterDialog();
			}
		});
		contentPane.add(btnRegistro);
	}
	
	public void mostrarRegisterDialog()
	{
		RegisterDialog dialog = new RegisterDialog(this);
		dialog.setSize(450, 350);
		dialog.setLocationRelativeTo(this);
		dialog.setVisible(true);
	}
	
	public String registrarUsuario(String nombre, String apellido, int edad, String direccion, String pais, String correo, String username, String nickname, String password)
	{
		return cliente.register(nombre, apellido, edad, direccion, pais, correo, username, nickname, password);
	}
	
	public void loginUsuario()
	{
		if(txtUsuario.getText().equals("") == false || new String(txtContrasena.getPassword()).equals("") == false)
		{
			String user = txtUsuario.getText();
			String pass = new  String(txtContrasena.getPassword());
			
			boolean respuestaServer = cliente.login(user, pass);
			
			if(respuestaServer == true)
			{
				ClientHomeDialog client = new ClientHomeDialog();
				client.setSize(450, 300);
				client.setLocationRelativeTo(this);
				client.setVisible(true);
			}
			else
			{
				JOptionPane.showMessageDialog(this, "El usuario o contraseña no coinciden.", "Error de Ingreso", JOptionPane.ERROR_MESSAGE);
			}
		}
	}
	
	public String commandManagenet(String command)
	{
		return cliente.commandManegement(command);
	}
	
	public void showDiasPane(int dias)
	{
		if(dias > 0)
		{
			JOptionPane.showMessageDialog(this, "Te quedan " + dias + " dias para cambiar tu contraseña.", "Advertencia", JOptionPane.INFORMATION_MESSAGE);
		}
		else
		{
			JOptionPane.showMessageDialog(this, "Su contraseña ha expirado, tiene que cambiarla", "Error", JOptionPane.ERROR_MESSAGE);
		}
	}
}
