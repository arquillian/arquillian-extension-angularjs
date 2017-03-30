/**
 * JBoss, Home of Professional Open Source
 * Copyright 2014, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.arquillian.graphene.angular.findby;

import org.jboss.arquillian.core.spi.Validate;
import org.jboss.arquillian.graphene.context.GrapheneContext;
import org.jboss.arquillian.graphene.javascript.JSInterfaceFactory;

/**
 * @author Ken Finnigan
 */
public class ByRepeatImpl extends ByAngularImpl {

    private final String repeaterText;

    public ByRepeatImpl(String repeaterText) {
        super(repeaterText, "ng-repeat");
        Validate.notNull(repeaterText, "Cannot find element when repeater text is null!");
        this.repeaterText = repeaterText;
    }

    @Override
    public String toString() {
        return "ByAngularRepeat(\"" + repeaterText + "\")";
    }

    @Override
    protected AngularSearchContext getSearchContext(GrapheneContext grapheneContext) {
        return JSInterfaceFactory.create(grapheneContext, AngularRepeatSearchContext.class);
    }
}
