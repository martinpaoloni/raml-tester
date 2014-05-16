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

import guru.nidi.ramltester.core.SchemaValidator;
import guru.nidi.ramltester.loader.RamlResourceLoader;
import guru.nidi.ramltester.loader.RamlResourceLoaderRamlParserResourceLoader;
import org.raml.model.Raml;
import org.raml.parser.visitor.RamlDocumentBuilder;

/**
 *
 */
public class RamlLoader {
    private final RamlResourceLoader resourceLoader;
    private final SchemaValidators schemaValidators;

    public RamlLoader(RamlResourceLoader resourceLoader, SchemaValidators schemaValidators) {
        this.resourceLoader = resourceLoader;
        this.schemaValidators = schemaValidators;
    }

    public RamlLoader(RamlResourceLoader resourceLoader) {
        this(resourceLoader, SchemaValidators.standard());
    }

    public RamlLoader addSchemaValidator(SchemaValidator schemaValidator) {
        return new RamlLoader(resourceLoader, schemaValidators.addSchemaValidator(schemaValidator));
    }

    public RamlDefinition load(String name){
        final Raml raml = new RamlDocumentBuilder(new RamlResourceLoaderRamlParserResourceLoader(resourceLoader)).build(name);
        final SchemaValidators validators = schemaValidators.withResourceLoader(resourceLoader);
        return new RamlDefinition(raml, validators);
    }

}
