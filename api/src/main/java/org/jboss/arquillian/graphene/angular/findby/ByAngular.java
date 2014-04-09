/**
 * JBoss, Home of Professional Open Source
 * Copyright 2014, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.arquillian.graphene.angular.findby;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.util.List;

import org.jboss.arquillian.core.spi.Validate;
import org.jboss.arquillian.graphene.spi.ImplementedBy;
import org.jboss.arquillian.graphene.spi.TypeResolver;
import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;

/**
 * @author Ken Finnigan
 */
public abstract class ByAngular extends By {
    protected By implementation;

    protected By instantiate(Class<? extends By> type, String lookup) {
        try {
            Class<? extends By> clazz = TypeResolver.resolveType(type);
            Constructor<? extends By> constructor = clazz.getConstructor(String.class);
            return constructor.newInstance(lookup);
        } catch (Exception e) {
            throw new IllegalStateException("Cannot instantiate " + type, e);
        }
    }

    @Override
    public WebElement findElement(SearchContext context) {
        return implementation.findElement(context);
    }

    @Override
    public List<WebElement> findElements(SearchContext context) {
        return implementation.findElements(context);
    }

    @Override
    public String toString() {
        return implementation.toString();
    }

    public static By model(final String modelName) {
        Validate.notNull(modelName, "Cannot find element when ng-model name is null!");
        return new ByModel(modelName);
    }

    public static By action(final String action) {
        Validate.notNull(action, "Cannot find element when action is null!");
        return new ByAction(action);
    }

    @ImplementedBy(className = "org.jboss.arquillian.graphene.angular.findby.ByModelImpl")
    public static class ByModel extends ByAngular implements Serializable {
        public ByModel(String modelName) {
            this.implementation = instantiate(ByModel.class, modelName);
        }
    }

    @ImplementedBy(className = "org.jboss.arquillian.graphene.angular.findby.ByActionImpl")
    public static class ByAction extends ByAngular implements Serializable {
        public ByAction(String action) {
            this.implementation = instantiate(ByAction.class, action);
        }
    }
}
