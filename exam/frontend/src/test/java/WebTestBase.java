import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import po.CreateUserPageObject;
import po.HomePageObject;
import po.LoginPageObject;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.atomic.AtomicLong;

import static org.junit.Assert.*;
import static org.junit.Assume.assumeTrue;

public class WebTestBase {

    private static final AtomicLong counter = new AtomicLong(System.currentTimeMillis());

    protected static HomePageObject home;
    private static WebDriver driver;

    protected WebDriver getDriver(){
        return driver;
    }

    private static boolean tryToSetGeckoIfExists(String property, Path path) {
        if (Files.exists(path)) {
            System.setProperty(property, path.toAbsolutePath().toString());
            return true;
        }
        return false;
    }

    private static void setupDriverExecutable(String executableName, String property) {
        String homeDir = System.getProperty("user.home");

        //first try Linux/Mac executable
        if (!tryToSetGeckoIfExists(property, Paths.get(homeDir, executableName))) {
            //then check if on Windows
            if (!tryToSetGeckoIfExists(property, Paths.get(homeDir, executableName + ".exe"))) {
                fail("Cannot locate the " + executableName + " in your home directory " + homeDir);
            }
        }
    }

    private static WebDriver getChromeDriver() {
        /*
            Need to have Chrome (eg version 53.x) and the Chrome Driver (eg 2.24),
            whose executable should be saved directly under your home directory
            see https://sites.google.com/a/chromium.org/chromedriver/getting-started
         */
        setupDriverExecutable("chromedriver", "webdriver.chrome.driver");

        return new ChromeDriver();
    }

    @BeforeClass
    public static void init() throws InterruptedException {
        driver = getChromeDriver();

        /*
            When the integration tests in this class are run, it might be
            that WildFly is not ready yet, although it was started. So
            we need to wait till it is ready.
         */
        for (int i = 0; i < 30; i++) {
            boolean ready = JBossUtil.isJBossUpAndRunning();
            if (!ready) {
                Thread.sleep(1_000); //check every second
                continue;
            } else {
                break;
            }
        }
    }

    protected static String getUniqueId() {
        return "unique" + counter.incrementAndGet();
    }

    protected static HomePageObject createAndLogNewUser(String userId){
        return createAndLogNewUser(userId, "name", "surname");
    }

    protected static HomePageObject createAndLogNewUser(String userId, String name, String surname) {
        home.logout();

        LoginPageObject login = home.toLogin();
        CreateUserPageObject create = login.clickCreateNewUser();
        create.createUser(userId, "password", "password", name, surname);

        assertTrue(home.isLoggedIn(userId));
        return home;
    }

    @Before
    public void startFromInitialPage() {
        assumeTrue(JBossUtil.isJBossUpAndRunning());

        home = new HomePageObject(getDriver());
        home.toStartingPage();
        home.logout();
        assertTrue(home.isOnPage());
        assertFalse(home.isLoggedIn());
    }

    @AfterClass
    public static void tearDown() {
        driver.close();
    }
}
