package com.jagoit.JagoITBE.service;

import com.jagoit.JagoITBE.model.BalanceTransaction;

import javax.persistence.Tuple;
import java.util.List;
import java.util.Map;

public interface BalanceService {

    public BalanceTransaction Withdrawal(Map<String, Object> paramMap);

    public BalanceTransaction Topup(Map<String, Object> paramMap);

    public BalanceTransaction Payment(Map<String, Object> paramMap);

    public BalanceTransaction updateTransaction(String status, String transaction_id, String user_id_admin);

    public List<BalanceTransaction> getTopupTransaction();

    public List<BalanceTransaction> getWithdrawalTransaction();

    public List<BalanceTransaction> getHistoryPayment(String user_id);

}
