package com.centurylink.rss.web.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.centurylink.rss.business.service.JDBCUtil;
import com.centurylink.rss.web.auth.CustomAuthenticationProvider;
import com.centurylink.rss.web.controller.util.BaseController;

@Controller
public class DataController extends BaseController {
		
	@Autowired
	JDBCUtil jdbcUtil;

	@Autowired
	private CustomAuthenticationProvider authenticationProvider;
	public final static String rsFilesTable = "RISK_SENSE_FILES";
	public final static String rsTable = "RISK_SENSE";
	public final static String dcFilesTable = "DATA_CATEGORY_FILES";
	public final static String assetTable = "BY_ASSET_VW_DT";
	public final static String gtroTable = "GTRO_VW";
	
	private static Log log = LogFactory.getLog(DataController.class);
	private static final Logger logger = Logger.getLogger(DataController.class);
	
	@RequestMapping(value = "/secure/upload", method = RequestMethod.GET)
    public String fileDisplayForm() {
        return "uploadfile";
    }
 
//	@RequestMapping(value = "/secure/savefiles", method = RequestMethod.POST)
//	public String fileSave(@ModelAttribute("uploadForm") FileUpload uploadForm, Model map)
//			throws IllegalStateException, IOException {
//		String success = "rssUploadSuccess";
//		String failure = "rssUploadFailure";
//		String nextPage = failure;
//		List<MultipartFile> uploadedFiles = uploadForm.getFiles();
//		List<String> fileNames = new ArrayList<String>();
//
//		// For each xlsx file uploaded, put into db with a timestamp
//		int i = 0;
//		for (MultipartFile mpf : uploadedFiles) {
//			// Creates regular file
//			File f = new File(mpf.getOriginalFilename());
//			if (!f.toString().equalsIgnoreCase("") && f != null) {
//				f.createNewFile();
//			
//				// Sets filename(s)
//				String fn = jdbcUtil.getMultipartFilename(mpf);
//				if (!"".equalsIgnoreCase(fn)) {
//					fileNames.add(fn);
//				}
//				// Fills file with output stream
//				FileOutputStream fos = new FileOutputStream(f);
//				fos.write(mpf.getBytes());
//				fos.close();
//				// Grab stream/file length
//				int fl = (int) f.length();
//				// Grab today's date
//				Date fd = jdbcUtil.getTodaySql();
//				// Create input stream
//				FileInputStream is = new FileInputStream(f);
//				// Pass for db insertion, returns if successful
//				String viewName = "";
//				if(i == 0) {
//					viewName = rsFilesTable;
//				} else if (i == 1) {
//					viewName = dcFilesTable;
//				}
//				if(!viewName.equals("")) {
//					if (jdbcUtil.insertExcelFile(is, fl, fn, fd, viewName)) {
//					}
//				}
//			} else {
//				logger.info("Empty file in slot "+i+", skipping");
//			}
//			i++;
//		}
//		map.addAttribute("files", fileNames);
//		
//		InputStream inputStream = null;
//		File[] files = null;
//		int[] ids = null;
//		Date[] dates = null;
//		try {
//			files = jdbcUtil.extractExcelFilesForParsing(inputStream, rsFilesTable);
//			ids = jdbcUtil.extractExcelIdsForParsing(inputStream, rsFilesTable);
//			dates = jdbcUtil.extractExcelDatesForParsing(inputStream, rsFilesTable);
//			i = 0;
//			for (File f: files) {
//				if (f != null) {
//					Date currDate = dates[i];
//					int currId = ids[i];
//					excelService.processOneSheet(f.getName(),3,excelService.riskSenseSheet, currDate, currId);
//					excelService.processOneSheet(f.getName(),4,excelService.riskSenseSheet, currDate, currId);
//					if (jdbcUtil.getLastUploadedProgress(rsFilesTable) != 2) {
//						jdbcUtil.markParsed(rsFilesTable,currId, 1);
//						nextPage = success;
//					} else {
//						logger.info("Completed attempt to process file, but processing failed for file "+currId);
//						nextPage = failure;
//					}
//				} else {
//					logger.debug("Null or already parsed Risk Sense file. Skipping and looking for another file...");
//				}
//				i++;
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		files = null;
//		ids = null;
//		try {
//			files = jdbcUtil.extractExcelFilesForParsing(inputStream, dcFilesTable);
//			ids = jdbcUtil.extractExcelIdsForParsing(inputStream, dcFilesTable);
//			i = 0;
//			for (File f: files) {
//				if (f != null) {
//					jdbcUtil.truncateTable("DATA_CATEGORY");
//					int currId = ids[i];
//					excelService.processOneSheet(f.getName(),1,excelService.dataCategorySheet, null, currId);
//					if (jdbcUtil.getLastUploadedProgress(dcFilesTable) != 2) {
//						jdbcUtil.markParsed(dcFilesTable,currId, 1);
//						nextPage = success;
//					} else {
//						logger.info("Completed attempt to process file, but processing failed for file "+currId);
//						nextPage = failure;
//					}
//				} else {
//					logger.debug("Null or already parsed Data Category file. Skipping and looking for another file...");
//				}
//				i++;
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return nextPage;
//	}
}
