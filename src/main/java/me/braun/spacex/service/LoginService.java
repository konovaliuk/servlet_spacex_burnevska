package me.braun.spacex.service;

import lombok.NoArgsConstructor;
import me.braun.spacex.command.commandImpl.ErrorCommand;
import me.braun.spacex.dao.DAOFactory;
import me.braun.spacex.entity.Account;
import me.braun.spacex.util.PasswordHashing;
import me.braun.spacex.util.exception.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import java.util.Optional;

public class LoginService {
    private final Logger log = LoggerFactory.getLogger(LoginService.class);

    public LoginService(DAOFactory factory) {
    }

    public Account signIn(String email, String password){
        log.info("Try to sign in");
        if (Objects.isNull(email) || Objects.isNull(password))
            throw new ServiceException("Please fill all of the fields of the form!");

        Optional<Account> user = DAOFactory.getAccountDAO().findByEmail(email);
        if (user.isEmpty()){
            throw new ServiceException("Wrong credentials");
        }
        Account account = user.get();
        try {
            if (!PasswordHashing.validatePassword(password, account.getPasswordHash())){
                throw new ServiceException("Wrong credentials");
            }
        } catch (Exception e){
            throw new ServiceException("Something went wrong", e);
        }
        return account;
    }
}
