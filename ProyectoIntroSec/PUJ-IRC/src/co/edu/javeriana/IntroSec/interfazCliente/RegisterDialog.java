package co.edu.javeriana.IntroSec.interfazCliente;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import javax.swing.JButton;
import javax.swing.JDialog;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class RegisterDialog extends JDialog
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private JTextField txtApellido;
	private JTextField txtEdad;
	private JTextField txtDireccion;
	private JTextField txtPais;
	private JTextField txtCorreo;
	private JTextField txtUsername;
	private JTextField txtNickname;
	private JTextField txtContrasena;
	private JTextField txtConfirmacion;
	private ClientInterface interfaz;
	private JLabel lblNombre;
	private JTextField txtNombre;
	private JLabel lblApellido;
	private JLabel lblEdad;
	private JLabel lblDireccion;
	private JLabel lblPais;
	private JLabel lblCorreo;
	private JLabel lblUsername;
	private JLabel lblNickname;
	private JLabel lblContrasena;
	private JLabel lblConfirmacionContrasena;
	private JButton btnEnviar;
	private JButton btnCancelar;

	/**
	 * Create the panel.
	 */
	public RegisterDialog(ClientInterface interfazP) 
	{
		setTitle("Registro");
		this.interfaz = interfazP;
		getContentPane().setLayout(null);
		
		lblNombre = new JLabel("Nombre");
		lblNombre.setBounds(75, 13, 66, 15);
		getContentPane().add(lblNombre);
		
		txtNombre = new JTextField();
		txtNombre.setBounds(240, 12, 124, 19);
		getContentPane().add(txtNombre);
		txtNombre.setColumns(10);
		
		lblApellido = new JLabel("Apellido");
		lblApellido.setBounds(75, 37, 66, 15);
		getContentPane().add(lblApellido);
		
		txtApellido = new JTextField();
		txtApellido.setBounds(240, 37, 124, 19);
		getContentPane().add(txtApellido);
		txtApellido.setColumns(10);
		
		lblEdad = new JLabel("Edad");
		lblEdad.setBounds(75, 62, 66, 15);
		getContentPane().add(lblEdad);
		
		txtEdad = new JTextField();
		txtEdad.setBounds(240, 62, 124, 19);
		getContentPane().add(txtEdad);
		txtEdad.setColumns(10);
		
		lblDireccion = new JLabel("Direccion");
		lblDireccion.setBounds(75, 87, 66, 15);
		getContentPane().add(lblDireccion);
		
		txtDireccion = new JTextField();
		txtDireccion.setBounds(240, 87, 124, 19);
		getContentPane().add(txtDireccion);
		txtDireccion.setColumns(10);
		
		lblPais = new JLabel("Pais");
		lblPais.setBounds(75, 112, 66, 15);
		getContentPane().add(lblPais);
		
		txtPais = new JTextField();
		txtPais.setBounds(240, 112, 124, 19);
		getContentPane().add(txtPais);
		txtPais.setColumns(10);
		
		lblCorreo = new JLabel("Correo");
		lblCorreo.setBounds(75, 137, 66, 15);
		getContentPane().add(lblCorreo);
		
		txtCorreo = new JTextField();
		txtCorreo.setBounds(240, 135, 124, 19);
		getContentPane().add(txtCorreo);
		txtCorreo.setColumns(10);
		
		lblUsername = new JLabel("Nombre de Usuario");
		lblUsername.setBounds(75, 162, 131, 15);
		getContentPane().add(lblUsername);
		
		txtUsername = new JTextField();
		txtUsername.setBounds(240, 160, 124, 19);
		getContentPane().add(txtUsername);
		txtUsername.setColumns(10);
		
		lblNickname = new JLabel("Pseudonombre");
		lblNickname.setBounds(75, 187, 103, 15);
		getContentPane().add(lblNickname);
		
		txtNickname = new JTextField();
		txtNickname.setBounds(240, 185, 124, 19);
		getContentPane().add(txtNickname);
		txtNickname.setColumns(10);
		
		lblContrasena = new JLabel("Contraseña");
		lblContrasena.setBounds(75, 212, 78, 15);
		getContentPane().add(lblContrasena);
		
		txtContrasena = new JPasswordField();
		txtContrasena.setBounds(240, 210, 124, 19);
		getContentPane().add(txtContrasena);
		txtContrasena.setColumns(10);
		
		lblConfirmacionContrasena = new JLabel("Confirmacion Contrasena");
		lblConfirmacionContrasena.setBounds(75, 235, 158, 15);
		getContentPane().add(lblConfirmacionContrasena);
		
		txtConfirmacion = new JPasswordField();
		txtConfirmacion.setBounds(240, 233, 124, 19);
		getContentPane().add(txtConfirmacion);
		txtConfirmacion.setColumns(10);
		
		btnEnviar = new JButton("Enviar");
		btnEnviar.addActionListener(new ActionListener() 
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				registrarUsuario();
			}
		});
		btnEnviar.setBounds(90, 275, 114, 25);
		getContentPane().add(btnEnviar);
		
		btnCancelar = new JButton("Cancelar");
		btnCancelar.addActionListener(new ActionListener() 
		{			
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				cancelarRegistro();
			}
		});
		btnCancelar.setBounds(250, 275, 114, 25);
		getContentPane().add(btnCancelar);
	}

	public void registrarUsuario()
	{
		if(txtNombre.getText().equals("") == false || txtApellido.getText().equals("") == false || txtEdad.getText().equals("") == false || 
				txtDireccion.getText().equals("") == false || txtPais.getText().equals("") == false || txtCorreo.getText().equals("") == false ||
				txtUsername.getText().equals("") == false || txtNickname.getText().equals("") == false || txtContrasena.getText().equals("") == false ||
				txtConfirmacion.getText().equals("") == false)
		{
			try
			{
				String nombre = txtNombre.getText().trim();
				String apellido = txtApellido.getText().trim();
				int edad = Integer.parseInt(txtEdad.getText().trim());
				String direccion = txtDireccion.getText().trim();
				String pais = txtPais.getText().trim();
				String correo = txtCorreo.getText().trim();
				String user = txtUsername.getText().trim();
				String nick = txtNickname.getText().trim();
				String pass = txtContrasena.getText().trim();
				String conf = txtConfirmacion.getText().trim();
				
				if(pass.equals(conf) == true && pass.length() >= 8 && !pass.equals(pass.toLowerCase()) && !pass.matches("[A-Za-z0-9 ]*"))
				{
					String reg = interfaz.registrarUsuario(nombre, apellido, edad, direccion, pais, correo, user, nick, pass);
					
					if(reg.equals("Exito"))
					{
						JOptionPane.showMessageDialog(this, "Registro exitoso.", "Exito", JOptionPane.INFORMATION_MESSAGE);
						dispose();
					}
					else if(reg.equals("User"))
					{
						JOptionPane.showMessageDialog(this, "El usuario ya existe.", "Error de Usuario", JOptionPane.ERROR_MESSAGE);
					}
				}
				else
				{
					JOptionPane.showMessageDialog(this, "Las contraseñas no cumplen los estandares de seguridad.", "Error de Contraseña", JOptionPane.ERROR_MESSAGE);
				}
			}
			catch(NumberFormatException e)
			{
				JOptionPane.showMessageDialog(this, "Hay error en algun campo ingresado.", "Error", JOptionPane.ERROR_MESSAGE);
			}
		}
		else
		{
			JOptionPane.showMessageDialog(this, "Algun campo ingresado esta vacio.", "Error", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	public void cancelarRegistro()
	{
		dispose();
	}
}
