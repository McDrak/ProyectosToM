package co.edu.javeriana.IntroSec.negocio;

public class Wallet 
{
	private int saldo;
	private User dueno;
	
	public Wallet(int saldo, User dueno) {
		this.saldo = saldo;
		this.dueno = dueno;
	}

	public int getSaldo() {
		return saldo;
	}

	public void setSaldo(int saldo) {
		this.saldo = saldo;
	}

	public User getDueno() {
		return dueno;
	}

	public void setDueno(User dueno) {
		this.dueno = dueno;
	}
}
