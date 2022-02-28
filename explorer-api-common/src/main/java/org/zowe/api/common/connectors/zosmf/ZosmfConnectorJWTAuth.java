/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright IBM Corporation 2020
 */
package org.zowe.api.common.connectors.zosmf;

import org.apache.http.Header;
import org.apache.http.message.BasicHeader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zowe.api.common.exceptions.NoAuthTokenException;

import javax.servlet.http.HttpServletRequest;

import java.util.Arrays;
import java.util.Optional;

@Service
public class ZosmfConnectorJWTAuth extends ZosmfConnector {

    @Autowired
    private HttpServletRequest request;

    @Autowired
    public ZosmfConnectorJWTAuth(ConnectionProperties properties) {
        super(properties);
    }

    public Header getAuthHeader() {
        // If user is passing jwt as a cookie
        String cookieHeader = request.getHeader("cookie");
        if (cookieHeader != null && !cookieHeader.isEmpty()) {
            String[] cookies = cookieHeader.split(";");
            Optional<String> authTokenCookie = Arrays.stream(cookies)
                    .filter(c -> c.contains("apimlAuthenticationToken")).findFirst();
            if (authTokenCookie.isPresent()) {
                return new BasicHeader("Authorization", "Bearer " + authTokenCookie.get().split("=")[1]);
            }
        } else {
            // If user is passing jwt in Authorization header
            String header = request.getHeader("authorization");
            if (header != null && !header.isEmpty()) {
                return new BasicHeader("Authorization", header);
            }
        }
        throw new NoAuthTokenException();
    }

}
