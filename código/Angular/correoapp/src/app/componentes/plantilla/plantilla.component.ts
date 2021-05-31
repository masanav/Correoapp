import { Component, OnInit, ViewChild } from '@angular/core';
import { MatPaginator, PageEvent } from '@angular/material/paginator';
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
  selector: 'app-plantilla',
  templateUrl: './plantilla.component.html'
})
export class PlantillaComponent implements OnInit {

  public perfil!: Perfil;
  public titulo: string = "Colección de plantillas";
  public lista!: Plantilla[];
  public empresa!: Empresa;

  totalRegistros = 0;
  paginaActual = 0;
  totalPorPagina = 4;
  pageSizeOptions: number[] = [3, 5, 10, 25, 100];
  columnsToDisplay = ['Indice', 'Asunto', 'Ordenar', 'Editar', 'Borrar'];

  @ViewChild(MatPaginator)
  paginator!: MatPaginator;

  constructor(private plantillaServicio: PlantillaService, private utileriaService: UtileriaService, private authService: AuthService,
    private perfilServicio: PerfilService, private empresaServicio: EmpresaService) { }

  ngOnInit() {
    //this.utileriaService.controlPermisoAutenticado();
    this.getPerfilYEmpresaYPlantillas(this.authService.usuario.id);
  }

  paginar(event: PageEvent): void {
    this.paginaActual = event.pageIndex;
    this.totalPorPagina = event.pageSize;
    this.calcularRangos();
  }

  private calcularRangos() {
    if (this.authService.usuario?.id == 1) {
      this.plantillaServicio.listarPaginas(this.paginaActual.toString(), this.totalPorPagina.toString())
        .subscribe(p => {
          this.lista = p.content as Plantilla[];
          this.totalRegistros = p.totalElements as number;
          this.paginator._intl.itemsPerPageLabel = 'Registros por página:';
        },
          (err) => console.log(err)
        );
    } else{
      this.empresaServicio.ver(this.empresa.id)
        .subscribe(p => {
          this.lista = p.plantillas;
          this.paginator._intl.itemsPerPageLabel = 'Registros por página:';
        }
          , (err => console.log())
          , () => (this.totalRegistros = this.lista.length)
        );
    }
  }

  public eliminar(plantilla: Plantilla): void {
    Swal.fire({
      title: 'Confirme:',
      text: `¿Seguro que desea eliminar a ${plantilla.asunto} ?`,
      icon: 'warning',
      showCancelButton: true,
      confirmButtonColor: '#3085d6',
      cancelButtonColor: '#d33',
      confirmButtonText: 'Sí, eliminar'
    }).then((result) => {
      if (result.isConfirmed) {
        this.plantillaServicio.eliminar(plantilla.id).subscribe(() => {
          this.calcularRangos();
          Swal.fire('Eliminada:', `${plantilla.asunto}`, 'success');
        });
      }
    });
  }

  getPerfilYEmpresaYPlantillas(id: number): void {
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