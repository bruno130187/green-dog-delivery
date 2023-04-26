package com.bruno.casadocodigo.greendogdelivery.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.Objects;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Log {

    @Transient
    public static final Sort SORT_BY_LOG_DATA_AT_ASC = Sort.by(Sort.Direction.DESC, "data");

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "O módulo não pode ser vazio!")
    private String modulo;

    @NotNull(message = "A ação não pode ser vazia!")
    private String acao;

    @NotNull(message = "A data não pode ser vazia!")
    @DateTimeFormat(pattern = "dd-MM-yyyy hh:mm:ss")
    private Date data;

    @NotNull(message = "O usuário não pode ser vazio!")
    private String usuario;

    @Override
    public String toString() {
        return "Log[" +
                "id=" + id +
                ", modulo='" + modulo + '\'' +
                ", acao='" + acao + '\'' +
                ", data='" + data + '\'' +
                ", usuario=" + usuario +
                ']';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Log log = (Log) o;
        return Objects.equals(id, log.id) && Objects.equals(modulo, log.modulo) && Objects.equals(acao, log.acao) && Objects.equals(data, log.data) && Objects.equals(usuario, log.usuario);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, modulo, acao, data, usuario);
    }
}
