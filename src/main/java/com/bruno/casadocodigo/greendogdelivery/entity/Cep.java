package com.bruno.casadocodigo.greendogdelivery.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Cep {

    private String cep;
    private String logradouro;
    private String complemento;
    private String bairro;
    private String localidade;
    private String uf;
    private String erro;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cep cep1 = (Cep) o;
        return Objects.equals(cep, cep1.cep) && Objects.equals(logradouro, cep1.logradouro) && Objects.equals(complemento, cep1.complemento) && Objects.equals(bairro, cep1.bairro) && Objects.equals(localidade, cep1.localidade) && Objects.equals(uf, cep1.uf) && Objects.equals(erro, cep1.erro);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cep, logradouro, complemento, bairro, localidade, uf, erro);
    }

    @Override
    public String toString() {
        return "Cep[" +
                "cep='" + cep + '\'' +
                ", logradouro='" + logradouro + '\'' +
                ", complemento='" + complemento + '\'' +
                ", bairro='" + bairro + '\'' +
                ", localidade='" + localidade + '\'' +
                ", uf='" + uf + '\'' +
                ", erro='" + erro + '\'' +
                ']';
    }
}
