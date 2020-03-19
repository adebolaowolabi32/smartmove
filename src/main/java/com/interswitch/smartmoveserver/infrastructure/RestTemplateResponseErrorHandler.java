package com.interswitch.smartmoveserver.infrastructure;

import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;

import static org.springframework.http.HttpStatus.Series.*;

/**
 * @author adebola.owolabi
 */
@Component
public class RestTemplateResponseErrorHandler implements ResponseErrorHandler {
 
    @Override
    public boolean hasError(ClientHttpResponse httpResponse)
      throws IOException {
        if(httpResponse.getStatusCode().value() == HttpStatus.NOT_FOUND.value()) return false;
        return (
          httpResponse.getStatusCode().series() == CLIENT_ERROR
          || httpResponse.getStatusCode().series() == SERVER_ERROR);
    }
 
    @Override
    public void handleError(ClientHttpResponse httpResponse) throws IOException {
        throw new ResponseStatusException(httpResponse.getStatusCode(), httpResponse.getBody().toString());
    }
}