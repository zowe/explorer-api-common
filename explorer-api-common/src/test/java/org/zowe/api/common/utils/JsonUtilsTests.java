/**
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright IBM Corporation 2014, 2018
 */

package org.zowe.api.common.utils.test;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Before;
import org.mockito.MockitoAnnotations;
import org.zowe.api.common.utils.JsonUtils;

public class ZoweApiTest {

    private final ClassLoader CLASS_LOADER = getClass().getClassLoader();

	@Before
	public void initMocks() {
		MockitoAnnotations.initMocks(this);
	}

    @Test
	protected void readJsonFile() throws Exception {
        expect(JsonUtils.readFileAsJsonElement(getTestResource("bad_json_syntax.json"))).toThrow(JsonSyntaxException.class);
        expect(JsonUtils.readFileAsJsonElement(getTestResource("good_json_syntax.json")));
    }
        
    
    private File getTestResource(String fileName){
        return new File(CLASS_LOADER.getResource(fileName));
    }
}
