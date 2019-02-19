/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright IBM Corporation 2019
 */
package org.zowe.api.common.exceptions;

import org.springframework.http.HttpStatus;

import java.io.UnsupportedEncodingException;

public class HtmlEscapedZoweApiRestException extends ZoweApiRestException {

    /**
     * 
     */
    private static final long serialVersionUID = 1646047102943409655L;

    public HtmlEscapedZoweApiRestException(HttpStatus status, String message) throws UnsupportedEncodingException {
        super(status, htmlEncodeString(message));
    }

}
