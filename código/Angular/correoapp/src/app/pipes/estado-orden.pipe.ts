import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'estadoOrden'
})
export class EstadoOrdenPipe implements PipeTransform {

  transform(value: boolean): string {
    if (value==true){
      return 'Procesada';
    }
    return 'En espera';
  }

}
