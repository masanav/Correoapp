package com.tfg.app.aplicacion.modelos.entidades;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "Clase del objeto Perfil")
@Entity
@Table(name = "perfiles")
@JsonIgnoreProperties(value = {"handler", "hibernateLazyInitializer", "_links"})
public class Perfil implements Serializable {

	@ApiModelProperty(notes = "Identificador del perfil", required = false, example = "1L", position = 0)
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ApiModelProperty(notes = "Username del perfil", required = true, example = "mialiasenlaaplicacion", position = 1)
	@NotBlank
	@Size(min = 5, max = 50)
	@Column(length = 50)
	private String username;

	@ApiModelProperty(notes = "Contraseña del perfil", required = true, example = "micontraseñasecreta", position = 2)
	@NotBlank
	@Size(min = 5, max = 60)
	@Column(length = 20)
	private String password;

	@ApiModelProperty(notes = "Estado del perfil", required = true, example = "true", position = 1)
	@NotNull
	private Boolean enabled;

	@ApiModelProperty(notes = "Rol del perfil", required = true, example = "1L", position = 3)
	@NotNull
	private Long rol;

	@ApiModelProperty(notes = "Intentos de autenticación del perfil", required = false, example = "mialiasenlaaplicacion", position = 4)
	@Transient
	private Integer intentos;

	@ApiModelProperty(notes = "Nombre del perfil", required = true, example = "Xitzel", position = 5)
	@NotBlank
	@Size(min = 3, max = 50)
	@Column(length = 50)
	private String nombre;

	@ApiModelProperty(notes = "Apellidos del perfil", required = true, example = "Tepal Montes de Oca", position = 6)
	@NotBlank
	@Size(min = 3, max = 50)
	@Column(length = 50)
	private String apellidos;
	
	@ApiModelProperty(notes = "Archivo de la foto", required = false, position = 9)
	@Lob
	@JsonIgnore
	@Column
	private byte[] fotolob;
	
	@ApiModelProperty(notes = "Empresa asociada al perfil", required = true, position = 10)
	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "empresa_id")
	@JsonIgnoreProperties(value = { "perfiles","contactos", "plantillas"}, allowSetters = true)
	@ToString.Exclude
	private Empresa empresa;

	@JsonIgnore
	private static final long serialVersionUID = 1L;
	
	public Perfil(String username, String password, Boolean enabled, Long rol, String nombre, String apellidos, @Valid Empresa empresa) {
		this.username = username;
		this.password = password;
		this.enabled = enabled;
		this.rol = rol;
		this.nombre = nombre;
		this.apellidos = apellidos;
		this.empresa = empresa;
	}
	
	public Integer getFotolobHashCode() {
		return (this.fotolob != null)? this.fotolob.hashCode(): null;
	}
	
}