package org.figurate.osgi

import org.figurate.ScriptEvaluator
import org.figurate.test.AbstractFrameworkSpec

/**
 * Created by fortuna on 13/02/14.
 */
class HttpVersionSpec extends AbstractFrameworkSpec {

    def evaluator

    def setupSpec() {
        loadConfig(new File('/Users/fortuna/Development/figurate-core/modules/figurate-osgi/src/test/resources/config/HttpVersionSpec.config'))
    }

    def 'test version REST method'() {
        setup:
        osgi.bundleContext.bundles.each {
            println "$it.bundleId $it.symbolicName $it.state"
        }

        and:
        evaluator = binding.serviceLocator.findService(new ServiceName() {
            @Override
            String getFilter() {
                return "(objectClass=${ScriptEvaluator.canonicalName})"
            }
        })

        and:
        evaluator.evaluate(getClass().getResource('/scripts/RegisterServlet.groovy').text,
                ['context': binding.bundleContext])

        and:
        evaluator.evaluate(getClass().getResource('/scripts/RegisterRestEndpoint.groovy').text,
                ['context': binding.bundleContext])

        expect:
//        TimeUnit.SECONDS.sleep(30)
        new URL('http://localhost:8081/hello').text == 'Hello World'
        new URL('http://localhost:8081/version').text == '1.0'
    }
}
