package vos;

import org.codehaus.jackson.annotate.JsonProperty;

public class Usuario {

	@JsonProperty(value= "id")
	private Long id;
	
	@JsonProperty(value= "nombre")
	private String nombre;
	
	@JsonProperty(value= "correoElectronico")
	private String correoElectronico;
	
	@JsonProperty(value= "rol")
	private String rol;
	
	@JsonProperty(value= "contrasenia")
	private String contrasenia;


	public Usuario(@JsonProperty(value= "id") Long id, @JsonProperty(value= "nombre") String nombre, @JsonProperty(value= "correoElectronico") String correoElectronico, @JsonProperty(value= "rol") String rol, @JsonProperty(value="contrasenia") String contrasenia) {
		
		this.id = id;
		this.nombre = nombre;
		this.correoElectronico = correoElectronico;
		this.rol = rol;
		this.contrasenia= contrasenia;
	}


	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public String getNombre() {
		return nombre;
	}


	public void setNombre(String nombre) {
		this.nombre = nombre;
	}


	public String getCorreoElectronico() {
		return correoElectronico;
	}


	public void setCorreoElectronico(String correoElectronico) {
		this.correoElectronico = correoElectronico;
	}


	public String getRol() {
		return rol;
	}


	public void setRol(String rol) {
		this.rol = rol;
	}


	public String getContrasenia() {
		return contrasenia;
	}


	public void setContrasenia(String constrasenia) {
		this.contrasenia = constrasenia;
	}
	
}
