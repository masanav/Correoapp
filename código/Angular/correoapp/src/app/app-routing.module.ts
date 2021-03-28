import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { SalirComponent } from './componentes/salir/salir.component';
import { ContactoComponent } from './componentes/contacto/contacto.component';
import { EmpresaComponent } from './componentes/empresa/empresa.component';
import { InicioComponent } from './componentes/inicio/inicio.component';
import { LoginComponent } from './componentes/login/login.component';
import { OrdenComponent } from './componentes/orden/orden.component';
import { PerfilFormComponent } from './componentes/perfil/perfil-form.component';
import { PerfilComponent } from './componentes/perfil/perfil.component';
import { PlantillaFormComponent } from './componentes/plantilla/plantilla-form.component';
import { PlantillaComponent } from './componentes/plantilla/plantilla.component';
import { ProveedorComponent } from './componentes/proveedor/proveedor.component';
import { ContactoFormComponent } from './componentes/contacto/contacto-form.component';
import { EmpresaFormComponent } from './componentes/empresa/empresa-form.component';
import { OrdenVerComponent } from './componentes/orden/orden-ver.component';
import { EnviarComponent } from './componentes/orden/enviar/enviar.component';
import { RolGuard } from './guards/rol.guard';
import { AuthGuard } from './guards/auth.guard';

const routes: Routes = [
  { path: '', pathMatch: 'full', redirectTo: 'inicio' },
  { path: 'inicio', component: InicioComponent },
  { path: 'login', component: LoginComponent },
  { path: 'salir', component: SalirComponent, canActivate: [AuthGuard] },
  { path: 'contactos', component: ContactoComponent, canActivate: [AuthGuard] },
  { path: 'contactos/form', component: ContactoFormComponent, canActivate: [AuthGuard] },
  { path: 'contactos/form/:id', component: ContactoFormComponent, canActivate: [AuthGuard] },
  { path: 'empresas', component: EmpresaComponent, canActivate: [AuthGuard, RolGuard], data: { rol: ['ROLE_ADMINISTRADOR'] }},
  { path: 'empresas/form', component: EmpresaFormComponent, canActivate: [AuthGuard, RolGuard], data: { rol: ['ROLE_ADMINISTRADOR'] } },
  { path: 'empresas/form/:id', component: EmpresaFormComponent, canActivate: [AuthGuard, RolGuard], data: { rol: ['ROLE_ADMINISTRADOR'] } },
  { path: 'ordenes', component: OrdenComponent, canActivate: [AuthGuard] },
  { path: 'ordenes/enviar/:id', component: EnviarComponent, canActivate: [AuthGuard] },
  { path: 'ordenes/ver/:id', component: OrdenVerComponent, canActivate: [AuthGuard] },
  { path: 'perfiles', component: PerfilComponent, canActivate: [AuthGuard, RolGuard], data: { rol: ['ROLE_ADMINISTRADOR', 'ROLE_SOPORTE'] }},
  { path: 'perfiles/form', component: PerfilFormComponent, canActivate: [AuthGuard, RolGuard], data: { rol: ['ROLE_ADMINISTRADOR', 'ROLE_SOPORTE'] } },
  { path: 'perfiles/form/:id', component: PerfilFormComponent, canActivate: [AuthGuard, RolGuard], data: { rol: ['ROLE_ADMINISTRADOR', 'ROLE_SOPORTE'] } },
  { path: 'plantillas', component: PlantillaComponent, canActivate: [AuthGuard] },
  { path: 'plantillas/form', component: PlantillaFormComponent, canActivate: [AuthGuard] },
  { path: 'plantillas/form/:id', component: PlantillaFormComponent, canActivate: [AuthGuard] },
  { path: 'proveedores', component: ProveedorComponent, canActivate: [AuthGuard, RolGuard], data: { rol: ['ROLE_ADMINISTRADOR'] } }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
