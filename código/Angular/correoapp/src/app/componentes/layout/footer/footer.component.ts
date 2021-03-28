import { Component, OnInit } from '@angular/core';
import { Proveedor } from 'src/app/modelos/proveedor';
import { ProveedorService } from 'src/app/servicios/proveedor.service';

@Component({
  selector: 'app-footer',
  templateUrl: './footer.component.html'
})
export class FooterComponent implements OnInit {

  proveedor: Proveedor = new Proveedor();

  constructor(private proveedorService: ProveedorService) { }

  ngOnInit(): void {
    this.proveedorService.verPrimero().subscribe(val=>this.proveedor=val);
  }

}
