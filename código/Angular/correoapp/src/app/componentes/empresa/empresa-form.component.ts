import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { Empresa } from 'src/app/modelos/empresa';
import { Perfil } from 'src/app/modelos/perfil';
import { AuthService } from 'src/app/servicios/auth-service.service';
import { EmpresaService } from 'src/app/servicios/empresa.service';
import { PerfilService } from 'src/app/servicios/perfil.service';
import { UtileriaService } from 'src/app/servicios/utileria.service';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-empresa-form',
  templateUrl: './empresa-form.component.html'
})
export class EmpresaFormComponent implements OnInit {

  public titulo = 'Creador de empresas';

  private comFinales: Boolean=false;
  public error: any;

  public perfil: Perfil = new Perfil();
  public empresa: Empresa = new Empresa();

  constructor(private perfilServicio: PerfilService, private utileriaServicio: UtileriaService, private authService: AuthService, private empresaServicio: EmpresaService,
    private router: Router, private route: ActivatedRoute) { }

  ngOnInit() {
    let id = this.route.snapshot.params.id;
    if (id) {
      this.empresaServicio.ver(id).subscribe(
        res => {
          this.empresa = res;
        }
        , (err) => console.log(err),
        ()=>{
          console.log("Empresa cargada")
        }
      );
    }
    this.controlPermisos();
  }

  public crear(): void {
    if (!this.comFinales){
      this.comFinales=this.comprobacionesFormulario();
      if (!this.comFinales){
        return;
      }
    }

    this.empresaServicio.guardar(this.empresa).subscribe(() => {
      Swal.fire('Empresa creada', `${this.empresa.nombre}`, 'success');
      this.router.navigate(['/empresas']);
    },
      err => {
        if (err.status === 400) {
          this.error = err.error;
        }
        console.log(err);
      }
    );
  }

  public comprobacionesFormulario(): boolean{
    if (!this.empresa.nombre){
      Swal.fire('Nombre', 'Escriba el nombre', 'warning');
      return false;
    }

    if (!this.empresa.direccion){
      Swal.fire('Dirección', 'Escriba la dirección', 'warning');
      return false;
    }

    if (!this.empresa.correo){
      Swal.fire('Correo', 'Escriba un correo', 'warning');
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
    this.empresaServicio.guardar(this.empresa).subscribe(() => {
      Swal.fire('Empresa actualizada', `${this.empresa.nombre}`, 'success');
      this.router.navigate(['/empresas']);
    },
      err => {
        if (err.status === 400) {
          this.error = err.error;
        };
        console.log(err);
      }
    );
  }

  setPerfil(id: number): void {
    this.perfilServicio.ver(this.authService.usuario.id)
      .subscribe((val: Perfil) => {
        this.perfil = val;
      }, (err) => {
        console.log(err)
      });
  }

  controlPermisos() {
    this.utileriaServicio.controlPermisoAdministradorYSoporte();
    this.setPerfil(this.authService.usuario.id);
  }
}
