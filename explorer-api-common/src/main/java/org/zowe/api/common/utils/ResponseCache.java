/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright IBM Corporation 2018, 2020
 */
package org.zowe.api.common.utils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.entity.ContentType;
import org.apache.http.util.EntityUtils;
import org.springframework.http.MediaType;
import org.springframework.util.Base64Utils;

import java.io.IOException;

public class ResponseCache {

    private int statusCode;
    private String entityString;
    private HttpEntity entity;
    private HttpResponse response;

    public ResponseCache(HttpResponse response) throws IOException {
        this.response = response;
        if (response.getStatusLine() != null) {
            this.statusCode = response.getStatusLine().getStatusCode();
        }
        this.entity = response.getEntity();
        if (entity != null) {
            if (isOctetStream()) {
                // binary data to base64 string
                byte[] entityContent = EntityUtils.toByteArray(entity);
                this.entityString = Base64Utils.encodeToString(entityContent);
            } else {
                this.entityString = EntityUtils.toString(entity, "UTF-8");
            }
        }
    }

    public int getStatus() {
        return statusCode;
    }

    public org.springframework.http.HttpStatus getSpringHttpStatus() {
        return org.springframework.http.HttpStatus.resolve(statusCode);
    }

    public String getEntity() {
        return entityString;
    }

    public JsonElement getEntityAsJson() throws IOException {
        return new Gson().fromJson(entityString, JsonElement.class);
    }

    public JsonObject getEntityAsJsonObject() throws IOException {
        return new Gson().fromJson(entityString, JsonObject.class);
    }

    public JsonArray getEntityAsJsonArray() throws IOException {
        return new Gson().fromJson(entityString, JsonArray.class);
    }

    public ContentType getContentType() {
        return ContentType.get(entity);
    }

    private boolean isOctetStream() {
        if (this.getContentType() != null) {
            return this.getContentType().getMimeType().equals(MediaType.APPLICATION_OCTET_STREAM_VALUE);
        }
        return false;
    }

    // TODO - do we need to cache this?
    public Header getFirstHeader(String name) {
        return response.getFirstHeader(name);
    }
}
