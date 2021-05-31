import { Empresa } from "./empresa";

export class Plantilla {
    id!: number;
    texto!: string;
    asunto!: string;
    empresa: Empresa=new Empresa();
}
