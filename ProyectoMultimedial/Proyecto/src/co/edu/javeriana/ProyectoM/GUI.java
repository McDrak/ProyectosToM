package co.edu.javeriana.ProyectoM;


import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import javax.media.CannotRealizeException;
import javax.media.Manager;
import javax.media.MediaLocator;
import javax.media.NoPlayerException;
import javax.media.Player;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.awt.event.ActionEvent;
import java.awt.BorderLayout;
import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.UIManager;

public class GUI extends JFrame {
	
	/** serialVersionUID serialVersionUID del objeto GUI.java
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	Player player;
    Component video;
    Component controles;
    private JTextField tx;
    private JTextField ty;
    private JTextField txf;
    private JTextField tyf;
    private JTextField tduracion;
    private int click = 0;
    String videoInput = null;
    String imageInput = "";
    String audioInput = "";
    String videoOutput = null;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
					GUI frame = new GUI();
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
	public GUI() {
		try {
			//UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception evento1) {
			// TODO Auto-generated catch block
			evento1.printStackTrace();
		}
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 920, 659);
		setLocationRelativeTo(null);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBounds(10, 0, 894, 609);
		contentPane.add(panel);
		panel.setLayout(null);
		
		JLabel posicionInicialImagen = new JLabel("Posicion Inicial imagen:");
		posicionInicialImagen.setBounds(703, 191, 156, 14);
		posicionInicialImagen.setVisible(false);
		panel.add(posicionInicialImagen);
		
		JLabel xi = new JLabel("X");
		xi.setBounds(703, 216, 30, 25);
		xi.setVisible(false);
		panel.add(xi);
		
		tx = new JTextField();
		tx.setBounds(713, 218, 38, 20);
		tx.setVisible(false);
		panel.add(tx);
		tx.setColumns(10);
		
		JLabel yi = new JLabel("Y");
		yi.setBounds(770, 216, 30, 25);
		yi.setVisible(false);
		panel.add(yi);
		
		ty = new JTextField();
		ty.setBounds(780, 218, 41, 20);
		ty.setVisible(false);
		panel.add(ty);
		ty.setColumns(10);
		
		JLabel posicionFinalImagen = new JLabel("Posicion Final imagen:");
		posicionFinalImagen.setBounds(703, 252, 156, 14);
		posicionFinalImagen.setVisible(false);
		panel.add(posicionFinalImagen);
		
		JLabel xf = new JLabel("X");
		xf.setBounds(703, 277, 30, 25);
		xf.setVisible(false);
		panel.add(xf);
		
		txf = new JTextField();
		txf.setBounds(713, 277, 38, 25);
		txf.setVisible(false);
		panel.add(txf);
		txf.setColumns(10);
		
		JLabel yf = new JLabel("Y");
		yf.setBounds(770, 277, 30, 25);
		yf.setVisible(false);
		panel.add(yf);
		
		tyf = new JTextField();
		tyf.setBounds(780, 277, 41, 23);
		tyf.setVisible(false);
		panel.add(tyf);
		tyf.setColumns(10);
		
		JLabel duracion = new JLabel("Duracion");
		duracion.setBounds(703, 312, 113, 14);
		duracion.setVisible(false);
		panel.add(duracion);
		
		tduracion = new JTextField();
		tduracion.setBounds(703, 329, 118, 14);
		tduracion.setVisible(false);
		panel.add(tduracion);
		tduracion.setColumns(10);
		
		JButton reiniciar = new JButton("Reiniciar");
		reiniciar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				tx.setText("");
				ty.setText("");
				txf.setText("");
				tyf.setText("");
				click=0;
			}
		});
		reiniciar.setBounds(703, 354, 89, 25);
		reiniciar.setVisible(false);
		panel.add(reiniciar);
		
		JPanel panelVideo = new JPanel();
		panelVideo.setBounds(10, 11, 683, 576);
		panel.add(panelVideo);
		panelVideo.setLayout(new BorderLayout(0, 0));		
		
		JButton btnCargarVideo = new JButton("Cargar video");
		btnCargarVideo.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) {
				JFileChooser escoger = new JFileChooser();
				escoger.setDialogTitle("Video");
				FileNameExtensionFilter filtro = new FileNameExtensionFilter("*.mpg", "mpg");
				escoger.setFileFilter(filtro);
				int seleccion=escoger.showOpenDialog(contentPane);
				if (seleccion==JFileChooser.APPROVE_OPTION) 
				{
					URL url = null;
					try {
						url = escoger.getSelectedFile().toURI().toURL();
						
					} catch (MalformedURLException evento) {
						evento.printStackTrace();
					}
					try {
						videoInput = escoger.getSelectedFile().getPath();
						player = Manager.createRealizedPlayer(new MediaLocator(url));
						video = player.getVisualComponent();
		                video.setSize(683,476);
		                video.setVisible(true);
		                MouseListener raton = new MouseAdapter() {
		        			@Override
		        			public void mouseClicked(MouseEvent e) {
		        				if(posicionInicialImagen.isVisible()) {
		        					if(click == 0) {
		        						String xy = String.valueOf(e.getX());
				        				tx.setText(xy);
				        				xy = String.valueOf(e.getY());
				        				ty.setText(xy);
				        				click++;
		        					}
			        				
			        				if(click >1 && click <=2) {
			        					String xyf = String.valueOf(e.getX());
				        				txf.setText(xyf);
				        				xyf = String.valueOf(e.getY());
				        				tyf.setText(xyf);
				        				click++;
			        				}
			        				click++;
		        				}				
		        			}
		        		};
		                video.addMouseListener(raton);
		                if(video!=null) {
		                	panelVideo.add("Center", video);
		                }

		                controles = player.getControlPanelComponent();
		                controles.setSize(683,100);
		                controles.setVisible(true);
		                if(controles != null) {
		                	panelVideo.add("South", controles);
		                }
		                
		                //getContentPane().add(panelVideo);
		                panelVideo.doLayout();
		                //panel.updateUI();
					} catch (NoPlayerException | CannotRealizeException | IOException evento) {
						evento.printStackTrace();
					}
				}
				
				
			}
		});
		btnCargarVideo.setBounds(738, 11, 133, 34);
		panel.add(btnCargarVideo);
		
		JButton btnCargarAudio = new JButton("Cargar audio");
		btnCargarAudio.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser escoger = new JFileChooser();
				escoger.setDialogTitle("Audio");
				FileNameExtensionFilter filtro = new FileNameExtensionFilter("*.MP3", "mp3");
				escoger.setFileFilter(filtro);
				int seleccion=escoger.showOpenDialog(contentPane);
				if (seleccion==JFileChooser.APPROVE_OPTION) {
					URL url3 = null;
					try {
						url3 = escoger.getSelectedFile().toURI().toURL();
						audioInput = escoger.getSelectedFile().getPath();
					} catch (MalformedURLException evento) {
						// TODO Auto-generated catch block
						evento.printStackTrace();
					}
					try {
						player = Manager.createRealizedPlayer(new MediaLocator(url3));
						controles = player.getControlPanelComponent();
		                controles.setSize(683,100);
		                controles.setVisible(true);
		                if(controles != null) {
		                	panelVideo.add(BorderLayout.NORTH, controles);
		                }
		                panelVideo.doLayout();
					} catch (NoPlayerException | CannotRealizeException | IOException evento) {
						evento.printStackTrace();
					}			
				}
				else {
					audioInput = "";
				}
			}
		});
		btnCargarAudio.setBounds(738, 56, 133, 34);
		panel.add(btnCargarAudio);
		
		JLabel imagen = new JLabel("");
		imagen.setBounds(709, 418, 175, 169);
		panel.add(imagen);
		
		JButton btnCargarImagen = new JButton("Cargar Imagen");
		btnCargarImagen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser escoger = new JFileChooser();
				escoger.setDialogTitle("Imagen");
				FileNameExtensionFilter filtro = new FileNameExtensionFilter("*.PNG", "PNG");
				escoger.setFileFilter(filtro);
				int seleccion=escoger.showOpenDialog(contentPane);
				if (seleccion==JFileChooser.APPROVE_OPTION) {
					try {
						URL url2 = escoger.getSelectedFile().toURI().toURL();
						imageInput = escoger.getSelectedFile().getPath();
						ImageIcon icon = new ImageIcon(url2); 
						imagen.setIcon(icon);
					} catch (MalformedURLException evento) {
						evento.printStackTrace();
					}
				}
			}
		});
		btnCargarImagen.setBounds(738, 101, 133, 34);
		panel.add(btnCargarImagen);

		JButton aplicar = new JButton("Aplicar");
		aplicar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) 
			{
				JOptionPane.showMessageDialog(contentPane, "El nombre del archivo sin espacios porfavor :D", "Atencion", JOptionPane.INFORMATION_MESSAGE);
				JFileChooser video = new JFileChooser();
				video.setDialogTitle("Escoger Salida");
				int seleccion = video.showOpenDialog(contentPane);
				
				if(seleccion == JFileChooser.APPROVE_OPTION)
				{
					videoOutput = video.getSelectedFile().getPath();
					VideoManager v = new VideoManager(videoInput, imageInput, videoOutput, audioInput);
					float px = (Integer.parseInt(tx.getText()) * 100)/panelVideo.getWidth();
					float py = (Integer.parseInt(ty.getText()) * 100)/panelVideo.getHeight();
					float pxf = (Integer.parseInt(txf.getText()) * 100)/panelVideo.getWidth();
					float pyf = (Integer.parseInt(tyf.getText()) * 100)/panelVideo.getHeight();
					v.imgEnVideo(px, py, pxf, pyf, Integer.parseInt(tduracion.getText()));
					posicionInicialImagen.setVisible(false);
					xi.setVisible(false);
					tx.setVisible(false);
					yi.setVisible(false);
					ty.setVisible(false);
					posicionFinalImagen.setVisible(false);
					xf.setVisible(false);
					txf.setVisible(false);
					yf.setVisible(false);
					tyf.setVisible(false);
					duracion.setVisible(false);
					tduracion.setVisible(false);
					reiniciar.setVisible(false);
					aplicar.setVisible(false);
				}
			}
		});
		aplicar.setBounds(703, 382, 89, 25);
		aplicar.setVisible(false);
		panel.add(aplicar);
		
		JButton btnEditar = new JButton("Editar");
		btnEditar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(!posicionFinalImagen.isVisible()) {
				posicionInicialImagen.setVisible(true);
				xi.setVisible(true);
				tx.setVisible(true);
				yi.setVisible(true);
				ty.setVisible(true);
				posicionFinalImagen.setVisible(true);
				xf.setVisible(true);
				txf.setVisible(true);
				yf.setVisible(true);
				tyf.setVisible(true);
				duracion.setVisible(true);
				tduracion.setVisible(true);
				reiniciar.setVisible(true);
				aplicar.setVisible(true);
				}
				else {
					posicionInicialImagen.setVisible(false);
					xi.setVisible(false);
					tx.setVisible(false);
					yi.setVisible(false);
					ty.setVisible(false);
					posicionFinalImagen.setVisible(false);
					xf.setVisible(false);
					txf.setVisible(false);
					yf.setVisible(false);
					tyf.setVisible(false);
					duracion.setVisible(false);
					tduracion.setVisible(false);
					reiniciar.setVisible(false);
					aplicar.setVisible(false);
					
				}
				
			}
		});
		btnEditar.setBounds(738, 146, 133, 34);
		panel.add(btnEditar);
		
		
		
	}
}
