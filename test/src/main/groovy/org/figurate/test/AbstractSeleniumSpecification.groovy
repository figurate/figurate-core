package org.figurate.test

import org.openqa.selenium.WebDriver
import spock.lang.Shared
import spock.lang.Specification

import java.util.concurrent.TimeUnit

/**
 * Created by fortuna on 17/04/14.
 */
abstract class AbstractSeleniumSpecification extends Specification {

    @Shared
    WebDriver driver

    static def drivers

    def setup() {
        driver.manage().timeouts().implicitlyWait 10, TimeUnit.SECONDS
    }

    def cleanup() {
        driver.quit()
    }

    /**
     *
     * @see http://www.solutionsiq.com/resources/agileiq-blog/bid/78219/Agile-Software-Development-BDD-With-Spock-and-Selenium
     * @return an iteration of browsers to test with
     */
    protected def browsers() {
        new Browsers(spec: this, delegate: drivers.iterator())
    }

    /**
     * An encapsulation of a specification with delegation to a web driver iteration.
     * @see http://www.solutionsiq.com/resources/agileiq-blog/bid/78219/Agile-Software-Development-BDD-With-Spock-and-Selenium
     */
    private static final class Browsers {
        @Delegate
        Iterator<WebDriver> delegate
        AbstractSeleniumSpecification spec

        @Override
        WebDriver next() {
            spec.driver = delegate.next()
        }
    }
}
