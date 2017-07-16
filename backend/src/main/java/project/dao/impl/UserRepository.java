package project.dao.impl;

import org.springframework.stereotype.Repository;
import project.entity.User;

import java.math.BigInteger;

@Repository
public interface UserRepository extends CustomRepository<User, BigInteger>, UserDao {
}
