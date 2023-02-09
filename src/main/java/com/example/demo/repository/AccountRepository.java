package com.example.demo.repository;

import com.example.demo.model.Account;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface AccountRepository extends PagingAndSortingRepository<Account, Integer> {

    Optional<Account> findByEmail(String email);

    Page<Account> findByFirstNameContainingAndLastNameContainingAndEmailContaining(String firstName, String lastName, String email, Pageable pageable);

}
