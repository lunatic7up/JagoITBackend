package com.jagoit.JagoITBE.helper;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Utils {

    protected static final Logger logger = LogManager.getLogger(Utils.class);

    public static String generate_process_id() {
        UUID id = UUID.randomUUID();
        return id.toString().substring(0,8).toUpperCase();
    }

    public static String trimUrlVideo(String url){
        String temp = url.substring(url.indexOf("=") + 1);
        if(temp.contains("&")){
            int index = temp.indexOf("&");
            temp = temp.substring(0, index);
            return temp;
        }else{
            return temp;
        }
    }

    public static JSONObject generateInputRESTSimple(Map Param) throws JSONException {
        JSONObject obj = new JSONObject();
        for (Object key : Param.keySet()) {
            if(Param.get(key) instanceof Map){
                obj.put(key.toString(), generateInputRESTSimple((Map)Param.get(key)));
            }
            else if(Param.get(key) instanceof List){
                obj.put(key.toString(), generateJsonArray((List)Param.get(key)));
            }
            else if(Param.get(key) instanceof String){
                obj.put(key.toString(), Param.get(key.toString()));
            }
            else if(Param.get(key) instanceof JSONArray){
                obj.put(key.toString(), Param.get(key.toString()));
            }
            else if(Param.get(key) instanceof Integer){
                obj.put(key.toString(), Param.get(key.toString()));
            }
            else if(Param.get(key) instanceof Boolean){
                obj.put(key.toString(), Param.get(key.toString()));
            }
            else if(Param.get(key) instanceof Double){
                obj.put(key.toString(), Param.get(key.toString()));
            }
            else if(Param.get(key) instanceof Byte[]){
                obj.put(key.toString(), Param.get(key.toString()));
            }
            else if(Param.get(key) == null){
                obj.put(key.toString(), JSONObject.NULL);
            }
            else{
                obj.put(key.toString(), Param.get(key.toString()));
//                logger.debug("key --> " + key);
//                logger.debug(key + " " + Param.get(key).getClass());
            }
        }
        return obj;
    }

    public static JSONArray generateJsonArray(List listJson) throws JSONException{
        JSONArray arr = new JSONArray();
        for(Object keylist : listJson){
            if(keylist instanceof Map){
                arr.put(generateInputRESTSimple((Map) keylist));
            }
            else if(keylist instanceof List){
                arr.put(generateJsonArray((List) keylist));
            }
            else if(keylist instanceof String){
                arr.put(keylist.toString());
            }
            else if(keylist instanceof Timestamp){
                arr.put(keylist.toString());
            }
        }
        return arr;
    }



}
