package com.github.dekaulitz.mockyup.server.service.common.helper;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.dekaulitz.mockyup.server.model.embeddable.Message;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

@Slf4j
public class MessageHelper {

  private static MessageHelper instance;
  private HashMap<MessageType, Message> messagesMap = new HashMap<>();

  private MessageHelper() {
  }

  public static MessageHelper loadInstance() {
    if (instance == null) {
      log.debug("load messages configuration");
      Resource resource = new ClassPathResource("messages.json");
      InputStreamReader isReader = null;
      try {
        isReader = new InputStreamReader(resource.getInputStream());
        BufferedReader reader = new BufferedReader(isReader);
        StringBuffer sb = new StringBuffer();
        String str;
        while ((str = reader.readLine()) != null) {
          sb.append(str);
        }
        ObjectMapper objectMapper = new ObjectMapper();
        instance = new MessageHelper();
        instance.messagesMap = objectMapper
            .readValue(sb.toString(), new TypeReference<HashMap<MessageType, Message>>() {
            });
      } catch (IOException e) {
        throw new RuntimeException(e);
      }

    }
    return instance;
  }

  private static MessageHelper getInstance() {
    if (instance == null) {
      return loadInstance();
    }
    return instance;
  }

  public static Message getMessage(MessageType messageType) {
    MessageHelper messageHelper = getInstance();
    HashMap<MessageType, Message> messagesMap = messageHelper.messagesMap;
    if (!messagesMap.containsKey(messageType)) {
      throw new RuntimeException("message not found with" + messageType);
    }
    return messagesMap.get(messageType);
  }
}
