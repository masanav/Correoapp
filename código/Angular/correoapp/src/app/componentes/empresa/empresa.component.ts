import { Component, OnInit, ViewChild } from '@angular/core';
import { MatPaginator, PageEvent } from '@angular/material/paginator';
import { URL_BACKEND } from 'src/app/config/config';
import { Empresa } from 'src/app/modelos/empresa';
import { Perfil } from 'src/app/modelos/perfil';
import { AuthService } from 'src/app/servicios/auth-service.service';
import { EmpresaService } from 'src/app/servicios/empresa.service';
import { PerfilService } from 'src/app/servicios/perfil.service';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-empresa',
  templateUrl: './empresa.component.html'
})
export class EmpresaComponent implements OnInit {

  public baseEndpoint: string = URL_BACKEND + '/empresas'

  public titulo: string = "Colección de empresas";
  public perfil!: Perfil;
  public empresas!: Empresa[];
  public empresa!: Empresa;

  totalRegistros = 0;
  paginaActual = 0;
  totalPorPagina = 7;
  pageSizeOptions: number[] = [10, 25, 50, 100];
  columnsToDisplay = ['Indice', 'Nombre', 'Dirección', 'Correo', 'Editar', 'Borrar'];

  @ViewChild(MatPaginator)
  paginator!: MatPaginator;

  constructor(private perfilServicio: PerfilService, public authService: AuthService, private empresaServicio: EmpresaService) { }

  ngOnInit() {
    //this.utileriaService.controlPermisoAdministrador();
    this.getPerfilYEmpresaYEmpresas(this.authService.usuario.id);
  }

  paginar(event: PageEvent): void {
    this.paginaActual = event.pageIndex;
    this.totalPorPagina = event.pageSize;
    this.calcularRangos();
  }

  private calcularRangos() {
    if (this.authService.usuario?.id == 1) {
      this.empresaServicio.listarPaginas(this.paginaActual.toString(), this.totalPorPagina.toString())
        .subscribe(p => {
          this.empresas = p.content as Empresa[];
          this.totalRegistros = p.totalElements as number;
          this.paginator._intl.itemsPerPageLabel = 'Registros por página:';
        },
          (err => console.log(err))
        );
    }
  }

  public eliminar(empresa: Empresa): void {
    Swal.fire({
      title: 'Confirme:',
      text: `¿Seguro que desea eliminar a ${empresa.nombre} ?`,
      icon: 'warning',
      showCancelButton: true,
      confirmButtonColor: '#3085d6',
      cancelButtonColor: '#d33',
      confirmButtonText: 'Si, eliminar'
    }).then((result) => {
      if (result.isConfirmed) {
        this.empresaServicio.eliminar(empresa.id).subscribe(() => {
          this.calcularRangos();
          Swal.fire('Eliminado:', `${empresa.nombre}`, 'success');
        });
      }
    });
  }

  getPerfilYEmpresaYEmpresas(id: number): void {
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