package com.QueryBuilder.QueryBuilder;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;

import com.QueryBuilder.QueryBuilder.constants.TestConstants;
import com.QueryBuilder.reader.QueryReader;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles(profiles = "localtest")
public class QueryBuilderApplicationTests {

	@Test
	public void contextLoads() {
	}
	
	@Test
	public void testReadQuery() {
		QueryReader mockQueryReader = Mockito.mock(QueryReader.class);
		ReflectionTestUtils.setField(mockQueryReader, "inputFile", getClass().getResource(TestConstants.TEST_QUERY).getFile());
		ReflectionTestUtils.invokeMethod(mockQueryReader, "readQuery");
		Object mockTableObj = ReflectionTestUtils.getField(mockQueryReader, "tableObj");
		assertNotNull(mockTableObj);
	}

}
