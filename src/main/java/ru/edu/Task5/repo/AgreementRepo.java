package ru.edu.Task5.repo;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.edu.Task5.model.AgreementModel;

import java.math.BigInteger;

@Repository
public interface AgreementRepo extends CrudRepository<AgreementModel, Long> {
    AgreementModel findFirstById(BigInteger id);
    AgreementModel findFirstByNumber(String number);
}
