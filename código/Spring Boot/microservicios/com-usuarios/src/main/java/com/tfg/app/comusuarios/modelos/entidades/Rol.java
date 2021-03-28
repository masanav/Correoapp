package com.tfg.app.comusuarios.modelos.entidades;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
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
@ApiModel(description = "Clase del objeto Rol")
@Entity
@Table(name = "roles")
public class Rol implements Serializable {

	@ApiModelProperty(notes = "Identificador del rol", required = false, example = "1L", position = 0)
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ApiModelProperty(notes = "Nombre del rol", required = true, example = "ROLE_MECANICO", position = 1)
	@NotBlank
	@Size(min = 5, max = 30)
	@Column(unique = true, length = 30)
	private String nombre;

	@ApiModelProperty(notes = "Lista de Usuarios con determinado rol", required = true, position = 3)
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "rol")
	@JsonIgnoreProperties(value = { "rol", "hibernateLazyInitializer" }, allowSetters = true)
	@ToString.Exclude
	private List<Usuario> usuarios;

	private static final long serialVersionUID = 1L;

}