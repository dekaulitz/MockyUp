package com.github.dekaulitz.mockyup.errorhandlers;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ErrorModel {
    private HttpStatus httpCode;
    private String errorCode;
    private String errorMessage;
}
