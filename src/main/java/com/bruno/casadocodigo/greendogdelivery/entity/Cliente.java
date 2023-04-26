package com.bruno.casadocodigo.greendogdelivery.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.domain.Sort;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Cliente {

    @Transient
    public static final Sort SORT_BY_CLIENTE_NOME_AT_ASC = Sort.by(Sort.Direction.ASC, "nome");

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "O nome não pode ser vazio!")
    @Length(min = 2, max = 30, message = "O Tamanho do nome deve ser entre {min} e {max} caracteres!")
    private String nome;

    @NotNull(message = "O endereço não pode ser vazio!")
    @Length(min = 2, max = 300, message = "O Tamanho do endereço deve ser entre {min} e {max} caracteres!")
    private String endereco;

    @OneToMany(mappedBy = "cliente", fetch = FetchType.EAGER)
    @Cascade(CascadeType.ALL)
    @JsonManagedReference
    private List<Pedido> pedidos;

    @NotNull(message = "O status não pode ser vazio!")
    private boolean status;

    @NotNull(message = "O cep não pode ser vazio!")
    @Min(value = 8, message = "O Tamanho do cep deve ser de 8 caracteres!")
    private int cep;

    public void novoPedido(Pedido pedido) {
        if (this.pedidos==null) pedidos = new ArrayList<Pedido>();
        pedidos.add(pedido);
    }

    @Override
    public String toString() {
        return "Cliente [id=" + id + ", nome=" + nome + ", endereco=" + endereco + ", status=" + status + ", cep=" + cep + "]";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cliente cliente = (Cliente) o;
        return status == cliente.status && Objects.equals(id, cliente.id) && Objects.equals(nome, cliente.nome) && Objects.equals(endereco, cliente.endereco) && Objects.equals(pedidos, cliente.pedidos) && Objects.equals(cep, cliente.cep);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nome, endereco, pedidos, status, cep);
    }
}
