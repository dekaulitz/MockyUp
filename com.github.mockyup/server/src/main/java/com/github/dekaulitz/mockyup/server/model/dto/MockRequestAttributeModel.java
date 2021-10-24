package com.github.dekaulitz.mockyup.server.model.dto;

import com.github.dekaulitz.mockyup.server.model.common.BaseModel;
import java.util.Map;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.apache.http.entity.ContentType;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class MockRequestAttributeModel extends BaseModel {

  @NotBlank
  private String contractId;
  @NotBlank
  private String requestPath;
  private Map<String, String> headers;
  private Map<String, String[]> parameters;
  private String body;
  private String requestMethod;
  private String contentType = ContentType.APPLICATION_JSON.toString();
}
