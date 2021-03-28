import { Component, OnInit, ViewChild } from '@angular/core';
import { MatPaginator, PageEvent } from '@angular/material/paginator';
import { Empresa } from 'src/app/modelos/empresa';
import { Perfil } from 'src/app/modelos/perfil';
import { AuthService } from 'src/app/servicios/auth-service.service';
import { EmpresaService } from 'src/app/servicios/empresa.service';
import { PerfilService } from 'src/app/servicios/perfil.service';
import Swal from 'sweetalert2';
import { URL_BACKEND } from 'src/app/config/config';
import { Contacto } from 'src/app/modelos/contacto';
import { ContactoService } from 'src/app/servicios/contacto.service';

@Component({
  selector: 'app-contacto',
  templateUrl: './contacto.component.html'
})
export class ContactoComponent implements OnInit {

  public baseEndpoint: string = URL_BACKEND + '/perfiles'

  public titulo: string = "Colección de contactos";
  public lista!: Contacto[];
  public perfil!: Perfil;
  public empresas!: Empresa[];
  public empresa!: Empresa;

  totalRegistros = 0;
  paginaActual = 0;
  totalPorPagina = 7;
  pageSizeOptions: number[] = [10, 25, 50, 100];
  columnsToDisplay = ['Indice', 'Estado', 'Nombre', 'Apellidos', 'Correo', 'Empresa', 'Editar', 'Borrar'];

  @ViewChild(MatPaginator)
  paginator!: MatPaginator;

  constructor(private perfilServicio: PerfilService, public authService: AuthService, private empresaServicio: EmpresaService,
    private contactoServicio: ContactoService) { }

  ngOnInit() {
    //this.utileriaService.controlPermisoAutenticado();
    this.getPerfilYEmpresaYContactos(this.authService.usuario?.id);
  }

  paginar(event: PageEvent): void {
    this.paginaActual = event.pageIndex;
    this.totalPorPagina = event.pageSize;
    this.calcularRangos();
  }

  private calcularRangos() {
    if (this.authService.usuario?.rol.nombre=="ROLE_ADMINISTRADOR") {
      this.contactoServicio.listarPaginas(this.paginaActual.toString(), this.totalPorPagina.toString())
        .subscribe(p => {
          this.lista = p.content as Contacto[];
          this.totalRegistros = p.totalElements as number;
          this.paginator._intl.itemsPerPageLabel = 'Registros por página:';
        },
          (err => console.log(err))
        );
    } else if (this.authService.usuario?.rol.nombre=="ROLE_SOPORTE"  || this.authService.usuario?.rol.nombre=="ROLE_USUARIO") {
      this.empresaServicio.ver(this.empresa.id)
        .subscribe(p => {
          this.lista = p.contactos;
          this.paginator._intl.itemsPerPageLabel = 'Registros por página:';
        }
          , (err => console.log(err))
          , () => {

            this.totalRegistros = this.lista.length;
          }
        );
    }
  }

  public eliminar(contacto: Contacto): void {
    Swal.fire({
      title: 'Confirme:',
      text: `¿Seguro que desea eliminar a ${contacto.nombre} ?`,
      icon: 'warning',
      showCancelButton: true,
      confirmButtonColor: '#3085d6',
      cancelButtonColor: '#d33',
      confirmButtonText: 'Si, eliminar'
    }).then((result) => {
      if (result.isConfirmed) {
        this.contactoServicio.eliminar(contacto.id).subscribe(() => {
          this.calcularRangos();
          Swal.fire('Eliminado:', `${contacto.nombre}`, 'success');
        });
      }
    });
  }

  getPerfilYEmpresaYContactos(id: number): void {
    this.perfilServicio.ver(this.authService.usuario.id)
      .subscribe((val: Perfil) => {
        this.perfil = val;
      }, (err) => {
        console.log(err)
      }, () => {
        console.log('perfil completo!');
        this.empresaServicio.ver(this.perfil.empresa.id)
          .subscribe(
            val => this.empresa = val,
            (err) => console.log(err),
            () => {
              console.log('empresa completo!');
              this.calcularRangos();
            }
          );
      });
  }

}