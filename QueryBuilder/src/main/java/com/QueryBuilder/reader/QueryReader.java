package com.QueryBuilder.reader;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.IntStream;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Service
public class QueryReader {

	private static String baseQuery = "INSERT INTO ";
	private static final String BLANK = " ";
	private static final String OPEN_PARANTHESIS = "(";
	private static final String CLOSE_PARANTHESIS = ")";
	private static final String COMMA = ",";
	private static final String VALUES = "VALUES";
	private static final String SEMI_COLON = ";";
	private static final String DOT = ".";
	private static final String VAL_SQL = "sql";
	private static final String SINGLE_QUOTE = "'";
	private static String eachQuery = null;

	public void readQuery() {

		try {
			FileInputStream inputFile = new FileInputStream(new File("C:/Users/DELL/Desktop/Query.xlsx"));

			// Create Workbook instance holding reference to .xlsx file
			XSSFWorkbook workbook = new XSSFWorkbook(inputFile);

			XSSFSheet sheet = workbook.getSheetAt(0);
			String sheetName = sheet.getSheetName();
			Row colNames = sheet.getRow(0);
			Row colTypes = sheet.getRow(1);
			int numberOfFields = colNames.getPhysicalNumberOfCells();
			int numberOfRows = sheet.getPhysicalNumberOfRows();
			List<String> tableObj = new ArrayList<>();
			baseQuery = baseQuery.concat(sheetName).concat(BLANK).concat(OPEN_PARANTHESIS);
			IntStream.range(0, numberOfFields).forEach(colNum -> {
				baseQuery = baseQuery.concat(colNames.getCell(colNum).toString()).concat(COMMA).concat(BLANK);
			});

			baseQuery = baseQuery.substring(0, baseQuery.length() - 2);
			baseQuery = baseQuery.concat(CLOSE_PARANTHESIS).concat(BLANK);
			baseQuery = baseQuery.concat(VALUES).concat(BLANK);

			IntStream.range(2, numberOfRows).forEach(eachRowNum -> {
				Row eachRow = sheet.getRow(eachRowNum);
				eachQuery = OPEN_PARANTHESIS;
				IntStream.range(0, numberOfFields).forEach(eachField -> {
					Cell eachCell = eachRow.getCell(eachField);
					if (colTypes.getCell(eachField).getBooleanCellValue()) {
						eachQuery = eachQuery.concat(SINGLE_QUOTE).concat(eachCell.getStringCellValue())
								.concat(SINGLE_QUOTE).concat(COMMA).concat(BLANK);
					} else {
						eachQuery = eachQuery.concat(String.valueOf((int) eachCell.getNumericCellValue())).concat(COMMA)
								.concat(BLANK);
					}
				});
				eachQuery = eachQuery.substring(0, eachQuery.length() - 2);
				eachQuery = eachQuery.concat(CLOSE_PARANTHESIS).concat(SEMI_COLON);
				tableObj.add(baseQuery.concat(eachQuery));

			});

			String fileName = sheetName.concat(DOT).concat(VAL_SQL);
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

			// Iterate through each rows one by one
			Iterator<Row> rowIterator = sheet.iterator();
			while (rowIterator.hasNext()) {
				Row row = rowIterator.next();
				// For each row, iterate through all the columns
				Iterator<Cell> cellIterator = row.cellIterator();

				while (cellIterator.hasNext()) {
					Cell cell = cellIterator.next();
					// Check the cell type and format accordingly
					switch (cell.getCellType()) {
					case Cell.CELL_TYPE_NUMERIC:
						System.out.print(cell.getNumericCellValue());
						break;
					case Cell.CELL_TYPE_STRING:
						System.out.print(cell.getStringCellValue());
						break;
					}
				}
				System.out.println("");
			}
			inputFile.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
