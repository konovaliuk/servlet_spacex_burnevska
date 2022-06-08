package me.braun.spacex.dao;

import me.braun.spacex.entity.Spacecraft;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

public interface ISpacecraftDAO extends DAO<Spacecraft, Long>{
    List<String> getTitles() ;
    Optional<Spacecraft> findByTitle(String title);

}
