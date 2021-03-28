import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { Contacto } from 'src/app/modelos/contacto';
import { Empresa } from 'src/app/modelos/empresa';
import { Perfil } from 'src/app/modelos/perfil';
import { AuthService } from 'src/app/servicios/auth-service.service';
import { ContactoService } from 'src/app/servicios/contacto.service';
import { EmpresaService } from 'src/app/servicios/empresa.service';
import { PerfilService } from 'src/app/servicios/perfil.service';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-contacto-form',
  templateUrl: './contacto-form.component.html'
})
export class ContactoFormComponent implements OnInit {

  public titulo = 'Creador de contactos';
  public contacto: Contacto = new Contacto();

  private comFinales: Boolean=false;
  public error: any;

  public perfil: Perfil = new Perfil();
  public empresa: Empresa = new Empresa();
  public empresas: Empresa[] = [];

  constructor(private perfilServicio: PerfilService, public authService: AuthService, private empresaServicio: EmpresaService,
    private contactoServicio: ContactoService, private router: Router, private route: ActivatedRoute) { }

  ngOnInit() {
    let id = this.route.snapshot.params.id;
    if (id) {
      this.contactoServicio.ver(id).subscribe(
        res => {
          this.contacto = res;
        }
        , (err) => console.log(err),
        ()=>{
          console.log("contacto cargado")
          console.log(this.contacto);
        }
      );
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

    if (!this.contacto.enabled){
      this.contacto.enabled=false;
    }

    if (this.authService.usuario?.rol.nombre=="ROLE_SOPORTE" || this.authService.usuario?.rol.nombre=="ROLE_USUARIO") {
      this.contacto.empresa = this.perfil.empresa;
    } else {
      this.contacto.empresa = this.empresa;
    }

    this.contactoServicio.guardar(this.contacto).subscribe(() => {
      Swal.fire('Contacto creado', `${this.contacto.nombre}`, 'success');
      this.router.navigate(['/contactos']);
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
    if (!this.contacto.nombre){
      Swal.fire('Nombre', 'Escriba el nombre', 'warning');
      return false;
    }

    if (!this.contacto.apellidos){
      Swal.fire('Apellidos', 'Escriba los apellidos', 'warning');
      return false;
    }

    if (!this.contacto.correo){
      Swal.fire('Correo', 'Escriba un correo', 'warning');
      return false;
    }

    if (this.authService.usuario?.rol.nombre=="ROLE_ADMINISTRADOR" && !this.empresa?.id) {
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
    if (this.authService.usuario?.rol.nombre=="ROLE_SOPORTE" || this.authService.usuario?.rol.nombre=="ROLE_USUARIO") {
      this.contacto.empresa = this.perfil.empresa;
    } else {
      this.contacto.empresa = this.empresa;
    }
    //let idempresa:number = this.contacto.empresa.id;
    //let e=new Object();
    //e.id=1;
    //this.contacto.empresa=e;
    this.contactoServicio.guardar(this.contacto).subscribe(() => {
      Swal.fire('Contacto actualizado', `${this.contacto.nombre}`, 'success');
      this.router.navigate(['/contactos']);
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
      }, () => {
        this.empresaServicio.listar().subscribe((event: Empresa[]) => { this.empresas = event }, (error) => console.log(error), () => console.log('Empresas cargadas!'))
      });
  }

  compararEmpresas(o1: Empresa, o2: Empresa): boolean {
    if (o1 === undefined && o2 === undefined) {
      return true;
    }

    return (o1 === null || o2 === null || o1 === undefined || o2 === undefined) ? false : o1.id === o2.id;
  }
}
