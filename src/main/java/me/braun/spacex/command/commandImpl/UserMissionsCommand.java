package me.braun.spacex.command.commandImpl;

import me.braun.spacex.command.ICommand;
import me.braun.spacex.entity.Account;
import me.braun.spacex.entity.Mission;
import me.braun.spacex.service.MissionService;
import me.braun.spacex.util.AttributeNames;
import me.braun.spacex.util.Config;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class UserMissionsCommand implements ICommand {
    private final Logger log = LoggerFactory.getLogger(UserMissionsCommand.class);
    private final MissionService missionService = new MissionService();
    @Override
    public @Nullable String execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Account account = (Account) request.getSession().getAttribute(AttributeNames.USER);
        List<Mission> missions = missionService.getUserMissions(account);
        request.setAttribute(AttributeNames.MISSION, missions);
        return Config.MISSION;
    }
}
