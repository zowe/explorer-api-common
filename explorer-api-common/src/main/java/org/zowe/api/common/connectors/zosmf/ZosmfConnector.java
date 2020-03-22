/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright IBM Corporation 2018, 2020
 */
package org.zowe.api.common.connectors.zosmf;

import lombok.extern.slf4j.Slf4j;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zowe.api.common.connectors.zosmf.exceptions.ZosmfConnectionException;
import org.zowe.api.common.exceptions.NoAuthTokenException;

import javax.net.ssl.*;
import javax.servlet.http.HttpServletRequest;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.GeneralSecurityException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.Optional;

@Slf4j
@Service
public class ZosmfConnector {

    private final String gatewayHost;
    private final int gatewayPort;

    @Autowired
    private HttpServletRequest request;


    public URI getFullUrl(String relativePath) throws URISyntaxException {
        return getFullUrl(relativePath, null);
    }

    public URI getFullUrl(String relativePath, String query) throws URISyntaxException {
        try {
            return new URI("https", null, gatewayHost, gatewayPort, "/api/v1/zosmf/" + relativePath, query, null);
        } catch (URISyntaxException e) {
            log.error("getFullUrl", e);
            throw e;
        }
    }

    @Autowired
    public ZosmfConnector(GatewayProperties properties) {
        gatewayHost = properties.getIpAddress();
        gatewayPort = properties.getHttpsPort();
    }

    private String getAuthorizationValueFromHeaders() {
        // If user is passing jwt as a cookie
        String cookieHeader = request.getHeader("cookie");
        if (cookieHeader != null && !cookieHeader.isEmpty()) {
            String[] cookies = cookieHeader.split(";");
            Optional<String> authTokenCookie = Arrays.stream(cookies).filter(c -> c.contains("apimlAuthenticationToken")).findFirst();
            if (authTokenCookie.isPresent()) {
                return "Bearer " + authTokenCookie.get().split("=")[1];
            }
        } else {
            // If user is passing jwt in Authorization header 
            String header = request.getHeader("authorization");
            if (header != null && !header.isEmpty()) {
               return header;
            }
        }
        throw new NoAuthTokenException();
    }

    public HttpResponse request(RequestBuilder requestBuilder) throws IOException {
        requestBuilder.setHeader("Authorization", getAuthorizationValueFromHeaders());
        requestBuilder.setHeader("X-CSRF-ZOSMF-HEADER", "");
        requestBuilder.setHeader("X-IBM-Response-Timeout", "600");

        HttpClient client;
        try {
            client = createIgnoreSSLClient();
        } catch (GeneralSecurityException e) {
            log.error("request", e);
            throw new ZosmfConnectionException(e);
        }
        return client.execute(requestBuilder.build());

    }

    public static HttpClient createIgnoreSSLClient() throws KeyManagementException, NoSuchAlgorithmException {
        SSLContext sslcontext = SSLContext.getInstance("TLS");
        sslcontext.init(null, new TrustManager[]{new X509TrustManager() {
            @Override
            public void checkClientTrusted(X509Certificate[] arg0, String arg1) {
            }

            @Override
            public void checkServerTrusted(X509Certificate[] arg0, String arg1) {
            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0];
            }

        } } , new java.security.SecureRandom());
        return HttpClientBuilder.create().setSSLContext(sslcontext).setSSLHostnameVerifier(new HostnameVerifier() {

            @Override
            public boolean verify(String s1, SSLSession s2) {
                return true;
            }

        }).build();
    }
}