/**
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright IBM Corporation 2015, 2018
 */

package org.zowe.api.common.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.validation.ObjectError;

public class InvalidObjectException extends ZoweApiRestException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4334200473302795434L;

	public InvalidObjectException(ObjectError error) {
		super(HttpStatus.BAD_REQUEST, "Invalid object {0} - {1}", error.getObjectName(), error.getDefaultMessage());
	}
}
