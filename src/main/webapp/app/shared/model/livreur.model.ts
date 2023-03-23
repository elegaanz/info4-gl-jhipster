import { ICommande } from 'app/shared/model/commande.model';

export interface ILivreur {
  id?: number;
  nom?: string;
  commandes?: ICommande[] | null;
}

export const defaultValue: Readonly<ILivreur> = {};
