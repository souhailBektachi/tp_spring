package com.glsid.digital_banking.repositories;

import com.glsid.digital_banking.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}
