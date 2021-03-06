package org.figurate

import org.apache.felix.scr.annotations.Component
import org.apache.felix.scr.annotations.Service
import org.gcontracts.annotations.Requires

/**
 * Created by fortuna on 10/02/14.
 */
//@CompileStatic
@Component(immediate = true)
@Service(value = ScriptEvaluator)
class ScriptEvaluator {
    Binding binding = []
    GroovyShell shell = [binding]

    @Requires({ expression != null })
    def evaluate(String expression, def bindingArgs = [:]) {
        bindingArgs.each { key, value ->
            binding.setVariable key, value
        }
        shell.evaluate(expression)
    }
}
