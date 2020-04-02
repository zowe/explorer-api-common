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
import io.restassured.http.Header;
import io.restassured.response.Response;

import static org.junit.Assert.assertEquals;

import java.nio.charset.StandardCharsets;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpStatus;
import org.junit.BeforeClass;
import org.zowe.api.common.errors.ApiError;
import org.zowe.api.common.exceptions.ZoweApiRestException;

import static org.hamcrest.CoreMatchers.equalTo;

public abstract class AbstractHttpIntegrationTest {

    private final static String SERVER_HOST = System.getProperty("server.host");
    private final static String SERVER_PORT = System.getProperty("server.port");

    protected final static String BASE_URL = getBaseUrl();

    protected final static String USER = System.getProperty("server.username");
    private final static String PASSWORD = System.getProperty("server.password");
    protected final static Header AUTH_HEADER = getAuthHeader();
    
    private static String getBaseUrl() {
        String baseUrl = "https://" + SERVER_HOST + ":" + SERVER_PORT;
        if (System.getProperty("test.version") != null && System.getProperty("test.version").equals("1")) {
            return baseUrl + "/api/v1/";
        }
        return baseUrl + "/api/v2/";
    }
    
    private static Header getAuthHeader() {        
        RestAssured.useRelaxedHTTPSValidation();
        if (System.getProperty("test.version") != null && System.getProperty("test.version").equals("1")) {
            return getBasicAuthHeader();
        }
        return getJWTAuthHeader();
    }
    
    private static Header getJWTAuthHeader() {
        Response response = RestAssured.given().contentType("application/json")
                .body("{\"username\":\"" + USER + "\",\"password\":\"" + PASSWORD + "\"}")
                .when().post("https://" + SERVER_HOST + ":" + SERVER_PORT + "/api/v1/gateway/auth/login");
        assertEquals(response.getStatusCode(), HttpStatus.SC_NO_CONTENT);
        return new Header("Authorization", "Bearer " + response.getCookie("apimlAuthenticationToken"));
    }
    
    private static Header getBasicAuthHeader() {
        String credentials = System.getProperty("server.username") + ":" + System.getProperty("server.password"); 
        byte[] encodedAuth = Base64.encodeBase64(credentials.getBytes(StandardCharsets.ISO_8859_1));
        return new Header("Authorization", "Basic " + new String(encodedAuth));
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
