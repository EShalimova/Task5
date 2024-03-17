package ru.edu.Task5.repo;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.edu.Task5.model.TppProduct;

import java.math.BigInteger;

@Repository
public interface TppProductRepo extends CrudRepository<TppProduct, BigInteger> {
    TppProduct findFirstById(BigInteger id);

    TppProduct findFirstByContNumber(String number);
}