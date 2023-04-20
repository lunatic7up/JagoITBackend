package com.jagoit.JagoITBE.repository;

import com.jagoit.JagoITBE.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepisotory extends JpaRepository<Message, String> {

    @Query(value = "SELECT * FROM MESSAGE WHERE CHAT_ID=?1 ORDER BY TIMESTAMP ASC", nativeQuery = true)
    List<Message> getMessagesByChatId(String id);
}
