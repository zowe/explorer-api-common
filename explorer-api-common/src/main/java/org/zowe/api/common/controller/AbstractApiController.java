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

import io.swagger.annotations.ApiOperation; 
import io.swagger.annotations.ApiResponse;  
import io.swagger.annotations.ApiResponses; 

import org.springframework.web.bind.annotation.GetMapping;  
import org.zowe.api.common.model.Username;  
import org.zowe.api.common.utils.ZosUtils;  

public abstract class AbstractApiController {   

    @GetMapping(value = "username", produces = { "application/json" })  
    @ApiOperation(value = "Get current userid", nickname = "getCurrentUserName", notes = "This API returns the caller's current TSO userid.", response = Username.class, tags = {   
            "System APIs", })   
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Ok", response = Username.class) })  
    public Username getCurrentUserName() {  
        return new Username(ZosUtils.getUsername());    
    }   
}