package com.ecommerce.project.Repositries;

import com.ecommerce.project.model.AppRole;
import com.ecommerce.project.model.Rolse;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.Optional;

public interface RolesRepository extends JpaRepository<Rolse,Long> {

    Optional<Rolse> findByRoleName(AppRole appRole);
}
