package me.braun.spacex.command.commandImpl;

import me.braun.spacex.command.ICommand;
import me.braun.spacex.entity.Account;
import me.braun.spacex.service.AdminService;
import me.braun.spacex.util.AttributeNames;
import me.braun.spacex.util.Config;
import me.braun.spacex.util.RequestParameters;
import org.jetbrains.annotations.Nullable;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;

public class SetRoleCommand implements ICommand {
    private final AdminService adminService = new AdminService();
    @Override
    public @Nullable String execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, ParseException {
        String email = request.getParameter(RequestParameters.EMAIL);
        byte statusId = Byte.parseByte(request.getParameter(RequestParameters.STATUSID));
        try {
            adminService.getUserByEmail(email);
            Account account = adminService.editUserRole(email, statusId);
            request.setAttribute(AttributeNames.USER, account);
            response.setStatus(200);
            return Config.ADMIN;
        }
        catch (Exception e){
            request.setAttribute(AttributeNames.ERROR, e.getMessage());
            return Config.ADMIN;
        }
    }
}
