package com.interswitch.smartmoveserver.infrastructure;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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
@Component
public class RestTemplateResponseErrorHandler implements ResponseErrorHandler {
    protected final Log logger = LogFactory.getLog(getClass());

    @Override
    public boolean hasError(ClientHttpResponse httpResponse)
      throws IOException {
        logger.info(httpResponse.getBody());
        if(httpResponse.getStatusCode().value() == HttpStatus.NOT_FOUND.value()) return false;
        return (
          httpResponse.getStatusCode().series() == CLIENT_ERROR
          || httpResponse.getStatusCode().series() == SERVER_ERROR);
    }
 
    @Override
    public void handleError(ClientHttpResponse httpResponse) throws IOException {
        throw new ResponseStatusException(httpResponse.getStatusCode(), StreamUtils.copyToString(httpResponse.getBody(), Charset.defaultCharset()));
    }
}