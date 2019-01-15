/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright IBM Corporation 2018
 */
package org.zowe.api.common.exceptions;

import org.springframework.http.HttpStatus;

public class PreconditionFailedException extends ZoweApiRestException {

    /**
     *
     */
    private static final long serialVersionUID = 2568539995597255984L;

    public PreconditionFailedException(String fileName) {
        super(HttpStatus.PRECONDITION_FAILED, "Precondition (eg. ETag) failed trying to edit ''{0}''", fileName);
    }

}