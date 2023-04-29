package is.tech;

import is.tech.dao.CarManufacturerDao;
import is.tech.dao.CarModelDao;
import is.tech.hibernate.CarManufacturerDaoHib;
import is.tech.hibernate.CarModelDaoHib;
import is.tech.jdbc.CarManufacturerDaoJdbc;
import is.tech.models.CarManufacturer;
import is.tech.models.CarModel;
import is.tech.mybatis.CarManufacturerDaoBatis;
import is.tech.mybatis.CarModelDaoBatis;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws SQLException, IOException {
        //CarManufacturerDao dao = new CarManufacturerDaoHib();

        var m2 = new CarManufacturer(1, "test2", LocalDate.now());
        var m3 = new CarManufacturer(2, "test3", LocalDate.now());

        CarModelDao dao = new CarModelDaoHib();

        /*
        CarModel mod1 = new CarModel(0, "x5", 5, 5, "sedan", m2, m2.getId());
        CarModel mod2 = new CarModel(1, "corolla", 6, 6, "sedan", m3, m3.getId());

        dao.save(mod1);
        dao.save(mod2);*/

        dao.deleteAll();




    }
}