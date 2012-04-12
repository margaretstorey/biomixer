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
package org.thechiselgroup.biomixer.client.visualization_component.graph.rendering;

import org.thechiselgroup.biomixer.client.core.geometry.PointDouble;
import org.thechiselgroup.biomixer.client.core.geometry.SizeDouble;

/**
 * A displayable node.
 * 
 * @author drusk
 * 
 */
public interface RenderedNode {

    PointDouble getCentre();

    double getLeftX();

    SizeDouble getSize();

    double getTopY();

    void setBackgroundColor(String color);

    void setBorderColor(String color);

    void setFontColor(String color);

    void setFontWeight(String fontWeight);

    void setLeftX(double x);

    void setTopY(double y);

}
