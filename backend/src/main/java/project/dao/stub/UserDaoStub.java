package project.dao.stub;


import org.springframework.stereotype.Service;
import project.dao.stub.util.IdGenerator;
import project.entity.User;

import java.math.BigInteger;

@Service
public class UserDaoStub extends BaseDaoStub<User, BigInteger> implements UserDao {

    @Override
    public <S extends User> BigInteger getId(S entity) {
        if (entity.getId() != null) {
            return entity.getId();
        }
        BigInteger id = IdGenerator.BIG_INTEGER.generateId();
        entity.setId(id);
        return id;
    }
}
