package me.braun.spacex.service;

import me.braun.spacex.dao.DAOFactory;
import me.braun.spacex.dao.IMissionDAO;
import me.braun.spacex.entity.Account;
import me.braun.spacex.entity.Mission;
import me.braun.spacex.entity.enams.EStatus;
import me.braun.spacex.util.exception.MissionAlreadyExistsException;
import me.braun.spacex.util.exception.MissionNotFoundException;
import me.braun.spacex.util.exception.ServiceException;

import java.sql.Date;
import java.util.List;

public class MissionService {
    private final IMissionDAO missionDAO = DAOFactory.getMissionDAO();

    public Mission createMission(String title, String description, long customer,
                                 long spacecraft, byte serviceType,
                                 double payloadWeigh, Date date, int duration) {
        missionDAO.findByTitle(title).ifPresent(account -> {
            throw new MissionAlreadyExistsException(title);
        });

        return missionDAO.save(Mission.builder()
                .title(title)
                .description(description)
                .customer(customer)
                .spaceCraft(spacecraft)
                .status(EStatus.PROCESS.getId())
                .serviceType(serviceType)
                .curator(7L)
                .payloadWeigh(payloadWeigh)
                .date(date)
                .duration(duration)
                .build());
    }

    public Mission editMission(Long id, String title, String description, long customer,
                               long spacecraft, byte serviceType,
                               double payloadWeigh, Date date, int duration) {
        missionDAO.findByTitle(title).ifPresent(account -> {
            throw new MissionNotFoundException();
        });

        return missionDAO.update(Mission.builder()
                .id(id)
                .title(title)
                .description(description)
                .customer(customer)
                .spaceCraft(spacecraft)
                .status(EStatus.PROCESS.getId())
                .serviceType(serviceType)
                .curator(7L)
                .payloadWeigh(payloadWeigh)
                .date(date)
                .duration(duration)
                .build());
    }

    public void deleteMission(Long id) {
        missionDAO.delete(id);
    }

    public List<Mission> getUserMissions(Account account) {
        return missionDAO.findByCustomer(account.getId());
    }

    public Mission getMissionByTitle(Account account, String title) {
        if (missionDAO.findByCustomer(account.getId()).stream().anyMatch(mission -> mission.getTitle().equals(title))) {
            return missionDAO.findByTitle(title).orElseThrow(MissionNotFoundException::new);
        }
        throw new ServiceException("This mission is unavailable for this user!");
    }

    public List<String> getSpacecraftTitles() {
        return DAOFactory.getSpaceCraftDAO().getTitles();
    }

    public List<String> getServiceTypes() {
        return DAOFactory.getServiceTypeDAO().findAllServices();
    }

}
