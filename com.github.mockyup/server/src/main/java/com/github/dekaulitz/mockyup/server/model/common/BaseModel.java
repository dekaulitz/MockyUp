package com.github.dekaulitz.mockyup.server.model.common;

import java.io.Serializable;

public class BaseModel implements Serializable {

  private static String removeToStringNullValues(String lombokToString) {
    //Pattern
    return lombokToString != null ? lombokToString
        .replaceAll("\\@.*?\\,", "")
        .replaceAll("(?<=(, |\\())[^\\s(]+?=null(?:, )?", "")
        .replaceFirst(", \\)$", ")") : null;
  }

  public String toStringSkipNulls() {
    return removeToStringNullValues(toString());
  }

}
