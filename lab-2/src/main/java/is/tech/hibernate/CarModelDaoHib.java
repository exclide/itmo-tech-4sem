package is.tech.hibernate;

import is.tech.dao.CarModelDao;
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

public class CarModelDaoHib implements CarModelDao {
    private SessionFactory sessionFactory;
    public CarModelDaoHib() {
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
    public CarModel save(CarModel entity) throws SQLException {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();

            session.persist(entity);

            transaction.commit();
        }

        return entity;
    }

    @Override
    public void deleteById(long id) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();

            var entity = session.get(CarModel.class, id);
            session.remove(entity);

            transaction.commit();
        }
    }

    @Override
    public void deleteByEntity(CarModel entity) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();

            session.remove(entity);

            transaction.commit();
        }
    }

    @Override
    public void deleteAll() {
        String deleteAll = "TRUNCATE car_model CASCADE";

        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();

            Query query = session.createNativeQuery(deleteAll, CarModel.class);
            query.executeUpdate();

            transaction.commit();
        }
    }

    @Override
    public CarModel update(CarModel entity) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();

            session.merge(entity);

            transaction.commit();
        }

        return entity;
    }

    @Override
    public CarModel getById(long id) {
        CarModel tmp = null;
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();

            tmp = session.get(CarModel.class, id);
        }

        return tmp;
    }

    @Override
    public List<CarModel> getAll() {
        List<CarModel> list = new ArrayList<>();

        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();

            list = session.createQuery("from CarModel", CarModel.class).list();
        }

        return list;
    }

    @Override
    public List<CarModel> getAllByVId(long id) {
        String hql = "select cm from CarModel cm where cm.carManufacturer=?";
        List<CarModel> list = new ArrayList<>();

        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();

            list = session.createQuery(hql, CarModel.class)
                    .setParameter(1, id)
                    .list();
        }

        return list;
    }
}
