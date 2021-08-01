import { Evenement } from "./evenement";

export interface EmploiTemps{
    id: number,
	nom: string,
	codeAcces: string,
	publique: boolean,
	evenements: Array<Evenement>
}