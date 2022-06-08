package me.braun.spacex.dao;

import me.braun.spacex.entity.Mission;

import java.util.List;
import java.util.Optional;

public interface IMissionDAO extends  DAO<Mission, Long>{

     List<Mission> findByCustomer(Long custId);
     Optional<Mission> findByTitle(String title);
}
