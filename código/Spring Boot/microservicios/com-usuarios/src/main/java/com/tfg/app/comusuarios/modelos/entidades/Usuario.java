package com.tfg.app.comusuarios.modelos.entidades;

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
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

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
@ApiModel(description = "Clase del objeto Usuario")
@Entity
@Table(name = "usuarios")
@JsonIgnoreProperties(value = { "handler", "hibernateLazyInitializer", "_links" })
public class Usuario implements Serializable {

	@ApiModelProperty(notes = "Identificador del usuario", required = false, example = "1L", position = 0)
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ApiModelProperty(notes = "Username del usuario", required = true, example = "mialiasenlaaplicacion", position = 1)
	@NotBlank
	@Size(min = 5, max = 50)
	@Column(unique = true, length = 50)
	private String username;

	@ApiModelProperty(notes = "Hash de la contraseña del usuario", required = true, example = "$2a$10$wCP3aoGWE4jkldY5sDZs5OtJoKlB2MfqqudWnh01U8yMmDWsWp/ym", position = 2)
	@NotBlank
	@Size(min = 12, max = 60)
	@Column(length = 20)
	private String password;

	@ApiModelProperty(notes = "Estado del usuario", required = true, example = "false", position = 3)
	@NotNull
	private Boolean enabled;

	@ApiModelProperty(notes = "Fecha de registro del usuario", required = false, example = "2021-01-27", position = 4)
	@Column(name = "fecha_registro")
	@Temporal(TemporalType.DATE)
	private Date fechaRegistro;

	@ApiModelProperty(notes = "Rol del", required = true, example = "1L", position = 5)
	@NotNull
	@JsonIgnoreProperties(value = { "usuarios", "hibernateLazyInitializer" }, allowSetters = true)
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "rol_id")
	@ToString.Exclude
	private Rol rol;

	@ApiModelProperty(notes = "Intentos de autenticación del usuario", required = false, example = "1", position = 6)
	private Integer intentos;

	private static final long serialVersionUID = 1L;

	@PrePersist
	public void prePersist() {
		this.fechaRegistro = new Date();
	}

}