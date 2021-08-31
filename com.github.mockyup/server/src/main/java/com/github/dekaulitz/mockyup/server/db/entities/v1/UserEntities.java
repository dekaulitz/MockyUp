package com.github.dekaulitz.mockyup.server.db.entities.v1;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.std.DateDeserializers.DateDeserializer;
import com.github.dekaulitz.mockyup.server.model.constants.DateTimeConstants;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.FieldNameConstants;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

@Data
@ToString(callSuper = true, onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true) //To maintain default values when using builder
@JsonIgnoreProperties(ignoreUnknown = true)
@Document
@FieldNameConstants
public class UserEntities implements Serializable {

  @Id
  private String id;
  @NotNull
  private String username;
  @NotNull
  private String email;
  @NotNull
  private String password;
  @NotNull
  private List<String> accessList;

  @DateTimeFormat(pattern = DateTimeConstants.DATE_TIME_FORMAT)
  @JsonDeserialize(using = DateDeserializer.class)
  private Date updatedDate = new Date();


  @JsonIgnore
  public String getPassword() {
    return password;
  }
}
