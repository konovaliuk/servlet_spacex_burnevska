package me.braun.spacex.dao.sql;

import me.braun.spacex.Main;
import me.braun.spacex.dao.ISpacecraftDAO;
import me.braun.spacex.entity.Spacecraft;
import me.braun.spacex.util.exception.SpacecraftNotFoundException;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SQLSpacecraftDAO implements ISpacecraftDAO {
    private static final Logger log = LoggerFactory.getLogger(SQLSpacecraftDAO.class);
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "title";
    private static final String COLUMN_CAPACITY = "capacity";
    private static final String COLUMN_MAX_WEIGHT = "max_weight";
    private static final String COLUMN_PRICE = "launch_price";
    private static SQLSpacecraftDAO instance;

    public static ISpacecraftDAO getInstance() {
        if (instance == null) {
            instance = new SQLSpacecraftDAO();
        }
        return instance;
    }

    private Spacecraft getSpacecraft(ResultSet resultSet) throws SQLException {
        long id = resultSet.getLong(COLUMN_ID);
        String title = resultSet.getString(COLUMN_NAME);
        double capacity = resultSet.getFloat(COLUMN_CAPACITY);
        int maxWeigh = resultSet.getInt(COLUMN_MAX_WEIGHT);
        int launchPrice = resultSet.getInt(COLUMN_PRICE);
        return new Spacecraft(id, title, capacity, maxWeigh, launchPrice);
    }


    @Override
    public @NotNull List<Spacecraft> findAll() {
        String query = "select * from spacecrafts;";
        List<Spacecraft> spacecraftList = new ArrayList<>();
        try {
            ResultSet resultSet = Main.statement.executeQuery(query);
            while (resultSet.next()) {
                Spacecraft spacecraft = getSpacecraft(resultSet);
                spacecraftList.add(spacecraft);
            }
            resultSet.close();
        } catch (SQLException e) {
            log.error("SQL Error!", e);
        }
        log.info(spacecraftList.size() + " spacecrafts were found");
        return spacecraftList;
    }

    public @NotNull List<String> getTitles() {
        String query = "select title from spacecrafts;";
        List<String> titleList = new ArrayList<>();
        try {
            ResultSet resultSet = Main.statement.executeQuery(query);
            while (resultSet.next()) {
                Spacecraft spacecraft = getSpacecraft(resultSet);
                titleList.add(spacecraft.getCraftTitle());
            }
            resultSet.close();
        } catch (SQLException e) {
            log.error("SQL Error!", e);
        }
        return titleList;
    }

    @Override
    public Optional<Spacecraft> findById(Long id) {
        String query = "select * from spacecrafts where id= '" + id + "';";
        Spacecraft spacecraft = null;
        try {
            ResultSet resultSet = Main.statement.executeQuery(query);
            while (resultSet.next()) {
                spacecraft = getSpacecraft(resultSet);
            }
            resultSet.close();
        } catch (SQLException e) {
            log.error("SQL Error!", e);
        }
        assert spacecraft != null;
        log.info("spacecraft number " + spacecraft.getId() + " was found");
        return Optional.of(spacecraft);
    }
    @Override
    public Optional<Spacecraft> findByTitle(String title) {
        String query = "select title from spacecrafts where title= '" + title + "';";
        Spacecraft spacecraft = null;
        try {
            ResultSet resultSet = Main.statement.executeQuery(query);
            while (resultSet.next()) {
                spacecraft = getSpacecraft(resultSet);
            }
            resultSet.close();
        } catch (SQLException e) {
            log.error("SQL Error!", e);
        }
        assert spacecraft != null;
        log.info("spacecraft number " + spacecraft.getCraftTitle() + " was found");
        return Optional.of(spacecraft);
    }

    @Override
    public Spacecraft save(@NotNull Spacecraft spacecraft) throws IllegalStateException {
        Spacecraft newSpacecraft = null;
        String query = "call insert_spacecraft(?, ?, ?, ?)";
        try {
            CallableStatement callableStatement = Main.connection.prepareCall(query);
            callableStatement.setString(1, spacecraft.getCraftTitle());
            callableStatement.setDouble(2, spacecraft.getCapacity());
            callableStatement.setInt(3, spacecraft.getMaxWeight());
            callableStatement.setInt(4, spacecraft.getLaunchPrice());
            callableStatement.execute();
            String queryFindId = "select max(id) from spacecrafts;";
            Long spacecraftMaxId = null;
            try {
                ResultSet resultSet = Main.statement.executeQuery(queryFindId);
                while (resultSet.next()) {
                    spacecraftMaxId = resultSet.getLong("max(id)");
                }
                resultSet.close();
            } catch (SQLException e) {
                log.error("SQL Error!", e);
            }
            assert spacecraftMaxId != null;
            newSpacecraft = findById(spacecraftMaxId)
                    .orElseThrow(SpacecraftNotFoundException::new);

            callableStatement.close();
        } catch (SQLException e) {
            log.error("SQL Error!", e);
        }
        assert newSpacecraft != null;

        log.info(("spacecraft created\nid: " + newSpacecraft.getId() + "\ntitle: " + newSpacecraft.getCraftTitle()
                + "\ncapacity: " + newSpacecraft.getCapacity()
                + "\nmax weight: " + newSpacecraft.getMaxWeight()
                + "\nlaunch price: " + newSpacecraft.getLaunchPrice()));
        return newSpacecraft;
    }

    @Override
    public Spacecraft update(Spacecraft spacecraft) {
        String query = "call update_spacecraft(?, ?, ?, ?, ?)";
        try {
            assert spacecraft.getId() != null;
            CallableStatement callableStatement = Main.connection.prepareCall(query);
            callableStatement.setLong(1, spacecraft.getId());
            callableStatement.setString(2, spacecraft.getCraftTitle());
            callableStatement.setDouble(3, spacecraft.getCapacity());
            callableStatement.setInt(4, spacecraft.getMaxWeight());
            callableStatement.setInt(5, spacecraft.getLaunchPrice());
            callableStatement.execute();
            callableStatement.close();
        } catch (SQLException e) {
            log.error("SQL Error!", e);
        } catch (AssertionError e) {
            log.error("Spacecraft id must not be null!", e);
        }
        log.info(("spacecraft updated\nid: " + spacecraft.getId()
                + "\ntitle: " + spacecraft.getCraftTitle()
                + "\ncapacity: " + spacecraft.getCapacity()
                + "\nmax weight: " + spacecraft.getMaxWeight()
                + "\nlaunch price: " + spacecraft.getLaunchPrice()));
        return spacecraft;
    }

    @Override
    public void delete(Long id) {
        String query = "call delete_spacecraft('" + id + "')";
        log.info("spacecraft deleted");
        try {
            Main.statement.execute(query);
        } catch (SQLException e) {
            log.error("SQL Error!", e);
        }
    }
}
