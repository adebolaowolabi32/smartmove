package com.interswitch.smartmoveserver.infrastructure;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.charset.Charset;

import static org.springframework.http.HttpStatus.Series.CLIENT_ERROR;
import static org.springframework.http.HttpStatus.Series.SERVER_ERROR;

/**
 * @author adebola.owolabi
 */
@Slf4j
@Component
//TODO:: Remove passport limitation of error handling
public class RestTemplateResponseErrorHandler implements ResponseErrorHandler {
    private final String X_APPLICATION_CONTEXT = "X-Application-Context";

    @Override
    public boolean hasError(ClientHttpResponse httpResponse)
      throws IOException {
        log.info("Remote Server Error:", httpResponse);
        HttpHeaders httpHeaders = httpResponse.getHeaders();
        if(httpHeaders.containsKey(X_APPLICATION_CONTEXT)){
            String applicationContextHeader = httpHeaders.get(X_APPLICATION_CONTEXT) != null ?
                    httpHeaders.get(X_APPLICATION_CONTEXT).get(0) : "";
            if(applicationContextHeader.contains("passport"))
                if(httpResponse.getStatusCode().value() == HttpStatus.NOT_FOUND.value())
                    return false;
        }
        return (
          httpResponse.getStatusCode().series() == CLIENT_ERROR
          || httpResponse.getStatusCode().series() == SERVER_ERROR);
    }
 
    @Override
    public void handleError(ClientHttpResponse httpResponse) throws IOException {
        throw new ResponseStatusException(httpResponse.getStatusCode(), StreamUtils.copyToString(httpResponse.getBody(), Charset.defaultCharset()));
    }
}