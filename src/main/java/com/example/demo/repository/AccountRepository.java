package com.example.demo.repository;

import com.example.demo.model.Account;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends PagingAndSortingRepository<Account, Integer> {

    Page<Account> findByEmail(String email, Pageable pageable);

    @Query("select u from Account u where u.email = :email")
    Optional<Account> searchByEmail(@Param("email") String email);

}
