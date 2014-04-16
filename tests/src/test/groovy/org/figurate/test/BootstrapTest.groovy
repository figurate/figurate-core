package org.figurate.test

import org.junit.Ignore
import org.junit.Test
import org.junit.runner.RunWith
import org.ops4j.pax.exam.Configuration
import org.ops4j.pax.exam.Option
import org.ops4j.pax.exam.junit.PaxExam
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy
import org.ops4j.pax.exam.spi.reactors.PerMethod

import static org.ops4j.pax.exam.CoreOptions.*

@Ignore("failing due to gcontracts not in OSGi environment")
@RunWith(PaxExam.class)
@ExamReactorStrategy(PerMethod.class)
class BootstrapTest {

	@Configuration
	public Option[] config() {
		[
			mavenBundle('org.codehaus.groovy', 'groovy-all', '2.1.7'),
			bundle('http://repository.amdatu.org/release/org.amdatu.web.rest.jaxrs/org.amdatu.web.rest.jaxrs-1.0.0.jar'),
			bundle('file:/Users/fortuna/Development/figurate-core/modules/figurate-common/build/libs/figurate-common.jar'),
			junitBundles()
		] as Option[]
	}
	
	@Test
	void test() {
		println 'works'
	}
}
