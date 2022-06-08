package me.braun.spacex.command.filter;

import me.braun.spacex.command.CommandFactory;
import me.braun.spacex.entity.Account;
import me.braun.spacex.entity.enams.ERole;
import me.braun.spacex.util.AttributeNames;
import me.braun.spacex.util.Config;
import me.braun.spacex.util.RequestParameters;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class AuthorizationFilter implements Filter {
    private static final Set<String> secureCommands = new HashSet<>();

    static {
        secureCommands.add(CommandFactory.SET_ROLE_COMMAND);
        secureCommands.add(CommandFactory.SET_STATUS_COMMAND);
        secureCommands.add(CommandFactory.GET_MISSION_COMMAND);

    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        String securePath = req.getContextPath() + "/admin";
        String command = req.getParameter(RequestParameters.COMMAND);
        boolean isSecurePageRequest = req.getRequestURI().startsWith(securePath);
        boolean isSecureCommandRequest = isSecureCommand(command);

        if (isSecurePageRequest || isSecureCommandRequest) {
            if (isUserAdmin(req)) {
                chain.doFilter(req, resp);
            } else {
                RequestDispatcher dispatcher = req.getRequestDispatcher(Config.ERROR);
                dispatcher.forward(req, response);
            }
        } else {
            chain.doFilter(req, resp);
        }
    }

    @Override
    public void destroy() {

    }

    private boolean isSecureCommand(String command) {
        if (Objects.isNull(command))
            return false;
        return secureCommands.contains(command);
    }

    private boolean isUserAdmin(HttpServletRequest req) {
        HttpSession session = req.getSession(false);
        boolean isLoggedIn = Objects.nonNull(session) && Objects.nonNull(session.getAttribute(AttributeNames.USER));
        if (isLoggedIn) {
            Account user = (Account) session.getAttribute(AttributeNames.USER);
            return user.getRole() == ERole.ADMIN.getId();
        } else {
            return false;
        }
    }
}
