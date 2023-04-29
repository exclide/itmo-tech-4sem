package is.tech.performance;

import is.tech.dao.CarManufacturerDao;
import is.tech.models.CarManufacturer;
import lombok.Data;

import java.sql.SQLException;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;

@Data
public class PerformanceTest {
    CarManufacturerDao dao;
    String orm;

    public PerformanceTest(CarManufacturerDao dao, String orm) throws SQLException {
        this.dao = dao;
        this.orm = orm;
    }
    public void measureCreate() throws SQLException {
        dao.deleteAll();

        Instant start = Instant.now();

        for (int i = 0; i < 100; i++) {
            var manufacturer = new CarManufacturer(i, "test", LocalDate.now());
            dao.save(manufacturer);
        }

        Instant finish = Instant.now();
        long timeElapsed = Duration.between(start, finish).toMillis();
        System.out.println(orm + " create: " + timeElapsed + " ms");
    }

    public void measureGet() {
        Instant start = Instant.now();

        var list = dao.getAll();

        Instant finish = Instant.now();
        long timeElapsed = Duration.between(start, finish).toMillis();
        System.out.println(orm + " get: " + timeElapsed + " ms");
    }
}
