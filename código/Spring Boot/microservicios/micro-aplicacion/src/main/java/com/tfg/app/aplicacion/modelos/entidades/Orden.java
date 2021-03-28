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
@ApiModel(description = "Clase del objeto Orden")
@Entity
@Table(name = "ordenes")
@JsonIgnoreProperties(value = {"handler", "hibernateLazyInitializer", "_links"})
public class Orden  implements Serializable{
	
	@ApiModelProperty(notes = "Identificador de la orden", required = false, example = "1L", position = 1)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

	@ApiModelProperty(notes = "Ordenes asociadas a una plantilla", required = false, position = 2)
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "plantilla_id")
    //@JsonIgnoreProperties(value = {"perfiles","contactos","plantillas"})
    @ToString.Exclude
    private Plantilla plantilla;
	
	@ApiModelProperty(notes = "Estado de envio de la orden", required = false, position = 3)
	private Boolean estado;
    
	@ApiModelProperty(notes = "Fecha de registro de la orden", required = false, position = 4)
    @Column(name = "fecha_registro")
	@Temporal(TemporalType.TIMESTAMP)
	private Date fechaRegistro;
    
    @JsonIgnore
    private static final long serialVersionUID = 1L;
    
    @PrePersist
	public void prePersist() {
		this.fechaRegistro = new Date();
	}
    
}