package org.figurate.osgi

import org.figurate.ScriptEvaluator
import org.figurate.log.LogAdapter
import org.figurate.test.AbstractFrameworkSpec

/**
 * Created by fortuna on 30/01/14.
 */
class OsgiAdapterSpec extends AbstractFrameworkSpec {

    def adapter
    def evaluator

    def setupSpec() {
        loadConfig(new File('/Users/fortuna/Development/figurate-core/modules/figurate-osgi/src/test/resources/config/OsgiAdapterSpec.config'))
    }

//    @Ignore
    def 'test logging'() {
        setup:
        adapter = binding.serviceLocator.findService(new ServiceName() {
            @Override
            String getFilter() {
                return "(objectClass=${LogAdapter.canonicalName})"
            }
        })

        and:
        evaluator = binding.serviceLocator.findService(new ServiceName() {
            @Override
            String getFilter() {
                return "(objectClass=${ScriptEvaluator.canonicalName})"
            }
        })

        and:
        evaluator.evaluate(getClass().getResource('/scripts/LogTestMessage.groovy').text,
                ['adapter': adapter])
    }
}
