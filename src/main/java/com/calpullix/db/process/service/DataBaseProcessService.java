package com.calpullix.db.process.service;

/**
 * 
 * @author juancarlospedrazaalcala
 *
 */
public interface DataBaseProcessService {

	void processCustomerSex();
	
	void processCustomerJobAndMunicipality();
	
	void processNewProducts();
	
	void processQuantityLowerLimitNewProducts();
	
	void processNewProductHistory();
	
	void processNewProductBranch();
	
	void processImageTwitterProfile() throws Exception;
	
	void processStatisticsBloxPlot();
	
	void processHeatMapImage();
	
	void processKMeansGraphic();
	
	void addCustomersProductBranch();
	
	void createCustomersKNearest();
	
	void testRepository();
	
	void processImageProduct();
	
	void processImageBranch();
	
	void processKmeansImage();
	
	void processProfileKNearestImage();
	
	void processProfileRegressionImage();
	
	void processTwitterImage();
	
	void processRegressionImage();
	
	void replaceCollate();
	
	void addIncomeAndDebtCustomerInformation();
	
	void updateImagesPromotions();
	
	void createDefaultDB();
	
	void processCatalogs();
	
	void processEmployee();
	
	void processRestEmployees();
	
	void processBranch();
	
	void processBranchServices();
	
	void processBranchDeduction();
	
	void processBranchEmployee();
	
	void processProvider();
	
	void processProduct();
	
	void processProductPurchaseOrderQuantity();
	
	void processProductHistory();
	
	void processDataSheet();
	
	void processDistributionCenter();
	
	void processProductBranch();
	
	void processProductBranchOtherStatus();
	
	void processPurchaseOrder();
	
	void processPromotions();
	
	void processCustomer();
	
	void processCustomersPromotions();
	
	void processProfilePromotions();
	
	void processProfileKMeans();
	
	void processProfileKNearest();
	
	void processProfileRegression();
	
	void processProductRecomendationProfile();
	
	void processPromotionsRecomendationProfile();
	
	void processSales();
	
	void processStatisticsVariableRelation();
	
	void processStatistics();
	
	void processStatisticsHeatMap();
	
	void processStatisticsCorrelationVariableRelation();
	
	void processStatisticsCorrelation();
	
	void processStatisticsAnova();
	
	void processTwitter();
	
	void processTwitterMessages();
	
	void processRegression();
	
	void processCustomerSales();
	
	void processPromotionsSales();
	
	void processCustomerProfile();
	
	void processBloxPlot();

	void processPromotionsNewProducts();
	
	void renamePromotions();
	
	void updateCustomersClassificationInformation();
	
	void updateCustomerClassificationRandom();
	
	void addRegressionCustomers();
	
	void processOldProductBranch();
	
	void processOldProductHistoryRows();
	
	void processProductBranchOld();
	
}
