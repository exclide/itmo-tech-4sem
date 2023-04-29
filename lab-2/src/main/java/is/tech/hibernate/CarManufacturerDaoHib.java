package is.tech.hibernate;

import is.tech.dao.CarManufacturerDao;
import is.tech.models.CarManufacturer;
import is.tech.models.CarModel;
import jakarta.persistence.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CarManufacturerDaoHib implements CarManufacturerDao {
    private SessionFactory sessionFactory;
    public CarManufacturerDaoHib() {
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure() // configures settings from hibernate.cfg.xml
                .build();
        try {
            sessionFactory = new MetadataSources( registry ).buildMetadata().buildSessionFactory();
        }
        catch (Exception e) {
            // The registry would be destroyed by the SessionFactory, but we had trouble building the SessionFactory
            // so destroy it manually.
            StandardServiceRegistryBuilder.destroy( registry );
        }
    }
    @Override
    public CarManufacturer save(CarManufacturer entity) throws SQLException {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();

            session.persist(entity);

            transaction.commit();
        }

        return entity;
    }

    @Override
    public void deleteById(long id) throws SQLException {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();

            var entity = session.get(CarManufacturer.class, id);
            session.remove(entity);

            transaction.commit();
        }
    }

    @Override
    public void deleteByEntity(CarManufacturer entity) throws SQLException {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();

            session.remove(entity);

            transaction.commit();
        }
    }

    @Override
    public void deleteAll() throws SQLException {
        String deleteAll = "TRUNCATE car_manufacturer CASCADE";

        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();

            Query query = session.createNativeQuery(deleteAll, CarManufacturer.class);
            query.executeUpdate();

            transaction.commit();
        }
    }

    @Override
    public CarManufacturer update(CarManufacturer entity) throws SQLException {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();

            session.merge(entity);

            transaction.commit();
        }

        return entity;
    }

    @Override
    public CarManufacturer getById(long id) throws SQLException {
        CarManufacturer tmp = null;
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();

            tmp = session.get(CarManufacturer.class, id);
        }

        return tmp;
    }

    @Override
    public List<CarManufacturer> getAll() {
        List<CarManufacturer> list = new ArrayList<>();

        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();

            list = session.createQuery("from CarManufacturer", CarManufacturer.class).list();
        }

        return list;
    }
}
