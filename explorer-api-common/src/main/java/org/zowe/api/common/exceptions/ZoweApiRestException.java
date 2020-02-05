/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright IBM Corporation 2015, 2018
 */
package org.zowe.api.common.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.util.HtmlUtils;
import org.zowe.api.common.errors.ApiError;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

public class ZoweApiRestException extends ZoweApiException {

    /**
     *
     */
    private static final long serialVersionUID = -5142352192545942244L;
    private HttpStatus status;

    public ZoweApiRestException(HttpStatus status, String message, Object... messageArguments) {
        super(message, messageArguments);
        this.status = status;
    }
    
    public ZoweApiRestException(HttpStatus status, String message) {
        super(message, Object.class);
        this.status = status;
    }

    public ApiError getApiError() {
        return ApiError.builder().status(status).message(getMessage()).build();
    }

    protected static String htmlEncodeString(String string) throws UnsupportedEncodingException {
        return HtmlUtils.htmlEscape(URLDecoder.decode(string, StandardCharsets.UTF_8.name()));
    }

}
