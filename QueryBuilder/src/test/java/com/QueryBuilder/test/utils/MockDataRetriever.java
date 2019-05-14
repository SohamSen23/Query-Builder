package com.QueryBuilder.test.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

@Component
public class MockDataRetriever {

	public XSSFWorkbook getResponseObject(File inputFile) throws IOException {
		FileInputStream returnObject = new FileInputStream(inputFile);
		returnObject.close();
		return new XSSFWorkbook(returnObject);
	}
}
