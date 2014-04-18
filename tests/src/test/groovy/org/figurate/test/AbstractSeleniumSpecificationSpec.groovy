package org.figurate.test

/**
 * Created by fortuna on 17/04/14.
 */
class AbstractSeleniumSpecificationSpec extends AbstractSeleniumSpecification {

    def "test load page"() {
        when:
        driver.get "http://google.com"

        then:
        driver.title == "Google"

        where:
        browser << browsers()
    }
}
