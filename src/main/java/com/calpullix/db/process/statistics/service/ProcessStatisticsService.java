package com.calpullix.db.process.statistics.service;

import java.io.IOException;
import java.util.List;

public interface ProcessStatisticsService {

	void processStatisticsInformation();
	
	void watchStatisticsFiles() throws IOException, InterruptedException;
	 
	void processFile(String informationFileName);
	
	List<String> processFileNameInformation(String informationFileName);
	
	void processReports(String directory);

	void createCustomersCSVFile();

	void createCsvExportDataBaseFile();

	void createCSVFile(String[] headers, String path, Integer idBranch, Integer idProduct, String initDate,
			String endDate) throws IOException;

	void createCustomersCSVFile(String[] headers, String path) throws IOException;
}
