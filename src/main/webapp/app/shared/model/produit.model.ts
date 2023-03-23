import { IBoutique } from 'app/shared/model/boutique.model';
import { ICommande } from 'app/shared/model/commande.model';

export interface IProduit {
  id?: number;
  nom?: string;
  prix?: number;
  boutique?: IBoutique | null;
  commandes?: ICommande[] | null;
}

export const defaultValue: Readonly<IProduit> = {};
