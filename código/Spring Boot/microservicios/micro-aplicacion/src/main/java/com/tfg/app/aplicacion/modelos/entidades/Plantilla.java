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
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

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
@ApiModel(description = "Clase del objeto Plantilla")
@Entity
@Table(name = "plantillas")
@JsonIgnoreProperties(value = {"handler", "hibernateLazyInitializer", "_links"})
public class Plantilla implements Serializable {

	@ApiModelProperty(notes = "Identificador de la plantilla", required = false, example = "1L", position = 0)
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ApiModelProperty(notes = "Texto de la plantilla", required = true, example = "¿Cómo van vuestros ahorros? Sacádlos porque llegan las ofertas de descuento del IVA invertido 79%", position = 1)
	@NotBlank
	@Column
	@Lob
	private String texto;
	
	@ApiModelProperty(notes = "Asunto de la plantilla", required = true, example = "Recordatorio de pago", position = 2)
	@NotBlank
	@Column
	private String asunto;
	
	@ApiModelProperty(notes = "Empresa asociada a la plantilla", required = true, position = 3)
	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "empresa_id")
	@JsonIgnoreProperties(value = {"perfiles","plantillas","contactos"})
	@ToString.Exclude
	private Empresa empresa;
	
	@JsonIgnore
	private static final long serialVersionUID = 1L;

	public Plantilla(String texto, String asunto, Empresa empresa) {
		this.texto = texto;
		this.asunto = asunto;
		this.empresa = empresa;
	}
}
