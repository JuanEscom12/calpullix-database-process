package com.calpullix.db.process.knearest.service;

import java.io.IOException;

public interface KNearestService {

	void createCustomersKNearestCSVFileIdprofileNull();

	void createCustomersKNearestCSVFile();

	void createCustomersKNearestCSVFileIdprofileNull(String[] headers, String path) throws IOException;

	void createCustomersKNearestCSVFile(String[] headers, String path) throws IOException;

	void watchKNearestFiles() throws IOException, InterruptedException;

	void processKNearest(String pathClassificationFile, String pathIdCustomersFile, String pathKNearestAccuracyImage,
			String pathKValuesFile) throws IOException;

}
