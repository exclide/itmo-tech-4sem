package is.tech.jdbc;

import is.tech.dao.CarManufacturerDao;
import is.tech.dao.CarModelDao;
import is.tech.models.CarManufacturer;
import is.tech.models.CarModel;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static is.tech.util.DbConnectionInfo.*;

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
        String getSql = "SELECT mod.name as model_name, length, width, body_style, manufacturer_id, " +
                "manu.name, manu.id as manu_id, manu.date as date " +
                "FROM car_model as mod " +
                "INNER JOIN car_manufacturer as manu " +
                "ON mod.manufacturer_id = manu.id " +
                "WHERE mod.id=?";

        CarModel res = null;

        try (PreparedStatement statement = db.prepareStatement(getSql)) {
            statement.setLong(1, id);
            ResultSet rs = statement.executeQuery();

            if (!rs.isBeforeFirst()) {
                throw new JdbcException("Entity not found");
            }
            CarManufacturer manufacturer = new CarManufacturer();
            manufacturer.setId(rs.getLong("manu_id"));
            manufacturer.setName(rs.getString("manu_name"));
            manufacturer.setDate(rs.getDate("date").toLocalDate());

            res = new CarModel();
            res.setId(id);
            res.setName(rs.getString("name"));
            res.setLength(rs.getInt("length"));
            res.setWidth(rs.getInt("width"));
            res.setBodyStyle(rs.getString("body_style"));
            res.setManufacturerId(rs.getLong("manufactured_id"));
            res.setCarManufacturer(manufacturer);

        } catch (SQLException | JdbcException e) {
            System.out.println(e.getMessage());
        }

        return res;
    }

    @Override
    public List<CarModel> getAll() {
        String getAllSql = "SELECT mod.id as model_id, mod.name as model_name, length, width, " +
                "body_style, manufacturer_id, " +
                "cm.name as manu_name, cm.id as manu_id, cm.date FROM car_model mod " +
                "INNER JOIN car_manufacturer cm on mod.manufacturer_id = cm.id";
        List<CarModel> res = new ArrayList<>();

        try (Statement statement = db.createStatement()) {
            ResultSet rs = statement.executeQuery(getAllSql);

            if (!rs.isBeforeFirst()) {
                throw new JdbcException("Get all failed or table is empty");
            }

            while (rs.next()) {
                CarModel tmp = new CarModel();
                CarManufacturer manu = new CarManufacturer();

                manu.setId(rs.getLong("manu_id"));
                manu.setName(rs.getString("manu_name"));
                manu.setDate(rs.getDate("date").toLocalDate());

                tmp.setCarManufacturer(manu);
                tmp.setId(rs.getLong("model_id"));
                tmp.setName(rs.getString("model_name"));
                tmp.setLength(rs.getInt("length"));
                tmp.setWidth(rs.getInt("width"));
                tmp.setBodyStyle(rs.getString("body_style"));
                tmp.setId(rs.getLong("manu_id"));

                res.add(tmp);
            }

        } catch (SQLException | JdbcException e) {
            System.out.println(e.getMessage());
        }


        return res;
    }

    @Override
    public List<CarModel> getAllByVId(long id) {
        String getAllSql = "SELECT mod.id as model_id, mod.name as model_name, length, width, " +
                "body_style, manufacturer_id, " +
                "cm.name as manu_name, cm.id as manu_id, cm.date FROM car_model mod " +
                "INNER JOIN car_manufacturer cm on mod.manufacturer_id = cm.id " +
                "where cm.id=?";
        List<CarModel> res = new ArrayList<>();

        try (PreparedStatement statement = db.prepareStatement(getAllSql)) {
            statement.setLong(1, id);
            ResultSet rs = statement.executeQuery();

            if (!rs.isBeforeFirst()) {
                throw new JdbcException("Get all failed or table is empty");
            }

            while (rs.next()) {
                CarModel tmp = new CarModel();
                CarManufacturer manu = new CarManufacturer();

                manu.setId(rs.getLong("manu_id"));
                manu.setName(rs.getString("manu_name"));
                manu.setDate(rs.getDate("date").toLocalDate());

                tmp.setCarManufacturer(manu);
                tmp.setId(rs.getLong("model_id"));
                tmp.setName(rs.getString("model_name"));
                tmp.setLength(rs.getInt("length"));
                tmp.setWidth(rs.getInt("width"));
                tmp.setBodyStyle(rs.getString("body_style"));
                tmp.setId(rs.getLong("manu_id"));

                res.add(tmp);
            }

        } catch (SQLException | JdbcException e) {
            System.out.println(e.getMessage());
        }


        return res;
    }

    @Override
    public void close() throws Exception {

    }
}
