/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright IBM Corporation 2019,2020
 */
package org.zowe.api.common.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import org.springframework.web.bind.annotation.GetMapping;
import org.zowe.api.common.model.Username;
import org.zowe.api.common.utils.ZosUtils;

public abstract class AbstractApiController {

    @GetMapping(value = "username", produces = {"application/json"})
    @Operation(summary = "Get current userid", description = "This API returns the caller's current TSO userid.", tags = {
            "System APIs", "getCurrentUserName"})
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Ok")})
    public Username getCurrentUserName() {
        return new Username(ZosUtils.getUsername());
    }
}