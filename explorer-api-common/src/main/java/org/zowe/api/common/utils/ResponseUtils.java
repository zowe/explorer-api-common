/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package org.zowe.api.common.utils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class ResponseUtils {

    public static int getStatus(HttpResponse response) {
        return response.getStatusLine().getStatusCode();
    }

    public static String getEntity(HttpResponse response) throws IOException {
        return EntityUtils.toString(response.getEntity(), "UTF-8");
    }

    public static JsonElement getEntityAsJson(HttpResponse response) throws IOException {
        return new Gson().fromJson(getEntity(response), JsonElement.class);
    }

    public static JsonObject getEntityAsJsonObject(HttpResponse response) throws IOException {
        return new Gson().fromJson(getEntity(response), JsonObject.class);
    }

    public static JsonArray getEntityAsJsonArray(HttpResponse response) throws IOException {
        return new Gson().fromJson(getEntity(response), JsonArray.class);
    }

    private static <T> T getEntityAs(HttpResponse response, Class<T> entityType) throws IOException {
        return JsonUtils.convertString(getEntity(response), entityType);
    }
}
