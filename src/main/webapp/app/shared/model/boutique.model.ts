import { IProduit } from 'app/shared/model/produit.model';

export interface IBoutique {
  id?: number;
  nom?: string;
  adresse?: string | null;
  produits?: IProduit[] | null;
}

export const defaultValue: Readonly<IBoutique> = {};
