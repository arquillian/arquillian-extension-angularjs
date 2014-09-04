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
package org.jboss.arquillian.graphene.angular;

import java.lang.annotation.Annotation;
import java.util.concurrent.TimeUnit;

import org.jboss.arquillian.core.spi.LoadableExtension;
import org.jboss.arquillian.drone.spi.DroneInstanceEnhancer;
import org.jboss.arquillian.drone.spi.InstanceOrCallableInstance;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.events.AbstractWebDriverEventListener;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import org.openqa.selenium.support.events.WebDriverEventListener;

/**
 * @author Ken Finnigan
 */
public class AngularJSDroneExtension implements LoadableExtension {
    @Override
    public void register(ExtensionBuilder extensionBuilder) {
        extensionBuilder.service(DroneInstanceEnhancer.class, AngularJSEnhancer.class);
    }

    public static class AngularJSEnhancer implements DroneInstanceEnhancer<WebDriver> {

        private WebDriverEventListener listener;

        @Override
        public boolean canEnhance(InstanceOrCallableInstance instance, Class<?> droneType, Class<? extends Annotation> qualifier) {
            return droneType.isAssignableFrom(EventFiringWebDriver.class);
        }

        @Override
        public WebDriver enhance(WebDriver instance, Class<? extends Annotation> qualifier) {
            instance.manage().timeouts().setScriptTimeout(2, TimeUnit.SECONDS);
            EventFiringWebDriver driver = new EventFiringWebDriver(instance);
            listener = new AngularJSEventHandler();
            driver.register(listener);
            return driver;
        }

        @Override
        public WebDriver deenhance(WebDriver enhancedInstance, Class<? extends Annotation> qualifier) {
            if (EventFiringWebDriver.class.isInstance(enhancedInstance)) {
                EventFiringWebDriver driver = (EventFiringWebDriver) enhancedInstance;
                driver.unregister(listener);
                listener = null;
                return driver.getWrappedDriver();
            }
            return enhancedInstance;
        }

        @Override
        public int getPrecedence() {
            return 0;
        }
    }

    public static class AngularJSEventHandler extends AbstractWebDriverEventListener {
        @Override
        public void afterNavigateTo(String url, WebDriver driver) {
            waitForLoad(driver);
        }

        @Override
        public void afterNavigateBack(WebDriver driver) {
            waitForLoad(driver);
        }

        @Override
        public void afterNavigateForward(WebDriver driver) {
            waitForLoad(driver);
        }

        @Override
        public void afterClickOn(WebElement element, WebDriver driver) {
            waitForLoad(driver);
        }

        @Override
        public void afterChangeValueOf(WebElement element, WebDriver driver) {
            waitForLoad(driver);
        }

        private void waitForLoad(WebDriver driver) {
            if (JavascriptExecutor.class.isInstance(driver)) {
                JavascriptExecutor executor = (JavascriptExecutor) driver;
                executor.executeAsyncScript(
                        "var callback = arguments[arguments.length - 1];" +
                        "var e1 = document.querySelector('body');" +
                        "if (window.angular) {" +
                            "angular.element(e1).injector().get('$browser').notifyWhenNoOutstandingRequests(callback);" +
                        "} else {callback()}"
                );
            }
        }
    }
}
