package com.github.dekaulitz.mockyup.server.service.common.helper;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.dekaulitz.mockyup.server.model.embeddable.Message;
import com.github.dekaulitz.mockyup.server.service.common.helper.constants.ResponseCode;
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
  private HashMap<ResponseCode, Message> messagesMap = new HashMap<>();

  private MessageHelper() {
  }

  public static MessageHelper loadInstance() {

    return instance;
  }

  private static MessageHelper getInstance() {
    if (instance == null) {
      return loadInstance();
    }
    return instance;
  }

  public static Message getMessage(ResponseCode responseCode) {
    MessageHelper messageHelper = getInstance();
    HashMap<ResponseCode, Message> messagesMap = messageHelper.messagesMap;
    if (!messagesMap.containsKey(responseCode)) {
      throw new RuntimeException("message not found with" + responseCode);
    }
    return messagesMap.get(responseCode);
  }
}
