package com.example.cookdi.config;

import com.example.cookdi.chat.ioSocketConnector.IOSocketConnector;

public class Config {
    
//    public static String BASE_URL = "http://localhost:3000";

    //Using this base url for emulator using
    public static String BASE_URL = "https://cookdi.herokuapp.com";
    public static int LOG_ROUND_SALT = 12;
    public static IOSocketConnector IOSocketChatConnector;
    public static int stepID = 1;
}
