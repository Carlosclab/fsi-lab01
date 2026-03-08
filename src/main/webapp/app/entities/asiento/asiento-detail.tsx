import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './asiento.reducer';

export const AsientoDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const asientoEntity = useAppSelector(state => state.asiento.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="asientoDetailsHeading">
          <Translate contentKey="aerolineaVirtualApp.asiento.detail.title">Asiento</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{asientoEntity.id}</dd>
          <dt>
            <span id="numero">
              <Translate contentKey="aerolineaVirtualApp.asiento.numero">Numero</Translate>
            </span>
          </dt>
          <dd>{asientoEntity.numero}</dd>
          <dt>
            <span id="clase">
              <Translate contentKey="aerolineaVirtualApp.asiento.clase">Clase</Translate>
            </span>
          </dt>
          <dd>{asientoEntity.clase}</dd>
          <dt>
            <span id="disponible">
              <Translate contentKey="aerolineaVirtualApp.asiento.disponible">Disponible</Translate>
            </span>
          </dt>
          <dd>{asientoEntity.disponible ? 'true' : 'false'}</dd>
        </dl>
        <Button tag={Link} to="/asiento" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/asiento/${asientoEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default AsientoDetail;
