package com.github.dekaulitz.mockyup.server.configuration.mongodb;


import com.github.dekaulitz.mockyup.server.model.constants.DateTimeConstants;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;

@WritingConverter
public class LocalTimeToStringConverter implements Converter<LocalTime, String> {

  @Override
  public String convert(LocalTime localTime) {
    final DateTimeFormatter timeFormatter = DateTimeFormatter
        .ofPattern(DateTimeConstants.TIME_FORMAT);
    return localTime.format(timeFormatter);
  }
}
