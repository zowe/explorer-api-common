/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright IBM Corporation 2018, 2018
 */
package org.zowe.api.common.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;

public class InvalidFieldException extends ZoweApiRestException {

    /**
     * 
     */
    private static final long serialVersionUID = -8193997010405097177L;

    public InvalidFieldException(FieldError error) {
        super(HttpStatus.BAD_REQUEST, "Invalid field {0} supplied to object {1} - {2}", error.getField(),
                error.getObjectName(), error.getDefaultMessage());
    }
}
