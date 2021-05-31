import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Proveedor } from 'src/app/modelos/proveedor';
import { ProveedorService } from 'src/app/servicios/proveedor.service';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-proveedor',
  templateUrl: './proveedor.component.html'
})
export class ProveedorComponent implements OnInit {

  titulo: string = 'Detalles del proovedor';
  proveedor: Proveedor = new Proveedor();

  public error: any;

  constructor(private proveedorService: ProveedorService, private router: Router) { }

  ngOnInit() {
    //this.utileriaService.controlPermisoAdministrador();
    this.proveedorService.verPrimero().subscribe(val => { this.proveedor = val }, (error: string) => { 'Se ha producido un error al descargar los datos' });
    //console.log(this.proveedor);
  }

  modificar(): void {
    //this.utileriaService.controlPermisoAdministrador();

    this.proveedorService.guardar(this.proveedor).subscribe(response => {
      this.router.navigate(['/inicio']);
      Swal.fire('Guardado', `Proveedor ${this.proveedor.nombre} guardado con éxito`, 'success');
    }, err => {
      if (err.status == 400) {
        this.error = err.error;
      }
      console.log(err)
    }, () => {
      console.log('Proveedor modificado');
    }
    );
  }

}
