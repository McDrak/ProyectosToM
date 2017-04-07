/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.javeriana.Proyecto2.Mundo;

import co.edu.javeriana.Proyecto2.Utils.ByteString;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author root
 */
public class ServidorDHCP 
{
    private ManejadorDHCP manejador;
    private int tiempoLease;
    
    public ServidorDHCP(int lease, byte[] ip, byte[] networkIp, int mask)
    {
        manejador = new ManejadorDHCP(networkIp, ip, mask);
        tiempoLease = lease;
    }
    
    public List<Byte> manejoMensaje(byte[] buffer, int longitud) 
    {
        String tipo = "";
        int leaseTime = this.tiempoLease;
        String hostName = "";
        List<Byte> bufferon= new ArrayList<Byte>();
        
        int i=0;
        while (i<longitud)
        {
            bufferon.add(buffer[i]);
            i++;
        }
        
        byte op =bufferon.get(0);
        bufferon.remove(0);
        
        byte htype =bufferon.get(0);
        bufferon.remove(0);
        
        byte hlen = bufferon.get(0);
        bufferon.remove(0);
        
        byte hops = bufferon.get(0);
        bufferon.remove(0);
        
        byte[] xid = new byte[4];
        for(int j=0;j<4;j++)
        {
            xid[j] = bufferon.get(0);
            bufferon.remove(0);
        }
        
        byte[] secs = new byte[2];
        for(int j=0;j<2;j++)
        {
            secs[j]= bufferon.get(0);
            bufferon.remove(0);
        }
        
        byte[] flags = new byte[2];
        for(int j=0;j<2;j++)
        {
            flags[j] = bufferon.get(0);
            bufferon.remove(0);
        }
        
        byte[] ciAddr = new byte[4];
        for(int j=0;j<4;j++)
        {
            ciAddr[j] = bufferon.get(0);
            bufferon.remove(0); 
        }
        
        byte[] yiAddr = new byte[4];
        for(int j=0;j<4;j++)
        {
            yiAddr[j] = bufferon.get(0);
            bufferon.remove(0);
        }
        
        byte[] siAddr = new byte[4];
        for(int j=0;j<4;j++)
        {
            siAddr[j] = bufferon.get(0);
            bufferon.remove(0);
        }
        
        byte[] giAddr = new byte[4];
        for(int j=0;j<4;j++)
        {
            giAddr[j] = bufferon.get(0);
            bufferon.remove(0);
        }
        
        byte[] chAddr = new byte[16];
        for(int j=0;j<16;j++)
        {
            chAddr[j]= bufferon.get(0);
            bufferon.remove(0);
        }
        
        byte[] varios = new byte[192];
        for(int j=0;j<192;j++)
        {
            varios[j] = bufferon.get(0);
            bufferon.remove(0);
        }
        
        byte[] magicCookie = new byte[4];
        for(int j=0;j<4;j++)
        {
            magicCookie[j]= bufferon.get(0);
            bufferon.remove(0);
        }

        while (bufferon.size()> 0) 
        {
            byte code = bufferon.get(0);
            bufferon.remove(0);
            byte optionLen = bufferon.get(0);
            bufferon.remove(0);
            if(code==53)
            {
                byte type = bufferon.get(0);
                bufferon.remove(0);
                if(type==1)
                {
                        tipo="DHCPDISCOVER";
                }
                if(type==2)
                {
                        tipo="DHCPOFFER";
                }
                if(type==3)
                {
                        tipo="DHCPREQUEST";
                }
                if(type==4)
                {
                        tipo="DHCPDECLINE";
                }
                if(type==5)
                {
                        tipo="DHCPACK";
                }
                if(type==6)
                {
                        tipo="DHCPNAK";
                }
                if(type==7)
                {
                        tipo="DHCPRELEASE";
                }
                if(type==8)
                {
                        tipo="DHCPINFORM";
                }
                break;
            }

            if(code==51)
            {
                byte[]holi = new byte[4];
                for(int j=0;j<4;j++)
                {
                    holi[j] = bufferon.get(0);
                    bufferon.remove(0);
                }
                leaseTime = byteArrayToInt(holi);
                break;
            }

            if(code==12)
            {
                    byte[] nameBytes = new byte[optionLen];
                    for(int j=0;j<optionLen;j++)
                    {
                        nameBytes[j] = bufferon.get(0);
                        bufferon.remove(0);
                    }
                    hostName = new String(nameBytes);
                    break;
            }

            else
            {
                break;
            }
        }
        
        ManejadorLogs.escribirEnLog("Mensaje Recibido: " + tipo.toString());
        ManejadorLogs.escribirEnLog("IP Cliente: " + ByteString.byteArrayToIpSring(ciAddr) + " - MAC Cliente: " + ByteString.ByteArrayToHexString(chAddr));

        if (tipo.equals("DHCPDISCOVER")) 
        {
            ManejadorLogs.escribirEnLog("DHCPOFFER Enviado!");
            return generarMensaje(xid, chAddr,2, leaseTime);
        }
        
        if (tipo.equals("DHCPREQUEST")) 
        {
            ManejadorLogs.escribirEnLog("DHCPACK Enviado!");
            return generarMensaje(xid, chAddr,5, leaseTime);
        }
        
        if (tipo.equals("DHCPRELEASE")) 
        {
            ManejadorLogs.escribirEnLog("DHCPACK Enviado!");
            return generarMensaje(xid, chAddr,5, leaseTime);
        }
        
        return null;
    }
    
    public List<Byte> generarMensaje(byte[] xid, byte[] clientHardwareAddr, int type, int currentLeaseTime) 
    {
        List<Byte> mensaje = new ArrayList<Byte>();
        String tipoMensaje = "";
        
        mensaje.add((byte)2); // op
        mensaje.add((byte)1); // htype
        mensaje.add((byte)6); // hlen
        mensaje.add((byte)0); // hops

        for (byte b : xid) mensaje.add(b); // xid                

        mensaje.add((byte)0); // secs
        mensaje.add((byte)0); // secs

        mensaje.add((byte)128); // flags
        mensaje.add((byte)0); // flags

        byte[] ciaddr = new byte[] { 0, 0, 0, 0 };
        for (byte b : ciaddr)
            mensaje.add(b); // ciaddr

        //Buscar la IP segun su MAC!!!!!!!!!!!!!!
        byte[] yiaddr = manejador.obtenerIpPorMac(clientHardwareAddr);
        if (yiaddr == null)
                yiaddr = manejador.obtenerIpLibre();
        for (byte b : yiaddr)
            mensaje.add(b); // yiaddr

        byte[] siaddr = manejador.obtenerIpServidor();
        for (byte b : siaddr)
            mensaje.add(b); // siaddr

        byte[] giaddr = new byte[] { 0, 0, 0, 0 };
        for (byte b : giaddr)
            mensaje.add(b); // giaddr

        for (byte b : clientHardwareAddr)
            mensaje.add(b);//chaddr

        byte[] snameNfile = new byte[192];
        for (byte b : snameNfile)
        mensaje.add(b); // sname and file

        byte[] magic = ByteString.HexStringToByteArray("63825363");
        for (byte b : magic)
        mensaje.add(b); // Magic cookie


        // OPTIONS 53 DHCP Message Type

        mensaje.add((byte)53); // type
        mensaje.add((byte)1); // len
        mensaje.add((byte)type); // type

        // OPTIONS 54 Server Identifier
        mensaje.add((byte)54); // type
        mensaje.add((byte)4); // len
        for (byte b : manejador.obtenerIpServidor())
            mensaje.add(b);// server address

        // OPTIONS 1 Subnet Mask
        mensaje.add((byte)1); // type
        mensaje.add((byte)4); // len

        byte[] mask = manejador.obtenerMascaraDecimal();
        for (byte b : mask)
        mensaje.add(b); // mask

        // OPTIONS 3 Router Option
        mensaje.add((byte)3); // type
        mensaje.add((byte)4); // len
        byte[] routerAddress = manejador.obtenerIpServidor();
        for (byte b : routerAddress)
        mensaje.add(b); // router address

        // OPTIONS 6 Domain Name Server Option
        mensaje.add((byte)6);
        mensaje.add((byte)8);

        byte[] nh = new byte[] { 8, 8, 8, 8 };
        for (byte b : nh)
            mensaje.add(b);
        byte[] nh2 = new byte[] { 8, 8, 4, 4 };
        for (byte b : nh2)
            mensaje.add(b);

        // OPTIONS 51 IP Address Lease Time
        mensaje.add((byte)51); // type
        mensaje.add((byte)4); // len

        byte[]time = new byte[] { (byte) (currentLeaseTime >>> 24),
                                  (byte) (currentLeaseTime >> 16 & 0xff),
                                  (byte) (currentLeaseTime >> 8 & 0xff),
                                  (byte) (currentLeaseTime & 0xff) };
        for (byte b : time)
            mensaje.add(b); // time

        mensaje.add((byte)59); // type
        mensaje.add((byte)4); // len

        int temp1 = (int) (0.75 * currentLeaseTime);
        byte[]time1 = new byte[] { (byte) (temp1 >>> 24),
                                  (byte) (temp1 >> 16 & 0xff),
                                  (byte) (temp1 >> 8 & 0xff),
                                  (byte) (temp1 & 0xff) };
        for (byte b : time1)
            mensaje.add(b); // time

        mensaje.add((byte)58); // type
        mensaje.add((byte)4); // len

        int temp2 = (int) (0.5 * currentLeaseTime);
        byte[]time2 = new byte[] { (byte) (temp2 >>> 24),
                                  (byte) (temp2 >> 16 & 0xff),
                                  (byte) (temp2 >> 8 & 0xff),
                                  (byte) (temp2 & 0xff) };
        for (byte b : time2)
            mensaje.add(b); // time

        int DHCPACK = 5;
        if (type == DHCPACK) {
            manejador.setIP(yiaddr, clientHardwareAddr, currentLeaseTime);
        }

        if(type == 2)
        {
            tipoMensaje = "DHCPOFFER";
        }
        else if(type == 5)
        {
            tipoMensaje = "DHCPACK";
        }
        else
        {
            tipoMensaje = "ERROR";
        }
        ManejadorLogs.escribirEnLog("Tipo de Mensaje Enviado: " + tipoMensaje);

        return mensaje;
    }
    
        
    public static int byteArrayToInt(byte[] b) 
    {
        /*if (b.length == 4)
          return b[0] << 24 | (b[1] & 0xff) << 16 | (b[2] & 0xff) << 8
              | (b[3] & 0xff);
        else if (b.length == 2)
          return 0x00 << 24 | 0x00 << 16 | (b[0] & 0xff) << 8 | (b[1] & 0xff);*/

        return ByteBuffer.wrap(b).getInt();
    }
}
