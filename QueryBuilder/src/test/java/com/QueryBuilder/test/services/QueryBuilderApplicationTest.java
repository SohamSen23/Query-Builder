package com.QueryBuilder.test.services;

import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;

import com.QueryBuilder.communicator.FileReaderWriter;
import com.QueryBuilder.reader.QueryReader;
import com.QueryBuilder.test.constants.TestConstants;
import com.QueryBuilder.test.utils.MockDataRetriever;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles(profiles = "testFileReaderWriter")
public class QueryBuilderApplicationTest {

	@Autowired
	FileReaderWriter fileReaderWriter;
	
	@Autowired
	MockDataRetriever mockDataRetriever;
	
	@Before
	public void setUp() {
		System.out.println("Test Class Begins");
	}
	
	
	@Test
	public void testReadQuery() throws IOException {
		QueryReader mockQueryReader = Mockito.mock(QueryReader.class);
		Mockito.when(fileReaderWriter.readFile())
		  .thenReturn(mockDataRetriever.getResponseObject(new File(getClass().getResource(TestConstants.TEST_QUERY).getFile())));
		Object mockTableObj = ReflectionTestUtils.getField(mockQueryReader, "tableObj");
		ReflectionTestUtils.invokeMethod(mockQueryReader, "readQuery");
		assertNotNull(mockTableObj);
	}

}
