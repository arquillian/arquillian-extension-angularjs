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
package org.jboss.arquillian.graphene.angular.ftest;

import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.angular.ftest.pageobjects.TodoList;
import org.jboss.arquillian.graphene.page.Page;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;

import java.net.URL;

import static org.assertj.core.api.Assertions.assertThat;
import static org.jboss.arquillian.graphene.angular.ftest.pageobjects.TodoItem.newTodo;

/**
 * @author Ken Finnigan
 */
public abstract class AngularTestTemplate {

    @Drone
    private WebDriver browser;

    @ArquillianResource
    private URL contextRoot;

    @Page
    private TodoList todoList;

    @Before
    public void loadPage() {
        browser.navigate().to(contextRoot + "index.html");
    }

    @Test
    public void should_have_two_items_on_todo_list_when_started() {
        assertThat(todoList.openTasks()).hasSize(2);
    }

    @Test
    public void should_archive_item_which_is_selected_by_default() {
        // when
        todoList.archive();

        // then
        assertThat(todoList.openTasks()).hasSize(1);
    }

    @Test
    public void should_add_new_todo_item() {
        // when
        todoList.add("This is a new TODO item");

        // then
        assertThat(todoList.openTasks()).hasSize(3)
            .contains(newTodo("This is a new TODO item"));
    }

    @Test
    public void should_archive_all_todo_items_after_ticking_second_entry() {
        // when
        todoList.tick(2);
        todoList.archive();

        // then
        assertThat(todoList.openTasks()).isEmpty();
    }
}
