/**
 * Copyright © 2010-2011 Nokia
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.googlecode.jsonschema2pojo;

import java.io.IOException;
import java.net.URL;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.googlecode.jsonschema2pojo.rules.RuleFactory;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JPackage;

/**
 * Generates Java types from a JSON schema. Can accept a factory which will be
 * used to create type generation rules for this mapper.
 */
public class SchemaMapper {

    private final RuleFactory ruleFactory;

    /**
     * Create a schema mapper with the given {@link RuleFactory}.
     * 
     * @param ruleFactory
     *            A factory used by this mapper to create Java type generation
     *            rules.
     */
    public SchemaMapper(RuleFactory ruleFactory) {
        this.ruleFactory = ruleFactory;
    }

    /**
     * Create a schema mapper with the default {@link RuleFactory}
     * implementation.
     * 
     * @see RuleFactory
     */
    public SchemaMapper() {
        this(new RuleFactory());
    }

    /**
     * Reads a schema and adds generated types to the given code model.
     * 
     * @param codeModel
     *            the java code-generation context that should be used to
     *            generated new types
     * @param className
     *            the name of the parent class the represented by this schema
     * @param packageName
     *            the target package that should be used for generated types
     * @param schemaUrl
     *            location of the schema to be used as input
     * @throws IOException
     *             if the schema content cannot be read
     */
    public void generate(JCodeModel codeModel, String className, String packageName, URL schemaUrl) throws IOException {

        JPackage jpackage = codeModel._package(packageName);

        ObjectNode schemaNode = new ObjectMapper().createObjectNode();
        schemaNode.put("$ref", schemaUrl.toString());

        ruleFactory.getSchemaRule().apply(className, schemaNode, jpackage, null);

    }

}