package com.bruno.casadocodigo.greendogdelivery.entity;

import lombok.Data;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

@Data
@Entity
@Table(name = "permission")
public class Permission implements GrantedAuthority, Serializable {

    @Transient
    public static final Sort SORT_BY_PERMISSION_DESCRIPTION_AT_ASC = Sort.by(Sort.Direction.ASC, "description");

    @Serial
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    public String description;

    public Permission(){

    }

    @Override
    public String getAuthority() {
        return this.description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Permission that = (Permission) o;
        return Objects.equals(id, that.id) && Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, description);
    }
}
