arquilian-graphene-angular
==========================

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

Angular attributes can offer you stable way how to describe

* form inputs that binds to model values (`@FindByNg(model = "..."`)
* repeated DOM blocks (`@FindByNg(repeat = "...")`)
* buttons or links that are source of actions (`@FindByNg(action = "..."`)

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

        @Before
        public void loadPage() {
            browser.navigate().to(contextRoot + "index.html");
        }

        @Test
        public void testNumberOfTodos() {
            assertEquals(2, todos.size());
        }
    }
