Arquillian AngularJS extensions to Arquillian Graphene
======================================================

> AngularJS extensions to Arquillian Graphene

Features
--------

* synchronization of AngularJS calls
* `@FindByNg` annotation support

Synchronization
---------------

This extension implements a WebDriver event listener that after each navigation, click or typing event makes sure that there are no other oustanding Angular requests in progress.

This ensures that application-specific computations are finished before the control is passed over to your test.

In order to use this extension, you don't need to do anything - it is registered and used automatically.

Selectors - `FindByNg`
-----------------------

Often you won't find any other meaningful selectors in AngularJS generated DOM than `ng-` attributes.

Angular annotations can offer you a stable way to describe

* form inputs that binds to model values (`@FindByNg(model = "..."`)
* repeated DOM blocks (`@FindByNg(repeat = "...")`)
* buttons, links or forms that are a source of actions (`@FindByNg(action = "..."`)

### Sample

    @RunWith(Arquillian.class)
    @RunAsClient
    public class AngularTest {

        @Deployment
        public static WebArchive createTestArchive() {
            return ...;
        }

        @Drone
        WebDriver browser;

        @ArquillianResource
        URL contextRoot;

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
        public void testNumberOfTodos() {
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
