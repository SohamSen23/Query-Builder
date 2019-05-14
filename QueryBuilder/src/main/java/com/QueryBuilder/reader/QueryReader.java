package com.QueryBuilder.reader;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.QueryBuilder.communicator.FileReaderWriter;
import com.QueryBuilder.constants.QueryBuilderConstants;

@Service
public class QueryReader {

	@Autowired
	QueryBuilderConstants QueryBuilderConstants;
	
	@Autowired
	FileReaderWriter fileReaderWriter;

	public void readQuery() {

		try {
			
			XSSFWorkbook workbook = fileReaderWriter.readFile();
			XSSFSheet sheet = workbook.getSheetAt(0);
			String sheetName = sheet.getSheetName();
			Row colNames = sheet.getRow(0);
			Row colTypes = sheet.getRow(1);
			int numberOfFields = colNames.getPhysicalNumberOfCells();
			int numberOfRows = sheet.getPhysicalNumberOfRows();
			List<String> tableObj = new ArrayList<>();
			QueryBuilderConstants.BASE_QUERY = QueryBuilderConstants.BASE_QUERY.concat(sheetName)
					.concat(QueryBuilderConstants.BLANK).concat(QueryBuilderConstants.OPEN_PARANTHESIS);
			IntStream.range(0, numberOfFields).forEach(colNum -> {
				QueryBuilderConstants.BASE_QUERY = QueryBuilderConstants.BASE_QUERY
						.concat(colNames.getCell(colNum).toString()).concat(QueryBuilderConstants.COMMA)
						.concat(QueryBuilderConstants.BLANK);
			});

			this.prepareBaseQuery();

			IntStream.range(2, numberOfRows).forEach(eachRowNum -> {
				Row eachRow = sheet.getRow(eachRowNum);
				QueryBuilderConstants.EACH_QUERY = QueryBuilderConstants.OPEN_PARANTHESIS;
				IntStream.range(0, numberOfFields).forEach(eachField -> {
					Cell eachCell = eachRow.getCell(eachField);
					if (colTypes.getCell(eachField).getBooleanCellValue()) {
						QueryBuilderConstants.EACH_QUERY = QueryBuilderConstants.EACH_QUERY
								.concat(QueryBuilderConstants.SINGLE_QUOTE).concat(eachCell.getStringCellValue())
								.concat(QueryBuilderConstants.SINGLE_QUOTE).concat(QueryBuilderConstants.COMMA)
								.concat(QueryBuilderConstants.BLANK);
					} else {
						QueryBuilderConstants.EACH_QUERY = QueryBuilderConstants.EACH_QUERY
								.concat(String.valueOf((int) eachCell.getNumericCellValue()))
								.concat(QueryBuilderConstants.COMMA).concat(QueryBuilderConstants.BLANK);
					}
				});
				this.prepareEachQuery();
				tableObj.add(QueryBuilderConstants.BASE_QUERY.concat(QueryBuilderConstants.EACH_QUERY));
			});

			fileReaderWriter.writeSQLFile(sheetName, tableObj);

			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void prepareEachQuery() {
		QueryBuilderConstants.EACH_QUERY = QueryBuilderConstants.EACH_QUERY.substring(0,
				QueryBuilderConstants.EACH_QUERY.length() - 2);
		QueryBuilderConstants.EACH_QUERY = QueryBuilderConstants.EACH_QUERY
				.concat(QueryBuilderConstants.CLOSE_PARANTHESIS).concat(QueryBuilderConstants.SEMI_COLON);
	}

	private void prepareBaseQuery() {
		QueryBuilderConstants.BASE_QUERY = QueryBuilderConstants.BASE_QUERY.substring(0,
				QueryBuilderConstants.BASE_QUERY.length() - 2);
		QueryBuilderConstants.BASE_QUERY = QueryBuilderConstants.BASE_QUERY
				.concat(QueryBuilderConstants.CLOSE_PARANTHESIS).concat(QueryBuilderConstants.BLANK);
		QueryBuilderConstants.BASE_QUERY = QueryBuilderConstants.BASE_QUERY.concat(QueryBuilderConstants.VALUES)
				.concat(QueryBuilderConstants.BLANK);
	}

	
}
