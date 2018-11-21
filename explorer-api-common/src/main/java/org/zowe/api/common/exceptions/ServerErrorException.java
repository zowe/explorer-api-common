/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package org.zowe.api.common.exceptions;

import org.springframework.http.HttpStatus;

public class ServerErrorException extends ZoweApiRestException {

    /**
     *
     */
    private static final long serialVersionUID = -3027740926911629836L;

    public ServerErrorException(Throwable e) {
        super(HttpStatus.INTERNAL_SERVER_ERROR, "Server error: {0}", e.getMessage());
    }
}
