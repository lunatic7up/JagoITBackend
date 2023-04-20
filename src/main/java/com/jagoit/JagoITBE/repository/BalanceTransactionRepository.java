package com.jagoit.JagoITBE.repository;

import com.jagoit.JagoITBE.helper.ActivityConstant;
import com.jagoit.JagoITBE.model.BalanceTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.persistence.Tuple;
import java.util.List;

@Repository
public interface BalanceTransactionRepository extends JpaRepository<BalanceTransaction, String> {

    @Query(value = "SELECT * FROM TXN_BALANCE" +
            " WHERE TXN_DETAIL_CODE='" + ActivityConstant.ACTIVITY_TOPUP_CODE + "'" +
            " ORDER BY CREATED_DATE ASC",
            nativeQuery = true)
    List<BalanceTransaction> getTopupTransaction();

    @Query(value = "SELECT *FROM TXN_BALANCE" +
            " WHERE TXN_DETAIL_CODE='" + ActivityConstant.ACTIVITY_WITHDRAWAL_CODE + "'" +
            " ORDER BY CREATED_DATE ASC",
            nativeQuery = true)
    List<BalanceTransaction> getWithdrawalTransaction();


    @Query(value = "SELECT * FROM TXN_BALANCE WHERE USER_ID=?1 ORDER BY CREATED_DATE DESC", nativeQuery = true)
    List<BalanceTransaction> getHistoryPayment(String user_id);

}
