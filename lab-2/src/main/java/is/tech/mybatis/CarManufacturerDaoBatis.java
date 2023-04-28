package is.tech.mybatis;

import is.tech.dao.CarManufacturerDao;
import is.tech.models.CarManufacturer;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.List;

public class CarManufacturerDaoBatis implements CarManufacturerDao, AutoCloseable {
    CarManufacturerMapper mapper;
    SqlSession session;
    public CarManufacturerDaoBatis() throws IOException {
        String resource = "mybatis-config.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        session = sqlSessionFactory.openSession();
        mapper = session.getMapper(CarManufacturerMapper.class);
    }
    @Override
    public CarManufacturer save(CarManufacturer entity) throws SQLException {
        mapper.save(entity);
        session.commit();
        return entity;
    }

    @Override
    public void deleteById(long id) throws SQLException {
        mapper.deleteById(id);
        session.commit();
    }

    @Override
    public void deleteByEntity(CarManufacturer entity) throws SQLException {
        deleteById(entity.getId());
    }

    @Override
    public void deleteAll() throws SQLException {
        mapper.deleteAll();
        session.commit();
    }

    @Override
    public CarManufacturer update(CarManufacturer entity) throws SQLException {
        mapper.update(entity);
        session.commit();
        return entity;
    }

    @Override
    public CarManufacturer getById(long id) throws SQLException {
        CarManufacturer carManufacturer = mapper.getById(id);
        return carManufacturer;
    }

    @Override
    public List<CarManufacturer> getAll() {
        var list = mapper.getAll();
        return list;
    }

    @Override
    public void close() throws Exception {
        session.close();
    }
}
