package com.jagoit.JagoITBE.service.impl;

import com.jagoit.JagoITBE.helper.HelperConstant;
import com.jagoit.JagoITBE.model.BalanceTransaction;
import com.jagoit.JagoITBE.model.TransactionDetail;
import com.jagoit.JagoITBE.repository.BalanceTransactionRepository;
import com.jagoit.JagoITBE.service.BalanceService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.tomcat.util.codec.binary.Base64;
import org.hibernate.id.UUIDHexGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Tuple;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class BalanceServiceImpl implements BalanceService {

    private static final Logger logger = LogManager.getLogger(BalanceServiceImpl.class);

    @Autowired
    private BalanceTransactionRepository balanceTransactionRepository;
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public BalanceTransaction Withdrawal(Map<String, Object> paramMap) {
        logger.debug(">> Withdrawal");
        BalanceTransaction balanceTransaction = new BalanceTransaction();
        String id = "JI-";
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMyyyy");
        String dates = dateFormat.format(date);
        id = id + dates + "-" + new UUIDHexGenerator().generate(null, null).toString();
        balanceTransaction.setId(id);
        balanceTransaction.setUser_id(paramMap.get("user_id").toString());
        balanceTransaction.setNominal((Integer) paramMap.get("nominal"));
        if(paramMap.containsKey("notes")){
            balanceTransaction.setNotes(paramMap.get("notes").toString());
        }


        TransactionDetail transactionDetail = entityManager.getReference(TransactionDetail.class, paramMap.get("activity"));
        if(transactionDetail != null){
//            balanceTransaction.setTransaction_detail(paramMap.get("activity").toString());
            balanceTransaction.setTransaction_detail(transactionDetail);
        }else{
            balanceTransaction.setTransaction_detail(null);
        }
        balanceTransaction.setStatus(HelperConstant.STATUS_REQUESTED);
        balanceTransaction.setCreated_date(new Timestamp(System.currentTimeMillis()));

        return this.balanceTransactionRepository.save(balanceTransaction);
    }

    @Override
    public BalanceTransaction Topup(Map<String, Object> paramMap) {
        logger.debug(">> Topup");
        BalanceTransaction balanceTransaction = new BalanceTransaction();

        String id = "JI-";
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMyyyy");
        String dates = dateFormat.format(date);
        id = id + dates + "-" + new UUIDHexGenerator().generate(null, null).toString();
        balanceTransaction.setId(id);
        balanceTransaction.setUser_id(paramMap.get("user_id").toString());
        balanceTransaction.setNominal(Integer.parseInt(paramMap.get("nominal").toString()));
        if(paramMap.containsKey("notes")){
            balanceTransaction.setNotes(paramMap.get("notes").toString());
        }


        TransactionDetail transactionDetail = entityManager.getReference(TransactionDetail.class, paramMap.get("activity"));
        if(transactionDetail != null){
            balanceTransaction.setTransaction_detail(transactionDetail);
        }else{
            balanceTransaction.setTransaction_detail(null);
        }
        balanceTransaction.setStatus(HelperConstant.STATUS_REQUESTED);
        balanceTransaction.setCreated_date(new Timestamp(System.currentTimeMillis()));
        byte[] imageBytes = Base64.decodeBase64(paramMap.get("topup_picture").toString());
        balanceTransaction.setProof_picture(imageBytes);

        return this.balanceTransactionRepository.save(balanceTransaction);
    }

    @Override
    public BalanceTransaction Payment(Map<String, Object> paramMap) {
        logger.debug(">> Payment");
        BalanceTransaction balanceTransaction = new BalanceTransaction();
        String id = "JI-";
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMyyyy");
        String dates = dateFormat.format(date);
        id = id + dates + "-" + new UUIDHexGenerator().generate(null, null).toString();
        balanceTransaction.setId(id);
        balanceTransaction.setUser_id(paramMap.get("user_id").toString());
        balanceTransaction.setNominal((Integer) paramMap.get("nominal"));
        
        TransactionDetail transactionDetail = entityManager.getReference(TransactionDetail.class, paramMap.get("activity"));
        if(transactionDetail != null){
//            balanceTransaction.setTransaction_detail(paramMap.get("activity").toString());
            balanceTransaction.setTransaction_detail(transactionDetail);
        }else{
            balanceTransaction.setTransaction_detail(null);
        }
        balanceTransaction.setStatus(HelperConstant.STATUS_DONE);
        balanceTransaction.setCreated_date(new Timestamp(System.currentTimeMillis()));

        return this.balanceTransactionRepository.save(balanceTransaction);
    }

    @Override
    public BalanceTransaction updateTransaction(String status, String transaction_id, String user_id_admin) {
        logger.debug(">> updateTransaction");
        Optional<BalanceTransaction> balanceTransaction = balanceTransactionRepository.findById(transaction_id);
        balanceTransaction.get().setStatus(status);
        balanceTransaction.get().setUpdated_date(new Timestamp(System.currentTimeMillis()));
        balanceTransaction.get().setUser_id_admin(user_id_admin);
        return balanceTransactionRepository.save(balanceTransaction.get());
    }

    @Override
    public List<BalanceTransaction> getTopupTransaction() {
        logger.debug(">> getTopupTransaction");
        return balanceTransactionRepository.getTopupTransaction();
    }

    @Override
    public List<BalanceTransaction> getWithdrawalTransaction() {
        logger.debug(">> getWithdrawalTransaction");
        return balanceTransactionRepository.getWithdrawalTransaction();
    }

    @Override
    public List<BalanceTransaction> getHistoryPayment(String user_id) {
        logger.debug(">> getHistoryPayment");
        return balanceTransactionRepository.getHistoryPayment(user_id);
    }

}
