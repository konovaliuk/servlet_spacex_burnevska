package me.braun.spacex.controller;

import lombok.SneakyThrows;
import me.braun.spacex.command.CommandFactory;
import me.braun.spacex.command.ICommand;
import me.braun.spacex.util.RequestParameters;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;

@WebServlet(name = "spacexServlet", urlPatterns = "/api/*")
public class SpaceXServlet extends HttpServlet {


    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, ParseException {

//        int index = request.getRequestURI().lastIndexOf("api/") + 4;
//        String commandURI = request.getRequestURI().substring(index);
        ICommand command = CommandFactory.getCommand(request.getParameter(RequestParameters.COMMAND));
        String page = command.execute(request, response);

        if (page != null) {
            if (page.startsWith("redirect:")) {
                response.sendRedirect(request.getContextPath() + page.substring(9));
            } else {
                RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(page);
                dispatcher.forward(request, response);
            }
        }

    }

    @SneakyThrows
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    public void init(ServletConfig servletConfig) {

    }

    @SneakyThrows
    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }


    @Override
    public ServletConfig getServletConfig() {
        return null;
    }

    @Override
    public void service(ServletRequest servletRequest, ServletResponse servletResponse) throws ServletException, IOException {

    }

    @Override
    public String getServletInfo() {
        return null;
    }

    @Override
    public void destroy() {

    }
}
