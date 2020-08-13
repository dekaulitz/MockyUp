package com.github.dekaulitz.mockyup.domain.mocks.vmodels;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.MediaType;

import java.util.Map;

@NoArgsConstructor
@Getter
@Setter
public class DtoMockResponseVmodel {
    private int httpCode;
    private Object response;
    private Map<String, Object> headers;
    private Object $ref;
    private Map<String, Map<String, Object>> content;


    private String mediaType;

    public Object get$ref() {
        return $ref;
    }

    public void set$ref(Object $ref) {
        this.$ref = $ref;
    }

    public void setMediaType(String mediaType) {
        if (mediaType != null) {
            String[] acceptHeader = mediaType.split(",");
            if (acceptHeader.length > 1)
                mediaType = MediaType.APPLICATION_JSON_VALUE;
        }
        this.mediaType = mediaType;
    }
}
