import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { Empresa } from 'src/app/modelos/empresa';
import { Perfil } from 'src/app/modelos/perfil';
import { AuthService } from 'src/app/servicios/auth-service.service';
import { EmpresaService } from 'src/app/servicios/empresa.service';
import { PerfilService } from 'src/app/servicios/perfil.service';
import { UsuarioService } from 'src/app/servicios/usuario.service';
import { URL_BACKEND } from './../../config/config';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-perfil-form',
  templateUrl: './perfil-form.component.html'
})
export class PerfilFormComponent implements OnInit {

  public titulo = 'Creador de perfiles';
  public perfil_nuevo: Perfil = new Perfil();
  public perfil: Perfil = new Perfil();

  public empresas: Empresa[] = [];
  public roles: number[] = [1, 2, 3];

  private fotoSeleccionada!: File;
  public imgeblob!: any;

  public baseEndpoint: string = URL_BACKEND + 'perfiles/';
  public error: any;
  private comFinales: Boolean = false;

  constructor(public authService: AuthService, private perfilServicio: PerfilService, private empresaServicio: EmpresaService,
    private usuarioServicio: UsuarioService, private router: Router, private route: ActivatedRoute) { }

  ngOnInit() {
    let id = this.route.snapshot.params.id;
    if (id) { //es actualizacion
      this.perfilServicio.ver(id).subscribe(
        res => {
          console.log(res);
          this.perfil_nuevo = res;
          this.perfil_nuevo.password = null as unknown as string;
        }
        , (err) => console.log(err)
        , () => {
          this.usuarioServicio.ver(id)
            .subscribe(
              us => {
                this.perfil_nuevo.username = us.username;
                this.perfil_nuevo.intentos = us.intentos;
                this.perfil_nuevo.enabled = us.enabled;
              }
              , () => console.log('Error al rescatar el username')
              , () => {
                console.log("Username rescatado");
                this.ajusteRoles();
              }
            )
        }
      );
    }
    else if (this.authService.usuario.rol.nombre=="ROLE_SOPORTE") { //es creacion. Solo el tipo 2 de rol necesita este ajuste, el tipo 1 puede ver las empresas y reaccionar al evento
      this.roles = [2, 3];
    }
    this.setPerfil(this.authService.usuario.id);
  }

  private ajusteRoles() {
    let id = this.route.snapshot.params.id;
    if (id) {
      if (this.authService.usuario?.rol.nombre=="ROLE_ADMINISTRADOR" ) {
        if (this.perfil_nuevo.empresa.id == this.perfil.empresa.id) {
          this.roles = [1, 2, 3]
        } else {
          this.roles = [2, 3]
        }
      } else if (this.authService.usuario?.rol.nombre=="ROLE_SOPORTE" ) {
        this.roles = [2, 3]
      }
    } else {
      if (this.authService.usuario?.rol.nombre=="ROLE_ADMINISTRADOR" ) {
        this.roles = []
      } else if (this.authService.usuario?.rol.nombre=="ROLE_SOPORTE" ) {
        this.roles = [1, 2, 3]
      }
    }
  }

  public seleccionarFoto(event: any): void {
    this.fotoSeleccionada = event.target.files[0];

    if (this.fotoSeleccionada.type.indexOf('image') < 0 && this.fotoSeleccionada.size > 204800) {
      this.fotoSeleccionada = new Object() as File;
      Swal.fire(
        'Error al seleccionar la foto:',
        'El archivo debe ser del tipo imagen y debe pesar menos de 200KB',
        'error');
    }
  }

  public crear(): void {
    if (!this.comFinales) {
      this.comFinales = this.comprobacionesFormulario();
      if (!this.comFinales) {
        return;
      }
    }

    if (!this.fotoSeleccionada) {
      this.perfilServicio.guardar(this.perfil_nuevo).subscribe(perfil => {
        console.log(perfil)
        Swal.fire('Perfil creado', `${this.perfil_nuevo.nombre}`, 'success');
        this.router.navigate(['/perfiles']);
      },
        err => {
          if (err.status === 400) {
            this.error = err.error;
          }else{
            console.log(this.error);
          }
        }
      );
    } else {
      this.perfilServicio.guardarConFoto(this.perfil_nuevo, this.fotoSeleccionada)
        .subscribe(() => {
          Swal.fire('Perfil creado', `${this.perfil_nuevo.nombre}`, 'success');
          this.router.navigate(['/perfiles']);
        }, err => {
          if (err.status === 400) {
            this.error = err.error;
            console.log(this.error);
          }
        });
    }
  }

  public comprobacionesFormulario(): boolean {
    if (!this.perfil_nuevo.username) {
      Swal.fire('Username', 'Escriba un username', 'warning');
      return false;
    }

    if (!this.perfil_nuevo.password) {
      Swal.fire('Contraseña', 'Escriba una contraseña', 'warning');
      return false;
    }

    if (!this.perfil_nuevo.nombre) {
      Swal.fire('Nombre', 'Escriba una nombre', 'warning');
      return false;
    }

    if (!this.perfil_nuevo.apellidos) {
      Swal.fire('Apellidos', 'Escriba una apellidos', 'warning');
      return false;
    }

    if (!this.perfil_nuevo.rol) {
      Swal.fire('Rol', 'Seleccione un rol', 'warning');
      return false;
    }

    if (this.authService.hasRole("ROLE_ADMINISTRADOR") && !this.perfil_nuevo.empresa?.id) {
      Swal.fire('Empresa', 'Seleccione una empresa', 'warning');
      return false;
    }else if (this.authService.hasRole("ROLE_SOPORTE")){
      this.perfil_nuevo.empresa = this.perfil.empresa;
    }

    return true;
  }

  public editar(): void {
    if (!this.comFinales) {
      this.comFinales = this.comprobacionesFormulario();
      if (!this.comFinales) {
        return;
      }
    }

    if (!this.fotoSeleccionada) {
      this.perfilServicio.guardar(this.perfil_nuevo).subscribe(perfil => {
        Swal.fire('Perfil actualizado', `${this.perfil_nuevo.nombre}`, 'success');
        this.router.navigate(['/perfiles']);
      },
        err => {
          if (err.status === 400) {
            this.error = err.error;
            console.log(this.error);
          }
        }
      );
    } else {

      this.perfilServicio.guardarConFoto(this.perfil_nuevo, this.fotoSeleccionada)
        .subscribe(perfil => {
          Swal.fire('Perfil actualizado', `${this.perfil_nuevo.nombre}`, 'success');
          this.router.navigate(['/perfiles']);
        }, err => {
          if (err.status === 400) {
            this.error = err.error;
            console.log(this.error);
          };
          console.log(err);
        });
    }
  }

  public onEmpresaSeleccionada(event: any) {
    const value = event.target.value as string;
    let idempresa: string = value.split(':')[0] as string
    if (Number(idempresa) == 1) {
      this.roles = [1, 2, 3];
    } else {
      this.roles = [2, 3];
    }
  }

  compararEmpresas(o1: Empresa, o2: Empresa): boolean {
    if (o1 === undefined && o2 === undefined) {
      return true;
    }

    return (o1 === null || o2 === null || o1 === undefined || o2 === undefined) ? false : o1.id === o2.id;
  }

  setPerfil(id: number): void {
    this.perfilServicio.ver(this.authService.usuario.id)
      .subscribe((val: Perfil) => {
        this.perfil = val;
      }, (err) => {
        console.log(err)
      }, () => {
        if (this.authService.usuario?.rol.nombre=="ROLE_ADMINISTRADOR" ) {
          this.empresaServicio.listar().subscribe((event: Empresa[]) => { this.empresas = event }, (error) => console.log(error), () => console.log('Empresas cargadas!'))
        }
      });
  }

}

