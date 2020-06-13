package com.calpullix.db.process.util.service;

import java.io.IOException;
import java.util.List;

import org.apache.commons.csv.CSVRecord;

public interface UtilService {

	List<List<String>> processCsvFileByIndex(String pathFile, List<Integer> index);
	
	Iterable<CSVRecord> readCsvFileByIndex(String path);
	
	List<List<String>> processCsvFile(String[] headers, String pathFile);
	
	Iterable<CSVRecord> readCsvFile(String[] headers, String path);
	
	List<String> readFile(String pathFile) throws IOException;
	
	void moveFile(String pathFile);
	
}
