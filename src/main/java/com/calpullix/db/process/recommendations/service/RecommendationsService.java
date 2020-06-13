package com.calpullix.db.process.recommendations.service;

import java.io.IOException;

public interface RecommendationsService {

	void processProfileRecommendationsDirectory(String path, Boolean isProductRecommendations);

	void createCustomersPromotionsPreferencesByProfileCSVProfile() throws IOException;

	void createCustomersProductPreferencesByProfileCSVFile() throws IOException;

	void createCustomerPromotionCSVFile() throws IOException;

	void createPromotionListCollaborativeFiltering() throws IOException;

	void createCustomerProductCSVFile(String[] headers, String path) throws IOException;

	void createProductListCSVFile();

	void createCustomerProductCSVFile();

	void createProductListCSVFile(String[] headers, String path) throws IOException;

}
