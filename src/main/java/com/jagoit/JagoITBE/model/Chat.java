package com.jagoit.JagoITBE.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;
import java.util.List;

@Entity
@Table(name = "CHATS")
public class Chat {

    @Id
    @Column(name = "id")
    private String chat_id;

    @Column(name = "first_user_id")
    private String first_user_id;

    @Column(name = "second_user_id")
    private String second_user_id;

    @Column(name = "created_date")
    private Timestamp created_date;

    public String getFirst_user_id() {
        return first_user_id;
    }

    public void setFirst_user_id(String first_user_id) {
        this.first_user_id = first_user_id;
    }

    public String getSecond_user_id() {
        return second_user_id;
    }

    public void setSecond_user_id(String second_user_id) {
        this.second_user_id = second_user_id;
    }

    public Timestamp getCreated_date() {
        return created_date;
    }

    public void setCreated_date(Timestamp created_date) {
        this.created_date = created_date;
    }

    public String getChat_id() {
        return chat_id;
    }

    public void setChat_id(String chat_id) {
        this.chat_id = chat_id;
    }


}
