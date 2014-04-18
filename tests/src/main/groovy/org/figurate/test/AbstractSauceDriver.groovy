package org.figurate.test

import org.openqa.selenium.remote.RemoteWebDriver

/**
 * Created by fortuna on 18/04/14.
 */
abstract class AbstractSauceDriver {

    String username
    String accessKey

    @Delegate RemoteWebDriver driver

    AbstractSauceDriver() {
        username = System.getenv('figurate.test.saucelabs.username')
        accessKey = System.getenv('figurate.test.saucelabs.accessKey')
    }
    void init(def capabilities) {
        driver = [new URL("http://$username:$accessKey@ondemand.saucelabs.com:80/wd/hub"), capabilities]
    }
}
