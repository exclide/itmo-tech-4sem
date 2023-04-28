package is.tech;

import is.tech.dao.CarManufacturerDao;
import is.tech.jdbc.CarManufacturerDaoJdbc;
import is.tech.models.CarManufacturer;
import is.tech.mybatis.CarManufacturerDaoBatis;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws SQLException, IOException {
        CarManufacturerDao dao = new CarManufacturerDaoBatis();

        CarManufacturer t1 = dao.getById(0);

        System.out.println(t1);
    }
}