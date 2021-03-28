package com.tfg.app.aplicacion.modelos.entidades;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "Clase del objeto Empresa")
@Entity
@Table(name = "empresas")
@JsonIgnoreProperties(value = {"handler", "hibernateLazyInitializer", "_links"})
public class Empresa implements Serializable {

	@ApiModelProperty(notes = "Identificador de la empresa", required = false, example = "1L", position = 0)
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ApiModelProperty(notes = "Nombre de la empresa", required = true, example = "Mi empresa espectacular", position = 1)
	@NotBlank
	@Size(min = 3, max = 50)
	@Column(length = 50)
	private String nombre;

	@ApiModelProperty(notes = "Dirección de la empresa", required = true, example = "Calle sin nombre S/N, 90564 Ciudad fantasma", position = 2)
	@NotBlank
	@Size(min = 12)
	@Column
	private String direccion;

	@ApiModelProperty(notes = "Fecha de registro", required = false, position = 3)
	@Column(name = "fecha_registro")
	@Temporal(TemporalType.DATE)
	private Date fechaRegistro;

	@ApiModelProperty(notes = "Perfiles asociados a la empresa", required = false, position = 4)
	@OneToMany(mappedBy = "empresa", fetch = FetchType.LAZY)
	@JsonIgnoreProperties(value = { "empresa" }, allowSetters = true)
	@ToString.Exclude
	private List<Perfil> perfiles;

	@ApiModelProperty(notes = "Contactos asociados a la empresa", required = false, position = 5)
	@OneToMany(mappedBy = "empresa", fetch = FetchType.LAZY)
	@JsonIgnoreProperties(value = { "empresa" }, allowSetters = true)
	@ToString.Exclude
	private List<Contacto> contactos;

	@ApiModelProperty(notes = "Plantillas asociadas a la empresa", required = false, position = 6)
	@OneToMany(mappedBy = "empresa", fetch = FetchType.LAZY)
	@JsonIgnoreProperties(value = { "empresa" }, allowSetters = true)
	@ToString.Exclude
	private List<Plantilla> plantillas;

	@ApiModelProperty(notes = "Correo electrónico de la empresa", required = true, example = "info@empresa.org", position = 7)
	@Email (regexp = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9]))\\.){3}(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9])|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])")
	@NotBlank
	@Size(min = 6, max = 50)
	@Column(unique = true, length = 50)
	private String correo;

	@ApiModelProperty(notes = "Firma del correo electrónico", required = false, example = "Yo y mi sombra asociados<br>Departamento de información", position = 8)
	@Column
	private String firmaCorreo;

	@JsonIgnore
	private static final long serialVersionUID = 1L;

	@PrePersist
	public void prePersist() {
		this.fechaRegistro = new Date();
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

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public Date getFechaRegistro() {
		return fechaRegistro;
	}

	public void setFechaRegistro(Date fechaRegistro) {
		this.fechaRegistro = fechaRegistro;
	}

	public List<Perfil> getPerfiles() {
		if (perfiles == null) {
			perfiles = new ArrayList<>();
		}
		return perfiles;
	}

	public void setPerfiles(List<Perfil> perfiles) {
		this.perfiles = perfiles;
	}

	public List<Contacto> getContactos() {
		if (contactos == null) {
			contactos = new ArrayList<>();
		}
		return contactos;
	}

	public void setContactos(List<Contacto> contactos) {
		this.contactos = contactos;
	}

	public List<Plantilla> getPlantillas() {
		if (plantillas == null) {
			plantillas = new ArrayList<>();
		}
		return plantillas;
	}

	public void setPlantillas(List<Plantilla> plantillas) {
		this.plantillas = plantillas;
	}

	public String getCorreo() {
		return correo;
	}

	public void setCorreo(String correo) {
		this.correo = correo;
	}

	public String getFirmaCorreo() {
		return firmaCorreo;
	}

	public void setFirmaCorreo(String firma_correo) {
		this.firmaCorreo = firma_correo;
	}

	public void addPlantilla(Plantilla plantilla) {
		getPlantillas().add(plantilla);
		plantilla.setEmpresa(this);
	}

	public void removePlantilla(Plantilla plantilla) {
		getPlantillas().remove(plantilla);
		plantilla.setEmpresa(null);
	}

	public void addContacto(Contacto contacto) {
		getContactos().add(contacto);
		contacto.setEmpresa(this);
	}

	public void removeContacto(Contacto contacto) {
		getContactos().remove(contacto);
		contacto.setEmpresa(null);
	}

	public void addPerfil(Perfil perfil) {
		getPerfiles().add(perfil);
	}

	public void removePerfil(Perfil perfil) {
		getPerfiles().remove(perfil);
	}
	/*
	public List<Orden> getOrdenes() {
		if (ordenes == null) {
			ordenes = new ArrayList<>();
		}
		return ordenes;
	}

	public void setOrdenes(List<Orden> ordenes) {
		this.ordenes = ordenes;
	}
	
	public void addOrden(Orden orden) {
		getOrdenes().add(orden);
		orden.setEmpresa(this);
	}

	public void removeOrden(Orden orden) {
		getOrdenes().remove(orden);
		orden.setEmpresa(null);
	}
*/
}