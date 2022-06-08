package me.braun.spacex.dao;

import me.braun.spacex.entity.Account;

import java.util.List;
import java.util.Optional;

public interface IAccountDAO extends DAO<Account, Long>{
    Optional<Account> findByEmail(String email);

    Optional<Account> findByPhone(String phone);
    List<Account> pagination(Long pageStart, Long pageCapacity);
}
