import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './pasajero.reducer';

export const PasajeroDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const pasajeroEntity = useAppSelector(state => state.pasajero.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="pasajeroDetailsHeading">
          <Translate contentKey="aerolineaVirtualApp.pasajero.detail.title">Pasajero</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{pasajeroEntity.id}</dd>
          <dt>
            <span id="nombre">
              <Translate contentKey="aerolineaVirtualApp.pasajero.nombre">Nombre</Translate>
            </span>
          </dt>
          <dd>{pasajeroEntity.nombre}</dd>
          <dt>
            <span id="apellido">
              <Translate contentKey="aerolineaVirtualApp.pasajero.apellido">Apellido</Translate>
            </span>
          </dt>
          <dd>{pasajeroEntity.apellido}</dd>
          <dt>
            <span id="email">
              <Translate contentKey="aerolineaVirtualApp.pasajero.email">Email</Translate>
            </span>
          </dt>
          <dd>{pasajeroEntity.email}</dd>
          <dt>
            <span id="telefono">
              <Translate contentKey="aerolineaVirtualApp.pasajero.telefono">Telefono</Translate>
            </span>
          </dt>
          <dd>{pasajeroEntity.telefono}</dd>
          <dt>
            <span id="fechaNacimiento">
              <Translate contentKey="aerolineaVirtualApp.pasajero.fechaNacimiento">Fecha Nacimiento</Translate>
            </span>
          </dt>
          <dd>
            {pasajeroEntity.fechaNacimiento ? (
              <TextFormat value={pasajeroEntity.fechaNacimiento} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
        </dl>
        <Button tag={Link} to="/pasajero" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/pasajero/${pasajeroEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default PasajeroDetail;
