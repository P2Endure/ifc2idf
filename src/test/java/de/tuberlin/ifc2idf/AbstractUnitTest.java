package de.tuberlin.ifc2idf;

import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.SpringApplicationConfiguration;

@RunWith(MockitoJUnitRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
public class AbstractUnitTest {

	protected Logger logger = LoggerFactory.getLogger(this.getClass());

}
