package com.QueryBuilder.communicator;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.QueryBuilder.constants.QueryBuilderConstants;

@Component
public class FileReaderWriter {
	
	
	public XSSFWorkbook readFile() throws IOException {
		FileInputStream inputFile = new FileInputStream(new File("C:/Users/DELL/Desktop/Query.xlsx"));
		XSSFWorkbook workbook = new XSSFWorkbook(inputFile);
		inputFile.close();
		return workbook ;
	}
	
	public void writeSQLFile(String sheetName, List<String> tableObj) throws IOException {
		String fileName = sheetName.concat(QueryBuilderConstants.DOT).concat(QueryBuilderConstants.VAL_SQL);
		File outpuFile = new File("C:/Users/DELL/Desktop/".concat(fileName));
		BufferedWriter writer = new BufferedWriter(new FileWriter(outpuFile));

		if (!CollectionUtils.isEmpty(tableObj)) {
			tableObj.stream().forEach(eachQuery -> {
				try {
					writer.write(eachQuery);
					writer.newLine();
				} catch (IOException e) {
					e.printStackTrace();
				}
			});
		}
		writer.close();
	}

}
