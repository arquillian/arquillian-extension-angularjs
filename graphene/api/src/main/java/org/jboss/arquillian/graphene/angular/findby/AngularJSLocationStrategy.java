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

import java.lang.annotation.Annotation;

import org.jboss.arquillian.graphene.spi.findby.LocationStrategy;
import org.openqa.selenium.By;

/**
 * @author Ken Finnigan
 */
public class AngularJSLocationStrategy implements LocationStrategy {

    @Override
    public By fromAnnotation(Annotation annotation) {
        FindByNg findBy = (FindByNg) annotation;

        if (!findBy.model().isEmpty()) {
            return ByAngular.model(findBy.model());
        } else if (!findBy.action().isEmpty()) {
            return ByAngular.action(findBy.action());
        } else if (!findBy.repeat().isEmpty()) {
            return ByAngular.repeat(findBy.repeat());
        }

        throw new IllegalStateException(
            String.format("Unable to determine the AngularJS operation from annotation %s", findBy));
    }
}
