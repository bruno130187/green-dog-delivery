package com.bruno.casadocodigo.greendogdelivery.repository;

import com.bruno.casadocodigo.greendogdelivery.entity.Cliente;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    @Query(value = "SELECT c FROM Cliente c WHERE c.status = true")
    List<Cliente> findAllClientesAtivos(Sort sort);

}
