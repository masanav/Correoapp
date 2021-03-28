package com.tfg.app.aplicacion.modelos.entidades;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
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
@ApiModel(description = "Clase del objeto Proveedor")
@Entity
@Table(name = "proveedores")
@JsonIgnoreProperties(value = {"handler", "hibernateLazyInitializer", "_links"})
public class Proveedor implements Serializable {

	@ApiModelProperty(notes = "Identificador del proveedor", required = false, example = "1L", position = 0)
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ApiModelProperty(notes = "Nombre del proveedor", required = true, example = "Proveedor comarcal de correo S.L.", position = 1)
	@NotBlank
	@Size(min = 5, max = 50)
	@Column(unique = true, length = 50)
	private String nombre;

	@ApiModelProperty(notes = "Dirección del proveedor", required = true, example = "Calle Sola 1, 10101 Pequepueblo", position = 2)
	@NotBlank
	@Size(min = 12)
	@Column
	private String direccion;

	@ApiModelProperty(notes = "Correo electrónico del proveedor", required = false, example = "naturarl@rural.es", position = 3)
	@Email
	@NotBlank
	@Size(min = 6, max = 50)
	@Column(unique = true, length = 50)
	private String correo;

	@ApiModelProperty(notes = "Teléfono del proveedor", required = false, example = "625.326.841", position = 4)
	@NotBlank
	@Size(min = 6, max = 20)
	@Column(unique = true, length = 20)
	private String telefono;

	@ApiModelProperty(notes = "Horario del proveedor", required = false, example = "De lunes a viernes en horario de oficina, en horario intensivo", position = 5)
	@Size(max = 100)
	@Column(length = 100)
	private String horario;
	
	@ApiModelProperty(notes = "Web del proveedor", required = true, example = "www.famosoproveedor.biz", position = 0)
	@NotBlank
	@Column(length = 100)
	private String url_direccion;

	@JsonIgnore
	private static final long serialVersionUID = 1L;

}