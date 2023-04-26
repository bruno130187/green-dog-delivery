package com.bruno.casadocodigo.greendogdelivery.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.Fetch;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Pedido {

    @Transient
    public static final Sort SORT_BY_PEDIDO_ID_AT_DESC = Sort.by(Sort.Direction.DESC, "id");

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "O cliente não pode ser vazio!")
    @ManyToOne(optional = true, fetch = FetchType.LAZY)
    @JsonBackReference
    private Cliente cliente;

    @ManyToMany(fetch = FetchType.LAZY)
    @Cascade(CascadeType.MERGE)
    private List<Item> itens;

    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private Date data;

    @Min(1)
    private BigDecimal valorTotal;

    private String status;

    @Override
    public String toString() {
        return "Pedido [id=" + id + ", cliente=" + cliente + ", itens=" + itens + ", data=" + data + ", valorTotal="
                + valorTotal + ", status=" + status + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Pedido other = (Pedido) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

}
