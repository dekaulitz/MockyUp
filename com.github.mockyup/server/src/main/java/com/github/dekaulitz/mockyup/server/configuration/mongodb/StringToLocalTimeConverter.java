package com.github.dekaulitz.mockyup.server.configuration.mongodb;

import com.github.dekaulitz.mockyup.server.model.constants.DateTimeConstants;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;

@WritingConverter
public class StringToLocalTimeConverter implements Converter<String, LocalTime> {

  @Override
  public LocalTime convert(String s) {
    final DateTimeFormatter timeFormatter = DateTimeFormatter
        .ofPattern(DateTimeConstants.TIME_FORMAT);
    return LocalTime.parse(s, timeFormatter);
  }
}
