package org.figurate.osgi

import org.figurate.ScriptEvaluator
import org.figurate.log.LogAdapter
import org.figurate.test.AbstractFrameworkSpecification

/**
 * Created by fortuna on 30/01/14.
 */
class OsgiAdapterSpec extends AbstractFrameworkSpecification {

    def adapter
    def evaluator

    @Override
    def getConfigLocation() {
        return new File('modules/figurate-osgi/src/test/resources/config/OsgiAdapterSpec.config')
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
