/*******************************************************************************
 * Copyright 2012 David Rusk
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 *
 *    http://www.apache.org/licenses/LICENSE-2.0 
 *     
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and 
 * limitations under the License.  
 *******************************************************************************/
package org.thechiselgroup.biomixer.client.services;

import org.thechiselgroup.biomixer.shared.workbench.util.json.JsonArray;
import org.thechiselgroup.biomixer.shared.workbench.util.json.JsonItem;
import org.thechiselgroup.biomixer.shared.workbench.util.json.JsonParser;

public abstract class AbstractJsonResultParser implements JsonParser {

    private JsonParser jsonParser;

    protected AbstractJsonResultParser(JsonParser jsonParser) {
        this.jsonParser = jsonParser;
    }

    @Override
    public JsonArray getArray(JsonItem jsonItem, String path) {
        return jsonParser.getArray(jsonItem, path);
    }

    @Override
    public JsonArray getArray(String json, String path) {
        return jsonParser.getArray(json, path);
    }

    @Override
    public JsonItem getItem(String json, String path) {
        return jsonParser.getItem(json, path);
    }

    @Override
    public String getString(JsonItem jsonItem, String path) {
        return jsonParser.getString(jsonItem, path);
    }

    @Override
    public String getString(String json, String path) {
        return jsonParser.getString(json, path);
    }

}