/**
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright IBM Corporation 2018, 2019
 */

package org.zowe.api.common.exceptions;

import org.zowe.api.common.errors.ApiError;

public class ZoweApiErrorException extends ZoweApiRestException {

    /**
     * 
     */
    private static final long serialVersionUID = 7920567730386688530L;

    // TODO - primarily used for tests, not sure of any dev use at the moment -
    // refactor out in test sub folder?
    public ZoweApiErrorException(ApiError apiError) {
        super(apiError.getStatus(), apiError.getMessage());
    }

}
