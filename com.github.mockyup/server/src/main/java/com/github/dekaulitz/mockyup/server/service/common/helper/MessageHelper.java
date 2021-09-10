package com.github.dekaulitz.mockyup.server.service.common.helper;

import com.github.dekaulitz.mockyup.server.model.dto.ErrorMessageModel;
import com.github.dekaulitz.mockyup.server.service.common.helper.constants.ResponseCode;
import java.util.HashMap;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MessageHelper {

  private static MessageHelper instance;
  private HashMap<ResponseCode, ErrorMessageModel> messagesMap = new HashMap<>();

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

  public static ErrorMessageModel getMessage(ResponseCode responseCode) {
    MessageHelper messageHelper = getInstance();
    HashMap<ResponseCode, ErrorMessageModel> messagesMap = messageHelper.messagesMap;
    if (!messagesMap.containsKey(responseCode)) {
      throw new RuntimeException("message not found with" + responseCode);
    }
    return messagesMap.get(responseCode);
  }
}
