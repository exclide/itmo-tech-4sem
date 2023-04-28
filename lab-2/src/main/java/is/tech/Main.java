package is.tech;

import is.tech.dao.CarManufacturerDao;
import is.tech.jdbc.CarManufacturerDaoJdbc;
import is.tech.models.CarManufacturer;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws SQLException {
        CarManufacturerDao test = new CarManufacturerDaoJdbc();

        CarManufacturer e1 = new CarManufacturer(0, "test1", LocalDate.now());
        CarManufacturer e2 = new CarManufacturer(1, "test2", LocalDate.now());
        CarManufacturer e3 = new CarManufacturer(2, "test3", LocalDate.now());

        test.save(e1);
        test.save(e2);
        test.save(e3);

        List<CarManufacturer> list = test.getAll();

        list.forEach(System.out::println);
    }
}