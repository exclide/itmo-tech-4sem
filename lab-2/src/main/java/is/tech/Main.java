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
import is.tech.performance.PerformanceTest;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void RunPerformanceTests() throws SQLException, IOException {
        PerformanceTest test = new PerformanceTest(new CarManufacturerDaoJdbc(), "jdbc");
        test.measureCreate();
        test.measureGet();

        test.setDao(new CarManufacturerDaoBatis());
        test.setOrm("mybatis");

        test.measureCreate();
        test.measureGet();

        test.setDao(new CarManufacturerDaoHib());
        test.setOrm("hibernate");

        test.measureCreate();
        test.measureGet();
    }
    public static void main(String[] args) throws SQLException, IOException {

    }
}