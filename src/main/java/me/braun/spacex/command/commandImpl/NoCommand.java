package me.braun.spacex.command.commandImpl;

import me.braun.spacex.command.ICommand;
import me.braun.spacex.entity.Account;
import me.braun.spacex.entity.enams.ERole;
import me.braun.spacex.util.AttributeNames;
import me.braun.spacex.util.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

public class NoCommand implements ICommand {

    private final Logger log = LoggerFactory.getLogger(NoCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.warn("Wrong command name");
        Account account = (Account) request.getSession().getAttribute(AttributeNames.USER);
        if (Objects.nonNull(account) && account.getRole() == ERole.ADMIN.getId()) {
            return Config.ADMIN;
        } else {
            return Config.INDEX;
        }
    }
}
