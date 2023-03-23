import dayjs from 'dayjs';
import { IProduit } from 'app/shared/model/produit.model';
import { ILivreur } from 'app/shared/model/livreur.model';
import { IClient } from 'app/shared/model/client.model';

export interface ICommande {
  id?: number;
  prixTotal?: number;
  date?: string;
  adresseLivraison?: string;
  produits?: IProduit[] | null;
  livreur?: ILivreur | null;
  client?: IClient | null;
}

export const defaultValue: Readonly<ICommande> = {};
