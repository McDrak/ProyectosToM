/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.javeriana.Proyecto2.Mundo;

import co.edu.javeriana.Proyecto2.Utils.ByteString;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JTextPane;

/**
 *
 * @author root
 */
public class InterfazProyecto extends javax.swing.JFrame 
{
    public final static int PUERTO_SERVER = 67;
    public final static int BUFFER = 1024;
    
    private static JTextPane paneDeEscritura = null;
    
    public static void Initialize()
    {
        Properties props = new Properties();
        
        InputStream is = null;
        
        try 
        {
            is = new FileInputStream("./data/configuracion.properties");
            props.load(is);
        } 
        catch (FileNotFoundException ex) 
        {
            Logger.getLogger(InterfazProyecto.class.getName()).log(Level.SEVERE, null, ex);
        } 
        catch (IOException ex) 
        {
            Logger.getLogger(InterfazProyecto.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally
        {
            if(is != null)
            {
                try 
                {
                    is.close();
                } 
                catch (IOException ex) 
                {
                    Logger.getLogger(InterfazProyecto.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        
        DatagramSocket ds;
        
        int lease = Integer.parseInt(props.getProperty("lease"));
        byte[] ip = ByteString.IpStringToByteArray(props.getProperty("ip"));
        byte[] networkIp = ByteString.IpStringToByteArray(props.getProperty("network"));
        int mask = Integer.parseInt(props.getProperty("mask"));
        ServidorDHCP servidor = new ServidorDHCP(lease, ip, networkIp, mask);
        
        try 
        {
            ds = new DatagramSocket(PUERTO_SERVER);
            ManejadorLogs.escribirEnLog("Puerto de servidor: " + PUERTO_SERVER);
            byte[] dataRecibida = new byte[BUFFER];
            
            while(true)
            {
                DatagramPacket mensajeRecibido = new DatagramPacket(dataRecibida, dataRecibida.length);
                ds.receive(mensajeRecibido);
                
                List<Byte> respuesta = servidor.manejoMensaje(dataRecibida, mensajeRecibido.getLength());
                
                int len = respuesta.size();
		byte[] bArrayRespuesta = new byte[len];
		for (int i = 0; i < len; i++) 
                {
			bArrayRespuesta[i] = respuesta.get(i);
		}
                
                if(respuesta != null)
                {
                    ds.send(new DatagramPacket(bArrayRespuesta, 0, respuesta.size(), 
                            InetAddress.getByAddress(new byte[]{-1, -1, -1, -1}), 
                            mensajeRecibido.getPort()));
                }
            }
        } 
        catch (SocketException ex) 
        {
            Logger.getLogger(InterfazProyecto.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(InterfazProyecto.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Creates new form InterfazProyecto
     */
    public InterfazProyecto() {
        initComponents();
        paneDeEscritura = txtPaneLog;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        btnIniciarServer = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtPaneLog = new javax.swing.JTextPane();
        jLabel1 = new javax.swing.JLabel();
        btnTerminarServidor = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        btnIniciarServer.setText("Iniciar Servidor");
        btnIniciarServer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnIniciarServerActionPerformed(evt);
            }
        });

        jScrollPane2.setViewportView(txtPaneLog);

        jLabel1.setFont(new java.awt.Font("Nimbus Sans L", 1, 24)); // NOI18N
        jLabel1.setText("Servidor DHCP - Proyecto Redes 2 2016-01");

        btnTerminarServidor.setText("Terminar Servidor");
        btnTerminarServidor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTerminarServidorActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(105, 105, 105)
                                .addComponent(btnIniciarServer)
                                .addGap(74, 74, 74)
                                .addComponent(btnTerminarServidor))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(52, 52, 52)
                                .addComponent(jLabel1)))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane2)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(42, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 276, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnIniciarServer)
                    .addComponent(btnTerminarServidor))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnIniciarServerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnIniciarServerActionPerformed
        new Thread(new Runnable() {
            @Override
            public void run() {
                Initialize();
            }
        }).start();
    }//GEN-LAST:event_btnIniciarServerActionPerformed

    private void btnTerminarServidorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTerminarServidorActionPerformed
        JOptionPane.showMessageDialog(null, "Gracias por usar la version de prueba! :D", "Mensaje Final", JOptionPane.INFORMATION_MESSAGE);
        Runtime.getRuntime().exit(0);
    }//GEN-LAST:event_btnTerminarServidorActionPerformed

    public static void escribirLineaTextPane(String linea)
    {
        if(paneDeEscritura != null)
        {
            paneDeEscritura.setText(paneDeEscritura.getText() + linea + "\n");
        }
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(InterfazProyecto.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(InterfazProyecto.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(InterfazProyecto.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(InterfazProyecto.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new InterfazProyecto().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnIniciarServer;
    private javax.swing.JButton btnTerminarServidor;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextPane txtPaneLog;
    // End of variables declaration//GEN-END:variables
}
