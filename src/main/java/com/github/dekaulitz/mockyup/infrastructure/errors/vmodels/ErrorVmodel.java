package com.github.dekaulitz.mockyup.infrastructure.errors.vmodels;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ErrorVmodel {
    private HttpStatus httpCode;
    private String errorCode;
    private String errorMessage;
}
