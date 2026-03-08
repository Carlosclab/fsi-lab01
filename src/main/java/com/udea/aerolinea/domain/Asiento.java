package com.udea.aerolinea.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Asiento.
 */
@Entity
@Table(name = "asiento")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Asiento implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "numero", nullable = false)
    private String numero;

    @NotNull
    @Column(name = "clase", nullable = false)
    private String clase;

    @NotNull
    @Column(name = "disponible", nullable = false)
    private Boolean disponible;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "asiento")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "vuelo", "asiento", "pasajero" }, allowSetters = true)
    private Set<Reserva> reservas = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Asiento id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumero() {
        return this.numero;
    }

    public Asiento numero(String numero) {
        this.setNumero(numero);
        return this;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getClase() {
        return this.clase;
    }

    public Asiento clase(String clase) {
        this.setClase(clase);
        return this;
    }

    public void setClase(String clase) {
        this.clase = clase;
    }

    public Boolean getDisponible() {
        return this.disponible;
    }

    public Asiento disponible(Boolean disponible) {
        this.setDisponible(disponible);
        return this;
    }

    public void setDisponible(Boolean disponible) {
        this.disponible = disponible;
    }

    public Set<Reserva> getReservas() {
        return this.reservas;
    }

    public void setReservas(Set<Reserva> reservas) {
        if (this.reservas != null) {
            this.reservas.forEach(i -> i.setAsiento(null));
        }
        if (reservas != null) {
            reservas.forEach(i -> i.setAsiento(this));
        }
        this.reservas = reservas;
    }

    public Asiento reservas(Set<Reserva> reservas) {
        this.setReservas(reservas);
        return this;
    }

    public Asiento addReserva(Reserva reserva) {
        this.reservas.add(reserva);
        reserva.setAsiento(this);
        return this;
    }

    public Asiento removeReserva(Reserva reserva) {
        this.reservas.remove(reserva);
        reserva.setAsiento(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Asiento)) {
            return false;
        }
        return getId() != null && getId().equals(((Asiento) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Asiento{" +
            "id=" + getId() +
            ", numero='" + getNumero() + "'" +
            ", clase='" + getClase() + "'" +
            ", disponible='" + getDisponible() + "'" +
            "}";
    }
}
