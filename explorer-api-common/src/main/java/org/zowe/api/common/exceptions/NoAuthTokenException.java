package org.zowe.api.common.exceptions;

import org.springframework.http.HttpStatus;

public class NoAuthTokenException extends ZoweApiRestException{

    /**
     * 
     */
    private static final long serialVersionUID = 2927394757111102057L;

    public NoAuthTokenException() {
        super(HttpStatus.UNAUTHORIZED, "No 'apimlAuthenticationToken' provided");
    }

}
