package is.tech.jdbc;

import is.tech.dao.CarManufacturerDao;
import is.tech.models.CarManufacturer;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static util.DbConnectionInfo.*;

public class CarManufacturerDaoJdbc implements CarManufacturerDao, AutoCloseable {
    Connection db;
    public CarManufacturerDaoJdbc() throws SQLException {
        try {
            db = DriverManager.getConnection(connectionUrl, username, password);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public CarManufacturer save(CarManufacturer entity) throws SQLException {
        String saveSql = "INSERT INTO car_manufacturer(id, name, foundation_date) " +
                "VALUES(?, ?, ?)";

        try (PreparedStatement statement = db.prepareStatement(saveSql)) {
            statement.setLong(1, entity.getId());
            statement.setString(2, entity.getName());
            statement.setDate(3, Date.valueOf(entity.getDate()));
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return entity;
    }

    @Override
    public void deleteById(long id) throws SQLException {
        String deleteSql = "DELETE FROM car_manufacturer WHERE id=?";

        try (PreparedStatement statement = db.prepareStatement(deleteSql)) {
            statement.setLong(1, id);

            int rowsChanged = statement.executeUpdate();

            if (rowsChanged == 0) {
                throw new JdbcException("Delete failed");
            }

        } catch (SQLException | JdbcException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void deleteByEntity(CarManufacturer entity) throws SQLException {
        deleteById(entity.getId());
    }

    @Override
    public void deleteAll() throws SQLException {
        String deleteAllSql = "TRUNCATE car_manufacturer CASCADE";

        try (Statement statement = db.createStatement()) {
            statement.execute(deleteAllSql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public CarManufacturer update(CarManufacturer entity) throws SQLException {
        String updateSql = "UPDATE car_manufacturer SET name=?, foundation_date=? " +
                "WHERE id=?";

        try (PreparedStatement statement = db.prepareStatement(updateSql)) {
            statement.setString(1, entity.getName());
            statement.setDate(2, Date.valueOf(entity.getDate()));
            statement.setLong(3, entity.getId());

            int rowsChanged = statement.executeUpdate();

            if (rowsChanged == 0) {
                throw new JdbcException("Update failed");
            }

        } catch (SQLException | JdbcException e) {
            System.out.println(e.getMessage());
        }

        return null;
    }

    @Override
    public CarManufacturer getById(long id) throws SQLException {
        String getSql = "SELECT name, foundation_date FROM car_manufacturer " +
                "WHERE id=?";

        CarManufacturer res = null;

        try (PreparedStatement statement = db.prepareStatement(getSql)) {
            statement.setLong(1, id);
            ResultSet rs = statement.executeQuery();

            if (!rs.isBeforeFirst()) {
                throw new JdbcException("Entity not found");
            }
            res = new CarManufacturer();
            res.setId(id);
            res.setName(rs.getString("name"));
            res.setDate(rs.getDate("foundation_date").toLocalDate());

        } catch (SQLException | JdbcException e) {
            System.out.println(e.getMessage());
        }

        return null;
    }

    @Override
    public List<CarManufacturer> getAll() {
        String getAllSql = "SELECT * FROM car_manufacturer";
        List<CarManufacturer> res = new ArrayList<>();

        try (Statement statement = db.createStatement()) {
            ResultSet rs = statement.executeQuery(getAllSql);

            if (!rs.isBeforeFirst()) {
                throw new JdbcException("Get all failed");
            }

            while (rs.next()) {
                CarManufacturer tmp = new CarManufacturer();
                tmp.setId(rs.getLong("id"));
                tmp.setName(rs.getString("name"));
                tmp.setDate(rs.getDate("foundation_date").toLocalDate());

                res.add(tmp);
            }

        } catch (SQLException | JdbcException e) {
            System.out.println(e.getMessage());
        }


        return res;
    }

    @Override
    public void close() throws Exception {
        db.close();
    }
}
