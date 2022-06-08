package me.braun.spacex.service;

import me.braun.spacex.dao.*;
import me.braun.spacex.entity.Account;
import me.braun.spacex.entity.Mission;
import me.braun.spacex.util.exception.AccountNotFoundException;
import me.braun.spacex.util.exception.MissionNotFoundException;
import me.braun.spacex.util.exception.RoleNotFoundException;

import java.util.List;

public class AdminService {

    private final IMissionDAO missionDAO = DAOFactory.getMissionDAO();
    private final IAccountDAO accountDAO = DAOFactory.getAccountDAO();
    private final IRoleDAO roleDAO = DAOFactory.getRoleDAO();

    public Mission editMissionStatus(Long missionId, Short missionStatus) {
        Mission mission = missionDAO.findById(missionId).orElseThrow(MissionNotFoundException::new);
        mission.setStatus(missionStatus);
        missionDAO.update(mission);
        return mission;
    }

    public Account editUserRole(String email, byte roleId){
        Account user = accountDAO.findByEmail(email).orElseThrow(AccountNotFoundException::new);
        user.setRole(roleDAO.findById(roleId).orElseThrow(RoleNotFoundException::new));
        accountDAO.update(user);
        return user;
    }

    public Account getUserByEmail(String email) throws AccountNotFoundException {
        return accountDAO.findByEmail(email).orElseThrow(AccountNotFoundException::new);
    }

    public Account getUserByPhone(String phone) throws AccountNotFoundException {
        return accountDAO.findByPhone(phone).orElseThrow(AccountNotFoundException::new);
    }

//    public List<Account> getAllUsers(Long pageNum){
//        return accountDAO.pagination((pageNum - 1)*10, 10L);
//    }
    public List<Account> getAllUsers(){
        return accountDAO.findAll();
    }

    public List<Mission> getUserMissions(Account account) {
        return missionDAO.findByCustomer(account.getId());
    }

    public Mission getMissionByTitle(String title) throws MissionNotFoundException{
        return missionDAO.findByTitle(title).orElseThrow(MissionNotFoundException::new);
    }

    public List<Mission> getMissions() {
        return missionDAO.findAll();
    }
}
