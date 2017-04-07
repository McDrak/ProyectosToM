package co.edu.javeriana.IntroSec.interfazCliente;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class ClientHomeDialog extends JDialog 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private final JPanel contentPanel = new JPanel();
	private JTextField txtCommands;
	private JTextArea txtLog;
	
	private ClientInterface interfaz;

	/**
	 * Create the dialog.
	 */
	public ClientHomeDialog() 
	{
		interfaz = new ClientInterface();
		setTitle("Home");
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout(0, 0));
		
		txtLog = new JTextArea();
		txtLog.setEditable(false);
		contentPanel.add(txtLog, BorderLayout.CENTER);
		
		txtCommands = new JTextField();
		txtCommands.addActionListener(new ActionListener() 
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				manejoComando(txtCommands.getText());
				txtCommands.setText("");
			}
		});
		contentPanel.add(txtCommands, BorderLayout.NORTH);
		txtCommands.setColumns(10);
	}
	
	public void manejoComando(String comando)
	{
		txtLog.append(comando + "\n");
		String resp = interfaz.commandManagenet(comando);
		txtLog.append(resp + "\n");
		
		if(resp.startsWith("EXIT"))
		{
			dispose();
			System.exit(0);
		}
	}
}
