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
package org.jboss.arquillian.graphene.angular.ftest;

import java.net.URL;
import java.util.List;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.angular.findby.FindByNg;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import static org.junit.Assert.assertEquals;

/**
 * @author Ken Finnigan
 */
@RunWith(Arquillian.class)
@RunAsClient
public class AngularElementTest {

    @Deployment
    public static WebArchive createTestArchive() {
        WebArchive war = ShrinkWrap.create(WebArchive.class, "angular-test.war");
        war.addAsWebResource("app/index.html", "index.html");
        war.addAsWebResource("app/app.js", "app.js");
        war.addAsWebResource("app/todo.js", "todo.js");
        war.addAsWebResource("app/lib/angular.js", "lib/angular.js");
        return war;
    }

    @Drone
    private WebDriver browser;

    @ArquillianResource
    private URL contextRoot;

    @FindByNg(model = "todo.done")
    List<WebElement> todos;

    @FindByNg(model = "todoText")
    WebElement todoEntry;

    @FindByNg(action = "archive()")
    WebElement archive;

    @FindByNg(action = "addTodo()")
    WebElement addTodo;

    @FindByNg(repeat = "todo in todos")
    List<WebElement> todoRepeat;

    @Before
    public void loadPage() {
        browser.navigate().to(contextRoot + "index.html");
    }

    @Test
    public void testNumberOfTodosAtStart() {
        assertEquals(2, todos.size());
    }

    @Test
    public void testArchive() {
        assertEquals(2, todos.size());
        archive.click();
        assertEquals(1, todos.size());
    }

    @Test
    public void testAddTodo() {
        assertEquals(2, todos.size());
        todoEntry.sendKeys("This is a new TODO item");
        addTodo.submit();
        assertEquals(3, todos.size());
    }

    @Test
    public void testRepeater() {
        assertEquals(2, todoRepeat.size());
        WebElement secondRow = todoRepeat.get(1);
        WebElement checkbox = secondRow.findElement(By.tagName("input"));
        WebElement todoItem = secondRow.findElement(By.tagName("span"));
        assertEquals("second todo", todoItem.getText());

        checkbox.click();
        archive.click();

        assertEquals(0, todoRepeat.size());
    }
}
