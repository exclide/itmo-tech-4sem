package is.tech.mybatis;

import is.tech.dao.CarModelDao;
import is.tech.models.CarModel;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.List;

public class CarModelDaoBatis implements CarModelDao, AutoCloseable {
    CarModelMapper mapper;
    SqlSession session;
    public CarModelDaoBatis() throws IOException {
        String resource = "mybatis-config.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        session = sqlSessionFactory.openSession();
        mapper = session.getMapper(CarModelMapper.class);
    }
    @Override
    public CarModel save(CarModel entity) throws SQLException {
        mapper.save(entity);
        session.commit();
        return entity;
    }

    @Override
    public void deleteById(long id) {
        mapper.deleteById(id);
        session.commit();
    }

    @Override
    public void deleteByEntity(CarModel entity) {
        mapper.deleteById(entity.getId());
    }

    @Override
    public void deleteAll() {
        mapper.deleteAll();
        session.commit();
    }

    @Override
    public CarModel update(CarModel entity) {
        mapper.update(entity);
        session.commit();
        return entity;
    }

    @Override
    public CarModel getById(long id) {
        return mapper.getById(id);
    }

    @Override
    public List<CarModel> getAll() {
        var models = mapper.getAll();
        return models;
    }

    @Override
    public List<CarModel> getAllByVId(long id) {
        var models = mapper.getAllByVId(id);
        return models;
    }

    @Override
    public void close() throws Exception {
        session.close();
    }
}
