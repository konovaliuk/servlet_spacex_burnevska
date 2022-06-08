package me.braun.spacex.command;

import me.braun.spacex.command.commandImpl.*;
import java.util.HashMap;
import java.util.Map;

public class CommandFactory {
    public static final String LOGIN_COMMAND = "signIn";
    public static final String REG_COMMAND = "signUp";
    public static final String LOGOUT_COMMAND = "logOut";
    public static final String EDIT_ACCOUNT_COMMAND = "userEdit";
    public static final String USER_MISSIONS_COMMAND = "userMission";
    public static final String CREATE_MISSION_COMMAND = "missionCreate";
    public static final String EDIT_MISSION_COMMAND = "missionEdit";
    public static final String SET_STATUS_COMMAND = "missionStatus";
    public static final String SET_ROLE_COMMAND = "userRole";
    public static final String GET_MISSION_COMMAND = "mission";
    public static final String LOCALIZATION_COMMAND = "lang";
    private static final Map<String, ICommand> commands = new HashMap<>();

    static {
        commands.put(LOGIN_COMMAND, new LoginCommand());
        commands.put(REG_COMMAND, new RegisterCommand());
        commands.put(LOGOUT_COMMAND, new LogOutCommand());
        commands.put(USER_MISSIONS_COMMAND, new UserMissionsCommand());
        commands.put(CREATE_MISSION_COMMAND, new CreateMissionCommand());
        commands.put(LOCALIZATION_COMMAND, new LocalizationCommand());
        commands.put(EDIT_ACCOUNT_COMMAND, new EditAccountCommand());
        commands.put(EDIT_MISSION_COMMAND, new EditMissionCommand());
        commands.put(SET_STATUS_COMMAND, new SetMissionStatus());
        commands.put(SET_ROLE_COMMAND, new SetRoleCommand());
        commands.put(GET_MISSION_COMMAND, new GetMissionCommand());
    }

    public static ICommand getCommand(String commandName) {
        ICommand command = commands.get(commandName);
        if (command == null) {
            command = new NoCommand();
        }
        return command;
    }
}