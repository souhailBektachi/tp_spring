package com.glsid.digital_banking.repositories;

import com.glsid.digital_banking.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    
    Optional<Role> findByName(String name);
    
    @Query("SELECT r FROM Role r WHERE UPPER(r.name) = UPPER(:name)")
    Optional<Role> findByNameIgnoreCase(@Param("name") String name);
    
    boolean existsByName(String name);
}
