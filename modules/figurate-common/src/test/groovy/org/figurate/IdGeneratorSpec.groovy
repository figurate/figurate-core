package org.figurate

import org.gcontracts.PreconditionViolation
import spock.lang.Specification

/**
 * Created by fortuna on 18/02/14.
 */
class IdGeneratorSpec extends Specification {

    IdGenerator generator = ['test']

    def "GenerateId"() {
        expect:
        generator.generateId().startsWith('test')
    }

    def 'Empty string not allowed for prefix'() {
        when: 'try to create an instance with an empty prefix'
        new IdGenerator('')

        then:
        thrown(PreconditionViolation)
    }
}
