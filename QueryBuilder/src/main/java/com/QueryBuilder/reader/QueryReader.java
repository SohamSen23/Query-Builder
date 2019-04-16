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
	private static final String blank = " ";
	public void readQuery() {

		try {
			FileInputStream file = new FileInputStream(new File("C:/Users/DELL/Desktop/Query.xlsx"));

			// Create Workbook instance holding reference to .xlsx file
			XSSFWorkbook workbook = new XSSFWorkbook(file);

			XSSFSheet sheet = workbook.getSheetAt(0);
			String sheetName = sheet.getSheetName();
			Row colNames = sheet.getRow(0);
			int numberOfFields = colNames.getPhysicalNumberOfCells();
			int numberOfQueries = sheet.getPhysicalNumberOfRows();
			List<String> tableObj = new ArrayList<String>();
			baseQuery = baseQuery.concat(sheetName).concat(blank);
			IntStream.range(0, numberOfFields).forEach(colNum -> {
				baseQuery = baseQuery.concat(colNames.getCell(colNum).toString()).concat(blank);
			});
			
			IntStream.range(0, numberOfQueries).forEach(eachQuery -> {
				
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

