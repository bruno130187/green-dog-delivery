package com.bruno.casadocodigo.greendogdelivery.entity;

import lombok.*;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.domain.Sort;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Item {

    @Transient
    public static final Sort SORT_BY_ITEM_NOME_AT_ASC = Sort.by(Sort.Direction.ASC, "nome");

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "O nome não pode ser vazio!")
    @Length(min = 2, max = 30, message = "O Tamanho do nome deve ser entre {min} e {max} caracteres!")
    private String nome;

    @NotNull(message = "O preço não pode ser vazio!")
    @Min(value = 20, message = "O Tamanho do nome deve ser maior ou igual a {value} reais!")
    private BigDecimal preco;

    @NotNull(message = "O status não pode ser vazio!")
    private boolean status;

    @Override
    public String toString() {
        return "Item [id=" + id + ", nome=" + nome + ", preco=" + preco + ", status=" + status + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ( (id == null) ? 0 : id.hashCode());
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
        Item other = (Item) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

}
