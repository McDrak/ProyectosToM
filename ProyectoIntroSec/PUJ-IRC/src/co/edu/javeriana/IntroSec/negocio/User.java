package co.edu.javeriana.IntroSec.negocio;

import java.util.Date;

public class User 
{
	private String username;
	private String nickname;
	private String nombre;
	private String apellido;
	private int edad;
	private String direccion;
	private String pais;
	private Wallet wallet;
	private String email;
	private String contrasena;
	private String last_hash;
	private String level;
	private MedicalStat condidcionMedica;
	private Date fechaCambio;
	
	public User(String username, String nickname, String nombre, String apellido, int edad, String direccion,
			String pais, String email, String contrasena) {
		this.username = username;
		this.nickname = nickname;
		this.nombre = nombre;
		this.apellido = apellido;
		this.edad = edad;
		this.direccion = direccion;
		this.pais = pais;
		this.wallet = new Wallet(0, this);
		this.email = email;
		this.contrasena = contrasena;		
		this.setLast_hash(last_hash);
		this.level = "Usuario";
		this.setFechaCambio(new Date());
		this.condidcionMedica = new MedicalStat(this);
	}

	@Override
	public String toString() {
		return "User [username=" + username + ", nickname=" + nickname + ", nombre=" + nombre + ", apellido=" + apellido
				+ ", edad=" + edad + ", direccion=" + direccion + ", pais=" + pais + ", wallet=" + wallet + ", email="
				+ email + ", contrasena=" + contrasena + ", last_hash=" + last_hash + ", level=" + level
				+ ", condidcionMedica=" + condidcionMedica + "]";
	}
	
	public int diasHastaCambioContrasena()
	{
		Date actual = new Date();
		
		int diff = (int) (actual.getTime() - this.getFechaCambio().getTime())/(1000 * 60 * 60 * 24);
		
		return diff;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellido() {
		return apellido;
	}

	public void setApellido(String apellido) {
		this.apellido = apellido;
	}

	public int getEdad() {
		return edad;
	}

	public void setEdad(int edad) {
		this.edad = edad;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public String getPais() {
		return pais;
	}

	public void setPais(String pais) {
		this.pais = pais;
	}

	public Wallet getWallet() {
		return wallet;
	}

	public void setWallet(Wallet wallet) {
		this.wallet = wallet;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getContrasena() {
		return contrasena;
	}

	public void setContrasena(String contrasena) {
		this.contrasena = contrasena;
	}

	public MedicalStat getCondidcionMedica() {
		return condidcionMedica;
	}

	public void setCondidcionMedica(MedicalStat condidcionMedica) {
		this.condidcionMedica = condidcionMedica;
	}

	public String getLast_hash() {
		return last_hash;
	}

	public void setLast_hash(String last_hash) {
		this.last_hash = last_hash;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public Date getFechaCambio() {
		return fechaCambio;
	}

	public void setFechaCambio(Date fechaCambio) {
		this.fechaCambio = fechaCambio;
	}
}
