package com.github.dekaulitz.mockyup.filters;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


public class RequestWrapper extends HttpServletRequestWrapper {
    private final String body;

    public RequestWrapper(HttpServletRequest request) {
        super(request);
        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader bufferedReader = null;
        try {
            InputStream inputStream = request.getInputStream();
            if (inputStream != null) {
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                char[] charBuffer = new char[128];
                int bytesRead = -1;
                while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
                    stringBuilder.append(charBuffer, 0, bytesRead);
                }
            } else {
            }
        } catch (IOException ex) {
            try {
                throw ex;
            } catch (IOException e) {
                e.printStackTrace();
            }
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException ex) {
                    try {
                        throw ex;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        body = stringBuilder.toString();
    }

    public String getBody() {
        return this.body;
    }
}
