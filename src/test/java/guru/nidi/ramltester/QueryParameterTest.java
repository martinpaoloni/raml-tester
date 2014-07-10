/*
 * Copyright (C) 2014 Stefan Niederhauser (nidin@gmx.ch)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package guru.nidi.ramltester;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

/**
 *
 */
public class QueryParameterTest extends HighlevelTestBase {
    private RamlDefinition simple = RamlLoaders.fromClasspath(getClass()).load("query.raml");

    @Test
    public void undefinedQueryParameter() throws Exception {
        assertOneRequestViolationThat(
                simple,
                get("/data?a=b"),
                jsonResponse(200, "\"hula\""),
                equalTo("Query parameter 'a' on action(GET /data) is not defined"));
    }

    @Test
    public void illegallyRepeatQueryParameter() throws Exception {
        assertOneRequestViolationThat(
                simple,
                get("/query?req=1&req=2"),
                jsonResponse(200, "\"hula\""),
                equalTo("Query parameter 'req' on action(GET /query) is not repeat but found repeatedly"));
    }

    @Test
    public void allowedRepeatQueryParameter() throws Exception {
        assertNoViolations(
                simple,
                get("/query?rep=1&rep=2&req=3"),
                jsonResponse(200, "\"hula\""));
    }

    @Test
    public void missingRequiredQueryParameter() throws Exception {
        assertOneRequestViolationThat(
                simple,
                get("/query?"),
                jsonResponse(200, "\"hula\""),
                equalTo("Query parameter 'req' on action(GET /query) is required but not found"));
    }

    @Test
    public void undefinedEmptyParam() throws Exception {
        assertOneRequestViolationThat(
                simple,
                get("/query?req&hula"),
                jsonResponse(200, "\"hula\""),
                equalTo("Query parameter 'hula' on action(GET /query) is not defined"));
    }

    @Test
    public void invalidEmptyParam() throws Exception {
        assertOneRequestViolationThat(
                simple,
                get("/query?req&int"),
                jsonResponse(200, "\"hula\""),
                equalTo("Query parameter 'int' on action(GET /query) : Value 'empty' is only allowed with type string"));
    }

    @Test
    public void validEmptyParam() throws Exception {
        assertNoViolations(
                simple,
                get("/query?req"),
                jsonResponse(200, "\"hula\""));
    }
}