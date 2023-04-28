package is.tech.jdbc;

import is.tech.dao.CarManufacturerDao;
import is.tech.dao.CarModelDao;
import is.tech.models.CarManufacturer;
import is.tech.models.CarModel;

import java.sql.*;
import java.util.List;

import static util.DbConnectionInfo.*;

public class CarModelDaoJdbc implements CarModelDao, AutoCloseable {
    Connection db;
    public CarModelDaoJdbc() throws SQLException {
        try {
            db = DriverManager.getConnection(connectionUrl, username, password);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    @Override
    public CarModel save(CarModel entity) throws SQLException {
        String saveSql = "INSERT INTO car_model(id, name, length, width, body_style, manufacturer_id) " +
                "VALUES(?, ?, ?, ?, ?, ?)";

        try (PreparedStatement statement = db.prepareStatement(saveSql)) {
            statement.setLong(1, entity.getId());
            statement.setString(2, entity.getName());
            statement.setInt(3, entity.getLength());
            statement.setInt(4, entity.getWidth());
            statement.setString(5, entity.getBodyStyle());
            statement.setLong(6, entity.getManufacturerId());
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return entity;
    }

    @Override
    public void deleteById(long id) {
        String deleteSql = "DELETE FROM car_model WHERE id=?";

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
    public void deleteByEntity(CarModel entity) {
        deleteById(entity.getId());
    }

    @Override
    public void deleteAll() {
        String deleteAllSql = "TRUNCATE car_manufacturer CASCADE";

        try (Statement statement = db.createStatement()) {
            statement.execute(deleteAllSql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public CarModel update(CarModel entity) {
        String updateSql = "UPDATE car_model " +
                "SET name=?, length=?, width=?, body_style=?, manufacturer_id=? " +
                "WHERE id=?";

        try (PreparedStatement statement = db.prepareStatement(updateSql)) {
            statement.setString(1, entity.getName());
            statement.setInt(2, entity.getLength());
            statement.setInt(3, entity.getWidth());
            statement.setString(4, entity.getBodyStyle());
            statement.setLong(5, entity.getManufacturerId());
            statement.setLong(6, entity.getId());

            int rowsChanged = statement.executeUpdate();

            if (rowsChanged == 0) {
                throw new JdbcException("Update failed");
            }

        } catch (SQLException | JdbcException e) {
            System.out.println(e.getMessage());
        }

        return entity;
    }

    @Override
    public CarModel getById(long id) {
        String getSql = "SELECT name, length, width, body_style, manufacturer_id FROM car_model " +
                "WHERE id=?";

        CarModel res = null;

        try (PreparedStatement statement = db.prepareStatement(getSql)) {
            statement.setLong(1, id);
            ResultSet rs = statement.executeQuery();

            if (!rs.isBeforeFirst()) {
                throw new JdbcException("Entity not found");
            }
            res = new CarModel();
            res.setId(id);
            res.setName(rs.getString("name"));
            res.setLength(rs.getInt("length"));
            res.setWidth(rs.getInt("width"));
            res.setBodyStyle(rs.getString("body_style"));
            res.setManufacturerId(rs.getLong("manufactured_id"));
            //KAK BLYA

        } catch (SQLException | JdbcException e) {
            System.out.println(e.getMessage());
        }

        return res;
    }

    @Override
    public List<CarModel> getAll() {
        return null;
    }

    @Override
    public List<CarModel> getAllByVId(long id) {
        return null;
    }

    @Override
    public void close() throws Exception {

    }
}
