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

import java.text.MessageFormat;

public class ZoweApiException extends RuntimeException {

    /**
     *
     */
    private static final long serialVersionUID = -375759766836147272L;
    protected String message;

    public ZoweApiException(String message, Object... messageArguments) {
        this.message = MessageFormat.format(message, messageArguments);
    }

    @Override
    public String getMessage() {
        return message;
    }

}
