package com.glsid.digital_banking.repositories;

import com.glsid.digital_banking.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(String name);
}
