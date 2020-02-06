package org.zowe.api.common.exceptions;

import org.springframework.http.HttpStatus;

public class InvalidAuthTokenException extends ZoweApiRestException{

    /**
     * 
     */
    private static final long serialVersionUID = 7850079578803386211L;

    public InvalidAuthTokenException() {
        super(HttpStatus.UNAUTHORIZED, "Provided 'apimlAuthenticationToken' is not valid");
    }
}
