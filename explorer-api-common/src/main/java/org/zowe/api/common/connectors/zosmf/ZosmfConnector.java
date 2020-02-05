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

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zowe.api.common.connectors.zosmf.exceptions.ZosmfConnectionException;

import javax.net.ssl.*;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.GeneralSecurityException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

@Slf4j
@Service
@Setter
public class ZosmfConnector {

    private final String gatewayHost;
    private final int gatewayPort;
    private String authToken;

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

    public HttpResponse request(RequestBuilder requestBuilder) throws IOException {

        requestBuilder.setHeader("Authorization", "Bearer " + this.authToken);
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

    /**
     * Make a Preemptive Basic Authentication HttpClient
     * @param credentialsProvider is the credential Provider
     * @return return the httpclient
     * @throws NoSuchAlgorithmException the exception
     * @throws KeyManagementException the exception too
     */
    public static HttpClient createPreemptiveHttpClientIgnoreSSL(CredentialsProvider credentialsProvider)
            throws NoSuchAlgorithmException, KeyManagementException {
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
        return HttpClientBuilder.create().setSSLContext(sslcontext).setDefaultCredentialsProvider(credentialsProvider)
                .setSSLHostnameVerifier(new HostnameVerifier() {

                    @Override
                    public boolean verify(String s1, SSLSession s2) {
                        return true;
                    }

                }).build();
    }


    public static HttpClient createIgnoreSSLClientWithPassword(String userName, String password)
            throws NoSuchAlgorithmException, KeyManagementException {
        CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(userName, password));

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
        return HttpClientBuilder.create().setSSLContext(sslcontext).setDefaultCredentialsProvider(credentialsProvider)
                .setSSLHostnameVerifier(new HostnameVerifier() {

                    @Override
                    public boolean verify(String s1, SSLSession s2) {
                        return true;
                    }

                }).build();
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