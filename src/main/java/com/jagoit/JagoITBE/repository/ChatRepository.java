package com.jagoit.JagoITBE.repository;

import com.jagoit.JagoITBE.model.Chat;
import com.jagoit.JagoITBE.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatRepository extends JpaRepository<Chat, String> {

    @Query(value = "SELECT * FROM CHATS WHERE id=?1", nativeQuery = true)
    Chat getDetailChat(String chat_id);

}
