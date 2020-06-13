package com.calpullix.db.process.regression.service;

import java.io.IOException;

public interface RegressionProcessService {

	void watchLogisticRegressionFiles() throws IOException, InterruptedException;

	void processLogisticRegressionFiles(String pathConfusion, String pathCsvClassification) throws IOException;

	void exportCustomersClassificationNull() throws IOException;

	void createCsvCustomerByProfileFile() throws IOException;

}
