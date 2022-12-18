package com.osh4.accounting.persistance.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.osh4.accounting.persistance.entity.user.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long>
{
	User findByLogin(final String login);

}
