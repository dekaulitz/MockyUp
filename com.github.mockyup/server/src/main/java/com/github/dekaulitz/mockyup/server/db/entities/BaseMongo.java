package com.github.dekaulitz.mockyup.server.db.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.std.DateDeserializers.DateDeserializer;
import com.github.dekaulitz.mockyup.server.model.constants.DateTimeConstants;
import java.util.Date;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.FieldNameConstants;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.format.annotation.DateTimeFormat;

@Data
@FieldNameConstants
@ToString(callSuper = true)
public class BaseMongo {

  private static final long serialVersionUID = 1L;

  @Id
  protected String id;

  @JsonIgnore
  @Version
  private Long version;

  @DateTimeFormat(pattern = DateTimeConstants.DATE_TIME_FORMAT)
  @JsonDeserialize(using = DateDeserializer.class)
  private Date updatedDate = new Date();
}
