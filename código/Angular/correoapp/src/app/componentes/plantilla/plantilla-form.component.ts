import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Empresa } from 'src/app/modelos/empresa';
import { Perfil } from 'src/app/modelos/perfil';
import { Plantilla } from 'src/app/modelos/plantilla';
import { AuthService } from 'src/app/servicios/auth-service.service';
import { EmpresaService } from 'src/app/servicios/empresa.service';
import { PerfilService } from 'src/app/servicios/perfil.service';
import { PlantillaService } from 'src/app/servicios/plantilla.service';
import { UtileriaService } from 'src/app/servicios/utileria.service';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-plantilla-form',
  templateUrl: './plantilla-form.component.html'
})
export class PlantillaFormComponent implements OnInit {

  public titulo = 'Creador de plantillas';
  public plantilla: Plantilla = new Plantilla;
  public perfil: Perfil = new Perfil();
  public empresas: Empresa[] = [];
  public empresa: Empresa = new Empresa();
  public error: any;
  private comFinales: Boolean=false;

  constructor(public authService: AuthService, private plantillaServicio: PlantillaService, private perfilServicio: PerfilService,
    private empresaServicio: EmpresaService, private router: Router, private route: ActivatedRoute, private UtileriaServicio: UtileriaService) { }

  ngOnInit() {
    let id = this.route.snapshot.params.id;
    if (id) {
      this.plantillaServicio.ver(id).subscribe(res => this.plantilla = res, (err) => console.log(err));
    }
    this.setPerfil(this.authService.usuario.id);
  }

  public crear(): void {
    if (!this.comFinales){
      this.comFinales=this.comprobacionesFormulario();
      if (!this.comFinales){
        return;
      }
    }

    console.log(this.perfil.empresa)
    console.log(this.empresa)
    console.log(this.plantilla.empresa)
    if (this.authService.hasRole("ROLE_ADMINISTRADOR") ) {
      console.log("metido1")
      this.plantilla.empresa = this.empresa;
    } else{
      console.log("metido2")
      this.plantilla.empresa = this.perfil.empresa;
    }

    //no funciona adecuadamente el evento que establece roles
    console.log(this.perfil.empresa)
    console.log(this.empresa)
    console.log(this.plantilla.empresa)
    this.plantillaServicio.guardar(this.plantilla).subscribe(plantilla => {
      Swal.fire('Plantilla creada', `${this.plantilla.asunto}`, 'success');
      this.router.navigate(['/plantillas']);
    },
      err => {
        if (err.status === 400) {
          this.error = err.error;
        }
      }
    );
  }

  public comprobacionesFormulario(): boolean{
    if (!this.plantilla.asunto){
      Swal.fire('Asunto', 'Escriba un asunto', 'warning');
      return false;
    }

    if (!this.plantilla.texto){
      Swal.fire('Texto', 'Escriba un texto', 'warning');
      return false;
    }

    if (this.authService.usuario?.rol.nombre=="ROLE_ADMINISTRADOR"  && !this.plantilla.empresa?.id) {
      Swal.fire('Empresa', 'Seleccione una empresa', 'warning');
      return false;
    } 

    return true;
  }

  public editar(): void {
    if (!this.comFinales){
      this.comFinales=this.comprobacionesFormulario();
      if (!this.comFinales){
        return;
      }
    }
    this.plantillaServicio.guardar(this.plantilla).subscribe(() => {
      Swal.fire('Plantilla actualizada', `${this.plantilla.asunto}`, 'success');
      this.router.navigate(['/plantillas']);
    },
      err => {
        if (err.status === 400) {
          this.error = err.error;
        }
      }
    );
  }

  setPerfil(id: number): void {
    this.perfilServicio.ver(this.authService.usuario.id)
      .subscribe((val: Perfil) => {
        this.perfil = val;
      }, (err) => {
        console.log(err)
      }, () => {
        console.log('perfil completo!');
        this.empresaServicio.listar().subscribe((event: Empresa[]) => { this.empresas = event }, (error) => console.log(error), () => console.log('Empresas cargadas!'))
      });
  }
}