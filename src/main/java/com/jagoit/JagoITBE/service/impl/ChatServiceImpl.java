package com.jagoit.JagoITBE.service.impl;

import com.jagoit.JagoITBE.model.Chat;
import com.jagoit.JagoITBE.model.Message;
import com.jagoit.JagoITBE.repository.ChatRepository;
import com.jagoit.JagoITBE.repository.MessageRepisotory;
import com.jagoit.JagoITBE.service.ChatService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.tomcat.util.codec.binary.Base64;
import org.hibernate.id.UUIDHexGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Service
public class ChatServiceImpl implements ChatService {

    private static final Logger logger = LogManager.getLogger(ChatServiceImpl.class);

    @Autowired
    private ChatRepository chatRepository;

    @Autowired
    private MessageRepisotory messageRepisotory;

    @Override
    public Chat createChat(String first_user, String second_user) {
        logger.debug(">> createChat");
        Chat chat = new Chat();
        chat.setChat_id(new UUIDHexGenerator().generate(null, null).toString());
        chat.setFirst_user_id(first_user);
        chat.setSecond_user_id(second_user);
        chat.setCreated_date(new Timestamp(System.currentTimeMillis()));
        return chatRepository.save(chat);
    }

    @Override
    public Message sendMessage(String chat_id, String sender, String message, String type, String file_name, String file_ext) {
        logger.debug(">> sendMessage");
        Message msg = new Message();
        msg.setId(new UUIDHexGenerator().generate(null, null).toString());
        msg.setChat_id(chat_id);
        msg.setSender(sender);
        msg.setContent_type(type);
        if(type.equals("text")){
            msg.setContent_text(message);
        }else{
            byte[] fileBytes = Base64.decodeBase64(message);
            msg.setContent_file(fileBytes);
            msg.setContent_file_type(file_ext);
            msg.setContent_file_name(file_name);
        }
        msg.setTimestamp(new Timestamp(System.currentTimeMillis()));
        return messageRepisotory.save(msg);
    }

    @Override
    public List<Message> getMessagesByChatId(String chat_id) {
        logger.debug(">> getMessagesByChatId");
        return messageRepisotory.getMessagesByChatId(chat_id);
    }

    @Override
    public Chat getDetailChat(String chat_id) {
        logger.debug(">> getDetailChat");
        return chatRepository.getDetailChat(chat_id);
    }

}
