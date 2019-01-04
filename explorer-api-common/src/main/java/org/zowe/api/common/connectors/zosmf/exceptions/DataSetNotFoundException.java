/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright IBM Corporation 2018, 2019
 */
package org.zowe.api.common.connectors.zosmf.exceptions;

import org.springframework.http.HttpStatus;
import org.zowe.api.common.exceptions.ZoweApiRestException;

public class DataSetNotFoundException extends ZoweApiRestException {

    /**
     * 
     */
    private static final long serialVersionUID = -1614747667957641995L;

    public DataSetNotFoundException(String dataSet) {
        super(HttpStatus.NOT_FOUND, "No data set with name ''{0}'' was found", dataSet);
    }

}
