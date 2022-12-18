package com.osh4.accounting.persistance.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.osh4.accounting.persistance.entity.user.Role;

public interface RoleRepository extends JpaRepository<Role, Long>
{
    Role findByName(String name);
}
