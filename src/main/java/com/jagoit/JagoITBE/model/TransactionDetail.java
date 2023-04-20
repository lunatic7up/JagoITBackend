package com.jagoit.JagoITBE.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;

@Entity
@Table(name = "MT_TXN")
public class TransactionDetail {

    @Id
    @Column(name="CODE")
    private String code;

    @Column(name="TXN_DETAIL_NAME")
    private String transaction_name;

    @Column(name="CREATED_DATE")
    private Timestamp created_date;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTransaction_name() {
        return transaction_name;
    }

    public void setTransaction_name(String transaction_name) {
        this.transaction_name = transaction_name;
    }

    public Timestamp getCreated_date() {
        return created_date;
    }

    public void setCreated_date(Timestamp created_date) {
        this.created_date = created_date;
    }
}
