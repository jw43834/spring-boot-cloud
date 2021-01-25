package com.github.svcb.controller;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

import java.util.Map;

@Slf4j
@Controller
public class ServiceBController {

    @MessageMapping("/message")
    @SendTo("/topic/reply")
    public String processMessageFromClient(@Payload String message){
        String name = new Gson().fromJson(message, Map.class).get("name").toString();
        log.info("aaaaaa");
        return "Hello " + name;
    }

    @MessageExceptionHandler
    @SendToUser("/topic/errors")
    public String handleException(Throwable exception) {
        return exception.getMessage();
    }
}