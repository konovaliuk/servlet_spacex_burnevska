package me.braun.spacex.dao;

import me.braun.spacex.entity.ServiceType;

import java.util.List;

public interface IServiceTypeDAO extends DAO<ServiceType, Byte>{
    List<String> findAllServices();
}
