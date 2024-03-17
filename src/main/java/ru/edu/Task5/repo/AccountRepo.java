package ru.edu.Task5.repo;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.edu.Task5.model.Account;

import java.math.BigInteger;
import java.util.Set;

@Repository
public interface AccountRepo extends CrudRepository<Account, Long> {
    Account findFirstById(BigInteger id);

    @Query(value = "select a.id id_acc " +
            "  from tpp_ref_product_register_type rt, account_pool ap, account a " +
            "  where rt.value = ap.registry_type_code" +
            "      and a.account_pool_id = ap.id" +
            "      and a.bussy = false " +
            "      and rt.account_type = 'Клиентский'" +
            "      and ap.branch_code = :branchCode" +
            "      and ap.currency_code = :currCode" +
            "      and ap.mdm_code = :mdmCode" +
            "      and ap.priority_code = :priorityCode" +
            "      and rt.value = :regType" +
            "      and (rt.register_type_start_date is null or rt.register_type_start_date <= current_date)" +
            "      and (rt.register_type_end_date   is null or rt.register_type_end_date   >= current_date)" +
            "  order by ap.priority_code nulls last, a.account_number nulls last", nativeQuery = true)
    Set<BigInteger> getAccountId(String branchCode, String currCode, String mdmCode, String priorityCode, String regType);

    @Query(value = "SELECT internal_id FROM tpp_ref_product_class t1 WHERE t1.value = :productCode", nativeQuery = true)
    BigInteger getProductId(String productCode);
}