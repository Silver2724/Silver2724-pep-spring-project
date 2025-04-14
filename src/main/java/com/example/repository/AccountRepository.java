package com.example.repository;

import java.util.Optional;
import com.example.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Integer>{
    //looks at Account table and finds the username that equals the given username
    //it will return the account that matches the given username
    Optional<Account> findByUsername(String username);
    
    //tells if there's an account in the database with the given username
    boolean existsByUsername(String username);
}
