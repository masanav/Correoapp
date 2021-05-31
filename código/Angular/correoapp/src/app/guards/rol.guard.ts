import { Injectable } from '@angular/core';
import { CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot, UrlTree, Router } from '@angular/router';
import { SweetAlert2LoaderService } from '@sweetalert2/ngx-sweetalert2';
import { Observable } from 'rxjs';
import Swal from 'sweetalert2';
import { AuthService } from '../servicios/auth-service.service';

@Injectable({
  providedIn: 'root'
})
export class RolGuard implements CanActivate {
  constructor(private authService: AuthService, private router: Router) { }

  canActivate(
    next: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): Observable<boolean> | Promise<boolean> | boolean {

    if (!this.authService.isAuthenticated()) {
      this.router.navigate(['/login']);
      return false;
    }
    let roles = next.data['rol'] as string[];
    for (let j in roles) {
      if (this.authService.hasRole(roles[j])) {
        return true;
      }
    }
    /*
    if (this.authService.hasRole(rol)) {
      return true;
    }*/
    Swal.fire('Acceso denegado', `${this.authService.usuario.username} no tienes acceso a este recurso!`, 'warning');
    this.router.navigate(['/inicio']);
    return false;
  }

}
