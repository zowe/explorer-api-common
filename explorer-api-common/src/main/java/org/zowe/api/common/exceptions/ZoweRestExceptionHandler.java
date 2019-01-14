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

import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.zowe.api.common.errors.ApiError;

@Slf4j
@ControllerAdvice
public class ZoweRestExceptionHandler extends ResponseEntityExceptionHandler {

    // TODO - unit test

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(final MethodArgumentNotValidException e,
            final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
        log.error("handleMethodArgumentNotValid", e);
        return handleBindException(e, e.getBindingResult(), headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleBindException(final BindException e, final HttpHeaders headers,
            final HttpStatus status, final WebRequest request) {
        log.error("handleMethodArgumentNotValid", e);
        return handleBindException(e, e, headers, status, request);
    }

    private ResponseEntity<Object> handleBindException(final Exception ex, final BindingResult result,
            final HttpHeaders headers, final HttpStatus status, final WebRequest request) {

        if (result.getFieldErrorCount() > 0) {
            return handleZoweException(new InvalidFieldException(result.getFieldError()), request);
        } else if (result.getGlobalErrorCount() > 0) {
            return handleZoweException(new InvalidObjectException(result.getGlobalError()), request);
        }
        throw new ServerErrorException(ex);
    }

    @ExceptionHandler({ ZoweApiRestException.class })
    public ResponseEntity<Object> handleZoweException(final ZoweApiRestException ex, final WebRequest request) {
        ApiError apiError = ex.getApiError();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        return handleExceptionInternal(ex, apiError, headers, apiError.getStatus(), request);
    }
}
