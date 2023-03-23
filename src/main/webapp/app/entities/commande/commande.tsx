import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { ICommande } from 'app/shared/model/commande.model';
import { getEntities } from './commande.reducer';

export const Commande = () => {
  const dispatch = useAppDispatch();

  const location = useLocation();
  const navigate = useNavigate();

  const commandeList = useAppSelector(state => state.commande.entities);
  const loading = useAppSelector(state => state.commande.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  return (
    <div>
      <h2 id="commande-heading" data-cy="CommandeHeading">
        <Translate contentKey="dmApp.commande.home.title">Commandes</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="dmApp.commande.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/commande/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="dmApp.commande.home.createLabel">Create new Commande</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {commandeList && commandeList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="dmApp.commande.id">ID</Translate>
                </th>
                <th>
                  <Translate contentKey="dmApp.commande.prixTotal">Prix Total</Translate>
                </th>
                <th>
                  <Translate contentKey="dmApp.commande.date">Date</Translate>
                </th>
                <th>
                  <Translate contentKey="dmApp.commande.adresseLivraison">Adresse Livraison</Translate>
                </th>
                <th>
                  <Translate contentKey="dmApp.commande.produit">Produit</Translate>
                </th>
                <th>
                  <Translate contentKey="dmApp.commande.livreur">Livreur</Translate>
                </th>
                <th>
                  <Translate contentKey="dmApp.commande.client">Client</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {commandeList.map((commande, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/commande/${commande.id}`} color="link" size="sm">
                      {commande.id}
                    </Button>
                  </td>
                  <td>{commande.prixTotal}</td>
                  <td>{commande.date ? <TextFormat type="date" value={commande.date} format={APP_DATE_FORMAT} /> : null}</td>
                  <td>{commande.adresseLivraison}</td>
                  <td>
                    {commande.produits
                      ? commande.produits.map((val, j) => (
                          <span key={j}>
                            <Link to={`/produit/${val.id}`}>{val.id}</Link>
                            {j === commande.produits.length - 1 ? '' : ', '}
                          </span>
                        ))
                      : null}
                  </td>
                  <td>{commande.livreur ? <Link to={`/livreur/${commande.livreur.id}`}>{commande.livreur.id}</Link> : ''}</td>
                  <td>{commande.client ? <Link to={`/client/${commande.client.id}`}>{commande.client.id}</Link> : ''}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/commande/${commande.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`/commande/${commande.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`/commande/${commande.id}/delete`} color="danger" size="sm" data-cy="entityDeleteButton">
                        <FontAwesomeIcon icon="trash" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.delete">Delete</Translate>
                        </span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && (
            <div className="alert alert-warning">
              <Translate contentKey="dmApp.commande.home.notFound">No Commandes found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default Commande;
