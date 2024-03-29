package com.github.dekaulitz.mockyup.server.db.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.std.DateDeserializers.DateDeserializer;
import com.github.dekaulitz.mockyup.server.model.constants.DateTimeConstants;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.format.annotation.DateTimeFormat;

@Data
@FieldNameConstants
@Getter
@Setter
public class BaseMongoEntity implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  private String id;

  @JsonIgnore
  @Version
  private Long version;
  @Indexed
  private String updatedByUserId;
  @Indexed
  private String createdByUserId;

  @DateTimeFormat(pattern = DateTimeConstants.DATE_TIME_FORMAT)
  @JsonDeserialize(using = DateDeserializer.class)
  private Date updatedDate = new Date();
  @DateTimeFormat(pattern = DateTimeConstants.DATE_TIME_FORMAT)
  @JsonDeserialize(using = DateDeserializer.class)
  private Date createdDate;
}
