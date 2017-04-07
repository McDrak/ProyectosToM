/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.javeriana.Proyecto2.Mundo;

import co.edu.javeriana.Proyecto2.Utils.ByteString;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author root
 */
public class ManejadorDHCP 
{
    private int serverIP;
    private int networkIP;
    private int mask;
    private Map<Integer, Dispositivo> pool;

    public static int byteAInt(byte[] b) 
    {
        /*if (b.length == 4)
          return b[0] << 24 | (b[1] & 0xff) << 16 | (b[2] & 0xff) << 8
              | (b[3] & 0xff);
        else if (b.length == 2)
          return 0x00 << 24 | 0x00 << 16 | (b[0] & 0xff) << 8 | (b[1] & 0xff);*/

        return ByteBuffer.wrap(b).getInt();
    }

    public static byte[] intAByte(int value) {
            return new byte[] { (byte) (value >>> 24), (byte) (value >> 16 & 0xff), (byte) (value >> 8 & 0xff),
                            (byte) (value & 0xff) };
    }

    public byte[] obtenerIpServidor() {
            return intAByte(serverIP);
    }

    public byte[] obtenerIpRed() {
            return intAByte(networkIP);
    }

    int obtenerHostsDisponibles(int mask) {
            return (int) Math.pow(2, 32 - mask);
    }

    public byte[] obtenerMascaraDecimal() {
            if (mask == 0)
                    return intAByte(0);
            int allOne = -1;
            int shifted = allOne << (32 - mask);
            return intAByte(shifted);
    }

    private void llenarMapa() 
    {
        int offset = 32 - mask;
        int primaryIP = (networkIP >> offset) << offset;

        int numDevices = obtenerHostsDisponibles(mask);
        for (int i = 3; i < numDevices - 1; i++) 
        {
            int ip = primaryIP + i;
            byte[] temp = intAByte(ip);
            
            if (ip != this.serverIP)
            {
                pool.put(ip, new Dispositivo(Estado.DISPONIBLE, null, 0));
            }
        }
    }

    public ManejadorDHCP(byte[] networkIP, byte[] serverIP, int mask) 
    {
            this.networkIP = byteAInt(networkIP);
            this.serverIP = byteAInt(serverIP);
            this.mask = mask;

            pool = new HashMap<Integer, Dispositivo>();
            llenarMapa();
    }

    public byte[] obtenerIpLibre() {
            for (Integer key : pool.keySet()) {
                    Dispositivo address = pool.get(key);
                    if (address.getStatus() == Estado.DISPONIBLE) {
                            return intAByte(key.intValue());
                    }
            }
            return null;
    }

    public void setIP(byte[] ip, byte[] mac, int leaseTime) {
            int numericIP = byteAInt(ip);
            pool.get(numericIP).setMacAddress(mac);
            pool.get(numericIP).setLeaseTime(leaseTime);
            pool.get(numericIP).setStatus(Estado.OCUPADO);
    }

    public byte[] obtenerIpPorMac(byte[] mac) {
            for (Integer key : pool.keySet()) {
                    Dispositivo address = pool.get(key);
                    if (address.getMacAddress() != null
                                    && ByteString.ByteArrayToHexString(address.getMacAddress()).equals(ByteString.ByteArrayToHexString(mac)))
                            return intAByte(key);
            }
            return null;
    }

    public void releaseIP(byte[] ip) {
            int ipAsInt = byteAInt(ip);
            if (ipAsInt != 0) {
                    pool.get(ipAsInt).setStatus(Estado.DISPONIBLE);
                    pool.get(ipAsInt).setMacAddress(null);
                    pool.get(ipAsInt).setLeaseTime(0);
            }
    }
}
