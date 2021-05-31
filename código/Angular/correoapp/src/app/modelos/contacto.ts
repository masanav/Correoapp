import { Empresa } from "./empresa";

export class Contacto {
    id!: number;
    nombre!: string;
    apellidos!: string;
    enabled: boolean=true;
    fechaRegistro!: string;
    correo!: string;
    empresa!: Empresa;
}
