import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { NavbarComponent } from './navbar/navbar.component';
import { FooterComponent } from './footer/footer.component';
import { AppRoutingModule } from 'src/app/app-routing.module';

@NgModule({
    declarations: [NavbarComponent,FooterComponent],
    exports: [NavbarComponent,FooterComponent],
    imports: [
        CommonModule,
        AppRoutingModule
    ]
})
export class LayoutModule { }