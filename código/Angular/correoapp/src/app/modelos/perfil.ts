import { Empresa } from "./empresa";
import { Rol } from "./rol";

export class Perfil {
    id!: number;
    username!: string;
    password!: string;
    enabled: boolean=false;
    rol!: number;
    intentos!: number;
    nombre!: string;
    apellidos!: string;
    empresa: Empresa = new Empresa();
    fotolobHashCode!: number;
}
