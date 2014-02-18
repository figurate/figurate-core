package org.figurate

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
}
