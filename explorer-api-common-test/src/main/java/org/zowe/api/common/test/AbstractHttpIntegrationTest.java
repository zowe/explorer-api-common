/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright IBM Corporation 2018, 2020
 */
package org.zowe.api.common.test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static org.junit.Assert.assertEquals;

import org.apache.http.HttpStatus;
import org.junit.BeforeClass;
import org.zowe.api.common.errors.ApiError;
import org.zowe.api.common.exceptions.ZoweApiRestException;

import static org.hamcrest.CoreMatchers.equalTo;

public abstract class AbstractHttpIntegrationTest {

    private final static String SERVER_HOST = System.getProperty("server.host");
    private final static String SERVER_PORT = System.getProperty("server.port");

    protected final static String BASE_URL = "https://" + SERVER_HOST + ":" + SERVER_PORT + "/api/v1/";

    protected final static String USER = System.getProperty("server.username");
    private final static String PASSWORD = System.getProperty("server.password");
    protected final static String AUTH_TOKEN = getGatewayAuthToken();
    
    private static String getGatewayAuthToken() {        
        RestAssured.useRelaxedHTTPSValidation();
        Response response = RestAssured.given().contentType("application/json")
                .body("{\"username\":\"" + USER + "\",\"password\":\"" + PASSWORD + "\"}")
                .when().post(BASE_URL + "gateway/auth/login");
        assertEquals(response.getStatusCode(), HttpStatus.SC_NO_CONTENT);
        return response.getCookie("apimlAuthenticationToken");
    }
    
    @BeforeClass
    public static void setupRestAssured() {
        RestAssured.baseURI = BASE_URL;
    }

    protected void verifyExceptionReturn(ZoweApiRestException expected, Response response) {
        verifyExceptionReturn(expected.getApiError(), response);
    }

    protected void verifyExceptionReturn(ApiError expectedError, Response response) {
        response.then().statusCode(expectedError.getStatus().value()).contentType(ContentType.JSON)
            .body("status", equalTo(expectedError.getStatus().name()))
            .body("message", equalTo(expectedError.getMessage()));
    }
}
