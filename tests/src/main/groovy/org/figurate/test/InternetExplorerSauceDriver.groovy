package org.figurate.test

import org.openqa.selenium.Platform
import org.openqa.selenium.remote.DesiredCapabilities

/**
 * Created by fortuna on 18/04/14.
 */
class InternetExplorerSauceDriver extends AbstractSauceDriver {

    InternetExplorerSauceDriver(String sessionName, Platform platform, String version) {
        def capabilities = DesiredCapabilities.internetExplorer()
        capabilities.setCapability("version", version)
        capabilities.setCapability("platform", platform)
        capabilities.setCapability("name", sessionName)

        init(capabilities)
    }
}
