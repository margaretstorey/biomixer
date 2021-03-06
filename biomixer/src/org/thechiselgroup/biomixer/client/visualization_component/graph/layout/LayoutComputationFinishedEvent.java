/*******************************************************************************
 * Copyright 2012 Lars Grammel, David Rusk 
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
package org.thechiselgroup.biomixer.client.visualization_component.graph.layout;

import java.util.EventObject;

public class LayoutComputationFinishedEvent extends EventObject {

    private static final long serialVersionUID = 1L;

    private final boolean aborted;

    private final RuntimeException exception;

    public LayoutComputationFinishedEvent(LayoutComputation computation,
            boolean aborted, RuntimeException exception) {

        super(computation);

        this.aborted = aborted;
        this.exception = exception;
    }

    public LayoutComputation getComputation() {
        return (LayoutComputation) source;
    }

    public RuntimeException getException() {
        return exception;
    }

    public boolean hasException() {
        return exception != null;
    }

    public boolean wasAborted() {
        return aborted || hasException();
    }

}