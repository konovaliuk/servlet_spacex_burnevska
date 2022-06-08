package me.braun.spacex.dao.sql;

import lombok.NoArgsConstructor;
import me.braun.spacex.Main;
import me.braun.spacex.connection.ConnectionPool;
import me.braun.spacex.dao.IAccountDAO;
import me.braun.spacex.entity.Account;
import me.braun.spacex.entity.Role;
import me.braun.spacex.util.exception.AccountNotFoundException;

import me.braun.spacex.util.exception.DatabaseException;
import me.braun.spacex.util.exception.ServiceException;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@NoArgsConstructor
public class SQLAccountDAO implements IAccountDAO {
    private static SQLAccountDAO instance;
    private static final Logger log = LoggerFactory.getLogger(SQLAccountDAO.class);
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_FIRST_NAME = "first_name";
    private static final String COLUMN_LAST_NAME = "last_name";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_PHONE = "phone";
    private static final String COLUMN_PASSWORD_HASH = "password_hash";
    private static final String COLUMN_ROLE = "role_id";

    private @NotNull Account getAccount(@NotNull ResultSet resultSet) throws SQLException {
        long id = resultSet.getLong(COLUMN_ID);
        String firstName = resultSet.getString(COLUMN_FIRST_NAME);
        String lastname = resultSet.getString(COLUMN_LAST_NAME);
        String email = resultSet.getString(COLUMN_EMAIL);
        String phone = resultSet.getString(COLUMN_PHONE);
        String password = resultSet.getString(COLUMN_PASSWORD_HASH);
        byte role = resultSet.getByte(COLUMN_ROLE);
        return new Account(id, firstName, lastname, email, phone, role, password);

    }

    public static IAccountDAO getInstance() {

        if (instance == null) {
            instance = new SQLAccountDAO();
        }
        return instance;
    }

    @Override
    public @NotNull List<Account> findAll() {
        String query = "select * from accounts;";
        List<Account> accountList = new ArrayList<>();
        try {
            ResultSet resultSet = Main.statement.executeQuery(query);
            while (resultSet.next()) {
                Account account = getAccount(resultSet);
                accountList.add(account);

            }
            resultSet.close();
        } catch (SQLException e) {
            log.error("SQL Error!", e);
        }
        log.info(accountList.size() + " users were found");
        return accountList;
    }


    public List<Account> pagination(Long pageStart, Long pageCapacity){
        String query = "select * from accounts order by id limit " + pageCapacity + " offset "+ pageStart +";";
        List<Account> accountList = new ArrayList<>();
        try {
            ResultSet resultSet = Main.statement.executeQuery(query);
            while (resultSet.next()) {
                Account account = getAccount(resultSet);
                accountList.add(account);

            }
            resultSet.close();
        } catch (SQLException e) {
            log.error("SQL Error!", e);
        }
        return accountList;
    }

    @Override
    public Optional<Account> findById(Long id) {
        String query = "select * from accounts where id= '" + id + "';";
        Account account = null;
        try {
            ResultSet resultSet = Main.statement.executeQuery(query);
            while (resultSet.next()) {
                account = getAccount(resultSet);
            }
            resultSet.close();
        } catch (SQLException e) {
            log.error("SQL Error!", e);
        }
        assert account != null;
        log.info("user " + account.getFirstName()
                + " " + account.getLastName() + " was found");
        return Optional.of(account);
    }

    @Override
    public Account save(@NotNull Account account) throws IllegalStateException {
        Account newAccount = null;
        String query = "call insert_user(?, ?, ?, ?, ?, ?)";
        try {
            CallableStatement callableStatement = Main.connection.prepareCall(query);
            callableStatement.setString(1, account.getFirstName());
            callableStatement.setString(2, account.getLastName());
            String email = account.getEmail();
            callableStatement.setString(3, email);
            callableStatement.setString(4, account.getPhone());
            callableStatement.setObject(5, account.getRole());
            callableStatement.setString(6, account.getPasswordHash());
            callableStatement.execute();
            newAccount = findByEmail(email).orElseThrow(AccountNotFoundException::new);

            callableStatement.close();
        } catch (SQLException e) {
            log.error("SQL Error!", e);
        }
        assert newAccount != null;
        log.info("created account\nid: " + newAccount.getId()
                + "\nfirstname: " + newAccount.getFirstName()
                + "\nlastname: " + newAccount.getLastName()
                + "\nemail: " + newAccount.getEmail()
                + "\nphone: " + newAccount.getPhone()
                + "\nrole id: " + newAccount.getRole());
        return newAccount;
    }

    public Optional<Account> findByEmail(String email) {
        String query = "select * from accounts where email = '" + email + "';";
        Account account = null;
        try {
            ResultSet resultSet = Main.statement.executeQuery(query);
            while (resultSet.next()) {
                account = getAccount(resultSet);
            }
            resultSet.close();
        } catch (SQLException e) {
            log.error("SQL Error!", e);
        }
        return Optional.ofNullable(account);
    }

    public Optional<Account> findByPhone(String phone) {
        String query = "select * from accounts where phone = '" + phone + "';";
        Account account = null;
        try {
            ResultSet resultSet = Main.statement.executeQuery(query);
            while (resultSet.next()) {
                account = getAccount(resultSet);
            }
            resultSet.close();
        } catch (SQLException e) {
            log.error("SQL Error!", e);
        }
        return Optional.ofNullable(account);
    }

    @Override
    public Account update(@NotNull Account account) {
        String query = "call update_user(?, ?, ?, ?, ?, ?, ?)";
        try {
            assert account.getId() != null;
            CallableStatement callableStatement = Main.connection.prepareCall(query);
            callableStatement.setLong(1, account.getId());
            callableStatement.setString(2, account.getFirstName());
            callableStatement.setString(3, account.getLastName());
            callableStatement.setString(4, account.getEmail());
            callableStatement.setString(5, account.getPhone());
            callableStatement.setByte(6, account.getRole());
            callableStatement.setString(7, account.getPasswordHash());
            callableStatement.execute();
            callableStatement.close();
        } catch (SQLException e) {
            log.error("SQL Error!", e);
        } catch (AssertionError e) {
            log.error("Account id must not be null!", e);
        }
        log.info("updated account\nid: " + account.getId()
                + "\nfirstname: " + account.getFirstName()
                + "\nlastname: " + account.getLastName()
                + "\nemail: " + account.getEmail()
                + "\nphone: " + account.getPhone()
                + "\nrole id: " + account.getRole());
        return account;
    }

    @Override
    public void delete(Long id) {
        String query = "call delete_user('" + id + "')";
        log.info("user deleted");
        try {
            Main.statement.execute(query);
        } catch (SQLException e) {
            log.error("SQL Error!", e);
        }

    }
}
