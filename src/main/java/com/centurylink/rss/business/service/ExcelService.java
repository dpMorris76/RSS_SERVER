package com.centurylink.rss.business.service;

import java.io.IOException;
import java.io.OutputStream;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.centurylink.rss.domain.entity.Story;

@Service
public class ExcelService {
	@Autowired
	@Qualifier("userAdminDataSource")
	DataSource ds;

	@Autowired
	JDBCUtil jdbcUtil;

	private static final Logger logger = Logger.getLogger(ExcelService.class);

	public Map<String, XSSFCellStyle> setColumnTitles(XSSFWorkbook workbook) {
		Map<String, XSSFCellStyle> columnTitles = new LinkedHashMap<String, XSSFCellStyle>();

		// Black bold font
		XSSFFont blackBoldFont = workbook.createFont();
		blackBoldFont.setBold(false);
		blackBoldFont.setColor(new XSSFColor(new java.awt.Color(255, 255, 255)));
		
		// Style sheet with white background and black bold font
				XSSFCellStyle styleWhiteBold = workbook.createCellStyle();
				styleWhiteBold.setFillForegroundColor(new XSSFColor(new java.awt.Color(255, 255, 255)));
				styleWhiteBold.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
				styleWhiteBold.setFont(blackBoldFont);
				styleWhiteBold.setBorderLeft(XSSFCellStyle.BORDER_THIN);
				styleWhiteBold.setBorderRight(XSSFCellStyle.BORDER_THIN);
				styleWhiteBold.setBorderBottom(XSSFCellStyle.BORDER_THIN);
				styleWhiteBold.setAlignment(XSSFCellStyle.ALIGN_CENTER);

		columnTitles.put("Story", styleWhiteBold);
		columnTitles.put("Story Count", styleWhiteBold);
		columnTitles.put("User Count", styleWhiteBold);
		

		return columnTitles;
	}

	

	
	public <T> void exportSpreadsheet2007(List<Story> storyList, OutputStream os, Integer userCount) {

		// when this function is called os must be declared as
		// ByteArrayOutputStream os = new ByteArrayOutputStream();
		// and then passed in.
		
		

		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet firstSheet;

		Map<String, XSSFCellStyle> columnTitles = setColumnTitles(workbook);
		logger.info("Exporting Tracking report...");
		firstSheet = (XSSFSheet) workbook.createSheet("Tracking Report");

		// Counter used for columns here
		int counter = 0;
		// Initial loop to put headers on first row
		XSSFRow rowA = firstSheet.createRow(counter);
		for (Entry<String, XSSFCellStyle> pair : columnTitles.entrySet()) {
			firstSheet.setColumnWidth(counter, 5000);
			XSSFCell cellA = rowA.createCell(counter);
			cellA.setCellValue(new XSSFRichTextString(pair.getKey()));
			cellA.setCellStyle(pair.getValue());
			counter++;
		}
		// Counter used for rows below here
		counter = 1;
		XSSFRow rowB;

		// Loops through as long as there is data and populates rows with it
		// column by column
		for (Story s : storyList) {

			rowB = firstSheet.createRow(counter);// create a new row
			int column = 0;

			XSSFCell cellStory = rowB.createCell(column++);
			if (s.getTitle() != null) {
				cellStory.setCellValue(new XSSFRichTextString(s.getTitle()));
			} else {
				cellStory.setCellValue(new XSSFRichTextString(""));
			}
			XSSFCell cellStoryCount = rowB.createCell(column++);
			if (s.getCount() != null) {
				cellStoryCount.setCellValue(new XSSFRichTextString(s.getCount().toString()));
			} else {
				cellStoryCount.setCellValue(new XSSFRichTextString(""));
			}
			XSSFCell cellUserCount = rowB.createCell(column++);
			if (userCount != null) {
				cellUserCount.setCellValue(new XSSFRichTextString(userCount.toString()));
			} else {
				cellUserCount.setCellValue(new XSSFRichTextString(""));
			}
		
			
	
			// Next row
			counter++;
		}

		try {
			workbook.write(os);
			logger.debug("Outstream written");
		} catch (IOException e) {
			e.printStackTrace();
			logger.error("Failed to Write to OutputStream on ExcelService.ExportSpreadsheet2007()");
		} finally {
			if (os != null) {
				try {
					os.flush();
					os.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	
}
