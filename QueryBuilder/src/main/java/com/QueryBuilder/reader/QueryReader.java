package com.QueryBuilder.reader;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.QueryBuilder.constants.QueryBuilderConstants;

@Service
public class QueryReader {

	@Autowired
	QueryBuilderConstants QueryBuilderConstants;

	public void readQuery() {

		try {
			FileInputStream inputFile = new FileInputStream(new File("C:/Users/DELL/Desktop/Query.xlsx"));

			XSSFWorkbook workbook = new XSSFWorkbook(inputFile);

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

			this.writeSQLFile(sheetName, tableObj);

			inputFile.close();
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

	private void writeSQLFile(String sheetName, List<String> tableObj) throws IOException {
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
