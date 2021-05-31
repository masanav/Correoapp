import { Empresa } from "./empresa";
import { Plantilla } from "./plantilla";

export class Orden {
    id!: number;
    plantilla!: Plantilla;
    estado!: boolean;
    fechaRegistro!: string;
}
