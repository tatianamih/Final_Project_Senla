package com.courses.senla.repositories;


import com.courses.senla.enums.ERole;
import com.courses.senla.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Boolean existsByName(ERole name);

    Role findByName(ERole name);
}
