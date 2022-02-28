/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright IBM Corporation 2020
 */
package org.zowe.api.common.exceptions;

import org.springframework.http.HttpStatus;

public class InvalidAuthTokenException extends ZoweApiRestException {

    /**
     * 
     */
    private static final long serialVersionUID = 7850079578803386211L;

    public InvalidAuthTokenException() {
        super(HttpStatus.UNAUTHORIZED, "Provided 'apimlAuthenticationToken' is not valid");
    }
}