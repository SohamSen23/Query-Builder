package com.QueryBuilder.test.services;

import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.IOException;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;

import com.QueryBuilder.communicator.FileReaderWriter;
import com.QueryBuilder.reader.QueryReader;
import com.QueryBuilder.test.constants.TestConstants;
import com.QueryBuilder.test.utils.MockDataRetriever;

@ActiveProfiles(profiles = "testFileReaderWriter")

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class QueryBuilderApplicationTest {

	@Mock
	FileReaderWriter fileReaderWriter;

	@Mock
	MockDataRetriever mockDataRetriever;
	

	@Before
	public void setUp() {
		System.out.println("Test Class Begins");
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testReadQuery() throws IOException {
		QueryReader mockQueryReader = new QueryReader(fileReaderWriter);
		File mockFile = new File(getClass().getResource(TestConstants.TEST_QUERY).getFile());
		XSSFWorkbook mockWorkBook = mockDataRetriever
				.getResponseObject(mockFile);
		//Mockito.when(fileReaderWriter.readFile()).thenReturn(mockWorkBook);

		//mockQueryReader.readQuery();
		Object mockTableObj = ReflectionTestUtils.getField(mockQueryReader, "tableObj");
		
		assertNotNull(mockTableObj);
	}

}
