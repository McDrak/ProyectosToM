/*
 * Tomado de Github
 * https://gist.github.com/easternHong/6ca75a7fdce15c6a23d3
 */
package co.edu.javeriana.Proyecto2.Utils;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author mcdrak
 */
public class ByteString 
{
    /**
     * Utility method to convert a byte array to a hexadecimal string.
     *
     * @param bytes Bytes to convert
     * @return String, containing hexadecimal representation.
     */
    public static String ByteArrayToHexString(byte[] bytes) {
        final char[] hexArray = {'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};
        char[] hexChars = new char[bytes.length * 2]; // Each byte has two hex characters (nibbles)
        int v;
        for (int j = 0; j < bytes.length; j++) {
            v = bytes[j] & 0xFF; // Cast bytes[j] to int, treating as unsigned value
            hexChars[j * 2] = hexArray[v >>> 4]; // Select hex character from upper nibble
            hexChars[j * 2 + 1] = hexArray[v & 0x0F]; // Select hex character from lower nibble
        }
        return new String(hexChars);
    }
    /**
     * Utility method to convert a hexadecimal string to a byte string.
     *
     * <p>Behavior with input strings containing non-hexadecimal characters is undefined.
     *
     * @param s String containing hexadecimal characters to convert
     * @return Byte array generated from input
     * @throws java.lang.IllegalArgumentException if input length is incorrect
     */
    public static byte[] HexStringToByteArray(String s) throws IllegalArgumentException {
        int len = s.length();
        if (len % 2 == 1) {
            throw new IllegalArgumentException("Hex string must have even number of characters");
        }
        byte[] data = new byte[len / 2]; // Allocate 1 byte per 2 hex characters
        for (int i = 0; i < len; i += 2) {
            // Convert each character into a integer (base-16), then bit-shift into place
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }
    
    public static byte[] IpStringToByteArray(String s)
    {
        byte[] resp = null;
        
        try 
        {
            InetAddress addr = InetAddress.getByName(s);
            resp = addr.getAddress();
        } 
        catch (UnknownHostException ex) 
        {
            Logger.getLogger(ByteString.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return resp;
    }
    
    public static String byteArrayToIpSring(byte[] ip) 
    {
	return ((int) ip[0] & 0xff) + "." + ((int) ip[1] & 0xff) + "." + ((int) ip[2] & 0xff) + "." + ((int) ip[3] & 0xff);
    }
}
