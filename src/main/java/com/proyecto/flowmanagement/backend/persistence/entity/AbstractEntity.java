package com.proyecto.flowmanagement.backend.persistence.entity;

import javax.persistence.*;

@MappedSuperclass
public abstract class AbstractEntity {
//Table NO

    //auto
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Basic(fetch = FetchType.EAGER)
    private Long id;

    public Long getId() {
        return id;
    }

    public Long setId(Long id) {
        return this.id = id;
    }

    public boolean isPersisted() {
        return id != null;
    }

    @Override
    public int hashCode() {
        if (getId() != null) {
            return getId().hashCode();
        }
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        AbstractEntity other = (AbstractEntity) obj;
        if (getId() == null || other.getId() == null) {
            return false;
        }
        return getId().equals(other.getId());
    }
}