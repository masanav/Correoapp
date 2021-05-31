import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'rolnombre'
})
export class RolNombrePipe implements PipeTransform {

  transform(value: number): string {
    if (value==1){
      return 'ADMINISTRADOR';
    }else if (value==2){
      return 'SOPORTE';
    }
    return 'Usuario';
  }

}
