package com.jagoit.JagoITBE.model;


import javax.persistence.*;
import java.sql.Timestamp;


@Entity
@Table(name = "TXN_BALANCE")
public class BalanceTransaction {

    @Id
    @Column(name = "ID")
    private String id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "TXN_DETAIL_CODE")
    private TransactionDetail transaction_detail;

    @Column(name = "USER_ID")
    private String user_id;

    @Column(name = "STATUS")
    private String status;

    @Column(name = "NOMINAL")
    private Integer nominal;

    @Column(name = "CREATED_DATE")
    private Timestamp created_date;

    @Column(name = "NOTES")
    private String notes;

    @Column(name = "PROOF_PICTURE")
    private byte[] proof_picture;

    @Column(name = "UPDATED_DATE")
    private Timestamp updated_date;

    @Column(name = "USER_ID_ADMIN")
    private String user_id_admin;

    public String getUser_id_admin() {
        return user_id_admin;
    }

    public void setUser_id_admin(String user_id_admin) {
        this.user_id_admin = user_id_admin;
    }

    public Timestamp getUpdated_date() {
        return updated_date;
    }

    public void setUpdated_date(Timestamp updated_date) {
        this.updated_date = updated_date;
    }

    public byte[] getProof_picture() {
        return proof_picture;
    }

    public void setProof_picture(byte[] proof_picture) {
        this.proof_picture = proof_picture;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public TransactionDetail getTransaction_detail() {
        return transaction_detail;
    }

    public void setTransaction_detail(TransactionDetail transaction_detail) {
        this.transaction_detail = transaction_detail;
    }

    //    public String getTransaction_detail() {
//        return transaction_detail;
//    }
//
//    public void setTransaction_detail(String transaction_detail) {
//        this.transaction_detail = transaction_detail;
//    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getNominal() {
        return nominal;
    }

    public void setNominal(Integer nominal) {
        this.nominal = nominal;
    }

    public Timestamp getCreated_date() {
        return created_date;
    }

    public void setCreated_date(Timestamp created_date) {
        this.created_date = created_date;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
