package me.braun.spacex.service;

import lombok.experimental.UtilityClass;
import me.braun.spacex.dao.DAOFactory;
import me.braun.spacex.dao.IAccountDAO;
import me.braun.spacex.dao.IRoleDAO;
import me.braun.spacex.entity.Account;
import me.braun.spacex.entity.enams.ERole;
import me.braun.spacex.util.PasswordHashing;
import me.braun.spacex.util.exception.AccountAlreadyExistsException;
import me.braun.spacex.util.exception.AccountNotFoundException;
import me.braun.spacex.util.exception.ServiceException;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;
import java.util.regex.Pattern;

@UtilityClass

public class AccountService {
    private final IAccountDAO accountDAO = DAOFactory.getAccountDAO();
    private final IRoleDAO roleDAO = DAOFactory.getRoleDAO();
    private final Logger log = LoggerFactory.getLogger(AccountService.class);

    private static final String emailRegex = "^(?=.{1,64}@)[A-Za-z0-9\\+_-]+(\\.[A-Za-z0-9\\+_-]+)*@"
            + "[^-][A-Za-z0-9\\+-]+(\\.[A-Za-z0-9\\+-]+)*(\\.[A-Za-z]{2,})$";

    private static void checkEmailValid(String email) throws ServiceException {
        if (!Pattern.compile(emailRegex).matcher(email).matches()) {
            throw new ServiceException("Email address isn't valid. Check it out!");
        }
    }

    @NotNull
    public Account registration(String firstName, String lastName, String email, String phone, String password) {

        checkEmailValid(email);
        PasswordHashing.checkPasswordValid(password);
        accountDAO.findByEmail(email).ifPresent(account -> {
            throw new AccountAlreadyExistsException(email);
        });
        try {
            password = PasswordHashing.createHash(password);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        Optional<Account> userPhone = accountDAO.findByPhone(phone);
        if (userPhone.isPresent())
            throw new ServiceException("User with the same phone already exists");

        return accountDAO.save(Account.builder()
                .firstName(firstName.toUpperCase())
                .lastName(lastName.toUpperCase())
                .email(email.toLowerCase())
                .phone(phone)
                .role(roleDAO.findById(ERole.CUSTOMER.getId()).get())
                .passwordHash(password).build());
    }

    public Account editAccount(Long userId, String firstName, String lastName, String email, String phone) {
        Account account = accountDAO.findById(userId).orElseThrow(AccountNotFoundException::new);
        account.setFirstName(firstName);
        account.setLastName(lastName);
        account.setEmail(email);
        account.setPhone(phone);
        accountDAO.update(account);
        return account;
    }

    public void changePassword(Long userId, String password) {
        Account account = accountDAO.findById(userId).orElseThrow(AccountNotFoundException::new);
        try {
            password = PasswordHashing.createHash(password);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        account.setPasswordHash(password);
        accountDAO.update(account);
    }

    public void deleteAccount(Long id) {
        accountDAO.delete(id);
    }


}
