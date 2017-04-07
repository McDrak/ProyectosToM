package co.edu.javeriana.IntroSec.negocio;

public class MedicalStat 
{
	private User dueno;

	public MedicalStat(User dueno) {
		this.dueno = dueno;
	}

	public User getDueno() {
		return dueno;
	}

	public void setDueno(User dueno) {
		this.dueno = dueno;
	}
}
