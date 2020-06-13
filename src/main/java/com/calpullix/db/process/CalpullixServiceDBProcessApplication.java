package com.calpullix.db.process;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.event.EventListener;

import com.calpullix.db.process.kmeans.service.KMeansService;
import com.calpullix.db.process.knearest.service.KNearestService;
import com.calpullix.db.process.recommendations.service.RecommendationsService;
import com.calpullix.db.process.regression.service.RegressionProcessService;
import com.calpullix.db.process.service.DataBaseProcessService;
import com.calpullix.db.process.service.ForecastService;
import com.calpullix.db.process.statistics.service.ProcessStatisticsService;
import com.calpullix.db.process.twitter.service.TwitterProcessService;

import lombok.extern.slf4j.Slf4j;

@SpringBootApplication
@EnableAutoConfiguration(exclude = RabbitAutoConfiguration.class)
@ComponentScan("com.calpullix")
@Slf4j
@SuppressWarnings("unused")
public class CalpullixServiceDBProcessApplication {
	
	private static final String BASIS_DIRECTORY_PRODUCT_RECOMMENDATIONS = "/Users/juancarlospedrazaalcala/Documents/TT/CSV_Content_Based_Recommendation_OUTPUT";

	private static final String BASIS_DIRECTORY_PROMOTION_RECOMMENDATIONS = "/Users/juancarlospedrazaalcala/Documents/TT/Collaborative_Filtering_OUTPUT";

	@Autowired
	private DataBaseProcessService dataBaseProcessService;

	@Autowired
	private ForecastService processFilesService;
	
	@Autowired
	private ProcessStatisticsService processStatisticsService;
	
	@Autowired
	private KMeansService kMeansService;
	
	@Autowired
	private KNearestService kNearestService;
	
	@Autowired
	private RecommendationsService recommendationsService;
	
	@Autowired
	private RegressionProcessService regressionService;
	
	@Autowired
	private TwitterProcessService twitterProcessService;
	
	
	public static void main(String[] args) {
		SpringApplication.run(CalpullixServiceDBProcessApplication.class, args);
	}
			
	
	@EventListener(ApplicationReadyEvent.class)
	public void executeAfterStartup() {
		log.info(":: Starting actions after start up ");
		// Steps: Create a CSV file from database with proper filters and correct layout
		// name of the file.
		// Execute Jupyter NooteBook.
		// Create a process to persist information from Jupyter into the Data Base.
		// Test DB information with app.

		// Create a CSV file.
		// createCsvExportDataBaseFile();
		// Process all Reports in a specific Directory
		// Layout Report Name:
		// boxplot_branch_NoBranch_year_NoYear_product_NoProduct_month_NoMonth.png;
		
//		processStatisticsInformation();
//		try {
//			watchStatisticsFiles();
//		} catch (IOException | InterruptedException e) {
//			log.error(":: Error watching statistics file ", e);
//		}
//	   createCustomersCSVFile();
//		try {
//			watchKMeansFiles();
//		} catch (IOException | InterruptedException e) {
//			log.error(":: Error watching K-Means file ", e);
//		}
//		try {
//			watchTwitterFiles();
//		} catch (IOException | InterruptedException e) {
//			log.error(":: Error watching Twitter files ", e);
//		}
//		try {
//			watchTwitterMessagesFiles();
//		} catch (IOException | InterruptedException e) {
//			log.error(":: Error watching twitter message files ", e);
//		}
//		createCustomersKNearestCSVFileIdprofileNull();
//		createCustomersKNearestCSVFile();
//		try {
//			watchKNearestFiles();
//		} catch (IOException | InterruptedException e) {
//			log.error(":: Error watching K-Nearest files ", e);
//		}
//		createProductListCSVFile();
//		createCustomerProductCSVFile();
//		try {
//			processFilesService.createCustomersProductPreferencesByProfileCSVFile();
//		} catch (IOException e) {
//			log.error(":: Error writing CustomersProductPreferencesByProfileCSVFile ", e);
//		}
//		processFilesService.processProfileRecommendationsDirectory(BASIS_DIRECTORY_PRODUCT_RECOMMENDATIONS, Boolean.TRUE);
		
//		try {
//			processFilesService.createPromotionListCollaborativeFiltering();
//		} catch (IOException e) {
//			log.error(":: Error writing product list collaborative filtering file  ", e);
//		}
//		try {
//			processFilesService.createCustomerPromotionCSVFile();
//		} catch (IOException e) {
//			log.error(":: Error writing promotion list collaborative filtering file  ", e);
//		}
//		try {
//			processFilesService.createCustomersPromotionsPreferencesByProfileCSVProfile();
//		} catch (IOException e) {
//			log.error(":: Error writing CustomersPromotionPreferencesByProfileCSVFile ", e);
//		}
//		processFilesService.processProfileRecommendationsDirectory(BASIS_DIRECTORY_PROMOTION_RECOMMENDATIONS, Boolean.FALSE);
		
		
//		dataBaseProcessService.updateCustomerClassificationRandom();
//		dataBaseProcessService.updateCustomersClassificationInformation();
//		try {
//			processFilesService.createCsvCustomerByProfileFile();
//		} catch (IOException e) {
//			log.error(":: Error writing CsvCustomerByProfileFile ", e);
//		}
//		dataBaseProcessService.addRegressionCustomers();
//		try {
//			processFilesService.exportCustomersClassificationNull();
//		} catch (IOException e) {
//			log.error(":: Error writing CustomersClassificationNull ", e);
//		}
//		try {
//			processFilesService.watchLogisticRegressionFiles();
//		} catch (IOException | InterruptedException e) {
//			log.error(":: Error watching Logistic Regression Files ", e);
//		}
//		dataBaseProcessService.processOldProductHistoryRows();
//		dataBaseProcessService.processProductBranchOld();
//		try {
//			processFilesService.createProductBranchSalesCSVFile();
//		} catch (IOException e) {
//			log.error(":: Error writing ProductBranchSalesCSVFile ", e);
//		}
//		try {
//			processFilesService.watchSalesForecastMessagesFiles();
//		} catch (IOException | InterruptedException e) {
//			log.error(":: Error watching SalesForecastMessagesFiles ", e);
//		}
		
		
		
//	   dataBaseProcessService.processCustomerSex();
//	   dataBaseProcessService.processCustomerJobAndMunicipality();
//	   dataBaseProcessService.createCustomersKNearest();
//	   dataBaseProcessService.processNewProducts();
//	   dataBaseProcessService.processQuantityLowerLimitNewProducts();
//	   dataBaseProcessService.processNewProductHistory();
//	   dataBaseProcessService.processNewProductBranch();
//	   dataBaseProcessService.addCustomersProductBranch();
//	   dataBaseProcessService.processPromotionsNewProducts();
//	   dataBaseProcessService.renamePromotions();
	
	   
//	   dataBaseProcessService.updateImagesPromotions();
//	   dataBaseProcessService.processImageProduct();
//	   dataBaseProcessService.processImageBranch();
//	   dataBaseProcessService.processKmeansImage();
//	   dataBaseProcessService.processProfileKNearestImage();	
//	   dataBaseProcessService.processProfileRegressionImage();
//	   try {
//		dataBaseProcessService.processImageTwitterProfile();
//	   } catch (Exception e) {
//		log.error(":: Error processing twitter image profile ", e);
//	   }
//	   dataBaseProcessService.processRegressionImage();
//	   dataBaseProcessService.processStatisticsBloxPlot();
//	   dataBaseProcessService.processHeatMapImage();
//	   dataBaseProcessService.processKMeansGraphic();
		

		
//	   dataBaseProcessService.processCatalogs();
//	   dataBaseProcessService.processEmployee();
//	   dataBaseProcessService.processRestEmployees();
//	   dataBaseProcessService.processBranch();
//	   dataBaseProcessService.processBranchServices();
//	   dataBaseProcessService.processBranchDeduction();
//	   dataBaseProcessService.processBranchEmployee();
//	   dataBaseProcessService.processProvider();
//	   dataBaseProcessService.processProduct();
//	   dataBaseProcessService.processProductHistory();
//	   dataBaseProcessService.processDataSheet();
//	   dataBaseProcessService.processDistributionCenter();
//	   dataBaseProcessService.processProductBranch();
//	   dataBaseProcessService.processCustomerProfile();
//	   dataBaseProcessService.processCustomer();
//	   dataBaseProcessService.processCustomerSales();
//	   dataBaseProcessService.processPromotions();
//	   dataBaseProcessService.processPromotionsSales();
//	   dataBaseProcessService.processProductBranchOtherStatus();
//	   dataBaseProcessService.processProfilePromotions();
//	   dataBaseProcessService.processProfileKMeans();
//	   dataBaseProcessService.processProfileKNearest();
//	   dataBaseProcessService.processProfileRegression();
//	   dataBaseProcessService.processProductRecomendationProfile();
//	   dataBaseProcessService.processPromotionsRecomendationProfile(); --> No execute
//	   dataBaseProcessService.processStatistics();
//	   dataBaseProcessService.processStatisticsVariableRelation();
//	   dataBaseProcessService.processStatisticsHeatMap();
//	   dataBaseProcessService.processStatisticsCorrelation();
//	   dataBaseProcessService.processStatisticsCorrelationVariableRelation();
//	   dataBaseProcessService.processTwitter();
//	   dataBaseProcessService.processTwitterMessages();
//	   dataBaseProcessService.processRegression();
//	   dataBaseProcessService.processStatisticsAnova();
//	   dataBaseProcessService.processBloxPlot();

//	 dataBaseProcessService.processPurchaseOrder(); --> Ejecutar tarea programada
//	 dataBaseProcessService.processProductPurchaseOrderQuantity();
		
		
//	 dataBaseProcessService.addIncomeAndDebtCustomerInformation();  	

		
//	 dataBaseProcessService.processSales();--
//	 dataBaseProcessService.processCustomersPromotions();--
//	 dataBaseProcessService.processProductCurrentYear();--
	}

}
