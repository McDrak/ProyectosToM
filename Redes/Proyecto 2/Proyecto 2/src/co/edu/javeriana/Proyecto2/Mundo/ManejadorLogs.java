/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.javeriana.Proyecto2.Mundo;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 *
 * @author root
 */
public class ManejadorLogs 
{
    private static Logger LOG;
    private static boolean corriendo = false;
    
    private static void initialize()
    {
        LOG = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
        FileHandler manejadorArchivos;
        try 
        {
            manejadorArchivos = new FileHandler("./data/logDhcp.log", true);
            LOG.addHandler(manejadorArchivos);
            SimpleFormatter formatoTexto = new SimpleFormatter();
            manejadorArchivos.setFormatter(formatoTexto);
        } 
        catch (IOException ex) 
        {
            Logger.getLogger(ManejadorLogs.class.getName()).log(Level.SEVERE, null, ex);
        } 
        catch (SecurityException ex) 
        {
            Logger.getLogger(ManejadorLogs.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        corriendo = true;
    }
    
    public static void escribirEnLog(String texto)
    {
        if(corriendo != true)
        {
            initialize();
        }
        
        LOG.info(texto);
        InterfazProyecto.escribirLineaTextPane(texto);
    }
}
