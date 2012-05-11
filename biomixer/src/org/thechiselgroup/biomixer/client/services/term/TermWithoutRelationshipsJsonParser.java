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
package org.thechiselgroup.biomixer.client.services.term;

import org.thechiselgroup.biomixer.client.Concept;
import org.thechiselgroup.biomixer.client.core.resources.Resource;
import org.thechiselgroup.biomixer.client.services.AbstractJsonResultParser;
import org.thechiselgroup.biomixer.shared.workbench.util.json.JsonItem;
import org.thechiselgroup.biomixer.shared.workbench.util.json.JsonParser;

public class TermWithoutRelationshipsJsonParser extends
        AbstractJsonResultParser {

    public TermWithoutRelationshipsJsonParser(JsonParser jsonParser) {
        super(jsonParser);
    }

    public Resource parseConcept(String ontologyId, String json) {
        JsonItem classBean = getItem(json, "$.success.data[0].classBean");

        String fullId = getString(classBean, "fullId");
        String shortId = getString(classBean, "id");
        String label = getString(classBean, "label");
        String type = getString(classBean, "type");

        Resource result = new Resource(Concept.toConceptURI(ontologyId, fullId));

        result.putValue(Concept.FULL_ID, fullId);
        result.putValue(Concept.SHORT_ID, shortId);
        result.putValue(Concept.VIRTUAL_ONTOLOGY_ID, ontologyId);
        result.putValue(Concept.TYPE, type);
        result.putValue(Concept.LABEL, label);

        return result;
    }

}