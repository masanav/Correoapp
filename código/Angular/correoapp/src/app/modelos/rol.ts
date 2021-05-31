import { Usuario } from "./usuario";

export class Rol {
    id!: number;
    nombre!: string;
    usuarios: Usuario[]=[];
}
