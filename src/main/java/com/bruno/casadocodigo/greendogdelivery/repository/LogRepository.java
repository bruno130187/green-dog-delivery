package com.bruno.casadocodigo.greendogdelivery.repository;

import com.bruno.casadocodigo.greendogdelivery.entity.Log;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LogRepository extends JpaRepository<Log, Long> {



}
