package com.calpullix.db.process.service;

import java.io.IOException;

public interface ForecastService {

	void watchSalesForecastMessagesFiles() throws IOException, InterruptedException;
	
	void createProductBranchSalesCSVFile() throws IOException;
			 
}
