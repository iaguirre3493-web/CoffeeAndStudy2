package com.Accesos.CoffeeAndStudy.repository;

import com.Accesos.CoffeeAndStudy.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    List<User> findByUsernameContainingIgnoreCase(String username);

    List<User> findByPremium(Boolean premium);

    List<User> findByAge(Integer age);

    List<User> findByUsernameContainingIgnoreCaseAndPremiumAndAge(String username, Boolean premium, Integer age);
}