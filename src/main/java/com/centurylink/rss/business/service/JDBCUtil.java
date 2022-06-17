package com.centurylink.rss.business.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.SQLSyntaxErrorException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Service
public class JDBCUtil {
	@Autowired
	@Qualifier("userAdminDataSource")
	DataSource ds;

	private static final Logger logger = Logger.getLogger(JDBCUtil.class);

	// Returns number of rows in table/view
	public int getRowCount(String viewName) {
		// Initialize
		int rowCount = 0;
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			conn = ds.getConnection();
			stmt = conn.createStatement();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			//Queries for row count
			String countQuery = "select COUNT(*) FROM " + viewName;
			logger.debug("Executing row count query: "+countQuery);
			rs = stmt.executeQuery(countQuery);
			while (rs.next()) {
				rowCount = rs.getInt(1);
			}
			logger.debug(rowCount+" rows returned");
		} catch (SQLException sqle) {
			logger.error("Could not successfully query "+viewName);
			sqle.printStackTrace();
		} finally {
			// Close out connection
			try {
				rs.close();
				stmt.close();
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				logger.error("Unable to close database connection to "+viewName);
				e.printStackTrace();
			}
		}
		return rowCount;
	}
	
}
