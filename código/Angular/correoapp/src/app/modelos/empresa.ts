import { Contacto } from "./contacto";
import { Orden } from "./orden";
import { Perfil } from "./perfil";
import { Plantilla } from "./plantilla";

export class Empresa {
    id!: number;
    nombre!: string;
    direccion!: string;
    fechaRegistro!: string;
    perfiles: Perfil[]=[];
    ordenes: Orden[]=[];
    contactos: Contacto[]=[];
    plantillas: Plantilla[]=[];
    correo!: string;
    firmaCorreo!: string;
}
