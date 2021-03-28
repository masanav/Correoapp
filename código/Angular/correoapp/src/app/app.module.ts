import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { ProveedorComponent } from './componentes/proveedor/proveedor.component';
import { EmpresaComponent } from './componentes/empresa/empresa.component';
import { OrdenComponent } from './componentes/orden/orden.component';
import { PerfilComponent } from './componentes/perfil/perfil.component';
import { PlantillaComponent } from './componentes/plantilla/plantilla.component';
import { ContactoComponent } from './componentes/contacto/contacto.component';
import { PlantillaFormComponent } from './componentes/plantilla/plantilla-form.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { LoginComponent } from './componentes/login/login.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MatTableModule } from '@angular/material/table';
import { MatPaginatorModule } from '@angular/material/paginator';
import { MatInputModule } from '@angular/material/input';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatTabsModule } from '@angular/material/tabs';
import { LayoutModule } from './componentes/layout/layout.module';
import { PerfilFormComponent } from './componentes/perfil/perfil-form.component';
import { EstadoPipe } from './pipes/estado.pipe';
import { RolNombrePipe} from './pipes/rol-nombre.pipe';
import { InicioComponent } from './componentes/inicio/inicio.component';
import { SalirComponent } from './componentes/salir/salir.component';
import { UrlseguraPipe } from './pipes/urlsegura.pipe';
import { ContactoFormComponent } from './componentes/contacto/contacto-form.component';
import { EmpresaFormComponent } from './componentes/empresa/empresa-form.component';
import { OrdenVerComponent } from './componentes/orden/orden-ver.component';
import { EstadoOrdenPipe } from './pipes/estado-orden.pipe';
import { EnviarComponent } from './componentes/orden/enviar/enviar.component';
import { TokenInterceptor } from './interceptores/token.interceptor';
import { AuthInterceptor } from './interceptores/auth.interceptor';

@NgModule({
  declarations: [
    AppComponent,
    ProveedorComponent,
    EmpresaComponent,
    OrdenComponent,
    PerfilComponent,
    PlantillaComponent,
    ContactoComponent,
    PlantillaFormComponent,
    LoginComponent,
    PerfilFormComponent,
    EstadoPipe,
    RolNombrePipe,
    InicioComponent,
    SalirComponent,
    UrlseguraPipe,
    ContactoFormComponent,
    EmpresaFormComponent,
    OrdenVerComponent,
    EstadoOrdenPipe,
    EnviarComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    FormsModule,
    BrowserAnimationsModule,
    MatTableModule,
    MatPaginatorModule,
    MatInputModule,
    MatCheckboxModule,
    MatButtonModule,
    MatCardModule,
    MatTabsModule,
    ReactiveFormsModule,
    LayoutModule
  ],
  providers: [
    { provide: HTTP_INTERCEPTORS, useClass: TokenInterceptor, multi: true },
    { provide: HTTP_INTERCEPTORS, useClass: AuthInterceptor, multi: true }
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
