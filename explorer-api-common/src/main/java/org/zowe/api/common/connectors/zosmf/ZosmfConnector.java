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

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.impl.client.HttpClientBuilder;
import org.zowe.api.common.connectors.zosmf.exceptions.ZosmfConnectionException;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.GeneralSecurityException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

@Slf4j
public abstract class ZosmfConnector {
    private final String host;
    private final int port;

    public ZosmfConnector(ConnectionProperties properties) {
        host = properties.getIpAddress();
        port = properties.getHttpsPort();
    }
    public URI getFullUrl(String relativePath) throws URISyntaxException {
        return getFullUrl(relativePath, null);
    }
    public URI getFullUrl(String relativePath, String query) throws URISyntaxException {
        try {
            return new URI("https", null, host, port, "/api/v1/zosmf/" + relativePath, query, null);
        } catch (URISyntaxException e) {
            log.error("getFullUrl", e);
            throw e;
        }
    }

    public abstract Header getAuthHeader();
    
    public HttpResponse executeRequest(RequestBuilder requestBuilder) throws IOException {
        requestBuilder.setHeader(getAuthHeader());
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
        sslcontext.init(null, new TrustManager[] { new X509TrustManager() {
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