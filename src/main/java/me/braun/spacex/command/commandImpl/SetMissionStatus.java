package me.braun.spacex.command.commandImpl;

import me.braun.spacex.command.ICommand;
import me.braun.spacex.dao.DAOFactory;
import me.braun.spacex.entity.Mission;
import me.braun.spacex.service.AdminService;
import me.braun.spacex.service.MissionService;
import me.braun.spacex.util.AttributeNames;
import me.braun.spacex.util.Config;
import me.braun.spacex.util.RequestParameters;
import org.jetbrains.annotations.Nullable;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;

public class SetMissionStatus implements ICommand {
    public final AdminService adminService = new AdminService();
    public final MissionService missionService = new MissionService();
    @Override
    public @Nullable String execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, ParseException {
        Short statusId = Short.parseShort(request.getParameter(RequestParameters.STATUSID));
        Long missionId = Long.parseLong(request.getParameter(RequestParameters.MISSIONID));
        try {
            Mission mission = adminService.editMissionStatus(missionId, statusId);
            request.setAttribute(AttributeNames.MISSION, mission);
            response.setStatus(200);
            return Config.MISSION;
        }
        catch (Exception e){
            request.setAttribute(AttributeNames.ERROR, e.getMessage());
            return Config.ADMIN;
        }
    }
}
