import { Rol } from "./rol";

export class Usuario {
    id!: number;
    username!: string;
    password!: string;
    enabled!: boolean;
    fechaRegistro!: string;
    rol: Rol=new Rol();
    intentos!: number;
}
