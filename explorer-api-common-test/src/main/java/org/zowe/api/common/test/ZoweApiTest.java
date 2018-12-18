/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright IBM Corporation 2014, 2018
 */
package org.zowe.api.common.test;

import org.junit.Before;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class ZoweApiTest {

    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    protected void shouldThrow(Throwable expectedException, RunnableWithException toThrow) throws Exception {
        try {
            toThrow.run();
            fail(String.format("Expected exception %s, but it wasn't throw", toThrow.getClass()));
        } catch (Throwable e) {
            shouldHaveThrown(expectedException, e);
        }
    }

    protected static void shouldHaveThrown(Throwable expected, Throwable actual) {
        if (expected.getClass() != actual.getClass()) {
            throw new Error(actual);
        }
        assertEquals(expected.getMessage(), actual.getMessage());
    }

    protected String loadFile(String path) throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, Charset.forName("UTF8"));
    }
}
