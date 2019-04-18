package com.QueryBuilder.reader;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.IntStream;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

@Service
public class QueryReader {

	private static String baseQuery = "INSERT INTO ";
	private static final String BLANK = " ";
	private static final String OPEN_PARANTHESIS = "(";
	private static final String CLOSE_PARANTHESIS =")";
	private static final String COMMA =",";
	private static final String VALUES ="VALUES";
	private static String eachQuery = null;
	public void readQuery() {

		try {
			FileInputStream file = new FileInputStream(new File("C:/Users/DELL/Desktop/Query.xlsx"));

			// Create Workbook instance holding reference to .xlsx file
			XSSFWorkbook workbook = new XSSFWorkbook(file);

			XSSFSheet sheet = workbook.getSheetAt(0);
			String sheetName = sheet.getSheetName();
			Row colNames = sheet.getRow(0);
			int numberOfFields = colNames.getPhysicalNumberOfCells();
			int numberOfRows = sheet.getPhysicalNumberOfRows();
			List<String> tableObj = new ArrayList<>();
			baseQuery = baseQuery.concat(sheetName).concat(BLANK).concat(OPEN_PARANTHESIS);
			IntStream.range(0, numberOfFields).forEach(colNum -> {
				baseQuery = baseQuery.concat(colNames.getCell(colNum).toString()).concat(COMMA).concat(BLANK);
			});
			
			baseQuery = baseQuery.substring(0, baseQuery.length()-2);
			baseQuery = baseQuery.concat(CLOSE_PARANTHESIS).concat(BLANK);
			baseQuery = baseQuery.concat(VALUES);
			
			IntStream.range(1, numberOfRows).forEach(eachRowNum -> {
				Row eachRow = sheet.getRow(eachRowNum);
				eachQuery = OPEN_PARANTHESIS;
				IntStream.range(0, numberOfFields).forEach(eachField ->{
					eachQuery = eachQuery.concat(eachRow.getCell(eachField).toString()).concat(COMMA).concat(BLANK);
				});
				eachQuery = eachQuery.substring(0, eachQuery.length()-2);
				eachQuery = eachQuery.concat(CLOSE_PARANTHESIS);
				tableObj.add(baseQuery.concat(eachQuery));
			});

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
			file.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

