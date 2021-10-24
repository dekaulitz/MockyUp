package com.github.dekaulitz.mockyup.server.db.entities;

import com.github.dekaulitz.mockyup.server.model.dto.MandatoryModel;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "logLogins")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@ToString(callSuper = true)
public class UserLogLoginEntity extends BaseMongoEntity implements Serializable {

  private String jti;
  private String userId;
  private MandatoryModel mandatoryModel;
}
