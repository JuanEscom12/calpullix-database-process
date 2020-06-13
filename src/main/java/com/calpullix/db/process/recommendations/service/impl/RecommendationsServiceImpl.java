package com.calpullix.db.process.recommendations.service.impl;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.StringTokenizer;
import java.util.stream.Stream;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.calpullix.db.process.customer.model.CustomerProfile;
import com.calpullix.db.process.customer.model.Customers;
import com.calpullix.db.process.customer.repository.CustomerProfileRepository;
import com.calpullix.db.process.customer.repository.CustomerRepository;
import com.calpullix.db.process.product.model.Product;
import com.calpullix.db.process.product.model.ProductBranch;
import com.calpullix.db.process.product.repository.ProductBranchRepository;
import com.calpullix.db.process.product.repository.ProductRepository;
import com.calpullix.db.process.profile.model.ProductRecomendationProfile;
import com.calpullix.db.process.profile.model.ProfilePromotions;
import com.calpullix.db.process.profile.repository.ProductRecomendationProfileRepository;
import com.calpullix.db.process.profile.repository.ProfilePromotionsRepository;
import com.calpullix.db.process.promotions.model.Promotions;
import com.calpullix.db.process.promotions.repository.PromotionsRepository;
import com.calpullix.db.process.recommendations.service.RecommendationsService;
import com.calpullix.db.process.service.impl.ProductPreferencesProfileRowMapper;
import com.calpullix.db.process.service.impl.ProductPromotionsPreferencesProfile;
import com.calpullix.db.process.service.impl.PromotionPreferencesProfileRowMapper;
import com.calpullix.db.process.util.service.UtilService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class RecommendationsServiceImpl implements RecommendationsService {

	private static final String BASIS_PATH_CUSTOMER_PRODUCT_PREFERENCES = "/Users/juancarlospedrazaalcala/Documents/TT/CSV_Content_Based_Recommendation_INPUT/";

	private static final String FILE_NAME_CUSTOMER_PRODUCT_PREFERENCES = "customer_product_preferences_profile_";

	private static final String FILE_NAME_CUSTOMER_PROMOTION_PREFERENCES = "customer_promotion_preferences_profile_";

	private static final String QUERY_PROFILE_PRODUCTS = "SELECT P.id as idProduct, COUNT(*) as quantityPurchases FROM Product_Branch as PB, Product_History as PH, Product as P, Customers as C WHERE PB.idCustomer = C.id AND C.id = ? AND PB.idProductHistory = PH.id AND PH.idProduct = P.id GROUP BY P.id ORDER BY COUNT(*) DESC LIMIT 5;";

	private static final String QUERY_PROFILE_PROMOTIONS = "SELECT P.id as idPromotion, COUNT(*) as quantityPurchases FROM Product_Branch as PB, Promotions as P, Customers as C WHERE PB.idCustomer = C.id AND C.id = ? AND PB.idPromotion = P.id GROUP BY P.id ORDER BY COUNT(*) DESC LIMIT 5;";
	
	private static final String PATH_COLLABORATIVE_FILTERING_INPUT = "/Users/juancarlospedrazaalcala/Documents/TT/Collaborative_Filtering_INPUT";

	private static final String PROMOTIONS_RATING_COLLABORATIVE_FILTERING_FILE = "promotions_rating_collaborative_filtering.csv";

	private static final String PROMOTION_LIST_COLLABORATIVE_FILTERING_FILE = "promotion_list_collaborative_filtering.csv";

	private static final String PIPE_SEPARATOR = "|";
	
	private static final String SEPARATOR_PATH = "/";

	private static final String IGNORE_FILE = ".DS_Store";

	private static final String CSV_EXTENSION = ".csv";

	private final static int PAGINATION_SIZE_CUSTOMER_PRODUCT_CSV = 1000;

	private static final int QUANTITY_RECOMMENDATIONS = 10;

	private static final int QUANTITY_PROMOTION_RECOMMENDATIONS = 15;

	@Autowired
	private ProductBranchRepository productBranchRepository;

	@Autowired
	private ProductRecomendationProfileRepository productRecomendationProfileRepository;

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private ProfilePromotionsRepository profilePromotionsRepository;
	
	@Autowired
	private CustomerRepository customerRepository;

	@Autowired
	private CustomerProfileRepository customerProfileRepository;

	@Autowired
	private PromotionsRepository promotionsRepository;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private UtilService utilService; 
	
	private SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	@Override
	public void createProductListCSVFile() {
		final String pathCsv = "/Users/juancarlospedrazaalcala/Documents/TT/CSV_Content_Based_Recomendation/product_content_based_2020_04_14.csv";
		
		final String[] headers = {"ProductId", "Name", "Category"}; 
		
		try {
			createProductListCSVFile(headers, pathCsv);
		} catch (IOException e) {
			log.error(":: Error creating product list csv ", e);
		}
	}

	@Override
	public void createProductListCSVFile(String[] headers, String path) throws IOException {
		log.info(":: Writing product list file {} ", path);
		final List<Product> products = productRepository.findAll();
		FileWriter out = new FileWriter(path);
		try (CSVPrinter printer = new CSVPrinter(out, CSVFormat.DEFAULT.withHeader(headers))) {
			products.forEach(item -> {
				try {
					printer.printRecord(item.getId(), item.getName(), item.getCategory().getDescription());
				} catch (IOException e) {
					log.error(":: Error al escribir el archivo " + path, e);
				}
			});
		}
		log.info(":: Ends up writing product list file {} ", path);
	}

	@Override
	public void createCustomerProductCSVFile() {
		final String pathCsv = "/Users/juancarlospedrazaalcala/Documents/TT/CSV_Content_Based_Recomendation/customer_product_collaborative_filtering.csv";
		
		final String[] headers = {"CustomerId", "IdProduct", "NoPurchases"}; 
		
		try {
			createCustomerProductCSVFile(headers, pathCsv);
		} catch (IOException e) {
			log.error(":: Error creating customer product csv ", e);
		}
	}

	@Override
	public void createCustomersProductPreferencesByProfileCSVFile() throws IOException {
		final List<CustomerProfile> customerProfile = customerProfileRepository.findAll();
		final List<ProductPromotionsPreferencesProfile> fileList = new ArrayList<>();
		List<Customers> customers;
		List<ProductPromotionsPreferencesProfile> customerProducts;
		Product product;
		for (final CustomerProfile custProf : customerProfile) {

			customers = customerRepository.findAllByIdprofile(custProf);
			fileList.clear();
			for (final Customers customer : customers) {

				customerProducts = jdbcTemplate.query(QUERY_PROFILE_PRODUCTS, new Object[] { customer.getId() },
						new ProductPreferencesProfileRowMapper());

				for (final ProductPromotionsPreferencesProfile prefProfile : customerProducts) {
					product = productRepository.findById(prefProfile.getIdProduct()).get();
					prefProfile.setName(product.getName());
					prefProfile.setIdCustomer(customer.getId());
				}
				fileList.addAll(customerProducts);
			}
			writeFileProductPreferencesByProfile(fileList, BASIS_PATH_CUSTOMER_PRODUCT_PREFERENCES
					+ FILE_NAME_CUSTOMER_PRODUCT_PREFERENCES + custProf.getName() + CSV_EXTENSION);
		}
	}
	
	@Override
	public void createCustomersPromotionsPreferencesByProfileCSVProfile() throws IOException {
		final List<CustomerProfile> customerProfile = customerProfileRepository.findAll();
		final List<ProductPromotionsPreferencesProfile> fileList = new ArrayList<>();
		List<Customers> customers;
		List<ProductPromotionsPreferencesProfile> customerProducts;
		Promotions promotion;
		for (final CustomerProfile custProf : customerProfile) {

			customers = customerRepository.findAllByIdprofile(custProf);
			fileList.clear();
			for (final Customers customer : customers) {

				customerProducts = jdbcTemplate.query(QUERY_PROFILE_PROMOTIONS, new Object[] { customer.getId() },
						new PromotionPreferencesProfileRowMapper());

				for (final ProductPromotionsPreferencesProfile prefProfile : customerProducts) {
					promotion = promotionsRepository.findById(prefProfile.getIdPromotion()).get();
					prefProfile.setName(promotion.getName());
					prefProfile.setIdCustomer(customer.getId());
				}
				fileList.addAll(customerProducts);
			}
			writeFileProductPreferencesByProfile(fileList, PATH_COLLABORATIVE_FILTERING_INPUT + SEPARATOR_PATH
					+ FILE_NAME_CUSTOMER_PROMOTION_PREFERENCES + custProf.getName() + CSV_EXTENSION);
		}
	}

	private void writeFileProductPreferencesByProfile(List<ProductPromotionsPreferencesProfile> fileList,
			String pathFile) throws IOException {
		final String[] headers = { "IdCustomer", "Name", "NoPurchases" };
		File file = new File(pathFile);
		file.delete();
		FileWriter out = new FileWriter(pathFile);
		try (CSVPrinter printer = new CSVPrinter(out, CSVFormat.DEFAULT.withHeader(headers))) {
			fileList.forEach(item -> {
				try {
					printer.printRecord(item.getIdCustomer(), item.getName(), item.getQuantityPurchases());
				} catch (IOException e) {
					log.error(":: Error writing file " + pathFile, e);
				}
			});
		}
	}


	@Override
	public void processProfileRecommendationsDirectory(String path, Boolean isProductRecommendations) {
		try (Stream<Path> paths = Files.walk(Paths.get(path))) {
			paths.filter(Files::isDirectory).forEach(item -> {
				Integer idProfile;
				if (item.getFileName().toString().toLowerCase().contains(
						com.calpullix.db.process.catalog.model.CustomerProfile.AVERAGE.name().toLowerCase())) {
					idProfile = com.calpullix.db.process.catalog.model.CustomerProfile.AVERAGE.getId();
				} else if (item.getFileName().toString().toLowerCase()
						.contains(com.calpullix.db.process.catalog.model.CustomerProfile.LOW.name().toLowerCase())) {
					idProfile = com.calpullix.db.process.catalog.model.CustomerProfile.LOW.getId();
				} else if (item.getFileName().toString().toLowerCase().contains(
						com.calpullix.db.process.catalog.model.CustomerProfile.PREMIUM.name().toLowerCase())) {
					idProfile = com.calpullix.db.process.catalog.model.CustomerProfile.PREMIUM.getId();
				} else {
					idProfile = com.calpullix.db.process.catalog.model.CustomerProfile.VIP.getId();
				}
				if (BooleanUtils.negate(item.toFile().getAbsolutePath().equals(path))) {
					final Map<Integer, Integer> map;
					if (isProductRecommendations) {
						final String[] headers = { "ProductId", "Name", "Category" };
						map = processProfileRecommendations(headers, item.toFile().getAbsolutePath(), "ProductId",
								idProfile);
						addProductRecommendationsDataBase(map, idProfile);
					} else {
						final String[] headers = { "PromotionId", "Name", "Category" };
						map = processProfileRecommendations(headers, item.toFile().getAbsolutePath(), "PromotionId",
								idProfile);
						addPromotionRecommendationsDataBase(map, idProfile);
					}
				}
			});
		} catch (IOException e) {
			log.error(":: Error processing " + path, e);
		}
	}

	private Map<Integer, Integer> processProfileRecommendations(String[] headers, String directory, String keyRecord,
			Integer idProfile) {
		final Map<Integer, Integer> mapProducts = new HashMap<>();
		log.info(":: Processing directory: {} ", directory);
		try (Stream<Path> paths = Files.walk(Paths.get(directory))) {
			paths.filter(Files::isRegularFile).forEach(item -> {
				if (item.toFile().exists() && BooleanUtils.negate(item.getFileName().toString().equals(IGNORE_FILE))) {
					final Iterable<CSVRecord> records = utilService.readCsvFile(headers,
							directory + SEPARATOR_PATH + item.getFileName());
					Integer productId;
					Integer numberItems;
					for (final CSVRecord record : records) {
						productId = Integer.valueOf(record.get(keyRecord));
						numberItems = mapProducts.get(productId);
						if (numberItems == null) {
							mapProducts.put(productId, 1);
						} else {
							numberItems++;
							mapProducts.put(productId, numberItems);
						}
					}
				}
			});

		} catch (IOException e) {
			log.error(":: Error process reports ", e);
		}
		return mapProducts;
	}

	@SuppressWarnings("unchecked")
	private void addProductRecommendationsDataBase(Map<Integer, Integer> mapProducts, Integer idProfile) {

		Object[] orderMapValues = mapProducts.entrySet().toArray();
		Arrays.sort(orderMapValues, new Comparator<Object>() {
			public int compare(Object o1, Object o2) {
				return ((Map.Entry<Integer, Integer>) o2).getValue()
						.compareTo(((Map.Entry<Integer, Integer>) o1).getValue());
			}
		});

		final CustomerProfile idprofile = new CustomerProfile();
		idprofile.setId(idProfile);
		Optional<List<ProductRecomendationProfile>> recommendationProfile = productRecomendationProfileRepository
				.findAllByIdprofileAndIsactive(idprofile, Boolean.TRUE);
		if (recommendationProfile.isPresent()) {
			for (final ProductRecomendationProfile item : recommendationProfile.get()) {
				item.setIsactive(Boolean.FALSE);
				productRecomendationProfileRepository.save(item);
			}
		}
		ProductRecomendationProfile newRecommendationProfile;
		Product product;
		for (int index = 0; index < QUANTITY_RECOMMENDATIONS; index++) {
			newRecommendationProfile = new ProductRecomendationProfile();
			newRecommendationProfile.setDate(formatDate.format(Calendar.getInstance().getTime()));
			product = productRepository.findById(((Map.Entry<Integer, Integer>) orderMapValues[index]).getKey()).get();
			newRecommendationProfile.setIdproduct(product);
			newRecommendationProfile.setIdprofile(idprofile);
			newRecommendationProfile.setIsactive(Boolean.TRUE);
			productRecomendationProfileRepository.save(newRecommendationProfile);
		}

		log.info(":: Products Map {} :: {} ", idprofile, mapProducts);
	}

	@SuppressWarnings("unchecked")
	private void addPromotionRecommendationsDataBase(Map<Integer, Integer> mapPromotions, Integer idProfile) {

		Object[] orderMapValues = mapPromotions.entrySet().toArray();
		Arrays.sort(orderMapValues, new Comparator<Object>() {
			public int compare(Object o1, Object o2) {
				return ((Map.Entry<Integer, Integer>) o2).getValue()
						.compareTo(((Map.Entry<Integer, Integer>) o1).getValue());
			}
		});

		final CustomerProfile idprofile = new CustomerProfile();
		idprofile.setId(idProfile);
		Optional<List<ProfilePromotions>> recommendationProfile = profilePromotionsRepository
				.findAllByIdprofileAndActive(idprofile, Boolean.TRUE);

		if (recommendationProfile.isPresent()) {
			for (final ProfilePromotions item : recommendationProfile.get()) {
				item.setActive(Boolean.FALSE);
				profilePromotionsRepository.save(item);
			}
		}

		ProfilePromotions newRecommendationProfile;
		Promotions promotion;
		List<Integer> idProduct = new ArrayList<>();
		int countPromotions = 0;
		for (int index = 0; index < orderMapValues.length; index++) {
			promotion = promotionsRepository.findById(((Map.Entry<Integer, Integer>) orderMapValues[index]).getKey())
					.get();
			if (BooleanUtils.negate(idProduct.contains(promotion.getIdproduct().getId()))) {
				newRecommendationProfile = new ProfilePromotions();
				newRecommendationProfile.setIdprofile(idprofile);
				newRecommendationProfile.setIdpromotion(promotion);
				newRecommendationProfile.setCreationdate(formatDate.format(Calendar.getInstance().getTime()));
				newRecommendationProfile.setAccepted(Boolean.FALSE);
				newRecommendationProfile.setActive(Boolean.TRUE);
				profilePromotionsRepository.save(newRecommendationProfile);
				++countPromotions;
			}
			idProduct.add(promotion.getIdproduct().getId());
			if (countPromotions == QUANTITY_PROMOTION_RECOMMENDATIONS) {
				break;
			}
		}
		log.info(":: Promotions Map {} :: {} ", idprofile, mapPromotions);
	}	
	
	@Override
	public void createPromotionListCollaborativeFiltering() throws IOException {
		log.info(":: Writing promotion list collaborative filtering file {} ", PATH_COLLABORATIVE_FILTERING_INPUT);
		final String[] headers = { "PromotionId", "Name", "PricePromotion", "CreationDate" };
		final List<Promotions> promotions = promotionsRepository.findAll();
		final FileWriter out = new FileWriter(
				PATH_COLLABORATIVE_FILTERING_INPUT + SEPARATOR_PATH + PROMOTION_LIST_COLLABORATIVE_FILTERING_FILE);
		try (CSVPrinter printer = new CSVPrinter(out, CSVFormat.DEFAULT.withHeader(headers))) {
			promotions.forEach(item -> {
				try {
					printer.printRecord(item.getId(), item.getName(), item.getPricepromotion(), item.getCreationdate());
				} catch (IOException e) {
					log.error(":: Error al escribir el archivo " + PROMOTION_LIST_COLLABORATIVE_FILTERING_FILE, e);
				}
			});
		}
		log.info(":: Ends up writing promotion list collaborative filtering file {} ",
				PROMOTION_LIST_COLLABORATIVE_FILTERING_FILE);
	}

	@Override
	public void createCustomerProductCSVFile(String[] headers, String path) throws IOException {
		log.info(":: Writing customer-product file {} ", path);
		Map<String, Integer> mapCustomerproduct = new HashMap<>();
		int count = 0;
		Pageable pagination = PageRequest.of(count, PAGINATION_SIZE_CUSTOMER_PRODUCT_CSV);
		Page<ProductBranch> productBranch = productBranchRepository.findAll(pagination);
		String key;
		Integer productCount;
		while (productBranch.hasContent()) {
			log.info(":: Paginaton {} {} {} ", count, productBranch.getSize(),
					productBranch.getContent().get(productBranch.getContent().size() - 1).getId());
			for (final ProductBranch item : productBranch.getContent()) {
				key = item.getIdcustomer().getId() + PIPE_SEPARATOR + item.getIdproducthistory().getIdproduct().getId();
				productCount = mapCustomerproduct.get(key);
				if (productCount == null) {
					mapCustomerproduct.put(key, 1);
				} else {
					mapCustomerproduct.put(key, ++productCount);
				}
			}
			count++;
			log.info("**** PAGE {} {} ", count, PAGINATION_SIZE_CUSTOMER_PRODUCT_CSV);
			pagination = PageRequest.of(count, PAGINATION_SIZE_CUSTOMER_PRODUCT_CSV);
			productBranch = productBranchRepository.findAll(pagination);

		}

		final File file = new File(path);
		file.delete();
		final FileWriter out = new FileWriter(path);
		try (CSVPrinter printer = new CSVPrinter(out, CSVFormat.DEFAULT.withHeader(headers))) {
			mapCustomerproduct.keySet().forEach(item -> {
				try {
					final StringTokenizer token = new StringTokenizer(item, PIPE_SEPARATOR);
					printer.printRecord(token.nextToken(), token.nextToken(), mapCustomerproduct.get(item));
				} catch (IOException e) {
					log.error(":: Error al escribir el archivo " + path, e);
				}
			});
		}
		log.info(":: Ends up writing customer-product file {} ", path);
	}

	@Override
	public void createCustomerPromotionCSVFile() throws IOException {
		log.info(":: Writing customer-promotion file {} ", PROMOTIONS_RATING_COLLABORATIVE_FILTERING_FILE);
		Map<String, Integer> mapCustomerPromotion = new HashMap<>();
		final String[] headers = { "CustomerId", "PromotionId", "NoPurchases" };
		int count = 0;
		Pageable pagination = PageRequest.of(count, PAGINATION_SIZE_CUSTOMER_PRODUCT_CSV);
		List<ProductBranch> productBranch = productBranchRepository.findAllByIdpromotionIsNotNull(pagination);
		String key;
		Integer promotionCount;
		while (BooleanUtils.negate(CollectionUtils.isEmpty(productBranch))) {
			log.info(":: Paginaton {} {} {} ", count, productBranch.size(),
					productBranch.get(productBranch.size() - 1).getId());

			for (final ProductBranch item : productBranch) {
				key = item.getIdcustomer().getId() + PIPE_SEPARATOR + item.getIdpromotion().getId();
				promotionCount = mapCustomerPromotion.get(key);
				if (promotionCount == null) {
					mapCustomerPromotion.put(key, 1);
				} else {
					mapCustomerPromotion.put(key, ++promotionCount);
				}
			}
			count++;
			log.info(":: PAGE {} {} ", count, PAGINATION_SIZE_CUSTOMER_PRODUCT_CSV);

			pagination = PageRequest.of(count, PAGINATION_SIZE_CUSTOMER_PRODUCT_CSV);
			productBranch = productBranchRepository.findAllByIdpromotionIsNotNull(pagination);
		}

		final File file = new File(
				PATH_COLLABORATIVE_FILTERING_INPUT + SEPARATOR_PATH + PROMOTIONS_RATING_COLLABORATIVE_FILTERING_FILE);
		file.delete();
		final FileWriter out = new FileWriter(
				PATH_COLLABORATIVE_FILTERING_INPUT + SEPARATOR_PATH + PROMOTIONS_RATING_COLLABORATIVE_FILTERING_FILE);
		try (CSVPrinter printer = new CSVPrinter(out, CSVFormat.DEFAULT.withHeader(headers))) {
			mapCustomerPromotion.keySet().forEach(item -> {
				try {
					final StringTokenizer token = new StringTokenizer(item, PIPE_SEPARATOR);
					printer.printRecord(token.nextToken(), token.nextToken(), mapCustomerPromotion.get(item));
				} catch (IOException e) {
					log.error(":: Error writing file: " + PROMOTIONS_RATING_COLLABORATIVE_FILTERING_FILE, e);
				}
			});
		}
		log.info(":: Ends up writing customer-promotion file {} ", PROMOTIONS_RATING_COLLABORATIVE_FILTERING_FILE);
	}

	
}
