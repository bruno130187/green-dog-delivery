package com.bruno.casadocodigo.greendogdelivery.repository;

import com.bruno.casadocodigo.greendogdelivery.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

@Repository
@RepositoryRestResource(collectionResourceRel="permissions", path="permissions")
public interface PermissionRepository extends JpaRepository<Permission, Long> {



}
