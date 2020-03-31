package org.zowe.api.common.connectors.zosmf;

import java.util.Arrays;
import java.util.Optional;

import org.apache.http.Header;
import org.apache.http.message.BasicHeader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zowe.api.common.exceptions.NoAuthTokenException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ZosmfConnectorV2 extends ZosmfConnector {
    
    @Autowired
    public ZosmfConnectorV2(ConnectionProperties properties) {
        super(properties);
    }
   
    protected Header getAuthHeader() {
    	return getJwtAuthHeader();
    }
    
    private Header getJwtAuthHeader() {
        // If user is passing jwt as a cookie
        String cookieHeader = request.getHeader("cookie");
        if (cookieHeader != null && !cookieHeader.isEmpty()) {
            String[] cookies = cookieHeader.split(";");
            Optional<String> authTokenCookie = Arrays.stream(cookies).filter(c -> c.contains("apimlAuthenticationToken")).findFirst();
            if(authTokenCookie.isPresent()) {
                return new BasicHeader("Authorization", "Bearer " + authTokenCookie.get().split("=")[1]);
            }
        } else {
            // If user is passing jwt in Authorization header 
            String header = request.getHeader("authorization");
            if(header != null && !header.isEmpty()) {
                return new BasicHeader("Authorization", "Bearer " + header);
            }
        }
        throw new NoAuthTokenException();
    }
}
