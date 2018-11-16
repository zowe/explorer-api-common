/**
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright IBM Corporation 2018
 */

package org.zowe.api.common.connectors.zosmf.exceptions;

import org.zowe.api.common.exceptions.ZoweApiException;

public class ZosmfConnectionException extends ZoweApiException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4271353879744471811L;

	public ZosmfConnectionException(Throwable cause) {
		super("Failed to connect to z/OS MF with exception {0}", cause.getMessage());
	}

}
