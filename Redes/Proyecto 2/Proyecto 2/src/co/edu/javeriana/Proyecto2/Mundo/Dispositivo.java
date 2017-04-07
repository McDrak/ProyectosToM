package co.edu.javeriana.Proyecto2.Mundo;

public class Dispositivo {
	private Estado status;
	private byte[] macAddress;
	private int leaseTime;
	
	public Estado getStatus() {
		return status;
	}
	public int getLeaseTime() {
		return leaseTime;
	}
	public void setLeaseTime(int leaseTime) {
		this.leaseTime = leaseTime;
	}
	public void setStatus(Estado status) {
		this.status = status;
	}
	public byte[] getMacAddress() {
		return macAddress;
	}
	public void setMacAddress(byte[] macAddress) {
		this.macAddress = macAddress;
	}
	public Dispositivo(Estado status, byte[] macAddress, int leaseTime) {
		this.status = status;
		this.macAddress = macAddress;
		this.leaseTime = leaseTime;
	}
}
