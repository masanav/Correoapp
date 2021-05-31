import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import Swal from 'sweetalert2';
import { Perfil } from '../modelos/perfil';
import { AuthService } from './auth-service.service';
import { PerfilService } from './perfil.service';

@Injectable({
  providedIn: 'root'
})
export class UtileriaService {

  constructor(private authService: AuthService, private router: Router) { }

  controlPermisoAdministrador() {
    if (!this.authService.isAuthenticated() || this.authService.usuario.rol.id != 1) {
      Swal.fire('Autentíquese', 'No tiene permisos suficientes para acceder este recurso', 'error');
      this.authService.logout();
      this.router.navigate(['/login']);
      return;
    }
  }

  controlPermisoAdministradorYSoporte() {
    if (!this.authService.isAuthenticated() || this.authService.usuario.rol.id == 3) {
      Swal.fire('Autentíquese', 'No tiene permisos suficientes para acceder este recurso', 'error');
      this.authService.logout();
      this.router.navigate(['/login']);
      return;
    }
  }

  controlPermisoAutenticado() {
    if (!this.authService.isAuthenticated()) {
      Swal.fire('Autentíquese', 'No tiene permisos suficientes para acceder este recurso. Autentíquese', 'error');
      this.authService.logout();
      this.router.navigate(['/login']);
      return;
    }
  }

  redirectTo(uri: string) {
    this.router.navigateByUrl('/', { skipLocationChange: true }).then(() =>
      this.router.navigate([uri]));
  }
}
