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

import java.util.List;

import org.jboss.arquillian.graphene.context.GrapheneContext;
import org.jboss.arquillian.graphene.proxy.GrapheneProxyInstance;
import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;

/**
 * @author Ken Finnigan
 */
public abstract class ByAngularImpl extends By {

    private String selector;
    private String elementNames;

    public ByAngularImpl(String selector, String elementNames) {
        this.selector = selector;
        this.elementNames = elementNames;
    }

    @Override
    public List<WebElement> findElements(SearchContext searchContext) {
        AngularSearchContext angularSearchContext = getSearchContext(getGrapheneContext(searchContext));
        List<WebElement> elements;

        try {
            if (searchContext instanceof WebElement) {
                elements = angularSearchContext.findElementsInElement(selector, (WebElement) searchContext);
            } else if (searchContext instanceof WebDriver) {
                elements = angularSearchContext.findElements(selector);
            } else {
                // Unknown case
                throw new WebDriverException(
                    "Unable to determine the SearchContext passed to findBy method! It is not an instance of WebDriver or WebElement. It is: "
                        + searchContext);
            }
        } catch (Exception e) {
            throw new WebDriverException(
                "Unable to locate " + elementNames + " element(s) for: " + selector + ". Check whether it is correct", e);
        }
        return elements;
    }

    protected abstract AngularSearchContext getSearchContext(GrapheneContext grapheneContext);

    protected GrapheneContext getGrapheneContext(SearchContext searchContext) {
        if (searchContext instanceof GrapheneProxyInstance) {
            return ((GrapheneProxyInstance) searchContext).getContext();
        } else {
            return GrapheneContext.lastContext();
        }
    }
}
