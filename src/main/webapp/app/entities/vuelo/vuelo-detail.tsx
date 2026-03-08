import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './vuelo.reducer';

export const VueloDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const vueloEntity = useAppSelector(state => state.vuelo.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="vueloDetailsHeading">
          <Translate contentKey="aerolineaVirtualApp.vuelo.detail.title">Vuelo</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{vueloEntity.id}</dd>
          <dt>
            <span id="numeroVuelo">
              <Translate contentKey="aerolineaVirtualApp.vuelo.numeroVuelo">Numero Vuelo</Translate>
            </span>
          </dt>
          <dd>{vueloEntity.numeroVuelo}</dd>
          <dt>
            <span id="origen">
              <Translate contentKey="aerolineaVirtualApp.vuelo.origen">Origen</Translate>
            </span>
          </dt>
          <dd>{vueloEntity.origen}</dd>
          <dt>
            <span id="destino">
              <Translate contentKey="aerolineaVirtualApp.vuelo.destino">Destino</Translate>
            </span>
          </dt>
          <dd>{vueloEntity.destino}</dd>
          <dt>
            <span id="fechaSalida">
              <Translate contentKey="aerolineaVirtualApp.vuelo.fechaSalida">Fecha Salida</Translate>
            </span>
          </dt>
          <dd>{vueloEntity.fechaSalida ? <TextFormat value={vueloEntity.fechaSalida} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="fechaLlegada">
              <Translate contentKey="aerolineaVirtualApp.vuelo.fechaLlegada">Fecha Llegada</Translate>
            </span>
          </dt>
          <dd>{vueloEntity.fechaLlegada ? <TextFormat value={vueloEntity.fechaLlegada} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
        </dl>
        <Button tag={Link} to="/vuelo" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/vuelo/${vueloEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default VueloDetail;
