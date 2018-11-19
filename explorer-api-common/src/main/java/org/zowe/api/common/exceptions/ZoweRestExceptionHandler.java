/**
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright IBM Corporation 2018
 */

package org.zowe.api.common.exceptions;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.zowe.api.common.errors.ApiError;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice
public class ZoweRestExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler({ ZoweApiRestException.class })
	public ResponseEntity<Object> handleAll(final ZoweApiRestException ex) {
		ApiError apiError = ex.getApiError();
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
		return new ResponseEntity<Object>(apiError, headers, apiError.getStatus());
	}
}
