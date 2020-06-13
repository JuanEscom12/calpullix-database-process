package com.calpullix.db.process.kmeans.service;

import java.io.IOException;

public interface KMeansService {

	void watchKMeansFiles() throws IOException, InterruptedException;
	
	void processKmeans(String pathFile, String pathImage);
	
}
