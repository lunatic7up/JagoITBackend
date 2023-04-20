package com.jagoit.JagoITBE.service;

import com.jagoit.JagoITBE.model.Chat;
import com.jagoit.JagoITBE.model.Message;

import java.util.List;

public interface ChatService {

    Chat createChat(String first_user, String second_user);

    Message sendMessage(String chat_id, String sender, String message, String type, String file_name, String file_ext);

    List<Message> getMessagesByChatId(String chat_id);

    Chat getDetailChat(String chat_id);

}
