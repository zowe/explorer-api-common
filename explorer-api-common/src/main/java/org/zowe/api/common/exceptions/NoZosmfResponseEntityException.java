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

public class NoZosmfResponseEntityException extends ZoweApiRestException {

    /**
     *
     */
    private static final long serialVersionUID = 2568539995597255984L;

    public NoZosmfResponseEntityException(HttpStatus status, String path) {
        super(status, "There was no response from z/OSMF for the request ''{0}''", path);
    }

}