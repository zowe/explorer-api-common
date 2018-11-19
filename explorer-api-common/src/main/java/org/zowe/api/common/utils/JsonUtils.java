/**
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright IBM Corporation 2015, 2018
 */

package org.zowe.api.common.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

public class JsonUtils {

	private static final ObjectMapper jsonMapper = new ObjectMapper();
	private static final JsonParser parser = new JsonParser();

	public static <T> T convertString(String streamContent, Class<T> outputType)
			throws JsonParseException, JsonMappingException, IOException {
		return jsonMapper.readValue(streamContent, outputType);
	}

	public static <T> T convertFilePath(Path filePath, Class<T> outputType)
			throws JsonParseException, JsonMappingException, IOException {
		return jsonMapper.readValue(filePath.toFile(), outputType);
	}

	public static String convertToJsonString(Object o) throws JsonProcessingException {
		return jsonMapper.writeValueAsString(o);
	}

	public static JsonElement readFileAsJsonElement(Path filePath) throws JsonSyntaxException, IOException {
		return readAsJsonElement(new String(Files.readAllBytes(filePath)));
	}

	public static JsonElement readAsJsonElement(String string) throws JsonSyntaxException, IOException {
		return parser.parse(string);
	}
}
