package ru.edu.Task5.repo;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;
import ru.edu.Task5.model.TppProductRegister;

import java.math.BigInteger;

@Repository
public interface TppProductRegisterRepo  extends CrudRepository<TppProductRegister, BigInteger> {
}
