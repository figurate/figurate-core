package org.figurate.test

import static org.ops4j.pax.exam.CoreOptions.*

import org.junit.Test;
import org.junit.runner.RunWith
import org.ops4j.pax.exam.Configuration
import org.ops4j.pax.exam.Option
import org.ops4j.pax.exam.junit.PaxExam
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy
import org.ops4j.pax.exam.spi.reactors.PerMethod

@RunWith(PaxExam.class)
@ExamReactorStrategy(PerMethod.class)
class BootstrapTest {

	@Configuration
	public Option[] config() {
		[
			mavenBundle('org.codehaus.groovy', 'groovy-all', '2.1.7'),
			bundle('http://repository.amdatu.org/release/org.amdatu.web.rest.jaxrs/org.amdatu.web.rest.jaxrs-1.0.0.jar'),
			bundle('file:modules/figurate-common/build/libs/figurate-common.jar'),
			junitBundles()
		] as Option[]
	}
	
	@Test
	void test() {
		println 'works'
	}
}
