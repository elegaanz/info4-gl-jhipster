import { ICommande } from 'app/shared/model/commande.model';

export interface IClient {
  id?: number;
  nom?: string;
  adresse?: string | null;
  email?: string | null;
  telephone?: string | null;
  commandes?: ICommande[] | null;
}

export const defaultValue: Readonly<IClient> = {};
