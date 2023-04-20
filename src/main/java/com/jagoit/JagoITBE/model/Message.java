package com.jagoit.JagoITBE.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;

@Entity
@Table(name = "MESSAGE")
public class Message {

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "chat_id")
    private String chat_id;

    @Column(name = "sender")
    private String sender;

    @Column(name = "timestamp")
    private Timestamp timestamp;

    @Column(name = "content_text")
    private String content_text;

    @Column(name = "content_type")
    private String content_type;

    @Column(name = "content_file")
    private byte[] content_file;

    @Column(name = "content_file_name")
    private String content_file_name;

    @Column(name = "content_file_type")
    private String content_file_type;

    public String getContent_file_name() {
        return content_file_name;
    }

    public void setContent_file_name(String content_file_name) {
        this.content_file_name = content_file_name;
    }

    public String getContent_file_type() {
        return content_file_type;
    }

    public void setContent_file_type(String content_file_type) {
        this.content_file_type = content_file_type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getChat_id() {
        return chat_id;
    }

    public void setChat_id(String chat_id) {
        this.chat_id = chat_id;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public String getContent_text() {
        return content_text;
    }

    public void setContent_text(String content_text) {
        this.content_text = content_text;
    }

    public String getContent_type() {
        return content_type;
    }

    public void setContent_type(String content_type) {
        this.content_type = content_type;
    }

    public byte[] getContent_file() {
        return content_file;
    }

    public void setContent_file(byte[] content_file) {
        this.content_file = content_file;
    }
}
