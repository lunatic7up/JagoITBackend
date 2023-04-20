package com.jagoit.JagoITBE.controller;

import com.jagoit.JagoITBE.helper.Utils;
import com.jagoit.JagoITBE.model.*;
import com.jagoit.JagoITBE.service.ChatService;
import com.jagoit.JagoITBE.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class ChatController {

    private static final Logger logger = LogManager.getLogger(ChatController.class);

//    @Autowired
//    private SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    private ChatService chatService;

    @Autowired
    private UserService userService;

//    @MessageMapping("/createChat")
//    public String createChat(String requestBody) throws JSONException {
//        MDC.put("processID", Utils.generate_process_id());
//
//        logger.debug(">> createChat");
//        try{
//            JSONObject object = new JSONObject(requestBody);
//            String first_user = object.getString("first_user");
//            String second_user = object.getString("second_user");
//            Chat chat = chatService.createChat(first_user, second_user);
//            if(chat != null){
//                return chat.getChat_id();
//            }
//        }catch (Exception e){
//            logger.debug(e);
//        }
//
//        return "";
//    }

    @MessageMapping("/sendMessage")
    @SendTo("/start/resume")
    public List<Message> sendMessage(String requestBody) throws JSONException {
        MDC.put("processID", Utils.generate_process_id());
        String chat_id = "";
        logger.debug(">> sendMessage");
        try{
            JSONObject object = new JSONObject(requestBody);
            logger.debug("object: " + object);
            chat_id = object.getString("chat_id");
            String sender = object.getString("sender");
            String content = object.getString("message");
            String type = object.getString("type");
            String file_name = object.has("file_name") ? object.getString("file_name") : "";
            String file_type = object.has("file_type") ? object.getString("file_type") : "";
            Message message = chatService.sendMessage(chat_id, sender, content, type, file_name, file_type);
            if(message != null){
                logger.debug("berhasil sent");

            }else{
                logger.debug("gagal sent");
            }
        }catch (Exception e){
            logger.debug(e);
        }
        List<Message> output = chatService.getMessagesByChatId(chat_id);
        logger.debug("size: " + output.size());
        return output;
    }

    @MessageMapping("/getListMessage")
    @SendTo("/start/initial")
    public Map<String, Object> getListMessage(String chat_id){
        MDC.put("processID", Utils.generate_process_id());
        logger.debug(">> getListMessage");
        Map<String, Object> output_schema = new HashMap<>();
        try{
            Chat chat = chatService.getDetailChat(chat_id);
            String first_user_id = chat.getFirst_user_id();
            String second_user_id = chat.getSecond_user_id();
            User firstUser = userService.getUserData(first_user_id);
            logger.debug("user: " + firstUser);
            Consultant secondUser = userService.getConsultantData(second_user_id);
            List<Message> output = chatService.getMessagesByChatId(chat_id);
            output_schema.put("lists", output);
            output_schema.put("firstUserId", first_user_id);
            output_schema.put("firstUsername", firstUser.getUser_name());
            output_schema.put("firstProfilePicture", firstUser.getProfile_picture());
            output_schema.put("secondUserId", second_user_id);
            output_schema.put("secondUsername", secondUser.getUser_name());
            output_schema.put("secondProfilePicture", secondUser.getProfile_picture());
            return output_schema;
        }catch (Exception e){
            logger.debug(e);
        }


        return new HashMap<>();
    }

//    @MessageMapping("/sendMessage")
//    public String sendMessage(String requestBody) throws JSONException {
//        MDC.put("processID", Utils.generate_process_id());
//
//        logger.debug(">> createChat");
//        try{
//            JSONObject object = new JSONObject(requestBody);
//            String chat_id = object.getString("chat_id");
//            String sender = object.getString("sender");
//            String content = object.getString("message");
//            Message message = chatService.sendMessage(chat_id, sender, content);
//            if(message != null){
//                return message.getId();
//            }
//        }catch (Exception e){
//            logger.debug(e);
//        }
//
//        return "";
//    }

//    @PostMapping("/getChats")
//    public List<Message> getChats(@RequestBody String user){
//        return chatService.findByParticipant(user);
//    }

//    @MessageMapping("/chat/{to}") //to = nome canale
//    public void sendMessage(@DestinationVariable String to , Message message) {
//        System.out.println("handling send message: " + message + " to: " + to);
//        message.setId(new UUIDHexGenerator().generate(null, null).toString());
//        message.setChat_id(createAndOrGetChat(to));
////        message.setTimestamp(generateTimeStamp());
//        message = chatService.sendMessage(message);
//        simpMessagingTemplate.convertAndSend("/topic/messages/" + to, message);
//    }
//
//    @PostMapping("/getChats")
//    public List<Chat> getChats(@RequestBody String user){
//        return chatService.findByParticipant(user);
//    }
//
//    //returns an empty list if the chat doesn't exist
//    @PostMapping("/getMessages")
//    public List<Message> getMessages(@RequestBody String chat) {
//        Chat chats = chatService.findByName(chat);
//
//        if(chats != null) {
//            return chatService.findAllByChat(chats.getChat_id());
//        }
//        else{
//            return new ArrayList<Message>();
//        }
//    }
//
//    //finds the chat whose name is the parameter, if it doesn't exist it gets created, the ID gets returned either way
//    private String createAndOrGetChat(String name) {
//        Chat chat = chatService.findByName(name);
//
//        if (chat != null) {
//            return chat.getChat_id();
//        }else {
//            Chat chatData = chatService.createChat(name);
//            return chatData.getChat_id();
//        }
//    }
//
//    private String generateTimeStamp() {
//        Instant i = Instant.now();
//        String date = i.toString();
//        System.out.println("Source: " + i.toString());
//        int endRange = date.indexOf('T');
//        date = date.substring(0, endRange);
//        date = date.replace('-', '/');
//        System.out.println("Date extracted: " + date);
//        String time = Integer.toString(i.atZone(ZoneOffset.UTC).getHour() + 1);
//        time += ":";
//
//        int minutes = i.atZone(ZoneOffset.UTC).getMinute();
//        if (minutes > 9) {
//            time += Integer.toString(minutes);
//        } else {
//            time += "0" + Integer.toString(minutes);
//        }
//
//        System.out.println("Time extracted: " + time);
//        String timeStamp = date + "-" + time;
//        return timeStamp;
//    }
}
