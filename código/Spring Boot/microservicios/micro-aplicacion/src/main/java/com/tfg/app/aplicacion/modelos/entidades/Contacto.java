package com.tfg.app.aplicacion.modelos.entidades;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Email;
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
@ApiModel(description = "Clase del objeto Contacto")
@Entity
@Table(name = "contactos")
@JsonIgnoreProperties(value = {"handler", "hibernateLazyInitializer", "_links"})
public class Contacto implements Serializable {

	@ApiModelProperty(notes = "Identificador del contacto", required = false, example = "1L", position = 0)
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ApiModelProperty(notes = "Nombre del contacto", required = true, example = "Freud", position = 1)
	@NotBlank
	@Size(min = 3, max = 50)
	@Column(length = 50)
	private String nombre;

	@ApiModelProperty(notes = "Apellidos del contacto", required = true, example = "López Castaña", position = 2)
	@NotBlank
	@Size(min = 3, max = 50)
	@Column(length = 50)
	private String apellidos;

	@ApiModelProperty(notes = "Estado del contacto", required = true, example = "true", position = 3)
	@NotNull
	private Boolean enabled;

	@ApiModelProperty(notes = "Fecha de registro del contacto", required = false, position = 4)
	@Column(name = "fecha_registro")
	@Temporal(TemporalType.DATE)
	private Date fechaRegistro;
	
	@ApiModelProperty(notes = "Correo electrónico del contacto", required = true, example = "micorreo@decontacto.ve", position = 5)
	@Email
	@NotBlank
	@Size(min = 6, max = 50)
	@Column(length = 50)
	private String correo;
	
	@ApiModelProperty(notes = "Empresa asociada al contacto", required = false, example = "1L", position = 6)
	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "empresa_id")
	@JsonIgnoreProperties(value = {"perfiles","contactos","plantillas"})
	@ToString.Exclude
	private Empresa empresa;
	
	@JsonIgnore
	private static final long serialVersionUID = 1L;

	@PrePersist
	public void prePersist() {
		this.fechaRegistro = new Date();
	}

}