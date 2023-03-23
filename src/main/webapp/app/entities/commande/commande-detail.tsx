import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './commande.reducer';

export const CommandeDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const commandeEntity = useAppSelector(state => state.commande.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="commandeDetailsHeading">
          <Translate contentKey="dmApp.commande.detail.title">Commande</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{commandeEntity.id}</dd>
          <dt>
            <span id="prixTotal">
              <Translate contentKey="dmApp.commande.prixTotal">Prix Total</Translate>
            </span>
          </dt>
          <dd>{commandeEntity.prixTotal}</dd>
          <dt>
            <span id="date">
              <Translate contentKey="dmApp.commande.date">Date</Translate>
            </span>
          </dt>
          <dd>{commandeEntity.date ? <TextFormat value={commandeEntity.date} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="adresseLivraison">
              <Translate contentKey="dmApp.commande.adresseLivraison">Adresse Livraison</Translate>
            </span>
          </dt>
          <dd>{commandeEntity.adresseLivraison}</dd>
          <dt>
            <Translate contentKey="dmApp.commande.produit">Produit</Translate>
          </dt>
          <dd>
            {commandeEntity.produits
              ? commandeEntity.produits.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.id}</a>
                    {commandeEntity.produits && i === commandeEntity.produits.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
          <dt>
            <Translate contentKey="dmApp.commande.livreur">Livreur</Translate>
          </dt>
          <dd>{commandeEntity.livreur ? commandeEntity.livreur.id : ''}</dd>
          <dt>
            <Translate contentKey="dmApp.commande.client">Client</Translate>
          </dt>
          <dd>{commandeEntity.client ? commandeEntity.client.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/commande" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/commande/${commandeEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default CommandeDetail;
