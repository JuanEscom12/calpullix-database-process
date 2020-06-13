package com.calpullix.db.process.service.impl;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Stream;

import javax.imageio.ImageIO;
import javax.sql.DataSource;

import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.stereotype.Service;

import com.calpullix.db.process.branch.model.Branch;
import com.calpullix.db.process.branch.model.BranchDeduction;
import com.calpullix.db.process.branch.model.BranchServices;
import com.calpullix.db.process.branch.repository.BranchDeductionRepository;
import com.calpullix.db.process.branch.repository.BranchRepository;
import com.calpullix.db.process.branch.repository.BranchServicesRepository;
import com.calpullix.db.process.catalog.model.BranchRegion;
import com.calpullix.db.process.catalog.model.Brand;
import com.calpullix.db.process.catalog.model.ContactType;
//import com.calpullix.db.process.catalog.model.CustomerProfile;
import com.calpullix.db.process.catalog.model.EducationLevel;
import com.calpullix.db.process.catalog.model.EmployeePosition;
import com.calpullix.db.process.catalog.model.MaritalStatus;
import com.calpullix.db.process.catalog.model.ProductBranchStatus;
import com.calpullix.db.process.catalog.model.ProductCategories;
import com.calpullix.db.process.catalog.model.ProductClassification;
import com.calpullix.db.process.catalog.model.ProductLocation;
import com.calpullix.db.process.catalog.model.ProductStatus;
import com.calpullix.db.process.catalog.model.PromotionsStatus;
import com.calpullix.db.process.catalog.model.PurchaseOrderStatus;
import com.calpullix.db.process.catalog.model.Services;
import com.calpullix.db.process.catalog.model.State;
import com.calpullix.db.process.catalog.model.TwitterTypeMessage;
import com.calpullix.db.process.catalog.model.WeightUnit;
import com.calpullix.db.process.customer.model.CustomerProfile;
import com.calpullix.db.process.customer.model.Customers;
import com.calpullix.db.process.customer.model.CustomersPromotions;
import com.calpullix.db.process.customer.repository.CustomerProfileRepository;
import com.calpullix.db.process.customer.repository.CustomerRepository;
import com.calpullix.db.process.customer.repository.CustomersPromotionsRepository;
import com.calpullix.db.process.employee.model.BranchEmployee;
import com.calpullix.db.process.employee.model.BranchEmployeeId;
import com.calpullix.db.process.employee.model.Employee;
import com.calpullix.db.process.employee.repository.BranchEmployeeRepository;
import com.calpullix.db.process.employee.repository.EmployeeRepository;
import com.calpullix.db.process.product.model.DataSheet;
import com.calpullix.db.process.product.model.DistributionCenter;
import com.calpullix.db.process.product.model.Product;
import com.calpullix.db.process.product.model.ProductBranch;
import com.calpullix.db.process.product.model.ProductHistory;
import com.calpullix.db.process.product.repository.DataSheetRepository;
import com.calpullix.db.process.product.repository.DistributionCenterRepository;
import com.calpullix.db.process.product.repository.ProductBranchRepository;
import com.calpullix.db.process.product.repository.ProductHistoryRepository;
import com.calpullix.db.process.product.repository.ProductRepository;
import com.calpullix.db.process.profile.model.ProductRecomendationProfile;
import com.calpullix.db.process.profile.model.ProfileKmeans;
import com.calpullix.db.process.profile.model.ProfileKnearest;
import com.calpullix.db.process.profile.model.ProfilePromotions;
import com.calpullix.db.process.profile.model.ProfileRegression;
import com.calpullix.db.process.profile.model.PromotionsRecomendationProfile;
import com.calpullix.db.process.profile.repository.ProductRecomendationProfileRepository;
import com.calpullix.db.process.profile.repository.ProfileKMeansRepository;
import com.calpullix.db.process.profile.repository.ProfileKNearestRepository;
import com.calpullix.db.process.profile.repository.ProfilePromotionsRepository;
import com.calpullix.db.process.profile.repository.ProfileRegressionRepository;
import com.calpullix.db.process.profile.repository.PromotionsRecomendationProfileRepository;
import com.calpullix.db.process.promotions.model.Promotions;
import com.calpullix.db.process.promotions.repository.PromotionsRepository;
import com.calpullix.db.process.provider.model.Provider;
import com.calpullix.db.process.provider.repository.ProviderRepository;
import com.calpullix.db.process.purchaseorder.model.Purchaseorder;
import com.calpullix.db.process.purchaseorder.repository.PurchaseOrderRepository;
import com.calpullix.db.process.sales.model.Sales;
import com.calpullix.db.process.sales.repository.SalesRepository;
import com.calpullix.db.process.service.DataBaseProcessService;
import com.calpullix.db.process.statistics.box.plot.StatisticsBloxPlot;
import com.calpullix.db.process.statistics.box.plot.repository.StatisticsBloxPlotRepository;
import com.calpullix.db.process.statistics.model.Statistics;
import com.calpullix.db.process.statistics.model.StatisticsAnova;
import com.calpullix.db.process.statistics.model.StatisticsCorrelation;
import com.calpullix.db.process.statistics.model.StatisticsCorrelationVariableRelation;
import com.calpullix.db.process.statistics.model.StatisticsHeatmap;
import com.calpullix.db.process.statistics.model.StatisticsVariableRelation;
import com.calpullix.db.process.statistics.repository.StatisticsAnovaRepository;
import com.calpullix.db.process.statistics.repository.StatisticsCorrelationRepository;
import com.calpullix.db.process.statistics.repository.StatisticsCorrelationVariableRelationRepository;
import com.calpullix.db.process.statistics.repository.StatisticsHeatMapRepository;
import com.calpullix.db.process.statistics.repository.StatisticsRepository;
import com.calpullix.db.process.statistics.repository.StatisticsVariableRelationRepository;
import com.calpullix.db.process.twitter.model.Twitter;
import com.calpullix.db.process.twitter.model.TwitterMessages;
import com.calpullix.db.process.twitter.repository.TwitterMessagesRepository;
import com.calpullix.db.process.twitter.repository.TwitterRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class DataBaseProcessServiceImpl implements DataBaseProcessService {

	private static final String PATH_IMAGE_BRANCHES = "/Users/juancarlospedrazaalcala/Documents/WorkSpaceServices"
			+ "/DataBaseProcess/calpullix-database-process/pictures/";

	private static final String PATH_IMAGE_PRODUCT = "/Users/juancarlospedrazaalcala/Documents/TT/Product_Images/";

	private static final String PATH_IMAGE_PROMOTIONS = "/Users/juancarlospedrazaalcala/Documents/TT/Promotions/promotion_";
	
	private static final String PATH = "/Users/juancarlospedrazaalcala/Documents/WorkSpaceServices/DataBaseProcess"
			+ "/calpullix-database-process/src/main/java/com/calpullix/db/process/service/impl/";

	private static final String PATH_ALL_PRODUCTS_HISTORY = "/Users/juancarlospedrazaalcala/Documents/WorkSpaceServices"
			+ "/DataBaseProcess/calpullix-database-process/src/main/java/com/calpullix/db/process/service/impl/ProductHistoryAll.txt";

	private static final String JPEG_EXTENSION = "jpeg";

	private static final String BRANCH_NAME = "branch_";

	private static final String BRANCH_NAME_COPY = "branchCopy_";

	private static final String FULL_STOP = ".";

	private static final String BASE_AMOUNT_MEDICAL_DEDUCTION = "200.50";

	private static final String BASE_AMOUNT_CONTRIBUTION_DEDUCTION = "1200.80";

	private static final String BASE_AMOUNT_COMPUTER_EQUIPMENT_DEDUCTION = "1500.50";

	private static final String BASE_AMOUNT_MORGAGE_DEDUCTION = "3000";

	private static final String BASE_AMOUNT_BRANCH_SERVICE = "300.70";

	private static final String BAR_CODE_IMG_NAME = "bar_code.png";

	private static final String BAR_CODE_COPY_IMG_NAME = "bar_code_copy.png";

	private static final String PNG_EXTENSION = "png";

	private static final String PRODUCT_FILE = "Product.txt";

	private static final String ALTER_PRODUCTS_FILE = "AlterProducts.txt";

	private static final String PRODUCT_HISTORY_FILE = "ProductHistory.txt";

	private static final String PRODUCT_HISTORY_NEW_FILE = "ProductHistoryNew.txt";

	private static final String TOKEN_PIPE = "|";

	private static final String IVA = "0.16";

	private static final String SUFFIX_TWO = "2";

	private static final String PROVIDER_FILE = "Provider.txt";

	private static final String BRANCH_FILE = "Branch.txt";

	private static final String BASE_DEDUCTION = "15400.70";

	private static final String BASE_RENT = "30000";

	private static final String MALE = "H";

	private static final String FEMALE = "M";

	private static final String FILE_TEXT_EMPLOYEE = "Employee.txt";

	private static final String TOKEN_COMA = ",";

	private static final int NUMBER_BRANCHES = 10;

	private static final int IMG_WIDTH = 100;

	private static final int IMG_HEIGHT = 100;

	private static final int NUMBER_PROFILE_PROMOTIONS = 10;

	private static final int ID_PRODUCT = 30;

	private static final double APPROXIMATE_PROMOTIONS_PERCENTAGE = 0.40;

	@Autowired
	private EmployeeRepository employeeRepository;

	@Autowired
	private BranchRepository branchRepository;

	@Autowired
	private BranchServicesRepository branchServicesRepository;

	@Autowired
	private BranchDeductionRepository branchDeductionRepository;

	@Autowired
	private BranchEmployeeRepository branchEmployeeRepository;

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private ProductHistoryRepository productHistoryRepository;

	@Autowired
	private ProviderRepository providerRepository;

	@Autowired
	private DataSheetRepository dataSheetRepository;

	@Autowired
	private ProductBranchRepository productBranchRepository;

	@Autowired
	private DistributionCenterRepository distributionCenterRepository;

	@Autowired
	private PurchaseOrderRepository purchaseOrderRepository;

	@Autowired
	private PromotionsRepository promotionsRepository;

	@Autowired
	private CustomerRepository customerRepository;

	@Autowired
	private CustomersPromotionsRepository customersPromotionsRepository;

	@Autowired
	private ProfilePromotionsRepository profilePromotionsRepository;

	@Autowired
	private ProfileKMeansRepository profileKMeansRepository;

	@Autowired
	private ProfileKNearestRepository profileKNearestRepository;

	@Autowired
	private ProductRecomendationProfileRepository productRecomendationProfileRepository;

	@Autowired
	private PromotionsRecomendationProfileRepository promotionsRecomendationProfileRepository;

	@Autowired
	private SalesRepository salesRepository;

	@Autowired
	private StatisticsVariableRelationRepository statisticsVariableRelationRepository;

	@Autowired
	private StatisticsRepository statisticsRepository;

	@Autowired
	private StatisticsHeatMapRepository statisticsHeatMapRepository;

	@Autowired
	private StatisticsCorrelationVariableRelationRepository statisticsCorrelationVariableRelationRepository;

	@Autowired
	private StatisticsCorrelationRepository statisticsCorrelationRepository;

	@Autowired
	private StatisticsAnovaRepository statisticsAnovaRepository;

	@Autowired
	private TwitterRepository twitterRepository;

	@Autowired
	private TwitterMessagesRepository twitterMessagesRepository;

	@Autowired
	private CustomerProfileRepository customerProfileRepository;

	@Autowired
	private ProfileRegressionRepository profileRegressionRepository;

	@Autowired
	private StatisticsBloxPlotRepository statisticsBloxPlotRepository;

	@Autowired
	private DataSource dataSource;

	private SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");

	private SimpleDateFormat formatTimestamp = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SS");

	
	@Override
	public void testRepository() {
		Pageable pagination = PageRequest.of(0, 5);
		Branch idbranch = new Branch();
		idbranch.setId(5);
		CompletableFuture<List<Product>> topProduct = productRepository.getProductTopFiveBySaledateAndIdbranchAndStatus(
				"2019-01-01", "2019-12-31", idbranch, ProductBranchStatus.SOLD.getId(), pagination);
		try {
			log.info(":: Top products {} ", topProduct.get().get(0).getId());
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void replaceCollate() {
		try {
			@SuppressWarnings("resource")
			Stream<String> stream = Files
					.lines(Paths.get("/Users/juancarlospedrazaalcala/Documents/WorkSpaceServices/DataBaseProcess/"
							+ "calpullix-database-process/src/main/resources/CURRENT/CALPULLIX_VIRTUAL_MANAGER_ASSISTANT_Branch_Deduction.sql"));
			try (PrintWriter pw = new PrintWriter("output.txt", "UTF-8")) {
				stream.map(s -> s.replaceAll("CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;",
						"CHARSET=utf8 COLLATE=utf8_general_ci;")).forEachOrdered(pw::println);
			}
		} catch (Exception e) {
			log.error(":: Error ", e);
		}
	}

	@Override
	public void updateCustomersClassificationInformation() {
		Integer age = 18;
		Integer endage = 50;
		String sex = "H";
		List<Integer> educationlevelvalue = new ArrayList<>();
		educationlevelvalue.add(1);
		educationlevelvalue.add(2);
		educationlevelvalue.add(3);

		List<Integer> statevalue = new ArrayList<>();

		statevalue.add(9);
		statevalue.add(22);
		statevalue.add(21);

		List<Integer> maritalstatusvalue = new ArrayList<>();
		maritalstatusvalue.add(1);
		maritalstatusvalue.add(2);
		maritalstatusvalue.add(3);
		maritalstatusvalue.add(4);

		List<String> jobs = new ArrayList<>();
		jobs.add("Administrativo");
		jobs.add("Ejecutivo de Ventas");
		jobs.add("Almacenista");
		jobs.add("Asesor financiero");
		jobs.add("Recepcionista");
		jobs.add("Carpintero");
		jobs.add("Construcción");
		jobs.add("Plomeria");

		List<Customers> customers = customerRepository.findAll();
		for (final Customers customer : customers) {
			customer.setClassification(1);
			customerRepository.save(customer);
		}

		customers = customerRepository.findAllByAgeAndSexAndEducationlevelAndStateAndMaritalstatusAndJob(age, endage,
				sex, educationlevelvalue, statevalue, maritalstatusvalue, jobs);
		log.info(":: Customers {} ", customers.size());
		for (final Customers customer : customers) {
			customer.setClassification(0);
			customerRepository.save(customer);
		}
	}

	@Override
	public void addRegressionCustomers() {
		// Se agregan 20 customers con valores para la clasificacion 1.
		// Identificar el perfil
		List<String> names = new ArrayList<>();
		names.add("Jorge Lugo Mendez");
		names.add("Carlos Velez Domingo");
		names.add("Sergio Jurado Hernandez");
		names.add("Omar Centeno Vidal");
		names.add("Antonio Castro Ferrer");
		names.add("Fernando Ledezma Orduñez");
		names.add("Daniel Bermudez Caso");
		names.add("Johan Mayen Duran");
		names.add("Carlos Mar Perez");
		names.add("Pedro Cruz Eguia");
		names.add("Graciela Morlan Baldobinos");
		names.add("Maria Castro Solis");
		names.add("Gabriela Aremndariz Gomez");
		names.add("Karina Roldan Graciela");
		names.add("Kymberly Chavez Granados");

		names.add("Antonio Garza Nuñez");
		names.add("Gutierrez Manriquez");
		names.add("Rodrigo Nieves Maya");
		names.add("Jaime Covarrubias Lopez");
		names.add("Sergio Luengas Bustamante");
		names.add("Ricardo Garcia Anatole");
		names.add("Mariana Romo Vivar");
		names.add("Guadalupe Villegas Cortes");
		names.add("Tania Morelos Chaidez");
		names.add("Marisol Ramirez Cruz");
		names.add("Pedro Illezcas Gonzalez");
		names.add("Tatiana Gil Alanis");
		names.add("Euridice Torres Alamos");
		names.add("Ana Valencia Garcia");
		names.add("Valentin Trujillo Mata");

		names.add("Victor Campos Barranco");
		names.add("Florencio Cruz Villaseñor");
		names.add("Alfredo Figueroa Ramos");
		names.add("Alejandra Barrios Uribe");
		names.add("Guillermo Ramirez Cuevas");

		final List<String> menNames = new ArrayList<String>();
		menNames.add("Jorge Lugo Mendez");
		menNames.add("Carlos Velez Domingo");
		menNames.add("Sergio Jurado Hernandez");
		menNames.add("Omar Centeno Vidal");
		menNames.add("Antonio Castro Ferrer");
		menNames.add("Fernando Ledezma Orduñez");
		menNames.add("Daniel Bermudez Caso");
		menNames.add("Johan Mayen Duran");
		menNames.add("Carlos Mar Perez");
		menNames.add("Pedro Cruz Eguia");
		menNames.add("Antonio Garza Nuñez");
		menNames.add("Gutierrez Manriquez");
		menNames.add("Rodrigo Nieves Maya");
		menNames.add("Jaime Covarrubias Lopez");
		menNames.add("Sergio Luengas Bustamante");
		menNames.add("Ricardo Garcia Anatole");
		menNames.add("Pedro Illezcas Gonzalez");
		menNames.add("Valentin Trujillo Mata");
		menNames.add("Victor Campos Barranco");
		menNames.add("Florencio Cruz Villaseñor");
		menNames.add("Alfredo Figueroa Ramos");
		menNames.add("Guillermo Ramirez Cuevas");

		final List<String> womenNames = new ArrayList<String>();
		womenNames.add("Graciela Morlan Baldobinos");
		womenNames.add("Maria Castro Solis");
		womenNames.add("Gabriela Aremndariz Gomez");
		womenNames.add("Karina Roldan Graciela");
		womenNames.add("Kymberly Chavez Granados");
		womenNames.add("Mariana Romo Vivar");
		womenNames.add("Guadalupe Villegas Cortes");
		womenNames.add("Tania Morelos Chaidez");
		womenNames.add("Marisol Ramirez Cruz");
		womenNames.add("Tatiana Gil Alanis");
		womenNames.add("Euridice Torres Alamos");
		womenNames.add("Ana Valencia Garcia");
		womenNames.add("Alejandra Barrios Uribe");

		List<String> municipalities = new ArrayList<>();
		municipalities.add("Pinal de Amoles");
		municipalities.add("Corregidora");
		municipalities.add("Ezequiel Montes");
		municipalities.add("Huimilpan");
		municipalities.add("Colón");
		municipalities.add("Cadereyta de Montes");
		municipalities.add("Arroyo Seco");
		municipalities.add("Amealco de Bonfil");
		municipalities.add("El Marqués");
		municipalities.add("Pedro Escobedo");
		municipalities.add("Peñamiller");
		municipalities.add("Querétaro");
		municipalities.add("San Joaquín	");
		municipalities.add("San Juan del Río");
		municipalities.add("Tequisquiapan");
		municipalities.add("Tolimán");
		municipalities.add("Miguel Hidalgo");
		municipalities.add("Coyoacán");
		municipalities.add("Cuauhtémoc");
		municipalities.add("Azcapotzalco");
		municipalities.add("Tlalpan");
		municipalities.add("Cuajimalpa");
		municipalities.add("Iztacalco");
		municipalities.add("Venustiano Carranza");
		municipalities.add("La Magdalena Contreras");
		municipalities.add("Xochimilco");
		municipalities.add("Gustavo A. Madero");
		municipalities.add("Álvaro Obregón");
		municipalities.add("Iztapalapa");
		municipalities.add("Tláhuac");
		municipalities.add("Milpa Alta");
		municipalities.add("Ahuatlán");
		municipalities.add("Amozoc");
		municipalities.add("Aquixtla");
		municipalities.add("Coyotepec");
		municipalities.add("Jalpan");
		municipalities.add("Nicolás Bravo");
		municipalities.add("San Andrés Cholula");
		municipalities.add("Puebla");
		municipalities.add("Palmar de Bravo");

		List<String> jobs = new ArrayList<>();
		jobs.add("Contador Público");
		jobs.add("Construcción");
		jobs.add("Abogado");
		jobs.add("Comerciante");
		jobs.add("Ingeniero");
		jobs.add("Administrativo");
		jobs.add("Ejecutivo de Ventas");
		jobs.add("Plomeria");
		jobs.add("Almacenista");
		jobs.add("Asesor financiero");

		jobs.add("Recepcionista");
		jobs.add("Asistente de Dirección");
		jobs.add("Enfermero");
		jobs.add("Maestro");
		jobs.add("Carpintero");

		List<Customers> customers = new ArrayList<>();
		Customers customer;
		int[] states = { 21, 22, 9, 8 };
		for (int index = 0; index < 20; index++) {
			customer = new Customers();
			customer.setAge(ThreadLocalRandom.current().nextInt(18, 51));
			customer.setEducationlevel(EducationLevel.of(ThreadLocalRandom.current().nextInt(1, 4)));

			customer.setJob(jobs.get(ThreadLocalRandom.current().nextInt(0, 15)));
			customer.setMaritalstatus(MaritalStatus.of(ThreadLocalRandom.current().nextInt(1, 5)));
			customer.setMunicipality(municipalities.get(ThreadLocalRandom.current().nextInt(0, 40)));
			customer.setName(names.get(ThreadLocalRandom.current().nextInt(0, 35)));
			if (womenNames.contains(customer.getName())) {
				customer.setSex(FEMALE);
			} else {
				customer.setSex(MALE);
			}
			customer.setState(State.of(states[ThreadLocalRandom.current().nextInt(0, 3)]));
			customers.add(customer);
			customerRepository.save(customer);
			log.info(":: Customer {} ", customer);
		}

		customers.forEach(item -> {
			item.setIncome(getIncome(item));
			item.setDebt(getDebt(item));
			customerRepository.save(item);
		});

		// Se agregan 20 customers con valores para la clasificacion 0.
		String sexInd = "H";
		List<Integer> educationlevelvalue = new ArrayList<>();
		educationlevelvalue.add(1);
		educationlevelvalue.add(2);
		educationlevelvalue.add(3);
		List<Integer> statevalue = new ArrayList<>();

		statevalue.add(9);
		statevalue.add(22);
		statevalue.add(21);

		List<Integer> maritalstatusvalue = new ArrayList<>();
		maritalstatusvalue.add(1);
		maritalstatusvalue.add(2);
		maritalstatusvalue.add(3);
		maritalstatusvalue.add(4);

		List<String> jobsInd = new ArrayList<>();
		jobsInd.add("Administrativo");
		jobsInd.add("Ejecutivo de Ventas");
		jobsInd.add("Almacenista");
		jobsInd.add("Asesor financiero");
		jobsInd.add("Recepcionista");
		jobsInd.add("Carpintero");
		jobsInd.add("Construcción");
		jobsInd.add("Plomeria");
		int[] statesInd = { 21, 22, 9 };

		customers.clear();
		for (int index = 0; index < 20; index++) {
			customer = new Customers();
			customer.setAge(ThreadLocalRandom.current().nextInt(18, 51));
			customer.setEducationlevel(EducationLevel
					.of(educationlevelvalue.get(ThreadLocalRandom.current().nextInt(0, educationlevelvalue.size()))));
			customer.setJob(jobsInd.get(ThreadLocalRandom.current().nextInt(0, jobsInd.size())));
			customer.setMaritalstatus(MaritalStatus
					.of(maritalstatusvalue.get(ThreadLocalRandom.current().nextInt(0, maritalstatusvalue.size()))));
			customer.setMunicipality(municipalities.get(ThreadLocalRandom.current().nextInt(0, municipalities.size())));
			customer.setName(menNames.get(ThreadLocalRandom.current().nextInt(0, menNames.size())));
			customer.setSex(sexInd);
			customer.setState(State.of(statesInd[ThreadLocalRandom.current().nextInt(0, statesInd.length)]));
			customers.add(customer);
			customerRepository.save(customer);
		}

		customers.forEach(item -> {
			item.setIncome(getIncome(item));
			item.setDebt(getDebt(item));
			customerRepository.save(item);
		});
	}

	@Override
	public void updateCustomerClassificationRandom() {
		List<Customers> customers = customerRepository.findAll();
		for (final Customers customer : customers) {
			customer.setClassification(ThreadLocalRandom.current().nextInt(0, 2));
			customerRepository.save(customer);
		}
	}

	@Override
	public void addCustomersProductBranch() {
		final List<ProductBranch> productBranch = productBranchRepository.findAllByIdcustomerIsNull();
		final List<Customers> customers = customerRepository.findAll();
		for (final ProductBranch item : productBranch) {
			item.setIdcustomer(customers.get(ThreadLocalRandom.current().nextInt(0, customers.size())));
			productBranchRepository.save(item);
		}
	}

	@Override
	public void processKMeansGraphic() {
		final List<ProfileKmeans> kmeans = profileKMeansRepository.findAll();
		for (final ProfileKmeans item : kmeans) {
			try {
				File image = new File(
						"/Users/juancarlospedrazaalcala/Documents/TT/BACK_UP/kmeans_scatterplot_profilecustomers_2020_4_5.png");
				item.setImage(Files.readAllBytes(image.toPath()));
				profileKMeansRepository.save(item);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void processNewProductBranch() {
		log.info(":: Process New Product Branch ");
		File image = null;
		try {
			processImage(PATH_IMAGE_PRODUCT + BAR_CODE_IMG_NAME, PATH_IMAGE_PRODUCT + BAR_CODE_COPY_IMG_NAME,
					PNG_EXTENSION);
			image = new File(PATH_IMAGE_PRODUCT + BAR_CODE_COPY_IMG_NAME);
		} catch (IOException e) {
			e.printStackTrace();
		}
		final List<Branch> branches = branchRepository.findAll();
		final List<DistributionCenter> distCenter = distributionCenterRepository.findAll();
		List<ProductHistory> productHistory;
		Calendar currentDate = Calendar.getInstance();
		Calendar date = Calendar.getInstance();
		Calendar auxDate = Calendar.getInstance();
		ProductBranch productBranch;
		Product idproduct = new Product();
		BigDecimal polinomy;
		BigDecimal sum;
		int year;
		int auxYear;
		idproduct.setId(ID_PRODUCT);

		// Por cada sucursal
		for (final Branch branch : branches) {
			log.info(":: Branch {} ", branch.getId());
			date.set(Calendar.YEAR, 2015);
			date.set(Calendar.MONTH, 0);
			date.set(Calendar.DATE, 1);
			year = date.get(Calendar.YEAR);
			productHistory = productHistoryRepository.findAllProductByYearAndIdproduct(year, idproduct);
			// Registro de ventas del periodo de cinco años por cada sucursal.
			while (currentDate.getTime().after(date.getTime())) {
				auxYear = date.get(Calendar.YEAR);
				if (auxYear != year) {
					year = auxYear;
					log.info(":: YEAR {} ", year);
					productHistory = productHistoryRepository.findAllProductByYearAndIdproduct(year, idproduct);
				}
				// Registro de ventas semanal.
				auxDate = (Calendar) date.clone();
				polinomy = getValuePolinomy(ThreadLocalRandom.current().nextInt(80, 140));
				sum = BigDecimal.ZERO;
				while (polinomy.compareTo(sum) == 1) {
					// Productos del año.
					for (final ProductHistory product : productHistory) {
						productBranch = new ProductBranch();
						productBranch.setIdbranch(branch);
						productBranch.setIdproducthistory(product);
						productBranch.setStatus(ProductBranchStatus.SOLD);
						auxDate.set(Calendar.DATE, ThreadLocalRandom.current().nextInt(date.get(Calendar.DATE),
								date.get(Calendar.DATE) + 6));
						productBranch.setSaledate(formatDate.format(auxDate.getTime()));
						auxDate.set(Calendar.DATE,
								auxDate.get(Calendar.DATE) - ThreadLocalRandom.current().nextInt(15, 30));
						productBranch.setCreationdate(formatDate.format(auxDate.getTime()));
						auxDate.set(Calendar.DATE, auxDate.get(Calendar.DATE) + 50);
						productBranch.setExpirationdate(formatDate.format(auxDate.getTime()));
						auxDate.set(Calendar.DATE, date.get(Calendar.DATE));
						auxDate.set(Calendar.MONTH, date.get(Calendar.MONTH));
						auxDate.set(Calendar.YEAR, date.get(Calendar.YEAR));
						productBranch.setDistributioncenter(
								distCenter.get(ThreadLocalRandom.current().nextInt(0, distCenter.size() - 1)));
						productBranch.setLocation(ProductLocation.SOLD);
						sum = sum.add(product.getSaleprice());
						try {
							productBranch.setBarcode(Files.readAllBytes(image.toPath()));
						} catch (IOException e) {
							e.printStackTrace();
						}
						productBranchRepository.save(productBranch);
					}
				}
				date.set(Calendar.DATE, date.get(Calendar.DATE) + 7);
			}

			// Otro ciclo para cubrir los dias restantes, si los hubiera.
			if (currentDate.get(Calendar.DATE) - date.get(Calendar.DATE) > 0) {
				auxDate = (Calendar) date.clone();
				polinomy = getValuePolinomy(120);
				sum = BigDecimal.ZERO;
				while (polinomy.compareTo(sum) == 1) {
					for (final ProductHistory product : productHistory) {
						// Un registro por cada producto
						productBranch = new ProductBranch();
						productBranch.setIdbranch(branch);
						productBranch.setIdproducthistory(product);
						productBranch.setStatus(ProductBranchStatus.SOLD);
						auxDate.set(Calendar.DATE, ThreadLocalRandom.current().nextInt(date.get(Calendar.DATE),
								date.get(Calendar.DATE) + currentDate.get(Calendar.DATE) - date.get(Calendar.DATE)));
						productBranch.setSaledate(formatDate.format(auxDate.getTime()));
						auxDate.set(Calendar.DATE,
								auxDate.get(Calendar.DATE) - ThreadLocalRandom.current().nextInt(15, 30));
						productBranch.setCreationdate(formatDate.format(auxDate.getTime()));
						auxDate.set(Calendar.DATE, auxDate.get(Calendar.DATE) + 50);
						productBranch.setExpirationdate(formatDate.format(auxDate.getTime()));
						auxDate.set(Calendar.DATE, date.get(Calendar.DATE));
						auxDate.set(Calendar.MONTH, date.get(Calendar.MONTH));
						auxDate.set(Calendar.YEAR, date.get(Calendar.YEAR));
						productBranch.setDistributioncenter(
								distCenter.get(ThreadLocalRandom.current().nextInt(0, distCenter.size() - 1)));
						productBranch.setLocation(ProductLocation.SOLD);
						sum = sum.add(product.getSaleprice());
						try {
							productBranch.setBarcode(Files.readAllBytes(image.toPath()));
						} catch (IOException e) {
							e.printStackTrace();
						}
						productBranchRepository.save(productBranch);
					}
				}
			}
		}

	}

	@Override
	public void processNewProducts() {
		final List<String> lines = readFile(PATH + ALTER_PRODUCTS_FILE);
		File image;
		StringTokenizer tokens;
		Product product;
		String file;
		String extension;
		List<Provider> provider = providerRepository.findAll();
		int count = 0;
		for (final String line : lines) {
			tokens = getStringTokenizer(line, TOKEN_PIPE);
			product = new Product();
			product.setName(tokens.nextToken());
			product.setDescription(tokens.nextToken());
			product.setBrand(Brand.of(Integer.valueOf(tokens.nextToken())));
			log.info(":: TOKEN {} ", ++count);
			product.setProvider(provider.get(ThreadLocalRandom.current().nextInt(0, provider.size() - 1)));
			product.setWeight(new BigDecimal(tokens.nextToken()));
			product.setWeightunit(WeightUnit.of(Integer.valueOf(tokens.nextToken())));
			product.setIndividualPackaging(Boolean.valueOf(tokens.nextToken()));
			product.setCofeprisPermission(Boolean.valueOf(tokens.nextToken()));
			product.setFragileMaterial(Boolean.valueOf(tokens.nextToken()));
			product.setCategory(ProductCategories.of(Integer.valueOf(tokens.nextToken())));
			product.setMeasurements(tokens.nextToken());
			try {
				file = tokens.nextToken();
				extension = tokens.nextToken();
				processImage(PATH_IMAGE_PRODUCT + file + FULL_STOP + extension,
						PATH_IMAGE_PRODUCT + file + SUFFIX_TWO + FULL_STOP + extension, extension);
				image = new File(PATH_IMAGE_PRODUCT + file + SUFFIX_TWO + FULL_STOP + extension);
				product.setImage(Files.readAllBytes(image.toPath()));
			} catch (IOException e) {
				e.printStackTrace();
			}
			productRepository.save(product);
			log.info(":: New Product {} ", product);
		}
	}

	@Override
	public void processQuantityLowerLimitNewProducts() {
		final List<Product> products = productRepository.findAllByQuantitylowerlimitIsNull();
		for (final Product product : products) {
			product.setQuantitylowerlimit(ThreadLocalRandom.current().nextInt(2200, 5900));
			productRepository.save(product);
		}
	}

	@Override
	public void processCustomerSex() {
		final List<String> menNames = new ArrayList<String>();
		menNames.add("Jorge Lugo Mendez");
		menNames.add("Carlos Velez Domingo");
		menNames.add("Sergio Jurado Hernandez");
		menNames.add("Omar Centeno Vidal");
		menNames.add("Antonio Castro Ferrer");
		menNames.add("Fernando Ledezma Orduñez");
		menNames.add("Daniel Bermudez Caso");
		menNames.add("Johan Mayen Duran");
		menNames.add("Carlos Mar Perez");
		menNames.add("Pedro Cruz Eguia");
		menNames.add("Antonio Garza Nuñez");
		menNames.add("Gutierrez Manriquez");
		menNames.add("Rodrigo Nieves Maya");
		menNames.add("Jaime Covarrubias Lopez");
		menNames.add("Sergio Luengas Bustamante");
		menNames.add("Ricardo Garcia Anatole");
		menNames.add("Pedro Illezcas Gonzalez");
		menNames.add("Valentin Trujillo Mata");
		menNames.add("Victor Campos Barranco");
		menNames.add("Florencio Cruz Villaseñor");
		menNames.add("Alfredo Figueroa Ramos");
		menNames.add("Guillermo Ramirez Cuevas");

		final List<String> womenNames = new ArrayList<String>();
		womenNames.add("Graciela Morlan Baldobinos");
		womenNames.add("Maria Castro Solis");
		womenNames.add("Gabriela Aremndariz Gomez");
		womenNames.add("Karina Roldan Graciela");
		womenNames.add("Kymberly Chavez Granados");
		womenNames.add("Mariana Romo Vivar");
		womenNames.add("Guadalupe Villegas Cortes");
		womenNames.add("Tania Morelos Chaidez");
		womenNames.add("Marisol Ramirez Cruz");
		womenNames.add("Tatiana Gil Alanis");
		womenNames.add("Euridice Torres Alamos");
		womenNames.add("Ana Valencia Garcia");
		womenNames.add("Alejandra Barrios Uribe");

		final List<Customers> customers = customerRepository.findAll();

		for (final Customers customer : customers) {
			if (menNames.contains(customer.getName())) {
				customer.setSex("H");
			} else {
				customer.setSex("M");
			}
			customerRepository.save(customer);
		}
	}

	@Override
	public void processCustomerJobAndMunicipality() {
		List<String> jobs = new ArrayList<>();
		jobs.add("Contador Público");
		jobs.add("Abogado");
		jobs.add("Maestro");
		jobs.add("Ingeniero");
		jobs.add("Asistente de Dirección");
		jobs.add("Enfermero");

		jobs.add("Administrativo");
		jobs.add("Ejecutivo de Ventas");
		jobs.add("Almacenista");
		jobs.add("Asesor financiero");
		jobs.add("Recepcionista");

		jobs.add("Carpintero");
		jobs.add("Construcción");
		jobs.add("Comerciante");
		jobs.add("Plomeria");

		List<String> municipalities = new ArrayList<>();
		municipalities.add("Pinal de Amoles");
		municipalities.add("Corregidora");
		municipalities.add("Ezequiel Montes");
		municipalities.add("Huimilpan");
		municipalities.add("Colón");
		municipalities.add("Cadereyta de Montes");
		municipalities.add("Arroyo Seco");
		municipalities.add("Amealco de Bonfil");
		municipalities.add("El Marqués");
		municipalities.add("Pedro Escobedo");
		municipalities.add("Peñamiller");
		municipalities.add("Querétaro");
		municipalities.add("San Joaquín	");
		municipalities.add("San Juan del Río");
		municipalities.add("Tequisquiapan");
		municipalities.add("Tolimán");

		municipalities.add("Miguel Hidalgo");
		municipalities.add("Coyoacán");
		municipalities.add("Cuauhtémoc");
		municipalities.add("Azcapotzalco");
		municipalities.add("Tlalpan");
		municipalities.add("Cuajimalpa");
		municipalities.add("Iztacalco");
		municipalities.add("Venustiano Carranza");
		municipalities.add("La Magdalena Contreras");
		municipalities.add("Xochimilco");
		municipalities.add("Gustavo A. Madero");
		municipalities.add("Álvaro Obregón");
		municipalities.add("Iztapalapa");
		municipalities.add("Tláhuac");
		municipalities.add("Milpa Alta");

		municipalities.add("Ahuatlán");
		municipalities.add("Amozoc");
		municipalities.add("Aquixtla");
		municipalities.add("Coyotepec");
		municipalities.add("Jalpan");
		municipalities.add("Nicolás Bravo");
		municipalities.add("San Andrés Cholula");
		municipalities.add("Puebla");
		municipalities.add("Palmar de Bravo");

		municipalities.add("Apozol");
		municipalities.add("Apulco");
		municipalities.add("Charo");
		municipalities.add("Chavinda");
		municipalities.add("Ixtenco");
		municipalities.add("Calpulalpan");
		municipalities.add("Muños");
		municipalities.add("Tila");
		municipalities.add("Tuxtla Chico");
		municipalities.add("Tzimol");

		final List<Customers> customers = customerRepository.findAll();
		for (final Customers customer : customers) {
			if (customer.getEducationlevel().equals(EducationLevel.BACHELOR_DEGREE)) {
				customer.setJob(jobs.get(ThreadLocalRandom.current().nextInt(0, 6)));
			} else if (customer.getEducationlevel().equals(EducationLevel.HIGH_SCHOOL)) {
				customer.setJob(jobs.get(ThreadLocalRandom.current().nextInt(6, 11)));
			} else if (customer.getEducationlevel().equals(EducationLevel.BASIC)) {
				customer.setJob(jobs.get(ThreadLocalRandom.current().nextInt(11, 15)));
			}

			if (customer.getState().equals(State.Queretaro)) {
				customer.setMunicipality(municipalities.get(ThreadLocalRandom.current().nextInt(0, 16)));
			} else if (customer.getState().equals(State.Ciudad_Mexico)) {
				customer.setMunicipality(municipalities.get(ThreadLocalRandom.current().nextInt(16, 31)));
			} else if (customer.getState().equals(State.Puebla)) {
				customer.setMunicipality(municipalities.get(ThreadLocalRandom.current().nextInt(31, 40)));
			} else {
				customer.setMunicipality(municipalities.get(ThreadLocalRandom.current().nextInt(40, 50)));
			}
			customerRepository.save(customer);
		}

	}

	@Override
	public void createCustomersKNearest() {
		final CustomerProfile profile = new CustomerProfile();
		profile.setId(1);
		Customers customer = new Customers();
		customer.setName("Aleida Villa Hernandez");
		customer.setAge(48);
		customer.setState(State.Colima);
		customer.setMunicipality("Venustiano Carranza");
		customer.setSex("M");
		customer.setIdprofile(profile);
		customer.setEducationlevel(EducationLevel.BASIC);
		customer.setJob("Asesor financiero");
		customer.setMaritalstatus(MaritalStatus.SINGLE);
		customer.setIncome(new BigDecimal(7504.00));
		customer.setDebt(new BigDecimal(754.00));

		customerRepository.save(customer);

		customer = new Customers();
		customer.setName("Avelina Molina Carrasco");
		customer.setAge(30);
		customer.setState(State.Queretaro);
		customer.setMunicipality("Venustiano Carranza");
		customer.setSex("M");
		customer.setIdprofile(profile);
		customer.setEducationlevel(EducationLevel.BASIC);
		customer.setJob("Abogado");
		customer.setMaritalstatus(MaritalStatus.DIVORCED);
		customer.setIncome(new BigDecimal(8579.05));
		customer.setDebt(new BigDecimal(600.53));

		customerRepository.save(customer);

		customer = new Customers();
		customer.setName("Alma Iglesias Marin");
		customer.setAge(42);
		customer.setState(State.Puebla);
		customer.setMunicipality("Benito Juarez");
		customer.setSex("M");
		customer.setIdprofile(profile);
		customer.setEducationlevel(EducationLevel.HIGH_SCHOOL);
		customer.setJob("Assistenete de dirección");
		customer.setMaritalstatus(MaritalStatus.MARRIED);
		customer.setIncome(new BigDecimal(9026.59));
		customer.setDebt(new BigDecimal(1805.32));

		customerRepository.save(customer);

		customer = new Customers();
		customer.setName("Alondra Montes Salazar");
		customer.setAge(25);
		customer.setState(State.Queretaro);
		customer.setMunicipality("Nicolás Bravo");
		customer.setSex("M");
		customer.setIdprofile(profile);
		customer.setEducationlevel(EducationLevel.HIGH_SCHOOL);
		customer.setJob("Recepcionista");
		customer.setMaritalstatus(MaritalStatus.MARRIED);
		customer.setIncome(new BigDecimal(15512.07));
		customer.setDebt(new BigDecimal(1551.21));

		customerRepository.save(customer);

		customer = new Customers();
		customer.setName("Raymundo Sosa Zepeda");
		customer.setAge(49);
		customer.setState(State.Puebla);
		customer.setMunicipality("Ezequiel Montes");
		customer.setSex("H");
		customer.setIdprofile(profile);
		customer.setEducationlevel(EducationLevel.BASIC);
		customer.setJob("Ejecutivo de Ventas");
		customer.setMaritalstatus(MaritalStatus.SINGLE);
		customer.setIncome(new BigDecimal(4564.51));
		customer.setDebt(new BigDecimal(754.00));

		customerRepository.save(customer);

		customer = new Customers();
		customer.setName("Maribel Regil Zarate");
		customer.setAge(26);
		customer.setState(State.Puebla);
		customer.setMunicipality("Ahuatlán");
		customer.setSex("M");
		customer.setIdprofile(profile);
		customer.setEducationlevel(EducationLevel.BACHELOR_DEGREE);
		customer.setJob("Enfermero");
		customer.setMaritalstatus(MaritalStatus.WIDOWER);
		customer.setIncome(new BigDecimal(16544.27));
		customer.setDebt(new BigDecimal(992.66));

		customerRepository.save(customer);

		customer = new Customers();
		customer.setName("Lidia Avila Orozco");
		customer.setAge(35);
		customer.setState(State.Puebla);
		customer.setMunicipality("Amozoc");
		customer.setSex("M");
		customer.setIdprofile(profile);
		customer.setEducationlevel(EducationLevel.BACHELOR_DEGREE);
		customer.setJob("Ingeniero");
		customer.setMaritalstatus(MaritalStatus.MARRIED);
		customer.setIncome(new BigDecimal(19171.65));
		customer.setDebt(new BigDecimal(2875.75));

		customerRepository.save(customer);

		customer = new Customers();
		customer.setName("Alberto Barranco Davila");
		customer.setAge(34);
		customer.setState(State.Puebla);
		customer.setMunicipality("Amozoc");
		customer.setSex("H");
		customer.setIdprofile(profile);
		customer.setEducationlevel(EducationLevel.BACHELOR_DEGREE);
		customer.setJob("Asistente de dirección");
		customer.setMaritalstatus(MaritalStatus.SINGLE);
		customer.setIncome(new BigDecimal(24671.01));
		customer.setDebt(new BigDecimal(1726.97));

		customerRepository.save(customer);

		customer = new Customers();
		customer.setName("David Ruiz Torres");
		customer.setAge(34);
		customer.setState(State.Queretaro);
		customer.setMunicipality("Jalpan");
		customer.setSex("H");
		customer.setIdprofile(profile);
		customer.setEducationlevel(EducationLevel.BACHELOR_DEGREE);
		customer.setJob("Asistente de dirección");
		customer.setMaritalstatus(MaritalStatus.SINGLE);
		customer.setIncome(new BigDecimal(19754.38));
		customer.setDebt(new BigDecimal(1382.81));

		customerRepository.save(customer);

		customer = new Customers();
		customer.setName("Maricela Morlan Gutierrez");
		customer.setAge(27);
		customer.setState(State.Ciudad_Mexico);
		customer.setMunicipality("Cuajimalpa");
		customer.setSex("M");
		customer.setIdprofile(profile);
		customer.setEducationlevel(EducationLevel.HIGH_SCHOOL);
		customer.setJob("Enfermero");
		customer.setMaritalstatus(MaritalStatus.SINGLE);
		customer.setIncome(new BigDecimal(17151.02));
		customer.setDebt(new BigDecimal(857.55));

		customerRepository.save(customer);

		customer = new Customers();
		customer.setName("Gonzalo Eguia Tabares");
		customer.setAge(35);
		customer.setState(State.Puebla);
		customer.setMunicipality("Colon");
		customer.setSex("H");
		customer.setIdprofile(profile);
		customer.setEducationlevel(EducationLevel.BACHELOR_DEGREE);
		customer.setJob("Asistente de dirección");
		customer.setMaritalstatus(MaritalStatus.DIVORCED);
		customer.setIncome(new BigDecimal(26796.01));
		customer.setDebt(new BigDecimal(2411.64));

		customerRepository.save(customer);

		customer = new Customers();
		customer.setName("Alvaro Ruiz Leon");
		customer.setAge(34);
		customer.setState(State.Ciudad_Mexico);
		customer.setMunicipality("Jalpan");
		customer.setSex("H");
		customer.setIdprofile(profile);
		customer.setEducationlevel(EducationLevel.BACHELOR_DEGREE);
		customer.setJob("Asistente de dirección");
		customer.setMaritalstatus(MaritalStatus.DIVORCED);
		customer.setIncome(new BigDecimal(31003.91));
		customer.setDebt(new BigDecimal(2790.35));

		customerRepository.save(customer);

		customer = new Customers();
		customer.setName("Sonia Alfaro Garrido");
		customer.setAge(34);
		customer.setState(State.Ciudad_Mexico);
		customer.setMunicipality("Toliman");
		customer.setSex("M");
		customer.setIdprofile(profile);
		customer.setEducationlevel(EducationLevel.BACHELOR_DEGREE);
		customer.setJob("Asistente de dirección");
		customer.setMaritalstatus(MaritalStatus.SINGLE);
		customer.setIncome(new BigDecimal(28146.63));
		customer.setDebt(new BigDecimal(1970.26));

		customerRepository.save(customer);

		customer = new Customers();
		customer.setName("Alfaro Garrido Chaires");
		customer.setAge(40);
		customer.setState(State.Puebla);
		customer.setMunicipality("Aquixtla");
		customer.setSex("H");
		customer.setIdprofile(profile);
		customer.setEducationlevel(EducationLevel.BACHELOR_DEGREE);
		customer.setJob("Contador Público");
		customer.setMaritalstatus(MaritalStatus.SINGLE);
		customer.setIncome(new BigDecimal(29147.84));
		customer.setDebt(new BigDecimal(2040.35));

		customerRepository.save(customer);

		customer = new Customers();
		customer.setName("Miguel Esparza Toledo");
		customer.setAge(37);
		customer.setState(State.Queretaro);
		customer.setMunicipality("Toliman");
		customer.setSex("H");
		customer.setIdprofile(profile);
		customer.setEducationlevel(EducationLevel.BACHELOR_DEGREE);
		customer.setJob("Asesor Financiero");
		customer.setMaritalstatus(MaritalStatus.WIDOWER);
		customer.setIncome(new BigDecimal(37300.84));
		customer.setDebt(new BigDecimal(2984.07));

		customerRepository.save(customer);

		customer = new Customers();
		customer.setName("Miguel Esparza Toledo");
		customer.setAge(24);
		customer.setState(State.Queretaro);
		customer.setMunicipality("Toliman");
		customer.setSex("H");
		customer.setIdprofile(profile);
		customer.setEducationlevel(EducationLevel.HIGH_SCHOOL);
		customer.setJob("Administrativo");
		customer.setMaritalstatus(MaritalStatus.WIDOWER);
		customer.setIncome(new BigDecimal(13247.06));
		customer.setDebt(new BigDecimal(794.82));

		customerRepository.save(customer);

		customer = new Customers();
		customer.setName("Anahi Morelos Camarena");
		customer.setAge(45);
		customer.setState(State.Queretaro);
		customer.setMunicipality("Toliman");
		customer.setSex("M");
		customer.setIdprofile(profile);
		customer.setEducationlevel(EducationLevel.BACHELOR_DEGREE);
		customer.setJob("Administrativo");
		customer.setMaritalstatus(MaritalStatus.SINGLE);
		customer.setIncome(new BigDecimal(12968.50));
		customer.setDebt(new BigDecimal(1296.85));

		customerRepository.save(customer);

		customer = new Customers();
		customer.setName("Carlos Zamarripa Vazquez");
		customer.setAge(24);
		customer.setState(State.Puebla);
		customer.setMunicipality("Toliman");
		customer.setSex("H");
		customer.setIdprofile(profile);
		customer.setEducationlevel(EducationLevel.HIGH_SCHOOL);
		customer.setJob("Administrativo");
		customer.setMaritalstatus(MaritalStatus.MARRIED);
		customer.setIncome(new BigDecimal(12197.30));
		customer.setDebt(new BigDecimal(1219.73));

		customerRepository.save(customer);

		customer = new Customers();
		customer.setName("Marina Chavez Larrea");
		customer.setAge(39);
		customer.setState(State.Ciudad_Mexico);
		customer.setMunicipality("Toliman");
		customer.setSex("M");
		customer.setIdprofile(profile);
		customer.setEducationlevel(EducationLevel.HIGH_SCHOOL);
		customer.setJob("Administrativo");
		customer.setMaritalstatus(MaritalStatus.DIVORCED);
		customer.setIncome(new BigDecimal(14226.37));
		customer.setDebt(new BigDecimal(1280.37));

		customerRepository.save(customer);

		customer = new Customers();
		customer.setName("Rene Medina Rincon");
		customer.setAge(28);
		customer.setState(State.Ciudad_Mexico);
		customer.setMunicipality("Toliman");
		customer.setSex("H");
		customer.setIdprofile(profile);
		customer.setEducationlevel(EducationLevel.BACHELOR_DEGREE);
		customer.setJob("Administrativo");
		customer.setMaritalstatus(MaritalStatus.DIVORCED);
		customer.setIncome(new BigDecimal(13637.83));
		customer.setDebt(new BigDecimal(954.65));

		customerRepository.save(customer);

	}

	@Override
	public void addIncomeAndDebtCustomerInformation() {
		final List<Customers> customers = customerRepository.findAll();
		// The income is calculated respect to: Age, State, Education level.
		// De 18 a 30 años rango: 5,000 - 25,000
		// De 30 a 40 años rango: 7,000 - 35,000
		// De 40 a 50 años rango: 4,000 - 20,000

		// Education Level 1: 10,000 - 25,000 | 15,000 - 35,000 | 9,000 - 20,000
		// Education Level 2: 7,000 - 15,000 | 8,000 - 17,000 | 6,000 - 11,000
		// Education Level 3: 5,000 10,000 | 7,000 - 12,000 | 4,000 - 9,000

		// Si el estado es 9 (CDMX) + 15%
		// Si el estado es 19 (Nuevo Leon) + 10%
		// Si el esatado es 22 Queretaro + 7%
		// Si el estado es 21 (Puebla) + 5%
		// El resto entre -7 y 15%.

		// Debt is calculated respect to: Age, Marital status, Income.
		// De 18 a 30 años y Casado: 10% del sueldo
		// De 30 a 40 años y Casado: 15% del sueldo
		// De 40 a 50 años y Casado: 20% del sueldo
		// De 18 a 30 años y Soltero: 5% del sueldo
		// De 30 a 40 años y Soltero: 7% del sueldo
		// De 40 a 50 años y Soltero: 10% del sueldo

		// De 18 a 30 años y Divorciado: 7% del sueldo
		// De 30 a 40 años y Divorciado: 9% del sueldo
		// De 40 a 50 años y Divorciado: 12% del sueldo

		// De 18 a 30 años y Viudo: 6% del sueldo
		// De 30 a 40 años y Viudo: 9% del sueldo
		// De 40 a 50 años y Viudo: 14% del sueldo

		customers.forEach(item -> {
			item.setIncome(getIncome(item));
			item.setDebt(getDebt(item));
			customerRepository.save(item);
		});
	}

	private BigDecimal getIncome(Customers item) {
		BigDecimal income = null;
		if (item.getAge() >= 18 && item.getAge() <= 30
				&& item.getEducationlevel().equals(EducationLevel.BACHELOR_DEGREE)) {
			income = new BigDecimal(ThreadLocalRandom.current().nextDouble(10000, 25_001)).setScale(2,
					RoundingMode.HALF_EVEN);

		} else if (item.getAge() > 30 && item.getAge() <= 40
				&& item.getEducationlevel().equals(EducationLevel.BACHELOR_DEGREE)) {
			income = new BigDecimal(ThreadLocalRandom.current().nextDouble(15_000, 35_001)).setScale(2,
					RoundingMode.HALF_EVEN);
		} else if (item.getAge() > 40 && item.getAge() <= 50
				&& item.getEducationlevel().equals(EducationLevel.BACHELOR_DEGREE)) {
			income = new BigDecimal(ThreadLocalRandom.current().nextDouble(9_000, 20_001)).setScale(2,
					RoundingMode.HALF_EVEN);

		} else if (item.getAge() >= 18 && item.getAge() <= 30
				&& item.getEducationlevel().equals(EducationLevel.HIGH_SCHOOL)) {
			income = new BigDecimal(ThreadLocalRandom.current().nextDouble(7_000, 15_001)).setScale(2,
					RoundingMode.HALF_EVEN);
		} else if (item.getAge() > 30 && item.getAge() <= 40
				&& item.getEducationlevel().equals(EducationLevel.HIGH_SCHOOL)) {
			income = new BigDecimal(ThreadLocalRandom.current().nextDouble(8_000, 17_001)).setScale(2,
					RoundingMode.HALF_EVEN);
		} else if (item.getAge() > 40 && item.getAge() <= 50
				&& item.getEducationlevel().equals(EducationLevel.HIGH_SCHOOL)) {
			income = new BigDecimal(ThreadLocalRandom.current().nextDouble(6_000, 11_001)).setScale(2,
					RoundingMode.HALF_EVEN);

		} else if (item.getAge() >= 18 && item.getAge() <= 30
				&& item.getEducationlevel().equals(EducationLevel.BASIC)) {
			income = new BigDecimal(ThreadLocalRandom.current().nextDouble(5_000, 10_001)).setScale(2,
					RoundingMode.HALF_EVEN);
		} else if (item.getAge() > 30 && item.getAge() <= 40 && item.getEducationlevel().equals(EducationLevel.BASIC)) {
			income = new BigDecimal(ThreadLocalRandom.current().nextDouble(7_000, 12_001)).setScale(2,
					RoundingMode.HALF_EVEN);
		} else if (item.getAge() > 40 && item.getAge() <= 50 && item.getEducationlevel().equals(EducationLevel.BASIC)) {
			income = new BigDecimal(ThreadLocalRandom.current().nextDouble(4_000, 9_001)).setScale(2,
					RoundingMode.HALF_EVEN);
		}

		if (item.getState().equals(State.Ciudad_Mexico)) {
			income = income.add(income.multiply(new BigDecimal(0.15), MathContext.DECIMAL128), MathContext.DECIMAL128)
					.setScale(2, RoundingMode.HALF_EVEN);
		} else if (item.getState().equals(State.Nuevo_Leon)) {
			income = income.add(income.multiply(new BigDecimal(0.10), MathContext.DECIMAL128), MathContext.DECIMAL128)
					.setScale(2, RoundingMode.HALF_EVEN);
		} else if (item.getState().equals(State.Queretaro)) {
			income = income.add(income.multiply(new BigDecimal(0.07), MathContext.DECIMAL128), MathContext.DECIMAL128)
					.setScale(2, RoundingMode.HALF_EVEN);
		} else if (item.getState().equals(State.Puebla)) {
			income = income.add(income.multiply(new BigDecimal(0.05), MathContext.DECIMAL128), MathContext.DECIMAL128)
					.setScale(2, RoundingMode.HALF_EVEN);
		} else {
			income = income.subtract(income.multiply(new BigDecimal(ThreadLocalRandom.current().nextDouble(0.07, 0.16)),
					MathContext.DECIMAL128), MathContext.DECIMAL128).setScale(2, RoundingMode.HALF_EVEN);
		}
		log.info(":: Income {} ", income);
		return income;
	}

	private BigDecimal getDebt(Customers item) {
		BigDecimal debt = null;
		if (item.getAge() >= 18 && item.getAge() <= 30 && item.getMaritalstatus().equals(MaritalStatus.MARRIED)) {
			debt = item.getIncome().multiply(new BigDecimal(0.10), MathContext.DECIMAL128).setScale(2,
					RoundingMode.HALF_EVEN);
		} else if (item.getAge() > 30 && item.getAge() <= 40 && item.getMaritalstatus().equals(MaritalStatus.MARRIED)) {
			debt = item.getIncome().multiply(new BigDecimal(0.15), MathContext.DECIMAL128).setScale(2,
					RoundingMode.HALF_EVEN);
		} else if (item.getAge() > 40 && item.getAge() <= 50 && item.getMaritalstatus().equals(MaritalStatus.MARRIED)) {
			debt = item.getIncome().multiply(new BigDecimal(0.20), MathContext.DECIMAL128).setScale(2,
					RoundingMode.HALF_EVEN);

		} else if (item.getAge() >= 18 && item.getAge() <= 30 && item.getMaritalstatus().equals(MaritalStatus.SINGLE)) {
			debt = item.getIncome().multiply(new BigDecimal(0.05), MathContext.DECIMAL128).setScale(2,
					RoundingMode.HALF_EVEN);
		} else if (item.getAge() > 30 && item.getAge() <= 40 && item.getMaritalstatus().equals(MaritalStatus.SINGLE)) {
			debt = item.getIncome().multiply(new BigDecimal(0.07), MathContext.DECIMAL128).setScale(2,
					RoundingMode.HALF_EVEN);
		} else if (item.getAge() > 40 && item.getAge() <= 50 && item.getMaritalstatus().equals(MaritalStatus.SINGLE)) {
			debt = item.getIncome().multiply(new BigDecimal(0.10), MathContext.DECIMAL128).setScale(2,
					RoundingMode.HALF_EVEN);

		} else if (item.getAge() >= 18 && item.getAge() <= 30
				&& item.getMaritalstatus().equals(MaritalStatus.WIDOWER)) {
			debt = item.getIncome().multiply(new BigDecimal(0.06), MathContext.DECIMAL128).setScale(2,
					RoundingMode.HALF_EVEN);
		} else if (item.getAge() > 30 && item.getAge() <= 40 && item.getMaritalstatus().equals(MaritalStatus.WIDOWER)) {
			debt = item.getIncome().multiply(new BigDecimal(0.08), MathContext.DECIMAL128).setScale(2,
					RoundingMode.HALF_EVEN);
		} else if (item.getAge() > 40 && item.getAge() <= 50 && item.getMaritalstatus().equals(MaritalStatus.WIDOWER)) {
			debt = item.getIncome().multiply(new BigDecimal(0.14), MathContext.DECIMAL128).setScale(2,
					RoundingMode.HALF_EVEN);

		} else if (item.getAge() >= 18 && item.getAge() <= 30
				&& item.getMaritalstatus().equals(MaritalStatus.DIVORCED)) {
			debt = item.getIncome().multiply(new BigDecimal(0.07), MathContext.DECIMAL128).setScale(2,
					RoundingMode.HALF_EVEN);
		} else if (item.getAge() > 30 && item.getAge() <= 40
				&& item.getMaritalstatus().equals(MaritalStatus.DIVORCED)) {
			debt = item.getIncome().multiply(new BigDecimal(0.09), MathContext.DECIMAL128).setScale(2,
					RoundingMode.HALF_EVEN);
		} else if (item.getAge() > 40 && item.getAge() <= 50
				&& item.getMaritalstatus().equals(MaritalStatus.DIVORCED)) {
			debt = item.getIncome().multiply(new BigDecimal(0.12), MathContext.DECIMAL128).setScale(2,
					RoundingMode.HALF_EVEN);
		}
		log.info(":: Debt {} ", debt);
		return debt;
	}

	@Override
	public void createDefaultDB() {
		String directory = "/Users/juancarlospedrazaalcala/Documents/WorkSpaceServices/"
				+ "DataBaseProcess/calpullix-database-process/src/main/resources/CURRENT";
		try (Stream<Path> paths = Files.walk(Paths.get(directory))) {
			paths.filter(Files::isRegularFile).forEach(item -> {
				log.info(":. FILE {} ", item.getFileName());
				Resource resource = new ClassPathResource("/CURRENT/" + item.getFileName().toString());
				ResourceDatabasePopulator databasePopulator = new ResourceDatabasePopulator(resource);
				databasePopulator.execute(dataSource);
			});
		} catch (IOException e1) {
			e1.printStackTrace();
		}

	}

	@Override
	public void processCustomerProfile() {
		log.info(":: Cust Profile ");
		CustomerProfile customerProfile;
		for (final com.calpullix.db.process.catalog.model.CustomerProfile item : com.calpullix.db.process.catalog.model.CustomerProfile
				.values()) {
			customerProfile = new CustomerProfile();
			customerProfile.setName(item.getDescription());
			customerProfile.setDescription(item.getDescription());
			customerProfile.setActive(Boolean.TRUE);
			customerProfile.setCreationdate(formatTimestamp.format(Calendar.getInstance().getTime()));
			customerProfileRepository.save(customerProfile);
			log.info(":: Cust Profile {} ", customerProfile);
		}
	}

	@Override
	public void processEmployee() {
		final List<String> lines = readFile(PATH + FILE_TEXT_EMPLOYEE);
		Employee employee;
		StringTokenizer tokens;
		for (final String line : lines) {
			tokens = getStringTokenizer(line, TOKEN_COMA);
			employee = new Employee();
			employee.setAddress(tokens.nextToken());
			employee.setAge(Integer.valueOf(tokens.nextToken()));
			employee.setIsr(new BigDecimal(tokens.nextToken()));
			employee.setMonthlysalary(new BigDecimal(tokens.nextToken()));
			employee.setName(tokens.nextToken());
			employee.setPosition(EmployeePosition.MANAGER);
			employee.setSex(tokens.nextToken());
			employeeRepository.save(employee);
			log.info(":: Employee service {} ", employee);
		}
	}

	@Override
	public void processRestEmployees() {
		final List<String> lines = readFile(PATH + FILE_TEXT_EMPLOYEE);
		Employee employee;
		StringTokenizer tokens;
		int index = 1;
		List<String> name = new ArrayList<>();
		name.add("Jorge Lugo Mendez");
		name.add("Carlos Velez Domingo");
		name.add("Sergio Jurado Hernandez");
		name.add("Omar Centeno Vidal");
		name.add("Antonio Castro Ferrer");
		name.add("Fernando Ledezma Orduñez");
		name.add("Daniel Bermudez Caso");
		name.add("Johan Mayen Duran");
		name.add("Carlos Mar Perez");
		name.add("Pedro Cruz Eguia");
		name.add("Graciela Morlan Baldobinos");
		name.add("Maria Castro Solis");
		name.add("Gabriela Aremndariz Gomez");
		name.add("Karina Roldan Graciela");
		name.add("Kymberly Chavez Granados");
		List<String> address = new ArrayList<>();
		address.add("Calle J. Enrique Pestalozzi 311");
		address.add("Calzada de Tlalpan 486 Int. 4");
		address.add("Matías Romero 1453");
		address.add("Lago Pte. 16 Américas Unidas");
		address.add("Calz. de la Viga 611, San Francisco");
		address.add("Caleta 612, Narvarte Poniente");
		address.add("5 de Febrero no 21, Centro Histórico");
		address.add("Hamburgo 68, Juárez, Cuauhtémoc");
		address.add("Tuxpan 47, Roma Sur, Cuauhtémoc");
		address.add("Concepción 555 Beistegui, Narvarte");
		address.add("Av. Río Churubusco 158, El Prado");
		address.add("Leonor 370, Nativitas, Benito Juárez");
		address.add("Lago Pte. 16, Américas Unidas");
		address.add("Altamirano 46, Tizapán San Ángel");
		address.add("Ferrocarril de Cuernavaca 2899");
		address.add("Palmas 9, Palmas, La Magdalena Contreras");
		for (int j = 0; j < 17; j++) {
			for (int k = 0; k < lines.size(); k++) {
				tokens = getStringTokenizer(lines.get(ThreadLocalRandom.current().nextInt(0, 9)), TOKEN_COMA);
				employee = new Employee();
				employee.setAddress(tokens.nextToken());
				employee.setAge(Integer.valueOf(tokens.nextToken()) + ThreadLocalRandom.current().nextInt(0, 5));
				employee.setIsr(new BigDecimal(tokens.nextToken()));
				employee.setMonthlysalary(new BigDecimal(tokens.nextToken())
						.subtract(new BigDecimal(ThreadLocalRandom.current().nextInt(100, 1000))));
				if (k % 2 == 0) {
					employee.setAddress(address.get(ThreadLocalRandom.current().nextInt(0, 15)));
					employee.setName(name.get(ThreadLocalRandom.current().nextInt(0, 14)));
					tokens.nextToken();
				} else {
					employee.setName(tokens.nextToken());
				}
				if (index <= 100) {
					employee.setPosition(EmployeePosition.ASSISTANT);
				} else if (index <= 130) {
					employee.setPosition(EmployeePosition.CASHIER);
				} else if (index <= 150) {
					employee.setPosition(EmployeePosition.DEPARTMENT_MANAGER);
				} else if (index <= 170) {
					employee.setPosition(EmployeePosition.JANITOR);
				}
				if (employee.getName().contains("Mariana") || employee.getName().contains("Mariana")
						|| employee.getName().contains("Guadalupe") || employee.getName().contains("Tania")
						|| employee.getName().contains("Marisol") || employee.getName().contains("Graciela")
						|| employee.getName().contains("Maria") || employee.getName().contains("Gabriela")
						|| employee.getName().contains("Karina") || employee.getName().contains("Kymberly")) {
					employee.setSex(FEMALE);
				} else {
					employee.setSex(MALE);
				}
				tokens.nextToken();
				employeeRepository.save(employee);
				log.info(":: Employee service {} ", employee);
				index++;
			}
		}
	}

	private StringTokenizer getStringTokenizer(final String line, final String token) {
		StringTokenizer tokens = new StringTokenizer(line, token);
		return tokens;
	}

	private List<String> readFile(String file) {
		List<String> read = null;
		Path path = Paths.get(file);
		try {
			read = Files.readAllLines(path);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return read;
	}

	@Override
	public void processBranch() {
		Branch branch;
		Employee manager;
		File image;
		final List<String> lines = readFile(PATH + BRANCH_FILE);
		StringTokenizer tokens;
		final BigDecimal deduction = new BigDecimal(BASE_DEDUCTION);
		final BigDecimal rent = new BigDecimal(BASE_RENT);
		final int numberEmployees = 40;
		for (int index = 1; index <= NUMBER_BRANCHES; index++) {
			try {
				processImage(PATH_IMAGE_BRANCHES + BRANCH_NAME + index + FULL_STOP + JPEG_EXTENSION,
						PATH_IMAGE_BRANCHES + BRANCH_NAME_COPY + index + FULL_STOP + JPEG_EXTENSION, JPEG_EXTENSION);
			} catch (IOException e) {
				e.printStackTrace();
			}
			tokens = getStringTokenizer(lines.get(index - 1), TOKEN_COMA);
			branch = new Branch();
			branch.setAddress(tokens.nextToken());
			branch.setContact(tokens.nextToken());
			branch.setContactType(ContactType.CELL_PHONE);
			try {
				image = new File(PATH_IMAGE_BRANCHES + BRANCH_NAME_COPY + index + FULL_STOP + JPEG_EXTENSION);
				branch.setImage(Files.readAllBytes(image.toPath()));
			} catch (IOException e) {
				e.printStackTrace();
			}
			manager = new Employee();
			manager.setId(index);
			branch.setManager(manager);
			branch.setMonthlydeductions(
					deduction.multiply(new BigDecimal(ThreadLocalRandom.current().nextDouble(0.5, 1))));
			branch.setMonthlyrent(rent.multiply(new BigDecimal(ThreadLocalRandom.current().nextDouble(0.5, 1))));
			branch.setMunicipality(tokens.nextToken());
			branch.setName(tokens.nextToken());
			branch.setNumberemployees(numberEmployees + ThreadLocalRandom.current().nextInt(5, 15));
			branch.setPostalcode(Integer.valueOf(tokens.nextToken()));
			branch.setLongitude(tokens.nextToken());
			branch.setLatitude(tokens.nextToken());
			if (index % 2 == 0) {
				branch.setRegion(BranchRegion.REGION_SOUTH);
				branch.setState(State.Ciudad_Mexico);
			} else {
				branch.setRegion(BranchRegion.REGION_NORTH);
				branch.setState(State.Queretaro);
			}
			branchRepository.save(branch);
			log.info(":: Branch Service service {} ", branch);
		}
	}

	@Override
	public void processImageTwitterProfile() throws Exception {
		Twitter twitterModel = twitterRepository.findById(2).get();
		URL url = new URL("http://pbs.twimg.com/profile_images/1247622936355319815/mLb4M7ZH_normal.jpg");
		BufferedImage image = ImageIO.read(url);
		File imageFile = new File("/Users/juancarlospedrazaalcala/Documents/TT/TwitterUser/" + "picture_profile.jpg");
		ImageIO.write(image, "jpg", imageFile);
		twitterModel.setProfilepicture(Files.readAllBytes(imageFile.toPath()));
		imageFile = new File("/Users/juancarlospedrazaalcala/Documents/TT/BACK_UP/cloudwords_twitter.png");
		twitterModel.setClowwords(Files.readAllBytes(imageFile.toPath()));
		twitterRepository.save(twitterModel);
	}

	@Override
	public void processImageBranch() {
		final List<Branch> branches = branchRepository.findAll();
		File image;
		int count = 1;
		for (final Branch item : branches) {
			try {
				processImage(PATH_IMAGE_BRANCHES + BRANCH_NAME + count + FULL_STOP + JPEG_EXTENSION,
						PATH_IMAGE_BRANCHES + BRANCH_NAME_COPY + count + FULL_STOP + JPEG_EXTENSION, JPEG_EXTENSION);
				image = new File(PATH_IMAGE_BRANCHES + BRANCH_NAME_COPY + count + FULL_STOP + JPEG_EXTENSION);
				item.setImage(Files.readAllBytes(image.toPath()));
				count++;
				branchRepository.save(item);
				log.info(":: Branch Service service {} ", item);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private boolean processImage(final String pathImage, final String pathCopy, final String imageExtension)
			throws IOException {
		log.info(":: Resize Image ");
		final BufferedImage originalImage = ImageIO.read(new File(pathImage));
		int type = originalImage.getType() == 0 ? BufferedImage.TYPE_INT_ARGB : originalImage.getType();
		final BufferedImage resizeImageJpg = resizeImage(originalImage, type);
		ImageIO.write(resizeImageJpg, imageExtension, new File(pathCopy));
		log.info(":: Type image ", type);
		return Boolean.TRUE;
	}

	private BufferedImage resizeImage(BufferedImage originalImage, int type) {
		BufferedImage resizedImage = new BufferedImage(IMG_WIDTH, IMG_HEIGHT, type);
		Graphics2D g = resizedImage.createGraphics();
		g.drawImage(originalImage, 0, 0, IMG_WIDTH, IMG_HEIGHT, null);
		g.dispose();
		return resizedImage;
	}

	@Override
	public void processBranchEmployee() {
		List<Branch> branches = branchRepository.findAll();
		List<Employee> employees = employeeRepository.findAll();
		int loop = 1;
		int index;
		int div = employees.size() / branches.size();
		BranchEmployee branchEmployee;
		BranchEmployeeId branchEmployeeId;
		for (final Branch branch : branches) {
			for (index = div * loop - div; index < div * loop; index++) {
				branchEmployee = new BranchEmployee();
				branchEmployeeId = new BranchEmployeeId();
				branchEmployeeId.setIdbranch(branch.getId());
				branchEmployeeId.setIdemployee(employees.get(index).getId());
				branchEmployee.setBranchEmployeeId(branchEmployeeId);
				branchEmployee.setActive(Boolean.TRUE);
				branchEmployeeRepository.save(branchEmployee);
				log.info(":: Process branch employee {} ", branchEmployee);
			}
			loop++;
		}
	}

	@Override
	public void processProvider() {
		final List<String> lines = readFile(PATH + PROVIDER_FILE);
		StringTokenizer tokens;
		Provider provider;
		for (final String line : lines) {
			tokens = getStringTokenizer(line, TOKEN_COMA);
			provider = new Provider();
			provider.setName(tokens.nextToken());
			provider.setAddress(tokens.nextToken());
			provider.setLongitude(tokens.nextToken());
			provider.setLatitude(tokens.nextToken());
			provider.setContact(tokens.nextToken());
			provider.setContactType(ContactType.CELL_PHONE);
			providerRepository.save(provider);
			log.info(":: Provider {} ", provider);
		}
	}

	@Override
	public void processProduct() {
		final List<String> lines = readFile(PATH + PRODUCT_FILE);
		File image;
		StringTokenizer tokens;
		Product product;
		String file;
		String extension;
		List<Provider> provider = providerRepository.findAll();
		int count = 0;
		for (final String line : lines) {
			tokens = getStringTokenizer(line, TOKEN_PIPE);
			product = new Product();
			product.setName(tokens.nextToken());
			product.setDescription(tokens.nextToken());
			product.setBrand(Brand.of(Integer.valueOf(tokens.nextToken())));
			log.info(":: TOKEN {} ", ++count);
			product.setProvider(provider.get(ThreadLocalRandom.current().nextInt(0, provider.size() - 1)));
			product.setWeight(new BigDecimal(tokens.nextToken()));
			product.setWeightunit(WeightUnit.of(Integer.valueOf(tokens.nextToken())));
			product.setIndividualPackaging(Boolean.valueOf(tokens.nextToken()));
			product.setCofeprisPermission(Boolean.valueOf(tokens.nextToken()));
			product.setFragileMaterial(Boolean.valueOf(tokens.nextToken()));
			product.setCategory(ProductCategories.of(Integer.valueOf(tokens.nextToken())));
			product.setMeasurements(tokens.nextToken());
			try {
				file = tokens.nextToken();
				extension = tokens.nextToken();
				processImage(PATH_IMAGE_PRODUCT + file + FULL_STOP + extension,
						PATH_IMAGE_PRODUCT + file + SUFFIX_TWO + FULL_STOP + extension, extension);
				image = new File(PATH_IMAGE_PRODUCT + file + SUFFIX_TWO + FULL_STOP + extension);
				product.setImage(Files.readAllBytes(image.toPath()));
			} catch (IOException e) {
				e.printStackTrace();
			}
			productRepository.save(product);
			log.info(":: Product {} ", product);
		}
	}

	public void processImageProduct() {
		final List<Product> products = productRepository.findAll();
		final List<String> lines = readFile(PATH + PRODUCT_FILE);
		File image;
		StringTokenizer tokens;
		String file;
		String extension;
		String line;
		int index = 0;
		for (final Product item : products) {
			line = lines.get(index);
			index++;
			tokens = getStringTokenizer(line, TOKEN_PIPE);
			tokens.nextToken();
			tokens.nextToken();
			tokens.nextToken();
			tokens.nextToken();
			tokens.nextToken();
			tokens.nextToken();
			tokens.nextToken();
			tokens.nextToken();
			tokens.nextToken();
			tokens.nextToken();
			try {
				file = tokens.nextToken();
				extension = tokens.nextToken();
				processImage(PATH_IMAGE_PRODUCT + file + FULL_STOP + extension,
						PATH_IMAGE_PRODUCT + file + SUFFIX_TWO + FULL_STOP + extension, extension);
				image = new File(PATH_IMAGE_PRODUCT + file + SUFFIX_TWO + FULL_STOP + extension);
				item.setImage(Files.readAllBytes(image.toPath()));
			} catch (IOException e) {
				e.printStackTrace();
			}
			productRepository.save(item);
			log.info(":: Product {} ", item);
		}

	}

	@Override
	public void processProductPurchaseOrderQuantity() {
		final List<Product> products = productRepository.findAll();
		for (final Product product : products) {
			product.setQuantitylowerlimit(ThreadLocalRandom.current().nextInt(2000, 5901));
			productRepository.save(product);
			log.info(":: Product {} ", product);
		}
	}

	@Override
	public void processProductHistory() {
		processProductHistoryRows(PATH + PRODUCT_HISTORY_FILE, 1);
	}

	@Override
	public void processNewProductHistory() {
		processProductHistoryRows(PATH + PRODUCT_HISTORY_NEW_FILE, 31);
	}

	private void processProductHistoryRows(String pathFile, int countIdProduct) {
		final List<String> lines = readFile(pathFile);
		StringTokenizer tokens;
		int year = Calendar.getInstance().get(Calendar.YEAR);
		int month = Calendar.getInstance().get(Calendar.MONTH);
		Calendar date = Calendar.getInstance();
		BigDecimal percentageIncrease;
		BigDecimal price;
		ProductHistory productHistory;
		Product product;
		int count;
		for (int index = year; index >= year - 5; index--) {
			count = countIdProduct;
			for (final String line : lines) {
				tokens = getStringTokenizer(line, TOKEN_PIPE);
				product = new Product();
				if (count == 33) {
					++count;
				}
				product.setId(count);
				count++;
				productHistory = new ProductHistory();
				productHistory.setIdproduct(product);
				price = new BigDecimal(tokens.nextToken());
				percentageIncrease = new BigDecimal(tokens.nextToken());
				productHistory.setSaleprice(getSalePrice(price, percentageIncrease, index, year));
				productHistory.setIva(
						productHistory.getSaleprice().multiply(new BigDecimal(IVA)).setScale(2, RoundingMode.HALF_UP));
				productHistory.setPurchaseprice(productHistory.getSaleprice()
						.subtract(productHistory.getSaleprice().multiply(BigDecimal
								.valueOf(ThreadLocalRandom.current().nextInt(5, 20)).divide(BigDecimal.valueOf(100))))
						.setScale(2, RoundingMode.HALF_UP));
				if (index == year) {
					productHistory.setStatus(ProductStatus.ACTIVE);
				} else {
					productHistory.setStatus(ProductStatus.INACTIVE);
				}
				date.set(Calendar.DATE, ThreadLocalRandom.current().nextInt(1, 28));
				date.set(Calendar.YEAR, index);
				if (index != year) {
					date.set(Calendar.MONTH, ThreadLocalRandom.current().nextInt(0, 11));
				} else {
					date.set(Calendar.MONTH, ThreadLocalRandom.current().nextInt(0, month));
				}
				productHistory.setCreationdate(formatDate.format(date.getTime()));
				productHistoryRepository.save(productHistory);
				log.info(":: ProductHistory {} ", productHistory);
			}

		}
	}

	@Override
	public void processOldProductHistoryRows() {
		
		final List<Product> products = productRepository.findAll();
		final List<String> lines = readFile(PATH_ALL_PRODUCTS_HISTORY);
		StringTokenizer tokens;

		int year = 2014;
		int month = 12;

		Calendar date = Calendar.getInstance();
		BigDecimal percentageIncrease;
		List<ProductHistory> productHistoryList;
		ProductHistory productHistory;
		int count;
		for (int index = year; index >= year - 4; index--) {
			count = 0;
			for (final String line : lines) {
				tokens = getStringTokenizer(line, TOKEN_PIPE);
				productHistory = new ProductHistory();
				productHistory.setIdproduct(products.get(count));
				// Consultar del primer producto historico y tomar su precio.
				productHistoryList = productHistoryRepository
						.findAllByIdproductOrderByCreationdateAsc(products.get(count));
				count++;

				percentageIncrease = new BigDecimal(tokens.nextToken());
				productHistory.setSaleprice(
						getSalePrice(productHistoryList.get(0).getSaleprice(), percentageIncrease, index, year));
				productHistory.setIva(
						productHistory.getSaleprice().multiply(new BigDecimal(IVA)).setScale(2, RoundingMode.HALF_UP));
				productHistory.setPurchaseprice(productHistory.getSaleprice()
						.subtract(productHistory.getSaleprice().multiply(BigDecimal
								.valueOf(ThreadLocalRandom.current().nextInt(5, 20)).divide(BigDecimal.valueOf(100))))
						.setScale(2, RoundingMode.HALF_UP));
				if (index == year) {
					productHistory.setStatus(ProductStatus.ACTIVE);
				} else {
					productHistory.setStatus(ProductStatus.INACTIVE);
				}
				date.set(Calendar.DATE, ThreadLocalRandom.current().nextInt(1, 28));
				date.set(Calendar.YEAR, index);
				if (index != year) {
					date.set(Calendar.MONTH, ThreadLocalRandom.current().nextInt(0, 11));
				} else {
					date.set(Calendar.MONTH, ThreadLocalRandom.current().nextInt(0, month));
				}
				productHistory.setCreationdate(formatDate.format(date.getTime()));
				productHistoryRepository.save(productHistory);
				log.info(":: ProductHistory - Old rows {} ", productHistory);
			}
		}
	}
	

	private BigDecimal getSalePrice(BigDecimal price, BigDecimal percentage, int year, int currenYear) {
		BigDecimal result;
		if (year == currenYear) {
			result = price;
		} else {
			result = price;
			for (int index = 0; index < currenYear - year; index++) {
				result = result.subtract(
						result.multiply(percentage.divide(BigDecimal.valueOf(100).setScale(2, RoundingMode.HALF_UP)))
								.setScale(2, RoundingMode.HALF_UP))
						.setScale(2, RoundingMode.HALF_UP);
			}
		}
		return result;
	}

	@Override
	public void processDataSheet() {
		List<Product> products = productRepository.findAll();
		List<String> ingredients = new ArrayList<>();
		ingredients.add("dimeticona");
		ingredients.add("cocamidopropil");
		ingredients.add("betaína");
		ingredients.add("alcohol cetílico");
		ingredients.add("cloruro de hidroxipropiltrimonio");
		ingredients.add("benzoato de sodio");
		ingredients.add("xilen sulfonato de sodio");
		ingredients.add("color vegetal");
		ingredients.add("fragancias vegetales");
		ingredients.add("laurilsulfato de sodio");
		List<String> ingredientsFood = new ArrayList<>();
		ingredientsFood.add("sodio");
		ingredientsFood.add("azúcar");
		ingredientsFood.add("cocoa");
		ingredientsFood.add("lecitina de soya");
		ingredientsFood.add("fibra soluble");
		ingredientsFood.add("saborizantes artificiales");
		ingredientsFood.add("sal yodada");
		ingredientsFood.add("sulfato de zinc");
		ingredientsFood.add("vitaminas (C, B1, D)");
		ingredientsFood.add("fosfato dicálcico");
		List<BigDecimal> weights = new ArrayList<>();
		weights.add(new BigDecimal("0.21"));
		weights.add(new BigDecimal("0.34"));
		weights.add(new BigDecimal("0.12"));
		weights.add(new BigDecimal("0.09"));
		weights.add(new BigDecimal("0.35"));
		weights.add(new BigDecimal("0.15"));
		weights.add(new BigDecimal("0.27"));
		weights.add(new BigDecimal("0.40"));
		weights.add(new BigDecimal("0.2"));
		weights.add(new BigDecimal("0.1"));
		DataSheet dataSheet;
		for (final Product product : products) {
			for (int index = 0; index < 5; index++) {
				dataSheet = new DataSheet();
				dataSheet.setIdproduct(product);
				if (product.getCategory().equals(ProductCategories.GROCERIES)) {
					dataSheet.setComponent(ingredientsFood.get(ThreadLocalRandom.current().nextInt(0, 9)));
				} else {
					dataSheet.setComponent(ingredients.get(ThreadLocalRandom.current().nextInt(0, 9)));
				}
				dataSheet.setWeight(
						product.getWeight().multiply(weights.get(ThreadLocalRandom.current().nextInt(0, 9))));
				dataSheet.setWeightunit(WeightUnit.GR);
				dataSheetRepository.save(dataSheet);
				log.info(":: DataSheet {} ", dataSheet);
			}
		}

	}

	@Override
	public void processDistributionCenter() {
		DistributionCenter item = new DistributionCenter();
		item.setAddress("Pte. 134 828, Industrial Vallejo, Azcapotzalco 02300");
		item.setName("Distribuidor norte");
		distributionCenterRepository.save(item);
		item = new DistributionCenter();
		item.setAddress(" Palma Norte #405 Desp 102, Centro, 06000");
		item.setName("Distribuidor sur");
		distributionCenterRepository.save(item);
		item = new DistributionCenter();
		item.setAddress("Calz. San Juan de Aragón 639A, Héroes de Chapultepec");
		item.setName("Distribuidor oriente");
		distributionCenterRepository.save(item);
		log.info(":: DistributionCenter {} ", item);
	}

	@Override
	public void processProductBranchOtherStatus() {
		File image = null;
		try {
			processImage(PATH_IMAGE_PRODUCT + BAR_CODE_IMG_NAME, PATH_IMAGE_PRODUCT + BAR_CODE_COPY_IMG_NAME,
					PNG_EXTENSION);
			image = new File(PATH_IMAGE_PRODUCT + BAR_CODE_COPY_IMG_NAME);
		} catch (IOException e) {
			e.printStackTrace();
		}
		final List<Branch> branches = branchRepository.findAll();
		final List<DistributionCenter> distCenter = distributionCenterRepository.findAll();
		List<ProductHistory> productHistory;
		Calendar currentDate = Calendar.getInstance();
		Calendar date = Calendar.getInstance();
		Calendar auxDate = Calendar.getInstance();
		ProductBranch productBranch;
		BigDecimal polinomy;
		BigDecimal sum;
		int year;
		int[] productBranchStatus = { 3, 4 };
		int counter;
		// Por cada sucursal
		for (final Branch branch : branches) {
			log.info(":: Branch {} ", branch.getId());
			date.set(Calendar.MONTH, 0);
			date.set(Calendar.DATE, 1);
			year = date.get(Calendar.YEAR);
			productHistory = productHistoryRepository.findAllProductByYear(year);
			// Registro de ventas del periodo de cinco años por cada sucursal.
			counter = 0;
			while (currentDate.getTime().after(date.getTime())) {
				// Registro de productos semanal.
				auxDate = (Calendar) date.clone();
				polinomy = getValuePolinomy(ThreadLocalRandom.current().nextInt(200, 300));
				sum = BigDecimal.ZERO;
				while (polinomy.compareTo(sum) == 1) {
					// Productos del año.
					for (final ProductHistory product : productHistory) {
						productBranch = new ProductBranch();
						productBranch.setIdbranch(branch);
						productBranch.setIdproducthistory(product);
						productBranch.setStatus(ProductBranchStatus.ON_SALE);
						if (counter % 100 == 0) {
							productBranch.setStatus(ProductBranchStatus
									.of(productBranchStatus[ThreadLocalRandom.current().nextInt(0, 2)]));
						}
						auxDate.set(Calendar.DATE, ThreadLocalRandom.current().nextInt(date.get(Calendar.DATE),
								date.get(Calendar.DATE) + 6));
						productBranch.setSaledate(formatDate.format(auxDate.getTime()));
						auxDate.set(Calendar.DATE,
								auxDate.get(Calendar.DATE) - ThreadLocalRandom.current().nextInt(15, 30));
						productBranch.setCreationdate(formatDate.format(auxDate.getTime()));
						auxDate.set(Calendar.DATE, auxDate.get(Calendar.DATE) + 50);
						productBranch.setExpirationdate(formatDate.format(auxDate.getTime()));
						auxDate.set(Calendar.DATE, date.get(Calendar.DATE));
						auxDate.set(Calendar.MONTH, date.get(Calendar.MONTH));
						auxDate.set(Calendar.YEAR, date.get(Calendar.YEAR));
						productBranch.setDistributioncenter(
								distCenter.get(ThreadLocalRandom.current().nextInt(0, distCenter.size())));
						productBranch.setLocation(ProductLocation.of(ThreadLocalRandom.current().nextInt(1, 3)));
						if (BooleanUtils.negate(productBranch.getStatus().equals(ProductBranchStatus.ON_SALE))) {
							productBranch.setLocation(ProductLocation.LOST);
						}
						sum = sum.add(product.getSaleprice());
						try {
							productBranch.setBarcode(Files.readAllBytes(image.toPath()));
						} catch (IOException e) {
							e.printStackTrace();
						}
						counter++;
						productBranchRepository.save(productBranch);
					}
				}
				date.set(Calendar.DATE, date.get(Calendar.DATE) + 7);
			}
			// Otro ciclo para cubrir los dias restantes, si los hubiera.
			if (currentDate.get(Calendar.DATE) - date.get(Calendar.DATE) > 0) {
				auxDate = (Calendar) date.clone();
				polinomy = getValuePolinomy(200);
				sum = BigDecimal.ZERO;
				counter = 0;
				while (polinomy.compareTo(sum) == 1) {
					for (final ProductHistory product : productHistory) {
						// Un registro por cada producto
						productBranch = new ProductBranch();
						productBranch.setIdbranch(branch);
						productBranch.setIdproducthistory(product);
						productBranch.setStatus(ProductBranchStatus.ON_SALE);
						if (counter % 100 == 0) {
							productBranch.setStatus(ProductBranchStatus
									.of(productBranchStatus[ThreadLocalRandom.current().nextInt(0, 2)]));
						}
						auxDate.set(Calendar.DATE, ThreadLocalRandom.current().nextInt(date.get(Calendar.DATE),
								date.get(Calendar.DATE) + currentDate.get(Calendar.DATE) - date.get(Calendar.DATE)));
						productBranch.setSaledate(formatDate.format(auxDate.getTime()));
						auxDate.set(Calendar.DATE,
								auxDate.get(Calendar.DATE) - ThreadLocalRandom.current().nextInt(15, 30));
						productBranch.setCreationdate(formatDate.format(auxDate.getTime()));
						auxDate.set(Calendar.DATE, auxDate.get(Calendar.DATE) + 50);
						productBranch.setExpirationdate(formatDate.format(auxDate.getTime()));
						auxDate.set(Calendar.DATE, date.get(Calendar.DATE));
						auxDate.set(Calendar.MONTH, date.get(Calendar.MONTH));
						auxDate.set(Calendar.YEAR, date.get(Calendar.YEAR));
						productBranch.setDistributioncenter(
								distCenter.get(ThreadLocalRandom.current().nextInt(0, distCenter.size())));
						productBranch.setLocation(ProductLocation.of(ThreadLocalRandom.current().nextInt(1, 3)));
						if (BooleanUtils.negate(productBranch.getStatus().equals(ProductBranchStatus.ON_SALE))) {
							productBranch.setLocation(ProductLocation.LOST);
						}
						sum = sum.add(product.getSaleprice());
						try {
							productBranch.setBarcode(Files.readAllBytes(image.toPath()));
						} catch (IOException e) {
							e.printStackTrace();
						}
						productBranchRepository.save(productBranch);
					}
				}
			}
		}
	}

	@Override
	public void processOldProductBranch() {

		File image = null;
		try {
			processImage(PATH_IMAGE_PRODUCT + BAR_CODE_IMG_NAME, PATH_IMAGE_PRODUCT + BAR_CODE_COPY_IMG_NAME,
					PNG_EXTENSION);
			image = new File(PATH_IMAGE_PRODUCT + BAR_CODE_COPY_IMG_NAME);
		} catch (IOException e) {
			e.printStackTrace();
		}
		final List<Branch> branches = branchRepository.findAll();
		final List<DistributionCenter> distCenter = distributionCenterRepository.findAll();
		List<ProductHistory> productHistory;

		Calendar currentDate = Calendar.getInstance();
		currentDate.set(Calendar.YEAR, 2015);

		Calendar date = Calendar.getInstance();
		Calendar auxDate = Calendar.getInstance();
		ProductBranch productBranch;
		BigDecimal polinomy;
		BigDecimal sum;
		int year;
		int auxYear;

		// Por cada sucursal
		for (final Branch branch : branches) {
			log.info(":: Branch {} ", branch.getId());
			date.set(Calendar.YEAR, 2015);
			date.set(Calendar.MONTH, 0);
			date.set(Calendar.DATE, 1);
			year = date.get(Calendar.YEAR);
			productHistory = productHistoryRepository.findAllProductByYear(year);
			// Registro de ventas del periodo de cinco años por cada sucursal.
			while (currentDate.getTime().after(date.getTime())) {
				auxYear = date.get(Calendar.YEAR);
				if (auxYear != year) {
					year = auxYear;
					log.info(":: YEAR {} ", year);
					productHistory = productHistoryRepository.findAllProductByYear(year);
				}
				// Registro de ventas semanal.
				auxDate = (Calendar) date.clone();
				polinomy = getValuePolinomy(ThreadLocalRandom.current().nextInt(150, 220));
				sum = BigDecimal.ZERO;
				while (polinomy.compareTo(sum) == 1) {
					// Productos del año.
					for (final ProductHistory product : productHistory) {
						productBranch = new ProductBranch();
						productBranch.setIdbranch(branch);
						productBranch.setIdproducthistory(product);
						productBranch.setStatus(ProductBranchStatus.SOLD);
						auxDate.set(Calendar.DATE, ThreadLocalRandom.current().nextInt(date.get(Calendar.DATE),
								date.get(Calendar.DATE) + 6));
						productBranch.setSaledate(formatDate.format(auxDate.getTime()));
						auxDate.set(Calendar.DATE,
								auxDate.get(Calendar.DATE) - ThreadLocalRandom.current().nextInt(15, 30));
						productBranch.setCreationdate(formatDate.format(auxDate.getTime()));
						auxDate.set(Calendar.DATE, auxDate.get(Calendar.DATE) + 50);
						productBranch.setExpirationdate(formatDate.format(auxDate.getTime()));
						auxDate.set(Calendar.DATE, date.get(Calendar.DATE));
						auxDate.set(Calendar.MONTH, date.get(Calendar.MONTH));
						auxDate.set(Calendar.YEAR, date.get(Calendar.YEAR));
						productBranch.setDistributioncenter(
								distCenter.get(ThreadLocalRandom.current().nextInt(0, distCenter.size() - 1)));
						productBranch.setLocation(ProductLocation.SOLD);
						sum = sum.add(product.getSaleprice());
						try {
							productBranch.setBarcode(Files.readAllBytes(image.toPath()));
						} catch (IOException e) {
							e.printStackTrace();
						}
						productBranchRepository.save(productBranch);
					}
				}
				date.set(Calendar.DATE, date.get(Calendar.DATE) + 7);
			}

			// Otro ciclo para cubrir los dias restantes, si los hubiera.
			if (currentDate.get(Calendar.DATE) - date.get(Calendar.DATE) > 0) {
				auxDate = (Calendar) date.clone();
				polinomy = getValuePolinomy(200);
				sum = BigDecimal.ZERO;
				while (polinomy.compareTo(sum) == 1) {
					for (final ProductHistory product : productHistory) {
						// Un registro por cada producto
						productBranch = new ProductBranch();
						productBranch.setIdbranch(branch);
						productBranch.setIdproducthistory(product);
						productBranch.setStatus(ProductBranchStatus.SOLD);
						auxDate.set(Calendar.DATE, ThreadLocalRandom.current().nextInt(date.get(Calendar.DATE),
								date.get(Calendar.DATE) + currentDate.get(Calendar.DATE) - date.get(Calendar.DATE)));
						productBranch.setSaledate(formatDate.format(auxDate.getTime()));
						auxDate.set(Calendar.DATE,
								auxDate.get(Calendar.DATE) - ThreadLocalRandom.current().nextInt(15, 30));
						productBranch.setCreationdate(formatDate.format(auxDate.getTime()));
						auxDate.set(Calendar.DATE, auxDate.get(Calendar.DATE) + 50);
						productBranch.setExpirationdate(formatDate.format(auxDate.getTime()));
						auxDate.set(Calendar.DATE, date.get(Calendar.DATE));
						auxDate.set(Calendar.MONTH, date.get(Calendar.MONTH));
						auxDate.set(Calendar.YEAR, date.get(Calendar.YEAR));
						productBranch.setDistributioncenter(
								distCenter.get(ThreadLocalRandom.current().nextInt(0, distCenter.size() - 1)));
						productBranch.setLocation(ProductLocation.SOLD);
						sum = sum.add(product.getSaleprice());
						try {
							productBranch.setBarcode(Files.readAllBytes(image.toPath()));
						} catch (IOException e) {
							e.printStackTrace();
						}
						productBranchRepository.save(productBranch);
					}
				}
			}
		}

	}
	
	@Override
	public void processProductBranchOld() {
		File image = null;
		try {
			processImage(PATH_IMAGE_PRODUCT + BAR_CODE_IMG_NAME, PATH_IMAGE_PRODUCT + BAR_CODE_COPY_IMG_NAME,
					PNG_EXTENSION);
			image = new File(PATH_IMAGE_PRODUCT + BAR_CODE_COPY_IMG_NAME);
		} catch (IOException e) {
			e.printStackTrace();
		}
		final List<Branch> branches = branchRepository.findAll();
		final List<DistributionCenter> distCenter = distributionCenterRepository.findAll();
		List<ProductHistory> productHistory;
		Calendar currentDate = Calendar.getInstance();
		currentDate.set(Calendar.YEAR, 2015);
		currentDate.set(Calendar.MONTH, 0);
		currentDate.set(Calendar.DATE, 1);
		Calendar date = Calendar.getInstance();
		Calendar auxDate = Calendar.getInstance();
		ProductBranch productBranch;
		BigDecimal polinomy;
		BigDecimal sum;
		int year;
		int auxYear;

		// Por cada sucursal
		for (final Branch branch : branches) {
			log.info(":: Branch {} ", branch.getId());
			date.set(Calendar.YEAR, 2010);
			date.set(Calendar.MONTH, 0);
			date.set(Calendar.DATE, 1);
			year = date.get(Calendar.YEAR);
			productHistory = productHistoryRepository.findAllProductByYear(year);
			// Registro de ventas del periodo de cinco años por cada sucursal.
			while (currentDate.getTime().after(date.getTime())) {
				auxYear = date.get(Calendar.YEAR);
				if (auxYear != year) {
					year = auxYear;
					productHistory = productHistoryRepository.findAllProductByYear(year);
				}
				log.info(":: YEAR {} ", year);
				// Registro de ventas semanal.
				auxDate = (Calendar) date.clone();
				polinomy = getValuePolinomy(ThreadLocalRandom.current().nextInt(50, 90));
				log.info("**** Polinomy {} ", polinomy);
				sum = BigDecimal.ZERO;
				while (polinomy.compareTo(sum) == 1) {
					// Productos del año.
					for (final ProductHistory product : productHistory) {
						productBranch = new ProductBranch();
						productBranch.setIdbranch(branch);
						productBranch.setIdproducthistory(product);
						productBranch.setStatus(ProductBranchStatus.SOLD);
						auxDate.set(Calendar.DATE, ThreadLocalRandom.current().nextInt(date.get(Calendar.DATE),
								date.get(Calendar.DATE) + 6));
						productBranch.setSaledate(formatDate.format(auxDate.getTime()));
						auxDate.set(Calendar.DATE,
								auxDate.get(Calendar.DATE) - ThreadLocalRandom.current().nextInt(15, 30));
						productBranch.setCreationdate(formatDate.format(auxDate.getTime()));
						auxDate.set(Calendar.DATE, auxDate.get(Calendar.DATE) + 50);
						productBranch.setExpirationdate(formatDate.format(auxDate.getTime()));
						auxDate.set(Calendar.DATE, date.get(Calendar.DATE));
						auxDate.set(Calendar.MONTH, date.get(Calendar.MONTH));
						auxDate.set(Calendar.YEAR, date.get(Calendar.YEAR));
						productBranch.setDistributioncenter(
								distCenter.get(ThreadLocalRandom.current().nextInt(0, distCenter.size() - 1)));
						productBranch.setLocation(ProductLocation.SOLD);
						sum = sum.add(product.getSaleprice());
						try {
							productBranch.setBarcode(Files.readAllBytes(image.toPath()));
						} catch (IOException e) {
							e.printStackTrace();
						}
						productBranchRepository.save(productBranch);
					}
				}
				date.set(Calendar.DATE, date.get(Calendar.DATE) + 7);
			}

			// Otro ciclo para cubrir los dias restantes, si los hubiera.
			if (currentDate.get(Calendar.DATE) - date.get(Calendar.DATE) > 0) {
				auxDate = (Calendar) date.clone();
				polinomy = getValuePolinomy(200);
				sum = BigDecimal.ZERO;
				while (polinomy.compareTo(sum) == 1) {
					for (final ProductHistory product : productHistory) {
						// Un registro por cada producto
						productBranch = new ProductBranch();
						productBranch.setIdbranch(branch);
						productBranch.setIdproducthistory(product);
						productBranch.setStatus(ProductBranchStatus.SOLD);
						auxDate.set(Calendar.DATE, ThreadLocalRandom.current().nextInt(date.get(Calendar.DATE),
								date.get(Calendar.DATE) + currentDate.get(Calendar.DATE) - date.get(Calendar.DATE)));
						productBranch.setSaledate(formatDate.format(auxDate.getTime()));
						auxDate.set(Calendar.DATE,
								auxDate.get(Calendar.DATE) - ThreadLocalRandom.current().nextInt(15, 30));
						productBranch.setCreationdate(formatDate.format(auxDate.getTime()));
						auxDate.set(Calendar.DATE, auxDate.get(Calendar.DATE) + 50);
						productBranch.setExpirationdate(formatDate.format(auxDate.getTime()));
						auxDate.set(Calendar.DATE, date.get(Calendar.DATE));
						auxDate.set(Calendar.MONTH, date.get(Calendar.MONTH));
						auxDate.set(Calendar.YEAR, date.get(Calendar.YEAR));
						productBranch.setDistributioncenter(
								distCenter.get(ThreadLocalRandom.current().nextInt(0, distCenter.size() - 1)));
						productBranch.setLocation(ProductLocation.SOLD);
						sum = sum.add(product.getSaleprice());
						try {
							productBranch.setBarcode(Files.readAllBytes(image.toPath()));
						} catch (IOException e) {
							e.printStackTrace();
						}
						productBranchRepository.save(productBranch);
					}
				}
			}
		}
	}

	@Override
	public void processProductBranch() {
		File image = null;
		try {
			processImage(PATH_IMAGE_PRODUCT + BAR_CODE_IMG_NAME, PATH_IMAGE_PRODUCT + BAR_CODE_COPY_IMG_NAME,
					PNG_EXTENSION);
			image = new File(PATH_IMAGE_PRODUCT + BAR_CODE_COPY_IMG_NAME);
		} catch (IOException e) {
			e.printStackTrace();
		}
		final List<Branch> branches = branchRepository.findAll();
		final List<DistributionCenter> distCenter = distributionCenterRepository.findAll();
		List<ProductHistory> productHistory;
		Calendar currentDate = Calendar.getInstance();
		Calendar date = Calendar.getInstance();
		Calendar auxDate = Calendar.getInstance();
		ProductBranch productBranch;
		BigDecimal polinomy;
		BigDecimal sum;
		int year;
		int auxYear;

		// Por cada sucursal
		for (final Branch branch : branches) {
			log.info(":: Branch {} ", branch.getId());
			date.set(Calendar.YEAR, 2015);
			date.set(Calendar.MONTH, 0);
			date.set(Calendar.DATE, 1);
			year = date.get(Calendar.YEAR);
			productHistory = productHistoryRepository.findAllProductByYear(year);
			// Registro de ventas del periodo de cinco años por cada sucursal.
			while (currentDate.getTime().after(date.getTime())) {
				auxYear = date.get(Calendar.YEAR);
				if (auxYear != year) {
					year = auxYear;
					log.info(":: YEAR {} ", year);
					productHistory = productHistoryRepository.findAllProductByYear(year);
				}
				// Registro de ventas semanal.
				auxDate = (Calendar) date.clone();
				polinomy = getValuePolinomy(ThreadLocalRandom.current().nextInt(200, 300));
				sum = BigDecimal.ZERO;
				while (polinomy.compareTo(sum) == 1) {
					// Productos del año.
					for (final ProductHistory product : productHistory) {
						productBranch = new ProductBranch();
						productBranch.setIdbranch(branch);
						productBranch.setIdproducthistory(product);
						productBranch.setStatus(ProductBranchStatus.SOLD);
						auxDate.set(Calendar.DATE, ThreadLocalRandom.current().nextInt(date.get(Calendar.DATE),
								date.get(Calendar.DATE) + 6));
						productBranch.setSaledate(formatDate.format(auxDate.getTime()));
						auxDate.set(Calendar.DATE,
								auxDate.get(Calendar.DATE) - ThreadLocalRandom.current().nextInt(15, 30));
						productBranch.setCreationdate(formatDate.format(auxDate.getTime()));
						auxDate.set(Calendar.DATE, auxDate.get(Calendar.DATE) + 50);
						productBranch.setExpirationdate(formatDate.format(auxDate.getTime()));
						auxDate.set(Calendar.DATE, date.get(Calendar.DATE));
						auxDate.set(Calendar.MONTH, date.get(Calendar.MONTH));
						auxDate.set(Calendar.YEAR, date.get(Calendar.YEAR));
						productBranch.setDistributioncenter(
								distCenter.get(ThreadLocalRandom.current().nextInt(0, distCenter.size() - 1)));
						productBranch.setLocation(ProductLocation.SOLD);
						sum = sum.add(product.getSaleprice());
						try {
							productBranch.setBarcode(Files.readAllBytes(image.toPath()));
						} catch (IOException e) {
							e.printStackTrace();
						}
						productBranchRepository.save(productBranch);
					}
				}
				date.set(Calendar.DATE, date.get(Calendar.DATE) + 7);
			}

			// Otro ciclo para cubrir los dias restantes, si los hubiera.
			if (currentDate.get(Calendar.DATE) - date.get(Calendar.DATE) > 0) {
				auxDate = (Calendar) date.clone();
				polinomy = getValuePolinomy(200);
				sum = BigDecimal.ZERO;
				while (polinomy.compareTo(sum) == 1) {
					for (final ProductHistory product : productHistory) {
						// Un registro por cada producto
						productBranch = new ProductBranch();
						productBranch.setIdbranch(branch);
						productBranch.setIdproducthistory(product);
						productBranch.setStatus(ProductBranchStatus.SOLD);
						auxDate.set(Calendar.DATE, ThreadLocalRandom.current().nextInt(date.get(Calendar.DATE),
								date.get(Calendar.DATE) + currentDate.get(Calendar.DATE) - date.get(Calendar.DATE)));
						productBranch.setSaledate(formatDate.format(auxDate.getTime()));
						auxDate.set(Calendar.DATE,
								auxDate.get(Calendar.DATE) - ThreadLocalRandom.current().nextInt(15, 30));
						productBranch.setCreationdate(formatDate.format(auxDate.getTime()));
						auxDate.set(Calendar.DATE, auxDate.get(Calendar.DATE) + 50);
						productBranch.setExpirationdate(formatDate.format(auxDate.getTime()));
						auxDate.set(Calendar.DATE, date.get(Calendar.DATE));
						auxDate.set(Calendar.MONTH, date.get(Calendar.MONTH));
						auxDate.set(Calendar.YEAR, date.get(Calendar.YEAR));
						productBranch.setDistributioncenter(
								distCenter.get(ThreadLocalRandom.current().nextInt(0, distCenter.size() - 1)));
						productBranch.setLocation(ProductLocation.SOLD);
						sum = sum.add(product.getSaleprice());
						try {
							productBranch.setBarcode(Files.readAllBytes(image.toPath()));
						} catch (IOException e) {
							e.printStackTrace();
						}
						productBranchRepository.save(productBranch);
					}
				}
			}
		}
	}

	private BigDecimal getValuePolinomy(Integer x) {
		return new BigDecimal(x + 5 * x + (8 / 7) * x + 10550);
	}

	@Override
	public void processPurchaseOrder() {
		final Purchaseorder item = new Purchaseorder();
		item.setCreationdate(formatDate.format(Calendar.getInstance().getTime()));
		item.setDeliverydate(formatDate.format(Calendar.getInstance().getTime()));
		item.setDescription("Test description");
		final Branch idbranch = new Branch();
		idbranch.setId(1);
		item.setIdbranch(idbranch);
		final Product idproduct = new Product();
		idproduct.setId(1);
		item.setIdproduct(idproduct);
		item.setQuantity(122);
		item.setStatus(PurchaseOrderStatus.CREATED);
		item.setStatusvalue(1);
		purchaseOrderRepository.save(item);
		log.info(":: Purchase Order {} ", item);
	}

	@Override
	public void updateImagesPromotions() {
		File image;
		int numberImage;
		final List<Promotions> promotions = promotionsRepository.findAll();
		for (final Promotions it : promotions) {
			try {
				numberImage = ThreadLocalRandom.current().nextInt(1, 21);
				processImage(PATH_IMAGE_PROMOTIONS + numberImage + FULL_STOP + JPEG_EXTENSION,
						PATH_IMAGE_PROMOTIONS + "Copy" + numberImage + FULL_STOP + JPEG_EXTENSION, JPEG_EXTENSION);
				image = new File(PATH_IMAGE_PROMOTIONS + "Copy" + numberImage + FULL_STOP + JPEG_EXTENSION);
				it.setImage(Files.readAllBytes(image.toPath()));
			} catch (IOException e) {
				e.printStackTrace();
			}
			promotionsRepository.save(it);
		}
	}

	@Override
	public void processPromotions() {
		final List<ProductHistory> products = productHistoryRepository.findAll();
		String name;
		Promotions promotion;
		Calendar date = Calendar.getInstance();
		Date auxDate = null;
		BigDecimal percentage;
		File image;
		int currentYear = date.get(Calendar.YEAR);
		int numberImage;

		for (final ProductHistory product : products) {
			name = "Promoción " + product.getIdproduct().getName();
			promotion = new Promotions();
			promotion.setName(name.length() > 30 ? name.substring(0, 30) : name);
			promotion.setDescription("Estrategia para incrementar la venta de " + product.getIdproduct().getName());
			try {
				auxDate = formatDate.parse(product.getCreationdate());
				date.setTime(auxDate);
			} catch (Exception e) {
				e.printStackTrace();
			}
			date.set(Calendar.DATE, date.get(Calendar.DATE) + ThreadLocalRandom.current().nextInt(10, 21));
			promotion.setCreationdate(formatDate.format(date.getTime()));
			date.setTime(auxDate);
			date.set(Calendar.DATE, date.get(Calendar.DATE) + ThreadLocalRandom.current().nextInt(21, 41));
			promotion.setEnddate(formatDate.format(date.getTime()));
			promotion.setIdproduct(product.getIdproduct());
			percentage = new BigDecimal(ThreadLocalRandom.current().nextDouble(0.1, 0.30)).setScale(2,
					RoundingMode.HALF_UP);
			promotion.setPricepromotion(product.getSaleprice().subtract(product.getSaleprice().multiply(percentage))
					.setScale(2, RoundingMode.HALF_UP));
			promotion.setPercentagediscount(percentage);
			promotion.setTaxes(
					promotion.getPricepromotion().multiply(new BigDecimal(0.16)).setScale(2, RoundingMode.HALF_UP));
			date.setTime(auxDate);
			if (date.get(Calendar.YEAR) < currentYear) {
				promotion.setStatus(PromotionsStatus.INACTIVE);
			} else {
				promotion.setStatus(PromotionsStatus.ACTIVE);
			}
			try {
				numberImage = ThreadLocalRandom.current().nextInt(1, 21);
				processImage(PATH_IMAGE_PROMOTIONS + numberImage + FULL_STOP + JPEG_EXTENSION,
						PATH_IMAGE_PROMOTIONS + "Copy" + numberImage + FULL_STOP + JPEG_EXTENSION, JPEG_EXTENSION);
				image = new File(PATH_IMAGE_PROMOTIONS + "Copy" + numberImage + FULL_STOP + JPEG_EXTENSION);
				promotion.setImage(Files.readAllBytes(image.toPath()));
			} catch (IOException e) {
				e.printStackTrace();
			}
			promotionsRepository.save(promotion);
			log.info(":: Promotions {} ", promotion);
		}
	}

	@Override
	public void processPromotionsNewProducts() {
		final Product idproduct = new Product();
		idproduct.setId(30);
		final List<ProductHistory> products = productHistoryRepository.findAllByIdproductGraterthan(idproduct);
		String name;
		Promotions promotion;
		Calendar date = Calendar.getInstance();
		Date auxDate = null;
		BigDecimal percentage;
		File image;
		int currentYear = date.get(Calendar.YEAR);
		int numberImage;

		for (final ProductHistory product : products) {
			name = "Promoción " + product.getIdproduct().getName();
			promotion = new Promotions();
			promotion.setName(name.length() > 30 ? name.substring(0, 30) : name);
			promotion.setDescription("Estrategia para incrementar la venta de " + product.getIdproduct().getName());
			try {
				auxDate = formatDate.parse(product.getCreationdate());
				date.setTime(auxDate);
			} catch (Exception e) {
				e.printStackTrace();
			}
			date.set(Calendar.DATE, date.get(Calendar.DATE) + ThreadLocalRandom.current().nextInt(10, 21));
			promotion.setCreationdate(formatDate.format(date.getTime()));
			date.setTime(auxDate);
			date.set(Calendar.DATE, date.get(Calendar.DATE) + ThreadLocalRandom.current().nextInt(21, 41));
			promotion.setEnddate(formatDate.format(date.getTime()));
			promotion.setIdproduct(product.getIdproduct());
			percentage = new BigDecimal(ThreadLocalRandom.current().nextDouble(0.1, 0.30)).setScale(2,
					RoundingMode.HALF_UP);
			promotion.setPricepromotion(product.getSaleprice().subtract(product.getSaleprice().multiply(percentage))
					.setScale(2, RoundingMode.HALF_UP));
			promotion.setPercentagediscount(percentage);
			promotion.setTaxes(
					promotion.getPricepromotion().multiply(new BigDecimal(0.16)).setScale(2, RoundingMode.HALF_UP));
			date.setTime(auxDate);
			if (date.get(Calendar.YEAR) < currentYear) {
				promotion.setStatus(PromotionsStatus.INACTIVE);
			} else {
				promotion.setStatus(PromotionsStatus.ACTIVE);
			}
			try {
				numberImage = ThreadLocalRandom.current().nextInt(1, 21);
				processImage(PATH_IMAGE_PROMOTIONS + numberImage + FULL_STOP + JPEG_EXTENSION,
						PATH_IMAGE_PROMOTIONS + "Copy" + numberImage + FULL_STOP + JPEG_EXTENSION, JPEG_EXTENSION);
				image = new File(PATH_IMAGE_PROMOTIONS + "Copy" + numberImage + FULL_STOP + JPEG_EXTENSION);
				promotion.setImage(Files.readAllBytes(image.toPath()));
			} catch (IOException e) {
				e.printStackTrace();
			}
			promotionsRepository.save(promotion);
			log.info(":: Promotions {} ", promotion);
		}
	}

	@Override
	public void processCustomer() {
		List<String> names = new ArrayList<>();
		names.add("Jorge Lugo Mendez");
		names.add("Carlos Velez Domingo");
		names.add("Sergio Jurado Hernandez");
		names.add("Omar Centeno Vidal");
		names.add("Antonio Castro Ferrer");
		names.add("Fernando Ledezma Orduñez");
		names.add("Daniel Bermudez Caso");
		names.add("Johan Mayen Duran");
		names.add("Carlos Mar Perez");
		names.add("Pedro Cruz Eguia");
		names.add("Graciela Morlan Baldobinos");
		names.add("Maria Castro Solis");
		names.add("Gabriela Aremndariz Gomez");
		names.add("Karina Roldan Graciela");
		names.add("Kymberly Chavez Granados");

		names.add("Antonio Garza Nuñez");
		names.add("Gutierrez Manriquez");
		names.add("Rodrigo Nieves Maya");
		names.add("Jaime Covarrubias Lopez");
		names.add("Sergio Luengas Bustamante");
		names.add("Ricardo Garcia Anatole");
		names.add("Mariana Romo Vivar");
		names.add("Guadalupe Villegas Cortes");
		names.add("Tania Morelos Chaidez");
		names.add("Marisol Ramirez Cruz");
		names.add("Pedro Illezcas Gonzalez");
		names.add("Tatiana Gil Alanis");
		names.add("Euridice Torres Alamos");
		names.add("Ana Valencia Garcia");
		names.add("Valentin Trujillo Mata");

		names.add("Victor Campos Barranco");
		names.add("Florencio Cruz Villaseñor");
		names.add("Alfredo Figueroa Ramos");
		names.add("Alejandra Barrios Uribe");
		names.add("Guillermo Ramirez Cuevas");

		List<String> municipalities = new ArrayList<>();
		municipalities.add("Pinal de Amoles");
		municipalities.add("Corregidora");
		municipalities.add("Ezequiel Montes");
		municipalities.add("Huimilpan");
		municipalities.add("Colón");
		municipalities.add("Cadereyta de Montes");
		municipalities.add("Arroyo Seco");
		municipalities.add("Amealco de Bonfil");
		municipalities.add("El Marqués");
		municipalities.add("Pedro Escobedo");
		municipalities.add("Peñamiller");
		municipalities.add("Querétaro");
		municipalities.add("San Joaquín	");
		municipalities.add("San Juan del Río");
		municipalities.add("Tequisquiapan");
		municipalities.add("Tolimán");
		municipalities.add("Miguel Hidalgo");
		municipalities.add("Coyoacán");
		municipalities.add("Cuauhtémoc");
		municipalities.add("Azcapotzalco");
		municipalities.add("Tlalpan");
		municipalities.add("Cuajimalpa");
		municipalities.add("Iztacalco");
		municipalities.add("Venustiano Carranza");
		municipalities.add("La Magdalena Contreras");
		municipalities.add("Xochimilco");
		municipalities.add("Gustavo A. Madero");
		municipalities.add("Álvaro Obregón");
		municipalities.add("Iztapalapa");
		municipalities.add("Tláhuac");
		municipalities.add("Milpa Alta");
		municipalities.add("Ahuatlán");
		municipalities.add("Amozoc");
		municipalities.add("Aquixtla");
		municipalities.add("Coyotepec");
		municipalities.add("Jalpan");
		municipalities.add("Nicolás Bravo");
		municipalities.add("San Andrés Cholula");
		municipalities.add("Puebla");
		municipalities.add("Palmar de Bravo");

		List<String> jobs = new ArrayList<>();
		jobs.add("Contador Público");
		jobs.add("Construcción");
		jobs.add("Abogado");
		jobs.add("Comerciante");
		jobs.add("Ingeniero");
		jobs.add("Administrativo");
		jobs.add("Ejecutivo de Ventas");
		jobs.add("Plomeria");
		jobs.add("Almacenista");
		jobs.add("Asesor financiero");

		jobs.add("Recepcionista");
		jobs.add("Asistente de Dirección");
		jobs.add("Enfermero");
		jobs.add("Maestro");
		jobs.add("Carpintero");

		Customers customer;
		int sex;
		int[] states = { 21, 22, 9 };
		final List<CustomerProfile> custProfile = customerProfileRepository.findAll();
		for (int index = 0; index < 2500; index++) {
			customer = new Customers();
			customer.setAge(ThreadLocalRandom.current().nextInt(18, 51));
			customer.setEducationlevel(EducationLevel.of(ThreadLocalRandom.current().nextInt(1, 4)));

			customer.setIdprofile(custProfile.get(4));
			customer.setJob(jobs.get(ThreadLocalRandom.current().nextInt(0, 15)));
			customer.setMaritalstatus(MaritalStatus.of(ThreadLocalRandom.current().nextInt(1, 5)));
			customer.setMunicipality(municipalities.get(ThreadLocalRandom.current().nextInt(0, 40)));
			customer.setName(names.get(ThreadLocalRandom.current().nextInt(0, 35)));
			sex = ThreadLocalRandom.current().nextInt(1, 9);
			if (sex % 2 == 0) {
				customer.setSex(FEMALE);
			} else {
				customer.setSex(MALE);
			}
			customer.setState(State.of(states[ThreadLocalRandom.current().nextInt(0, 3)]));
			customerRepository.save(customer);
			log.info(":: Customer {} ", customer);
		}
	}

	@Override
	public void processCustomersPromotions() {
		final CustomersPromotions item = new CustomersPromotions();
		item.setDate(formatDate.format(Calendar.getInstance().getTime()));
		final Customers idcustomer = new Customers();
		idcustomer.setId(1);
		item.setIdcustomer(idcustomer);
		final Promotions idpromotion = new Promotions();
		idpromotion.setId(2);
		item.setIdpromotion(idpromotion);
		item.setQuantity(22);
		customersPromotionsRepository.save(item);
		log.info(":: CustomersPromotions {} ", item);
	}

	@Override
	public void processProfilePromotions() {
//		final List<ProfilePromotions> profilePromotions = profilePromotionsRepository.findAll();
//		for (ProfilePromotions items: profilePromotions) {
//			items.setActive(true);
//			profilePromotionsRepository.save(items);
//		}
//		if (true) {
//			return;
//		}

		final List<CustomerProfile> custProfile = customerProfileRepository.findAll();
		final List<Promotions> promotions = promotionsRepository.findAll();
		ProfilePromotions profilePromotion;
		for (final CustomerProfile item : custProfile) {
			for (int index = 0; index < NUMBER_PROFILE_PROMOTIONS; index++) {
				profilePromotion = new ProfilePromotions();
				profilePromotion.setAccepted(Boolean.TRUE);
				profilePromotion.setActive(Boolean.TRUE);
				profilePromotion.setCreationdate(formatDate.format(Calendar.getInstance().getTime()));
				profilePromotion.setIdprofile(item);
				profilePromotion
						.setIdpromotion(promotions.get(ThreadLocalRandom.current().nextInt(0, promotions.size())));
				profilePromotionsRepository.save(profilePromotion);
				log.info(":: ProfilePromotions {} ", profilePromotion);
			}
		}
	}

	@Override
	public void processProfileKMeans() {
		final List<CustomerProfile> custProfile = customerProfileRepository.findAll();
		ProfileKmeans item;
		File image;
		String[] colors = { "yellow", "green", "red", "blue", "grey" };
		int index = 0;
		for (final CustomerProfile profile : custProfile) {
			item = new ProfileKmeans();
			item.setIdprofile(profile);
			item.setDate(formatDate.format(Calendar.getInstance().getTime()));
			item.setIsactive(Boolean.TRUE);
			item.setNumbercustomers(550);
			try {
				image = new File("/Users/juancarlospedrazaalcala/Documents/Images/k_means_cluster.png");
				item.setImage(Files.readAllBytes(image.toPath()));
			} catch (Exception e) {
				e.printStackTrace();
			}
			item.setColor(colors[index]);
			index++;
			profileKMeansRepository.save(item);
			log.info(":: ProfileKMeans {} ", item);
		}
	}

	public void processKmeansImage() {
		final List<ProfileKmeans> kmeans = profileKMeansRepository.findAll();
		File image;
		for (final ProfileKmeans item : kmeans) {
			try {
				image = new File("/Users/juancarlospedrazaalcala/Documents/Images/k_means_cluster.png");
				item.setImage(Files.readAllBytes(image.toPath()));
				profileKMeansRepository.save(item);
				log.info(":: ProfileKMeans {} ", item);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void processProfileKNearest() {
		final List<CustomerProfile> custProfile = customerProfileRepository.findAll();
		ProfileKnearest item;
		File image;
		for (final CustomerProfile profile : custProfile) {
			item = new ProfileKnearest();
			item.setIdprofile(profile);
			item.setIsactive(Boolean.TRUE);
			item.setDate(formatDate.format(Calendar.getInstance().getTime()));
			item.setNumbercustomers(534);
			try {
				image = new File("/Users/juancarlospedrazaalcala/Documents/Images/k_nearest.png");
				item.setImage(Files.readAllBytes(image.toPath()));
			} catch (Exception e) {
				e.printStackTrace();
			}
			profileKNearestRepository.save(item);
			log.info(":: ProfileKNearest {} ", item);
		}
	}

	public void processProfileKNearestImage() {
		final List<ProfileKnearest> knearest = profileKNearestRepository.findAll();
		File image;
		for (final ProfileKnearest item : knearest) {
			item.setNumbercustomers(534);
			try {
				image = new File("/Users/juancarlospedrazaalcala/Documents/Images/k_nearest.png");
				item.setImage(Files.readAllBytes(image.toPath()));
			} catch (Exception e) {
				e.printStackTrace();
			}
			profileKNearestRepository.save(item);
			log.info(":: ProfileKNearest {} ", item);
		}
	}

	@Override
	public void processProfileRegression() {
		final List<CustomerProfile> custProfile = customerProfileRepository.findAll();
		ProfileRegression regression;
		File image;
		for (final CustomerProfile profile : custProfile) {
			regression = new ProfileRegression();
			regression.setIdprofile(profile);
			regression.setDate(formatDate.format(Calendar.getInstance().getTime()));
			regression.setIsactive(Boolean.TRUE);
			regression.setNumbercustomersleft(217);
			regression.setNumbercustomersloyal(272);
			try {
				image = new File("/Users/juancarlospedrazaalcala/Documents/Images/confusion_matrix.png");
				regression.setImageconfusion(Files.readAllBytes(image.toPath()));
			} catch (Exception e) {
				e.printStackTrace();
			}
			profileRegressionRepository.save(regression);
			log.info(":: ProfileRegression {} ", regression);
		}
	}

	public void processProfileRegressionImage() {
		final List<ProfileRegression> regression = profileRegressionRepository.findAll();
		File image;
		for (final ProfileRegression item : regression) {
			try {
				image = new File("/Users/juancarlospedrazaalcala/Documents/Images/confusion_matrix.png");
				item.setImageconfusion(Files.readAllBytes(image.toPath()));
			} catch (Exception e) {
				e.printStackTrace();
			}
			profileRegressionRepository.save(item);
			log.info(":: ProfileRegression {} ", item);
		}

	}

	@Override
	public void processProductRecomendationProfile() {
		final List<CustomerProfile> custProfile = customerProfileRepository.findAll();
		final List<Product> products = productRepository.findAll();
		ProductRecomendationProfile item;
		int index;
		for (final CustomerProfile profile : custProfile) {
			for (index = 0; index < 10; index++) {
				item = new ProductRecomendationProfile();
				item.setDate(formatTimestamp.format(Calendar.getInstance().getTime()));
				item.setIdproduct(products.get(index));
				item.setIdprofile(profile);
				item.setIsactive(Boolean.TRUE);
				productRecomendationProfileRepository.save(item);
				log.info(":: ProductRecomendationProfile {} ", item);
			}
		}
	}

	@Override
	public void processPromotionsRecomendationProfile() {
		final List<CustomerProfile> custProfile = customerProfileRepository.findAll();
		final PromotionsRecomendationProfile item = new PromotionsRecomendationProfile();
		item.setDate(formatTimestamp.format(Calendar.getInstance().getTime()));
		item.setIdprofile(custProfile.get(0));
		final Promotions idpromotion = new Promotions();
		idpromotion.setId(2);
		item.setIdpromotion(idpromotion);
		item.setIsactive(Boolean.TRUE);
		promotionsRecomendationProfileRepository.save(item);
		log.info(":: PromotionsRecomendationProfile {} ", item);
	}

	@Override
	public void processSales() {
		final Sales item = new Sales();
		final Branch idbranch = new Branch();
		idbranch.setId(1);
		item.setIdbranch(idbranch);
		final Customers idcustomer = new Customers();
		idcustomer.setId(1);
		item.setIdcustomer(idcustomer);
		final Product idproduct = new Product();
		idproduct.setId(1);
		item.setIdproduct(idproduct);
		final Promotions idpromotion = new Promotions();
		idpromotion.setId(2);
		item.setIdpromotion(idpromotion);
		item.setQuantity(22);
		salesRepository.save(item);
		log.info(":: Sales {} ", item);
	}

	@Override
	public void processStatisticsVariableRelation() {
		StatisticsVariableRelation item = new StatisticsVariableRelation();
		Statistics idstatistics = new Statistics();
		idstatistics.setId(1);
		item.setStatisticvar("Media");
		item.setVarvalueone("0.798237893");
		item.setVarvaluetwo("0.3183091831");
		item.setVarvaluethree("0.38109391");
		item.setVarvaluefour("0.137128937");
		item.setIdstatistics(idstatistics);
		statisticsVariableRelationRepository.save(item);
		log.info(":: StatisticsVariableRelation {} ", item);

		item = new StatisticsVariableRelation();
		item.setStatisticvar("Moda");
		item.setVarvalueone("0.798237893");
		item.setVarvaluetwo("0.3183091831");
		item.setVarvaluethree("0.38109391");
		item.setVarvaluefour("0.137128937");
		item.setIdstatistics(idstatistics);
		statisticsVariableRelationRepository.save(item);
		log.info(":: StatisticsVariableRelation {} ", item);

		item = new StatisticsVariableRelation();
		item.setStatisticvar("Desv. Estandar");
		item.setVarvalueone("0.798237893");
		item.setVarvaluetwo("0.3183091831");
		item.setVarvaluethree("0.38109391");
		item.setVarvaluefour("0.137128937");
		item.setIdstatistics(idstatistics);
		statisticsVariableRelationRepository.save(item);
		log.info(":: StatisticsVariableRelation {} ", item);

		item = new StatisticsVariableRelation();
		item.setStatisticvar("Cuartil");
		item.setVarvalueone("0.798237893");
		item.setVarvaluetwo("0.3183091831");
		item.setVarvaluethree("0.38109391");
		item.setVarvaluefour("0.137128937");
		item.setIdstatistics(idstatistics);
		statisticsVariableRelationRepository.save(item);
		log.info(":: StatisticsVariableRelation {} ", item);

		idstatistics = new Statistics();
		idstatistics.setId(2);
		item.setStatisticvar("Media");
		item.setVarvalueone("0.798237893");
		item.setVarvaluetwo("0.3183091831");
		item.setVarvaluethree("0.38109391");
		item.setVarvaluefour("0.137128937");
		item.setIdstatistics(idstatistics);
		statisticsVariableRelationRepository.save(item);
		log.info(":: StatisticsVariableRelation {} ", item);

		item = new StatisticsVariableRelation();
		item.setStatisticvar("Moda");
		item.setVarvalueone("0.798237893");
		item.setVarvaluetwo("0.3183091831");
		item.setVarvaluethree("0.38109391");
		item.setVarvaluefour("0.137128937");
		item.setIdstatistics(idstatistics);
		statisticsVariableRelationRepository.save(item);
		log.info(":: StatisticsVariableRelation {} ", item);

		item = new StatisticsVariableRelation();
		item.setStatisticvar("Desv. Estandar");
		item.setVarvalueone("0.798237893");
		item.setVarvaluetwo("0.3183091831");
		item.setVarvaluethree("0.38109391");
		item.setVarvaluefour("0.137128937");
		item.setIdstatistics(idstatistics);
		statisticsVariableRelationRepository.save(item);
		log.info(":: StatisticsVariableRelation {} ", item);

		item = new StatisticsVariableRelation();
		item.setStatisticvar("Cuartil");
		item.setVarvalueone("0.798237893");
		item.setVarvaluetwo("0.3183091831");
		item.setVarvaluethree("0.38109391");
		item.setVarvaluefour("0.137128937");
		item.setIdstatistics(idstatistics);
		statisticsVariableRelationRepository.save(item);
		log.info(":: StatisticsVariableRelation {} ", item);

		idstatistics = new Statistics();
		idstatistics.setId(3);
		item.setStatisticvar("Media");
		item.setVarvalueone("0.798237893");
		item.setVarvaluetwo("0.3183091831");
		item.setVarvaluethree("0.38109391");
		item.setVarvaluefour("0.137128937");
		item.setIdstatistics(idstatistics);
		statisticsVariableRelationRepository.save(item);
		log.info(":: StatisticsVariableRelation {} ", item);

		item = new StatisticsVariableRelation();
		item.setStatisticvar("Moda");
		item.setVarvalueone("0.798237893");
		item.setVarvaluetwo("0.3183091831");
		item.setVarvaluethree("0.38109391");
		item.setVarvaluefour("0.137128937");
		item.setIdstatistics(idstatistics);
		statisticsVariableRelationRepository.save(item);
		log.info(":: StatisticsVariableRelation {} ", item);

		item = new StatisticsVariableRelation();
		item.setStatisticvar("Desv. Estandar");
		item.setVarvalueone("0.798237893");
		item.setVarvaluetwo("0.3183091831");
		item.setVarvaluethree("0.38109391");
		item.setVarvaluefour("0.137128937");
		item.setIdstatistics(idstatistics);
		statisticsVariableRelationRepository.save(item);
		log.info(":: StatisticsVariableRelation {} ", item);

		item = new StatisticsVariableRelation();
		item.setStatisticvar("Cuartil");
		item.setVarvalueone("0.798237893");
		item.setVarvaluetwo("0.3183091831");
		item.setVarvaluethree("0.38109391");
		item.setVarvaluefour("0.137128937");
		item.setIdstatistics(idstatistics);
		statisticsVariableRelationRepository.save(item);
		log.info(":: StatisticsVariableRelation {} ", item);
	}

	@Override
	public void processStatistics() {
		Statistics item = new Statistics();
		Branch idbranch = new Branch();
		idbranch.setId(1);
		item.setIdbranch(idbranch);
		Product idproduct = new Product();
		idproduct.setId(2);
		item.setIdproduct(idproduct);
		item.setMonth(2);
		item.setYear(2019);
		StatisticsVariableRelation variablerelations = new StatisticsVariableRelation();
		variablerelations.setId(1);
		item.setVarnameone("Estado");
		item.setVarnametwo("Proveedor");
		item.setVarnamethree("Marca");
		item.setVarnamefour("Promocion");
		statisticsRepository.save(item);
		log.info(":: Statistics {} ", item);

		item = new Statistics();
		item.setIdbranch(idbranch);
		item.setYear(2019);
		idproduct = new Product();
		idproduct.setId(2);
		item.setIdproduct(idproduct);
		item.setVarnameone("Estado");
		item.setVarnametwo("Proveedor");
		item.setVarnamethree("Marca");
		item.setVarnamefour("Promocion");
		statisticsRepository.save(item);
		log.info(":: Statistics {} ", item);

		item = new Statistics();
		item.setIdbranch(idbranch);
		item.setYear(2019);
		item.setVarnameone("Estado");
		item.setVarnametwo("Proveedor");
		item.setVarnamethree("Marca");
		item.setVarnamefour("Promocion");
		statisticsRepository.save(item);
		log.info(":: Statistics {} ", item);

	}

	@Override
	public void processStatisticsHeatMap() {
		StatisticsHeatmap item = new StatisticsHeatmap();
		Branch idbranch = new Branch();
		idbranch.setId(1);
		item.setIdbranch(idbranch);
		Product idproduct = new Product();
		idproduct.setId(2);
		item.setIdproduct(idproduct);
		item.setKeyvar("Ventas");
		try {
			final File image = new File("/Users/juancarlospedrazaalcala/Documents/Images/heatMap.jpeg");
			log.info(":: Length image {} ", image.length());
			item.setImage(Files.readAllBytes(image.toPath()));
		} catch (IOException e) {
			e.printStackTrace();
		}
		item.setMonth(2);
		item.setVerticalvarone("Sucursal");
		item.setVerticalvartwo("Categoria");
		item.setYear(2019);
		statisticsHeatMapRepository.save(item);
		log.info(":: StatisticsHeatMap {} ", item);

		item = new StatisticsHeatmap();
		idbranch = new Branch();
		idbranch.setId(1);
		item.setIdbranch(idbranch);
		item.setKeyvar("Ventas");
		try {
			final File image = new File("/Users/juancarlospedrazaalcala/Documents/Images/heatMap.jpeg");
			log.info(":: Length image {} ", image.length());
			item.setImage(Files.readAllBytes(image.toPath()));
		} catch (IOException e) {
			e.printStackTrace();
		}
		item.setVerticalvarone("Sucursal");
		item.setVerticalvartwo("Categoria");
		item.setYear(2019);
		statisticsHeatMapRepository.save(item);
		log.info(":: StatisticsHeatMap {} ", item);

		item = new StatisticsHeatmap();
		idbranch = new Branch();
		idbranch.setId(1);
		item.setIdbranch(idbranch);
		idproduct = new Product();
		idproduct.setId(2);
		item.setIdproduct(idproduct);
		item.setKeyvar("Ventas");
		try {
			final File image = new File("/Users/juancarlospedrazaalcala/Documents/Images/heatMap.jpeg");
			log.info(":: Length image {} ", image.length());
			item.setImage(Files.readAllBytes(image.toPath()));
		} catch (IOException e) {
			e.printStackTrace();
		}

		item.setVerticalvarone("Sucursal");
		item.setVerticalvartwo("Categoria");
		item.setYear(2019);
		statisticsHeatMapRepository.save(item);
		log.info(":: StatisticsHeatMap {} ", item);
	}

	@Override
	public void processStatisticsCorrelation() {
		StatisticsCorrelation item = new StatisticsCorrelation();
		Branch idbranch = new Branch();
		idbranch.setId(1);
		item.setIdbranch(idbranch);
		Product idproduct = new Product();
		idproduct.setId(2);
		item.setIdproduct(idproduct);
		item.setMonth(2);
		item.setVerticalvarname("Ventas");
		item.setHorizontalvarnameone("Estado");
		item.setHorizontalvarnametwo("Clasificación");
		item.setHorizontalvarnamethree("Provedor");
		item.setHorizontalvarnamefour("Marca");
		item.setYear(2019);
		statisticsCorrelationRepository.save(item);
		log.info(":: StatisticsCorrelation {} ", item);

		item = new StatisticsCorrelation();
		idbranch = new Branch();
		idbranch.setId(1);
		item.setIdbranch(idbranch);
		item.setVerticalvarname("Ventas");
		item.setHorizontalvarnameone("Estado");
		item.setHorizontalvarnametwo("Clasificación");
		item.setHorizontalvarnamethree("Provedor");
		item.setHorizontalvarnamefour("Marca");
		item.setYear(2019);
		statisticsCorrelationRepository.save(item);
		log.info(":: StatisticsCorrelation {} ", item);

		item = new StatisticsCorrelation();
		idbranch = new Branch();
		idbranch.setId(1);
		item.setIdbranch(idbranch);
		idproduct = new Product();
		idproduct.setId(2);
		item.setIdproduct(idproduct);
		item.setVerticalvarname("Ventas");
		item.setHorizontalvarnameone("Estado");
		item.setHorizontalvarnametwo("Clasificación");
		item.setHorizontalvarnamethree("Provedor");
		item.setHorizontalvarnamefour("Marca");
		item.setYear(2019);
		statisticsCorrelationRepository.save(item);
		log.info(":: StatisticsCorrelation {} ", item);
	}

	@Override
	public void processStatisticsCorrelationVariableRelation() {
		StatisticsCorrelationVariableRelation item = new StatisticsCorrelationVariableRelation();
		item.setRelationvalueone("0.0389038");
		item.setRelationvaluetwo("0.39038033");
		item.setRelationvaluethree("0.91039039");
		item.setRelationvaluefour("0.83091839");
		StatisticsCorrelation stat = new StatisticsCorrelation();
		stat.setId(1);
		item.setIdstatisticscorrelation(stat);
		statisticsCorrelationVariableRelationRepository.save(item);
		log.info(":: StatisticsCorrelationVariableRelation {} ", item);

		item = new StatisticsCorrelationVariableRelation();
		item.setRelationvalueone("0.0389038");
		item.setRelationvaluetwo("0.39038033");
		item.setRelationvaluethree("0.91039039");
		item.setRelationvaluefour("0.83091839");
		stat = new StatisticsCorrelation();
		stat.setId(1);
		item.setIdstatisticscorrelation(stat);
		statisticsCorrelationVariableRelationRepository.save(item);
		log.info(":: StatisticsCorrelationVariableRelation {} ", item);

		item = new StatisticsCorrelationVariableRelation();
		item.setRelationvalueone("0.0389038");
		item.setRelationvaluetwo("0.39038033");
		item.setRelationvaluethree("0.91039039");
		item.setRelationvaluefour("0.83091839");
		stat = new StatisticsCorrelation();
		stat.setId(1);
		item.setIdstatisticscorrelation(stat);
		statisticsCorrelationVariableRelationRepository.save(item);
		log.info(":: StatisticsCorrelationVariableRelation {} ", item);

		item = new StatisticsCorrelationVariableRelation();
		item.setRelationvalueone("0.0389038");
		item.setRelationvaluetwo("0.39038033");
		item.setRelationvaluethree("0.91039039");
		item.setRelationvaluefour("0.83091839");
		stat = new StatisticsCorrelation();
		stat.setId(1);
		item.setIdstatisticscorrelation(stat);
		statisticsCorrelationVariableRelationRepository.save(item);
		log.info(":: StatisticsCorrelationVariableRelation {} ", item);

		item = new StatisticsCorrelationVariableRelation();
		item.setRelationvalueone("0.0389038");
		item.setRelationvaluetwo("0.39038033");
		item.setRelationvaluethree("0.91039039");
		item.setRelationvaluefour("0.83091839");
		stat = new StatisticsCorrelation();
		stat.setId(2);
		item.setIdstatisticscorrelation(stat);
		statisticsCorrelationVariableRelationRepository.save(item);
		log.info(":: StatisticsCorrelationVariableRelation {} ", item);

		item = new StatisticsCorrelationVariableRelation();
		item.setRelationvalueone("0.0389038");
		item.setRelationvaluetwo("0.39038033");
		item.setRelationvaluethree("0.91039039");
		item.setRelationvaluefour("0.83091839");
		stat = new StatisticsCorrelation();
		stat.setId(2);
		item.setIdstatisticscorrelation(stat);
		statisticsCorrelationVariableRelationRepository.save(item);
		log.info(":: StatisticsCorrelationVariableRelation {} ", item);

		item = new StatisticsCorrelationVariableRelation();
		item.setRelationvalueone("0.0389038");
		item.setRelationvaluetwo("0.39038033");
		item.setRelationvaluethree("0.91039039");
		item.setRelationvaluefour("0.83091839");
		stat = new StatisticsCorrelation();
		stat.setId(2);
		item.setIdstatisticscorrelation(stat);
		statisticsCorrelationVariableRelationRepository.save(item);
		log.info(":: StatisticsCorrelationVariableRelation {} ", item);

		item = new StatisticsCorrelationVariableRelation();
		item.setRelationvalueone("0.0389038");
		item.setRelationvaluetwo("0.39038033");
		item.setRelationvaluethree("0.91039039");
		item.setRelationvaluefour("0.83091839");
		stat = new StatisticsCorrelation();
		stat.setId(2);
		item.setIdstatisticscorrelation(stat);
		statisticsCorrelationVariableRelationRepository.save(item);
		log.info(":: StatisticsCorrelationVariableRelation {} ", item);

		item = new StatisticsCorrelationVariableRelation();
		item.setRelationvalueone("0.0389038");
		item.setRelationvaluetwo("0.39038033");
		item.setRelationvaluethree("0.91039039");
		item.setRelationvaluefour("0.83091839");
		stat = new StatisticsCorrelation();
		stat.setId(3);
		item.setIdstatisticscorrelation(stat);
		statisticsCorrelationVariableRelationRepository.save(item);
		log.info(":: StatisticsCorrelationVariableRelation {} ", item);

		item = new StatisticsCorrelationVariableRelation();
		item.setRelationvalueone("0.0389038");
		item.setRelationvaluetwo("0.39038033");
		item.setRelationvaluethree("0.91039039");
		item.setRelationvaluefour("0.83091839");
		stat = new StatisticsCorrelation();
		stat.setId(3);
		item.setIdstatisticscorrelation(stat);
		statisticsCorrelationVariableRelationRepository.save(item);
		log.info(":: StatisticsCorrelationVariableRelation {} ", item);

		item = new StatisticsCorrelationVariableRelation();
		item.setRelationvalueone("0.0389038");
		item.setRelationvaluetwo("0.39038033");
		item.setRelationvaluethree("0.91039039");
		item.setRelationvaluefour("0.83091839");
		stat = new StatisticsCorrelation();
		stat.setId(3);
		item.setIdstatisticscorrelation(stat);
		statisticsCorrelationVariableRelationRepository.save(item);
		log.info(":: StatisticsCorrelationVariableRelation {} ", item);

		item = new StatisticsCorrelationVariableRelation();
		item.setRelationvalueone("0.0389038");
		item.setRelationvaluetwo("0.39038033");
		item.setRelationvaluethree("0.91039039");
		item.setRelationvaluefour("0.83091839");
		stat = new StatisticsCorrelation();
		stat.setId(3);
		item.setIdstatisticscorrelation(stat);
		statisticsCorrelationVariableRelationRepository.save(item);
		log.info(":: StatisticsCorrelationVariableRelation {} ", item);
	}

	@Override
	public void processStatisticsAnova() {
		StatisticsAnova item = new StatisticsAnova();
		item.setFtestscore("68.7979798797");
		Branch idbranch = new Branch();
		idbranch.setId(1);
		item.setIdbranch(idbranch);
		Product idproduct = new Product();
		idproduct.setId(2);
		item.setIdproduct(idproduct);
		item.setMonth(2);
		item.setPvalue("3.389839839383983");

		item.setVarsindname("Ventas");
		item.setVardepname("Marca");
		item.setYear(2019);
		statisticsAnovaRepository.save(item);
		log.info(":: StatisticsCorrelation {} ", item);

		item = new StatisticsAnova();
		item.setFtestscore("68.7979798797");
		idbranch = new Branch();
		idbranch.setId(1);
		item.setIdbranch(idbranch);
		idproduct = new Product();
		idproduct.setId(2);
		item.setIdproduct(idproduct);
		item.setMonth(2);
		item.setPvalue("3.389839839383983");

		item.setVardepname("Ventas");
		item.setVarsindname("Sucursal");
		item.setYear(2019);
		statisticsAnovaRepository.save(item);
		log.info(":: StatisticsCorrelation {} ", item);

		item = new StatisticsAnova();
		item.setFtestscore("68.7979798797");
		idbranch = new Branch();
		idbranch.setId(1);
		item.setIdbranch(idbranch);
		idproduct = new Product();
		idproduct.setId(2);
		item.setIdproduct(idproduct);
		item.setMonth(2);
		item.setPvalue("3.389839839383983");
		item.setVardepname("Ventas");
		item.setVarsindname("Esatdo");
		item.setYear(2019);
		statisticsAnovaRepository.save(item);
		log.info(":: StatisticsCorrelation {} ", item);

		item = new StatisticsAnova();
		item.setFtestscore("68.7979798797");
		idbranch = new Branch();
		idbranch.setId(1);
		item.setIdbranch(idbranch);
		idproduct = new Product();
		idproduct.setId(2);
		item.setIdproduct(idproduct);
		item.setMonth(2);
		item.setPvalue("3.389839839383983");

		item.setVardepname("Ventas");
		item.setVarsindname("Sucursal");
		item.setYear(2019);
		statisticsAnovaRepository.save(item);
		log.info(":: StatisticsCorrelation {} ", item);

		item = new StatisticsAnova();
		item.setFtestscore("68.7979798797");
		idbranch = new Branch();
		idbranch.setId(1);
		item.setIdbranch(idbranch);
		item.setPvalue("3.389839839383983");

		item.setVardepname("Ventas");
		item.setVarsindname("Marca");
		item.setYear(2019);
		statisticsAnovaRepository.save(item);
		log.info(":: StatisticsCorrelation {} ", item);

		item = new StatisticsAnova();
		item.setFtestscore("68.7979798797");
		idbranch = new Branch();
		idbranch.setId(1);
		item.setIdbranch(idbranch);
		item.setPvalue("3.389839839383983");

		item.setVardepname("Ventas");
		item.setVarsindname("Sucursal");
		item.setYear(2019);
		statisticsAnovaRepository.save(item);
		log.info(":: StatisticsCorrelation {} ", item);

		item = new StatisticsAnova();
		item.setFtestscore("68.7979798797");
		idbranch = new Branch();
		idbranch.setId(1);
		item.setIdbranch(idbranch);
		item.setPvalue("3.389839839383983");

		item.setVardepname("Ventas");
		item.setVarsindname("Esatdo");
		item.setYear(2019);
		statisticsAnovaRepository.save(item);
		log.info(":: StatisticsCorrelation {} ", item);

		item = new StatisticsAnova();
		item.setFtestscore("68.7979798797");
		idbranch = new Branch();
		idbranch.setId(1);
		item.setIdbranch(idbranch);
		item.setPvalue("3.389839839383983");

		item.setVardepname("Ventas");
		item.setVarsindname("Sucursal");
		item.setYear(2019);
		statisticsAnovaRepository.save(item);
		log.info(":: StatisticsCorrelation {} ", item);

		item = new StatisticsAnova();
		item.setFtestscore("68.7979798797");
		idbranch = new Branch();
		idbranch.setId(1);
		item.setIdbranch(idbranch);
		idproduct = new Product();
		idproduct.setId(2);
		item.setIdproduct(idproduct);
		item.setPvalue("3.389839839383983");

		item.setVardepname("Ventas");
		item.setVarsindname("Marca");
		item.setYear(2019);
		statisticsAnovaRepository.save(item);
		log.info(":: StatisticsCorrelation {} ", item);

		item = new StatisticsAnova();
		item.setFtestscore("68.7979798797");
		idbranch = new Branch();
		idbranch.setId(1);
		item.setIdbranch(idbranch);
		idproduct = new Product();
		idproduct.setId(2);
		item.setIdproduct(idproduct);
		item.setPvalue("3.389839839383983");

		item.setVardepname("Ventas");
		item.setVarsindname("Sucursal");
		item.setYear(2019);
		statisticsAnovaRepository.save(item);
		log.info(":: StatisticsCorrelation {} ", item);

		item = new StatisticsAnova();
		item.setFtestscore("68.7979798797");
		idbranch = new Branch();
		idbranch.setId(1);
		item.setIdbranch(idbranch);
		idproduct = new Product();
		idproduct.setId(2);
		item.setIdproduct(idproduct);
		item.setPvalue("3.389839839383983");

		item.setVardepname("Ventas");
		item.setVarsindname("Esatdo");
		item.setYear(2019);
		statisticsAnovaRepository.save(item);
		log.info(":: StatisticsCorrelation {} ", item);

		item = new StatisticsAnova();
		item.setFtestscore("68.7979798797");
		idbranch = new Branch();
		idbranch.setId(1);
		item.setIdbranch(idbranch);
		idproduct = new Product();
		idproduct.setId(2);
		item.setIdproduct(idproduct);
		item.setPvalue("3.389839839383983");

		item.setVardepname("Ventas");
		item.setVarsindname("Sucursal");
		item.setYear(2019);
		statisticsAnovaRepository.save(item);
		log.info(":: StatisticsCorrelation {} ", item);

	}

	@Override
	public void processTwitter() {
		final Twitter item = new Twitter();
		try {
			processImage("/Users/juancarlospedrazaalcala/Documents/Images/logo_CalpulliX.png",
					"/Users/juancarlospedrazaalcala/Documents/Images/logo_CalpulliX_Resize.png", PNG_EXTENSION);
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			File image = new File("/Users/juancarlospedrazaalcala/Documents/Images/cloud_word.png");
			log.info(":: Length image {} ", image.length());
			item.setClowwords(Files.readAllBytes(image.toPath()));
			item.setDate(formatTimestamp.format(Calendar.getInstance().getTime()));
			image = new File("/Users/juancarlospedrazaalcala/Documents/Images/image_perfil.jpeg");
			item.setProfilepicture(Files.readAllBytes(image.toPath()));
		} catch (IOException e) {
			e.printStackTrace();
		}
		item.setKeywordfive("Oferta");
		item.setKeywordfour("Promoción");
		item.setKeywordone("Calidad");
		item.setKeywordthree("Servicio");
		item.setKeywordtwo("Rapidez");
		item.setIsactive(Boolean.TRUE);
		twitterRepository.save(item);
		log.info(":: Twitter {} ", item);
	}

	public void processTwitterImage() {
		final List<Twitter> twitter = twitterRepository.findAll();
		for (final Twitter item : twitter) {
			try {
				File image = new File("/Users/juancarlospedrazaalcala/Documents/Images/cloud_word.png");
				log.info(":: Length image {} ", image.length());
				item.setClowwords(Files.readAllBytes(image.toPath()));
				image = new File("/Users/juancarlospedrazaalcala/Documents/Images/image_perfil.jpeg");
				item.setProfilepicture(Files.readAllBytes(image.toPath()));
				twitterRepository.save(item);
				log.info(":: Twitter {} ", item);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void processTwitterMessages() {
		final Twitter idtwitter = new Twitter();
		idtwitter.setId(1);
		TwitterMessages item = new TwitterMessages();
		item.setAtuser("@David_Garcia");
		item.setDate(formatDate.format(Calendar.getInstance().getTime()));
		item.setMessage("El servicio es muy rapido y eficiente.");
		item.setTypemessage(TwitterTypeMessage.POSITIVE);
		item.setUser("David_García");
		item.setIdtwitter(idtwitter);
		twitterMessagesRepository.save(item);

		item = new TwitterMessages();
		item.setAtuser("@Antonio_Alonso");
		item.setDate(formatDate.format(Calendar.getInstance().getTime()));
		item.setMessage("Calidad en la atención.");
		item.setTypemessage(TwitterTypeMessage.POSITIVE);
		item.setUser("Antonio_Alonso");
		item.setIdtwitter(idtwitter);
		twitterMessagesRepository.save(item);

		item = new TwitterMessages();
		item.setAtuser("@Felipe_Leon");
		item.setDate(formatDate.format(Calendar.getInstance().getTime()));
		item.setMessage("Las sucursales estan muy accesibles.");
		item.setTypemessage(TwitterTypeMessage.POSITIVE);
		item.setUser("Felipe_Leon");
		item.setIdtwitter(idtwitter);
		twitterMessagesRepository.save(item);

		item = new TwitterMessages();
		item.setAtuser("@Marco_Mendez");
		item.setDate(formatDate.format(Calendar.getInstance().getTime()));
		item.setMessage("Los productos son muy recomendables.");
		item.setTypemessage(TwitterTypeMessage.POSITIVE);
		item.setUser("Marco_Mendez");
		item.setIdtwitter(idtwitter);
		twitterMessagesRepository.save(item);

		item = new TwitterMessages();
		item.setAtuser("@Maria_Lorca");
		item.setDate(formatDate.format(Calendar.getInstance().getTime()));
		item.setMessage("El personal es muy atento.");
		item.setTypemessage(TwitterTypeMessage.POSITIVE);
		item.setUser("Maria_Lorca");
		item.setIdtwitter(idtwitter);
		twitterMessagesRepository.save(item);

		item = new TwitterMessages();
		item.setAtuser("@Veronica_Lara");
		item.setDate(formatDate.format(Calendar.getInstance().getTime()));
		item.setMessage("Lentitud en la entrega.");
		item.setTypemessage(TwitterTypeMessage.NEGATIVE);
		item.setUser("Veronica_Lara");
		item.setIdtwitter(idtwitter);
		twitterMessagesRepository.save(item);

		item = new TwitterMessages();
		item.setAtuser("@Jose_Ruiz");
		item.setDate(formatDate.format(Calendar.getInstance().getTime()));
		item.setMessage("Fallas en la adminsitración.");
		item.setTypemessage(TwitterTypeMessage.NEGATIVE);
		item.setUser("Maria_Lorca");
		item.setIdtwitter(idtwitter);
		twitterMessagesRepository.save(item);

		item = new TwitterMessages();
		item.setAtuser("@Miguel_Lopez");
		item.setDate(formatDate.format(Calendar.getInstance().getTime()));
		item.setMessage("Falta de sucursales.");
		item.setTypemessage(TwitterTypeMessage.NEGATIVE);
		item.setUser("Miguel_Lopez");
		item.setIdtwitter(idtwitter);
		twitterMessagesRepository.save(item);

		item = new TwitterMessages();
		item.setAtuser("@Raul_Sanchez");
		item.setDate(formatDate.format(Calendar.getInstance().getTime()));
		item.setMessage("No llegan los pedidos.");
		item.setTypemessage(TwitterTypeMessage.NEGATIVE);
		item.setUser("Raul_Sanchez");
		item.setIdtwitter(idtwitter);
		twitterMessagesRepository.save(item);

		item = new TwitterMessages();
		item.setAtuser("@Tania_Morelos");
		item.setDate(formatDate.format(Calendar.getInstance().getTime()));
		item.setMessage("Muy mal servicio.");
		item.setTypemessage(TwitterTypeMessage.NEGATIVE);
		item.setUser("Tania_Morelos");
		item.setIdtwitter(idtwitter);
		twitterMessagesRepository.save(item);

		item = new TwitterMessages();
		item.setAtuser("@Dorantes_Laura");
		item.setDate(formatDate.format(Calendar.getInstance().getTime()));
		item.setMessage("Compre un desodorante con ustedes.");
		item.setTypemessage(TwitterTypeMessage.NEUTRAL);
		item.setUser("Dorantes_Laura");
		item.setIdtwitter(idtwitter);
		twitterMessagesRepository.save(item);

		item = new TwitterMessages();
		item.setAtuser("@Aleida_Jimenez");
		item.setDate(formatDate.format(Calendar.getInstance().getTime()));
		item.setMessage("Quiero saber la dirección de las sucursales.");
		item.setTypemessage(TwitterTypeMessage.NEUTRAL);
		item.setUser("Aleida_Jimenez");
		item.setIdtwitter(idtwitter);
		twitterMessagesRepository.save(item);

		item = new TwitterMessages();
		item.setAtuser("@Jaz_Milos");
		item.setDate(formatDate.format(Calendar.getInstance().getTime()));
		item.setMessage("En donde los puedo encontrar.");
		item.setTypemessage(TwitterTypeMessage.NEUTRAL);
		item.setUser("Jaz_Milos");
		item.setIdtwitter(idtwitter);
		twitterMessagesRepository.save(item);

		item = new TwitterMessages();
		item.setAtuser("@Fabi_Ortiz");
		item.setDate(formatDate.format(Calendar.getInstance().getTime()));
		item.setMessage("Son la misma que esta en Atenas?");
		item.setTypemessage(TwitterTypeMessage.NEUTRAL);
		item.setUser("Fabi_Ortiz");
		item.setIdtwitter(idtwitter);
		twitterMessagesRepository.save(item);

		item = new TwitterMessages();
		item.setAtuser("@Pepe_Alcantar");
		item.setDate(formatDate.format(Calendar.getInstance().getTime()));
		item.setMessage("Cual es su web?");
		item.setTypemessage(TwitterTypeMessage.NEUTRAL);
		item.setUser("Pepe_Alcantar");
		item.setIdtwitter(idtwitter);
		twitterMessagesRepository.save(item);

		log.info(":: TwitterMessages {} ", item);
	}

	@Override
	public void processRegression() {
//		// Informacion de dos años
//		ForecastSales item = new ForecastSales();
//		item.setSaleforecast("12987097");
//		item.setVardep("Ventas");
//		item.setVarindone("Productos");
//		item.setVarindonecoef("0.90809809");
//		item.setVarindtwo("Clientes");
//		item.setVarindtwocoef("0.8908457");
//		item.setVarindthree("Estado");
//		item.setVarindthreecoef("0.75430930");
//		item.setVarindfour("Clasificación");
//		item.setVarindfourcoef("0.86536439");
//		try {
//			File image = new File("/Users/juancarlospedrazaalcala/Documents/Images/scatter_plot_1.png");
//			item.setImagevarone(Files.readAllBytes(image.toPath()));
//
//			image = new File("/Users/juancarlospedrazaalcala/Documents/Images/scatter_plot_2.png");
//			item.setImagevartwo(Files.readAllBytes(image.toPath()));
//
//			image = new File("/Users/juancarlospedrazaalcala/Documents/Images/scatter_plot_1.png");
//			item.setImagevarthree(Files.readAllBytes(image.toPath()));
//
//			image = new File("/Users/juancarlospedrazaalcala/Documents/Images/scatter_plot_2.png");
//			item.setImagevarfour(Files.readAllBytes(image.toPath()));
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		Branch idbranch = new Branch();
//		idbranch.setId(1);
//		item.setIdbranch(idbranch);
//		item.setYear(2021);
//		regressionRepository.save(item);
//		log.info(":: Regression {} ", item);
//
//		item = new ForecastSales();
//		item.setSaleforecast("12987097");
//		item.setVardep("Ventas");
//		item.setVarindone("Productos");
//		item.setVarindonecoef("0.788289909");
//		item.setVarindtwo("Clientes");
//		item.setVarindtwocoef("0.89900220");
//		item.setVarindthree("Estado");
//		item.setVarindthreecoef("0.9298277237");
//		item.setVarindfour("Clasificación");
//		item.setVarindfourcoef("0.828292299");
//		try {
//			File image = new File("/Users/juancarlospedrazaalcala/Documents/Images/scatter_plot_1.png");
//			item.setImagevarone(Files.readAllBytes(image.toPath()));
//
//			image = new File("/Users/juancarlospedrazaalcala/Documents/Images/scatter_plot_2.png");
//			item.setImagevartwo(Files.readAllBytes(image.toPath()));
//
//			image = new File("/Users/juancarlospedrazaalcala/Documents/Images/scatter_plot_1.png");
//			item.setImagevarthree(Files.readAllBytes(image.toPath()));
//
//			image = new File("/Users/juancarlospedrazaalcala/Documents/Images/scatter_plot_2.png");
//			item.setImagevarfour(Files.readAllBytes(image.toPath()));
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		idbranch = new Branch();
//		idbranch.setId(1);
//		item.setIdbranch(idbranch);
//		item.setYear(2021);
//		regressionRepository.save(item);
//		log.info(":: Regression {} ", item);
//
//		// Informacion de 12 meses
//		for (int index = 1; index <= 12; index++) {
//			item = new ForecastSales();
//			item.setSaleforecast("12987097");
//			item.setVardep("Ventas");
//			item.setVarindone("Productos");
//			item.setVarindonecoef("0.788289909");
//			item.setVarindtwo("Clientes");
//			item.setVarindtwocoef("0.89900220");
//			item.setVarindthree("Estado");
//			item.setVarindthreecoef("0.9298277237");
//			item.setVarindfour("Clasificación");
//			item.setVarindfourcoef("0.828292299");
//			try {
//				File image = new File("/Users/juancarlospedrazaalcala/Documents/Images/scatter_plot_1.png");
//				item.setImagevarone(Files.readAllBytes(image.toPath()));
//
//				image = new File("/Users/juancarlospedrazaalcala/Documents/Images/scatter_plot_2.png");
//				item.setImagevartwo(Files.readAllBytes(image.toPath()));
//
//				image = new File("/Users/juancarlospedrazaalcala/Documents/Images/scatter_plot_1.png");
//				item.setImagevarthree(Files.readAllBytes(image.toPath()));
//
//				image = new File("/Users/juancarlospedrazaalcala/Documents/Images/scatter_plot_2.png");
//				item.setImagevarfour(Files.readAllBytes(image.toPath()));
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//			idbranch = new Branch();
//			idbranch.setId(1);
//			item.setIdbranch(idbranch);
//			item.setYear(2020);
//			item.setMonth(index);
//			regressionRepository.save(item);
//			log.info(":: Regression Month {} ", item);
//		}

	}

	@Override
	public void processHeatMapImage() {
		StatisticsHeatmap heatMap = statisticsHeatMapRepository.findById(1).get();
		File image = new File(
				"/Users/juancarlospedrazaalcala/Documents/TT/CSV_Jupyetr/heatmap_branch_2_year_2019|Provider Name Product|Brand Name Product|Purchase-Price-Product.png");
		try {
			heatMap.setImage(Files.readAllBytes(image.toPath()));
			statisticsHeatMapRepository.save(heatMap);
		} catch (IOException e) {
			log.error(":: Heat-Map image error ", e);
		}
	}

	@Override
	public void processStatisticsBloxPlot() {
		StatisticsBloxPlot boxPlot = statisticsBloxPlotRepository.findById(1).get();
		File image = new File(
				"/Users/juancarlospedrazaalcala/Documents/TT/BACK_UP/boxplot_branch_2_year_2019|Product Category Name|Sale-Price-Product|.png");
		try {
			boxPlot.setImage(Files.readAllBytes(image.toPath()));
			statisticsBloxPlotRepository.save(boxPlot);

			boxPlot = statisticsBloxPlotRepository.findById(2).get();
			image = new File(
					"/Users/juancarlospedrazaalcala/Documents/TT/BACK_UP/boxplot_two_branch_2_year_2019|Weight|Sale-Price-Product| 13.46.52.png");
			boxPlot.setImage(Files.readAllBytes(image.toPath()));
			statisticsBloxPlotRepository.save(boxPlot);
		} catch (IOException e) {
			log.error(":: Box plot image error ", e);
		}
	}

	@Override
	public void processRegressionImage() {
//		final List<ForecastSales> regression = regressionRepository.findAll();
//		for (final ForecastSales item : regression) {
//			try {
//				File image = new File("/Users/juancarlospedrazaalcala/Documents/Images/scatter_plot_1.png");
//				item.setImagevarone(Files.readAllBytes(image.toPath()));
//
//				image = new File("/Users/juancarlospedrazaalcala/Documents/Images/scatter_plot_2.png");
//				item.setImagevartwo(Files.readAllBytes(image.toPath()));
//
//				image = new File("/Users/juancarlospedrazaalcala/Documents/Images/scatter_plot_1.png");
//				item.setImagevarthree(Files.readAllBytes(image.toPath()));
//
//				image = new File("/Users/juancarlospedrazaalcala/Documents/Images/scatter_plot_2.png");
//				item.setImagevarfour(Files.readAllBytes(image.toPath()));
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//			regressionRepository.save(item);
//			log.info(":: Regression {} ", item);
//		}
	}

	@Override
	public void processCatalogs() {

		for (final EmployeePosition employeePosition : EmployeePosition.values()) {
			executeInsertCatalogs("Employee_Position", employeePosition.getDescription(), "description");
		}
		for (final ContactType contactType : ContactType.values()) {
			executeInsertCatalogs("Contact_Type", contactType.getDescription(), "description");
		}
		for (final State state : State.values()) {
			executeInsertCatalogs("State", state.getDescription(), "name");
		}
		for (final BranchRegion branchRegion : BranchRegion.values()) {
			executeInsertCatalogs("Branch_Region", branchRegion.getDescription(), "description");
		}
		for (final Services services : Services.values()) {
			executeInsertCatalogs("Services", services.getDescription(), "name");
		}
		for (final ProductStatus item : ProductStatus.values()) {
			executeInsertCatalogs("Product_Status", item.getDescription(), "description");
		}
		for (final Brand item : Brand.values()) {
			executeInsertCatalogs("Brand", item.getDescription(), "name");
		}
		for (final WeightUnit item : WeightUnit.values()) {
			executeInsertCatalogs("Weight_Units", item.getDescription(), "description");
		}
		for (final ProductClassification item : ProductClassification.values()) {
			executeInsertCatalogs("Product_Classification", item.getDescription(), "name");
		}
		for (final ProductCategories item : ProductCategories.values()) {
			executeInsertCatalogs("Product_Categories", item.getDescription(), "name");
		}
		for (final ProductLocation item : ProductLocation.values()) {
			executeInsertCatalogs("Product_Location", item.getDescription(), "description");
		}
		for (final ProductBranchStatus item : ProductBranchStatus.values()) {
			executeInsertCatalogs("Product_Branch_Status", item.getDescription(), "description");
		}
		for (final PurchaseOrderStatus item : PurchaseOrderStatus.values()) {
			executeInsertCatalogs("PurchaseOrder_Status", item.getDescription(), "description");
		}
		for (final PromotionsStatus item : PromotionsStatus.values()) {
			executeInsertCatalogs("Promotions_Status", item.getDescription(), "description");
		}
		for (final EducationLevel item : EducationLevel.values()) {
			executeInsertCatalogs("Education_Level", item.getDescription(), "name");
		}
		for (final MaritalStatus item : MaritalStatus.values()) {
			executeInsertCatalogs("Marital_Status", item.getDescription(), "description");
		}
		for (final TwitterTypeMessage item : TwitterTypeMessage.values()) {
			executeInsertCatalogs("Twitter_Type_Message", item.getDescription(), "description");
		}
	}

	private void executeInsertCatalogs(String tableName, String valueColumn, String nameColumn) {
		final SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(dataSource).withTableName(tableName);
		final Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put(nameColumn, valueColumn);

		int result = simpleJdbcInsert.execute(parameters);
		log.info(":: INSERT RESULT CATALOG {} ", result);
	}

//	private void executeInsertCustomerCatalogs(String tableName, String valueColumn, String nameColumn) {
//		final SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(dataSource).withTableName(tableName);
//		final Map<String, Object> parameters = new HashMap<String, Object>();
//		parameters.put(nameColumn, valueColumn);
//		parameters.put("description", valueColumn);
//		int result = simpleJdbcInsert.execute(parameters);
//		log.info(":: INSERT RESULT CATALOG {} ", result);
//	}

	@Override
	public void processBranchServices() {
		List<Branch> branches = branchRepository.findAll();
		BranchServices item;
		BigDecimal amount = new BigDecimal(BASE_AMOUNT_BRANCH_SERVICE);
		int index = 0;
		for (final Branch branch : branches) {
			for (final Services service : Services.values()) {
				item = new BranchServices();
				item.setName(service.getDescription());
				item.setIdbranch(branch);
				item.setIdservice(service);
				item.setMonthlyamount(
						amount.multiply(new BigDecimal(ThreadLocalRandom.current().nextInt(1 * index, 15))));
				branchServicesRepository.save(item);
				log.info(":: BranchServices {} ", item);
			}
			index++;
		}
	}

	@Override
	public void processBranchDeduction() {
		List<Branch> branches = branchRepository.findAll();
		BranchDeduction item;
		int year = 2015;
		int currentYear = Calendar.getInstance().get(Calendar.YEAR);
		Calendar calendar = Calendar.getInstance();
		final Map<String, BigDecimal> mapDeductions = new HashMap<>();
		mapDeductions.put("Gastos médicos", new BigDecimal(BASE_AMOUNT_MEDICAL_DEDUCTION));
		mapDeductions.put("Donativos", new BigDecimal(BASE_AMOUNT_CONTRIBUTION_DEDUCTION));
		mapDeductions.put("Equipo de computo", new BigDecimal(BASE_AMOUNT_COMPUTER_EQUIPMENT_DEDUCTION));
		mapDeductions.put("Hipoteca", new BigDecimal(BASE_AMOUNT_MORGAGE_DEDUCTION));
		int index = 0;
		log.debug(":: Branches {} ", branches.size());
		for (final Branch branch : branches) {
			for (int k = year; k <= currentYear; k++) {
				for (int m = 0; m < 12; m++) {
					for (final String key : mapDeductions.keySet()) {
						item = new BranchDeduction();
						item.setAmount(mapDeductions.get(key)
								.multiply(new BigDecimal(ThreadLocalRandom.current().nextInt(1, 3))));
						item.setDescription(key);
						item.setIdbranch(branch);
						calendar.set(Calendar.YEAR, k);
						calendar.set(Calendar.MONTH, m);
						calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DATE));
						item.setCreatedate(formatDate.format(calendar.getTime()));
						branchDeductionRepository.save(item);
						index++;
					}
				}
			}
		}
		log.info(":: Index {} ", index);
	}

	@Override
	public void processCustomerSales() {
		final List<ProductBranch> products = productBranchRepository.findAll();
		final List<Customers> customers = customerRepository.findAll();
		for (final ProductBranch product : products) {
			product.setIdcustomer(customers.get(ThreadLocalRandom.current().nextInt(0, customers.size())));
			productBranchRepository.save(product);
		}
		log.info(":: Customer-Sales updated ");
	}

	@Override
	public void processPromotionsSales() {
		final List<Promotions> promotions = promotionsRepository.findAll();
		final List<ProductBranch> productBranch = productBranchRepository.findAll();
		ProductBranch product;
		int stop = (int) (productBranch.size() * APPROXIMATE_PROMOTIONS_PERCENTAGE);
		int count = 0;
		for (int index = 0; index < productBranch.size(); index++) {
			product = productBranch.get(ThreadLocalRandom.current().nextInt(0, productBranch.size()));
			product.setIdpromotion(getPromotion(promotions, product.getSaledate()));
			if (++count == stop) {
				break;
			}
			productBranchRepository.save(product);
		}
		log.info(":: End processPromotionsSales {} ", count);
	}

	@Override
	public void renamePromotions() {
		final List<Promotions> promotions = promotionsRepository.findAll();
		for (final Promotions promotion : promotions) {
			promotion.setName("Promoción " + promotion.getIdproduct().getName() + " "
					+ ThreadLocalRandom.current().nextInt(0, 201));
//			promotion.setName(promotion.getName() + "" + ThreadLocalRandom.current().nextInt(0, 201));
//			if (promotion.getName().length() > 30) {
//				promotion.setName(promotion.getName().substring(0, 30));
//			}
			promotionsRepository.save(promotion);
		}

	}

	private Promotions getPromotion(final List<Promotions> promotions, final String saleDate) {
		Calendar dateSale = Calendar.getInstance();
		try {
			dateSale.setTime(formatDate.parse(saleDate));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Promotions result = null;
		Calendar initProm = Calendar.getInstance();
		Calendar endProm = Calendar.getInstance();
		for (final Promotions promotion : promotions) {
			try {
				initProm.setTime(formatDate.parse(promotion.getCreationdate()));
				endProm.setTime(formatDate.parse(promotion.getEnddate()));
			} catch (ParseException e) {
				e.printStackTrace();
			}
			if ((dateSale.getTime().compareTo(initProm.getTime()) == 0
					|| dateSale.getTime().compareTo(initProm.getTime()) > 0)
					&& (dateSale.getTime().compareTo(endProm.getTime()) == 0
							|| dateSale.getTime().compareTo(endProm.getTime()) < 0)) {

				result = promotion;
				break;
			}
		}
		return result;
	}

	public void processBloxPlot() {
		StatisticsBloxPlot boxPlot = new StatisticsBloxPlot();
		Branch idbranch = new Branch();
		idbranch.setId(1);
		boxPlot.setIdbranch(idbranch);
		Product idProduct = new Product();
		idProduct.setId(2);
		boxPlot.setIdproduct(idProduct);
		boxPlot.setYear(2019);
		boxPlot.setMonth(2);
		boxPlot.setVardepname("Ventas");
		boxPlot.setVarindname("Estado");
		try {
			File image = new File("/Users/juancarlospedrazaalcala/Documents/Images/blox_plot_two.jpeg");
			boxPlot.setImage(Files.readAllBytes(image.toPath()));
		} catch (IOException e) {
			e.printStackTrace();
		}
		statisticsBloxPlotRepository.save(boxPlot);
		log.info(":: StatisticsBloxPlot {} ", boxPlot);

		boxPlot = new StatisticsBloxPlot();
		idbranch = new Branch();
		idbranch.setId(1);
		boxPlot.setIdbranch(idbranch);
		boxPlot.setYear(2019);
		boxPlot.setVardepname("Ventas");
		boxPlot.setVarindname("Estado");
		try {
			File image = new File("/Users/juancarlospedrazaalcala/Documents/Images/blox_plot_two.jpeg");
			boxPlot.setImage(Files.readAllBytes(image.toPath()));
		} catch (IOException e) {
			e.printStackTrace();
		}
		statisticsBloxPlotRepository.save(boxPlot);
		log.info(":: StatisticsBloxPlot {} ", boxPlot);

		boxPlot = new StatisticsBloxPlot();
		idbranch = new Branch();
		idbranch.setId(1);
		boxPlot.setIdbranch(idbranch);
		idProduct = new Product();
		idProduct.setId(2);
		boxPlot.setIdproduct(idProduct);
		boxPlot.setYear(2019);
		boxPlot.setVardepname("Ventas");
		boxPlot.setVarindname("Estado");
		try {
			File image = new File("/Users/juancarlospedrazaalcala/Documents/Images/blox_plot_two.jpeg");
			boxPlot.setImage(Files.readAllBytes(image.toPath()));
		} catch (IOException e) {
			e.printStackTrace();
		}
		statisticsBloxPlotRepository.save(boxPlot);
		log.info(":: StatisticsBloxPlot {} ", boxPlot);

		boxPlot = new StatisticsBloxPlot();
		idbranch = new Branch();
		idbranch.setId(1);
		boxPlot.setIdbranch(idbranch);
		idProduct = new Product();
		idProduct.setId(2);
		boxPlot.setIdproduct(idProduct);
		boxPlot.setYear(2019);
		boxPlot.setMonth(2);
		boxPlot.setVardepname("Ventas");
		boxPlot.setVarindname("Sucursal");
		try {
			File image = new File("/Users/juancarlospedrazaalcala/Documents/Images/blox_plot_two.jpeg");
			boxPlot.setImage(Files.readAllBytes(image.toPath()));
		} catch (IOException e) {
			e.printStackTrace();
		}
		statisticsBloxPlotRepository.save(boxPlot);
		log.info(":: StatisticsBloxPlot {} ", boxPlot);

		boxPlot = new StatisticsBloxPlot();
		idbranch = new Branch();
		idbranch.setId(1);
		boxPlot.setIdbranch(idbranch);
		boxPlot.setYear(2019);
		boxPlot.setVardepname("Ventas");
		boxPlot.setVarindname("Sucursal");
		try {
			File image = new File("/Users/juancarlospedrazaalcala/Documents/Images/blox_plot_two.jpeg");
			boxPlot.setImage(Files.readAllBytes(image.toPath()));
		} catch (IOException e) {
			e.printStackTrace();
		}
		statisticsBloxPlotRepository.save(boxPlot);
		log.info(":: StatisticsBloxPlot {} ", boxPlot);

		boxPlot = new StatisticsBloxPlot();
		idbranch = new Branch();
		idbranch.setId(1);
		boxPlot.setIdbranch(idbranch);
		idProduct = new Product();
		idProduct.setId(2);
		boxPlot.setIdproduct(idProduct);
		boxPlot.setYear(2019);
		boxPlot.setVardepname("Ventas");
		boxPlot.setVarindname("Sucursal");
		try {
			File image = new File("/Users/juancarlospedrazaalcala/Documents/Images/blox_plot_two.jpeg");
			boxPlot.setImage(Files.readAllBytes(image.toPath()));
		} catch (IOException e) {
			e.printStackTrace();
		}
		statisticsBloxPlotRepository.save(boxPlot);
		log.info(":: StatisticsBloxPlot {} ", boxPlot);
	}

}
