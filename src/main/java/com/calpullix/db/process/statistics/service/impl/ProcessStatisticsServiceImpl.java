package com.calpullix.db.process.statistics.service.impl;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.StringTokenizer;
import java.util.stream.Stream;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.calpullix.db.process.branch.model.Branch;
import com.calpullix.db.process.catalog.model.ProductBranchStatus;
import com.calpullix.db.process.customer.model.Customers;
import com.calpullix.db.process.customer.repository.CustomerRepository;
import com.calpullix.db.process.product.model.Product;
import com.calpullix.db.process.product.model.ProductBranch;
import com.calpullix.db.process.product.repository.ProductBranchRepository;
import com.calpullix.db.process.statistics.box.plot.StatisticsBloxPlot;
import com.calpullix.db.process.statistics.box.plot.repository.StatisticsBloxPlotRepository;
import com.calpullix.db.process.statistics.model.Statistics;
import com.calpullix.db.process.statistics.model.StatisticsAnova;
import com.calpullix.db.process.statistics.model.StatisticsCorrelation;
import com.calpullix.db.process.statistics.model.StatisticsCorrelationVariableRelation;
import com.calpullix.db.process.statistics.model.StatisticsGroupby;
import com.calpullix.db.process.statistics.model.StatisticsGroupbyVariableRelation;
import com.calpullix.db.process.statistics.model.StatisticsHeatmap;
import com.calpullix.db.process.statistics.model.StatisticsVariableRelation;
import com.calpullix.db.process.statistics.repository.StatisticsAnovaRepository;
import com.calpullix.db.process.statistics.repository.StatisticsCorrelationRepository;
import com.calpullix.db.process.statistics.repository.StatisticsCorrelationVariableRelationRepository;
import com.calpullix.db.process.statistics.repository.StatisticsGroupbyRepository;
import com.calpullix.db.process.statistics.repository.StatisticsGroupbyVariableRelationRepository;
import com.calpullix.db.process.statistics.repository.StatisticsHeatMapRepository;
import com.calpullix.db.process.statistics.repository.StatisticsRepository;
import com.calpullix.db.process.statistics.repository.StatisticsVariableRelationRepository;
import com.calpullix.db.process.statistics.service.ProcessStatisticsService;
import com.calpullix.db.process.util.service.UtilService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ProcessStatisticsServiceImpl implements ProcessStatisticsService {

	private static final String BASE_STATISTICS_DIRECTORY = "/Users/juancarlospedrazaalcala/Documents/TT/CSV_Jupyetr/";

	private static final String BACKUP_PATH = "/Users/juancarlospedrazaalcala/Documents/TT/BACK_UP/";

	private static final String CSV_EXTENSION = ".csv";

	private static final String TXT_EXTENSION = ".txt";

	private static final String PNG_EXTENSION = ".png";

	private static final String IGNORE_FILE = ".DS_Store";

	private static final String REL_COLUMN = "Rel.";

	private static final String CORRELATION_REPORT = "correlation";

	private static final String DESCRIPTIVE_REPORT = "descriptive";

	private static final String GROUPBY_REPORT = "groupby";

	private static final String ANOVA_REPORT = "anova";

	private static final String BOXPLOT_REPORT = "boxplot";

	private static final String HEATMAP_REPORT = "heatmap";

	private static final String TOKEN = "/";

	private static final String PIPE_SEPARATOR = "|";
	
	private static final int INDEX_NAME_REPORT = 0;

	private static final int INDEX_ID_BRANCH = 1;

	private static final int INDEX_YEAR = 2;

	private static final int INDEX_ID_PRODUCT = 3;

	private static final int INDEX_MONTH = 4;

	@Autowired
	private StatisticsCorrelationVariableRelationRepository statisticsCorrelationVariableRelationRepository;

	@Autowired
	private StatisticsCorrelationRepository statisticsCorrelationRepository;

	@Autowired
	private StatisticsRepository statisticsRepository;

	@Autowired
	private StatisticsVariableRelationRepository statisticsVariableRelationRepository;

	@Autowired
	private StatisticsGroupbyRepository statisticsGroupbyRepository;

	@Autowired
	private StatisticsGroupbyVariableRelationRepository statisticsGroupbyVariableRelationRepository;

	@Autowired
	private StatisticsAnovaRepository statisticsAnovaRepository;

	@Autowired
	private StatisticsBloxPlotRepository statisticsBloxPlotRepository;

	@Autowired
	private StatisticsHeatMapRepository statisticsHeatMapRepository;
	
	@Autowired
	private ProductBranchRepository productBranchRepository;

	@Autowired
	private CustomerRepository customerRepository;

	@Autowired
	private UtilService utilService;
	
	private SimpleDateFormat formatTimestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:SS.sss");

	
	@Override
	public void createCsvExportDataBaseFile() {
		String pathCsv = "/Users/juancarlospedrazaalcala/Documents/TT/CSV_MYSQL/sales_branch4_2019_08.csv";

		String[] headersCsv = { "Id", "IdBranch", "BranchName", "Number Employees", "State branch",
				"Municipality Branch", "Region Branch", "Creation Date", "Expiration Date", "Sale Date",
				"Distribution Center", "Id Customer", "IdProductHistory", "IdProduct", "Product Name",
				"Sale-Price-Product", "Purchase-Price-Product", "IdBrand Product", "Brand Name Product",
				"Product Category", "Product Category Name", "Weight", "Is Fragile Product", "IdProvider Product",
				"Provider Name Product", "Status", "Id Location", "Location Description", "id Promotion",
				"Price promotion" };

		Integer idBranch = 4;
		Integer idProduct = null;
		String initDate = "2019-08-01";
		String endDate = "2019-08-31";

		try {
			createCSVFile(headersCsv, pathCsv, idBranch, idProduct, initDate, endDate);
		} catch (IOException e) {
			log.error(":: Error al escribir el archivo " + pathCsv, e);
		}
	}
	
	@Override
	public void createCustomersCSVFile() {
		String pathCsv = "/Users/juancarlospedrazaalcala/Documents/TT/CSV_MYSQL/customers_profile_2020_04_04.csv";

		String[] headersCsv = { "Id", "Age", "State", "Education Level", "Marital Status", "Income", "Debt" };

		try {
			createCustomersCSVFile(headersCsv, pathCsv);
		} catch (IOException e) {
			log.error(":: Error creating customers csv ", e);
		}
	}
	
	@Override
	public void createCustomersCSVFile(String[] headers, String path) throws IOException {
		log.info(":: Writing customers file {} {} ", path);
		final File file = new File(path);
		file.delete();
		final List<Customers> customers = customerRepository.findAll();
		FileWriter out = new FileWriter(path);
		try (CSVPrinter printer = new CSVPrinter(out, CSVFormat.DEFAULT.withHeader(headers))) {
			customers.forEach(item -> {
				try {
					printer.printRecord(item.getId(), item.getAge(), item.getStatevalue(),
							item.getEducationlevel().getId(), item.getMaritalstatus().getId(), item.getIncome(),
							item.getDebt());
				} catch (IOException e) {
					log.error(":: Error al escribir el archivo " + path, e);
				}
			});
		}
		log.info(":: Ends up writing customers file {} ", path);
	}

	@Override
	public void createCSVFile(String[] headers, String path, Integer idBranch, Integer idProduct, String initDate,
			String endDate) throws IOException {
		File fileToDelete = new File(path);
		boolean success = fileToDelete.delete();
		log.info(":: Writing file {} {} ", path, success);

		final Branch branch = new Branch();
		branch.setId(idBranch);
		final List<ProductBranch> productBranch;

		if (idProduct == null) {
			productBranch = productBranchRepository.findAllByIdbranchidAndSaledateAndStatus(branch, initDate, endDate,
					ProductBranchStatus.SOLD.getId());
		} else {
			final Product idproduct = new Product();
			idproduct.setId(idProduct);
			productBranch = productBranchRepository.findAllByIdbranchAndIdproductAndSaledateAndStatus(branch, idproduct,
					initDate, endDate, ProductBranchStatus.SOLD.getId());
		}

		FileWriter out = new FileWriter(path);

		try (CSVPrinter printer = new CSVPrinter(out, CSVFormat.DEFAULT.withHeader(headers))) {
			productBranch.forEach(item -> {
				try {
					printer.printRecord(item.getId(), item.getIdbranch().getId(), item.getIdbranch().getName(),
							item.getIdbranch().getNumberemployees(), item.getIdbranch().getStatevalue(),
							item.getIdbranch().getMunicipality(), item.getIdbranch().getRegionvalue(),
							item.getCreationdate(), item.getExpirationdate(), item.getSaledate(),
							item.getDistributioncenter().getId(), item.getIdcustomer().getId(),
							item.getIdproducthistory().getId(), item.getIdproducthistory().getIdproduct().getId(),
							item.getIdproducthistory().getIdproduct().getName(),
							item.getIdproducthistory().getSaleprice(), item.getIdproducthistory().getPurchaseprice(),
							item.getIdproducthistory().getIdproduct().getBrand().getId(),
							item.getIdproducthistory().getIdproduct().getBrand().getDescription(),
							item.getIdproducthistory().getIdproduct().getCategoryvalue(),
							item.getIdproducthistory().getIdproduct().getCategory().getDescription(),
							item.getIdproducthistory().getIdproduct().getWeight(),
							item.getIdproducthistory().getIdproduct().getFragileMaterial(),
							item.getIdproducthistory().getIdproduct().getProvider().getId(),
							item.getIdproducthistory().getIdproduct().getProvider().getName(), item.getStatusvalue(),
							item.getLocationvalue(), item.getLocation().getDescription(),
							item.getIdpromotion() == null ? 0 : item.getIdpromotion().getId(),
							item.getIdpromotion() == null ? 0 : item.getIdpromotion().getPricepromotion());
				} catch (IOException e) {
					log.error(":: Error al escribir el archivo " + path, e);
				}
			});
		}
		log.info(":: Ends up writing file {} ", path);
	}

	
	@Override
	public void processStatisticsInformation() {
		processReports(BASE_STATISTICS_DIRECTORY);
	}
	
	@Override
	public void watchStatisticsFiles() throws IOException, InterruptedException {
		Path faxFolder = Paths.get(BASE_STATISTICS_DIRECTORY);
		WatchService watchService = FileSystems.getDefault().newWatchService();
		faxFolder.register(watchService, StandardWatchEventKinds.ENTRY_CREATE);
		boolean valid = true;
		do {
			WatchKey watchKey = watchService.take();
			for (WatchEvent<?> event : watchKey.pollEvents()) {
				if (StandardWatchEventKinds.ENTRY_CREATE.equals(event.kind())) {
					String fileName = event.context().toString();
					log.info(":: Statistics File Created: {} ", BASE_STATISTICS_DIRECTORY + fileName);
					processFile(BASE_STATISTICS_DIRECTORY + fileName);
				}
			}
			valid = watchKey.reset();
		} while (valid);
	}

	@Override
	public void processReports(String directory) {
		try (Stream<Path> paths = Files.walk(Paths.get(directory))) {
			paths.filter(Files::isRegularFile).forEach(item -> {
				if (item.toFile().exists() && BooleanUtils.negate(item.getFileName().toString().equals(IGNORE_FILE))) {
					processFile(directory + item.getFileName());
				}
			});
		} catch (IOException e) {
			log.error(":: Error process reports ", e);
		}
	}

	@Override
	public void processFile(String informationFileName) {
		log.info(":: Process file {} ", informationFileName);
		final List<String> informationFile = processFileNameInformation(informationFileName);
		if (informationFile.get(INDEX_NAME_REPORT).equals(CORRELATION_REPORT)) {
			processCorrelationReport(informationFileName);
		} else if (informationFile.get(INDEX_NAME_REPORT).equals(DESCRIPTIVE_REPORT)) {
			processDescriptiveStatisticsReport(informationFileName);
		} else if (informationFile.get(INDEX_NAME_REPORT).equals(GROUPBY_REPORT)) {
			processGroupByReport(informationFileName);
		} else if (informationFile.get(INDEX_NAME_REPORT).equals(ANOVA_REPORT)) {
			processAnovaReport(informationFileName);
		} else if (informationFile.get(INDEX_NAME_REPORT).equals(BOXPLOT_REPORT)) {
			processBloxPlotReport(informationFileName);
		} else if (informationFile.get(INDEX_NAME_REPORT).equals(HEATMAP_REPORT)) {
			processHeatMapReport(informationFileName);
		}
	}
	
	private void processCorrelationReport(String informationFileName) {
		log.info(":: Processing correlation file {} ", informationFileName);
		final List<String> informationFile = processFileNameInformation(informationFileName);
		String[] headers;
		List<List<String>> rows;
		Branch idbranch;
		Product idproduct;
		File file = new File(informationFileName);
		File newFile = new File(informationFileName + "_" + formatTimestamp.format(Calendar.getInstance().getTime()));

		headers = new String[] { "Distribution Center", "IdProvider Product", "Product Category",
				"Purchase-Price-Product" };
		rows = utilService.processCsvFile(headers, informationFileName);

		StatisticsCorrelation correlation = new StatisticsCorrelation();
		idbranch = new Branch();
		idbranch.setId(Integer.valueOf(informationFile.get(INDEX_ID_BRANCH)));
		correlation.setIdbranch(idbranch);
		if (BooleanUtils.negate(informationFile.get(INDEX_ID_PRODUCT) == null)) {
			idproduct = new Product();
			idproduct.setId(Integer.valueOf(informationFile.get(INDEX_ID_PRODUCT)));
			correlation.setIdproduct(idproduct);
		}
		correlation.setYear(Integer.valueOf(informationFile.get(INDEX_YEAR)));
		correlation.setMonth(informationFile.get(4) == null ? null : Integer.valueOf(informationFile.get(INDEX_MONTH)));
		headers = new String[] { "Centro Distr.", "Id Proveedor", "Categoria", "Precio de Compra" };
		correlation.setVerticalvarname(REL_COLUMN);
		correlation.setHorizontalvarnameone(headers[0]);
		correlation.setHorizontalvarnametwo(headers[1]);
		correlation.setHorizontalvarnamethree(headers[2]);
		correlation.setHorizontalvarnamefour(headers[3]);
		correlation = statisticsCorrelationRepository.save(correlation);
		StatisticsCorrelationVariableRelation variableRelation;
		for (final List<String> row : rows) {
			variableRelation = new StatisticsCorrelationVariableRelation();
			variableRelation.setIdstatisticscorrelation(correlation);
			variableRelation.setRelationvalueone(row.get(0).length() > 20 ? row.get(0).substring(0, 20) : row.get(0));
			variableRelation.setRelationvaluetwo(row.get(1).length() > 20 ? row.get(1).substring(0, 20) : row.get(1));
			variableRelation.setRelationvaluethree(row.get(2).length() > 20 ? row.get(2).substring(0, 20) : row.get(2));
			variableRelation.setRelationvaluefour(row.get(3).length() > 20 ? row.get(3).substring(0, 20) : row.get(3));
			statisticsCorrelationVariableRelationRepository.save(variableRelation);
		}

		file.renameTo(newFile);
		try {
			log.info("****  New PATH {} {} ", newFile, BACKUP_PATH + newFile.getName());
			Files.move(Paths.get(newFile.getAbsolutePath()), Paths.get(BACKUP_PATH + newFile.getName()),
					StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			log.error(":: Error moving file ", e);
		}
	}

	private void processDescriptiveStatisticsReport(String informationFileName) {
		final List<String> informationFile = processFileNameInformation(informationFileName);
		String[] headers;
		List<List<String>> rows;
		Branch idbranch;
		Product idproduct;
		File file = new File(informationFileName);
		File newFile = new File(informationFileName + "_" + formatTimestamp.format(Calendar.getInstance().getTime()));

		log.info(":: Processing descriptive statistics report ");
		headers = new String[] { "Sale-Price-Product", "Purchase-Price-Product", "Weight", "Price promotion" };
		rows = utilService.processCsvFile(headers, informationFileName);

		Statistics statistics = new Statistics();
		idbranch = new Branch();
		idbranch.setId(Integer.valueOf(informationFile.get(INDEX_ID_BRANCH)));
		statistics.setIdbranch(idbranch);
		if (BooleanUtils.negate(informationFile.get(INDEX_ID_PRODUCT) == null)) {
			idproduct = new Product();
			idproduct.setId(Integer.valueOf(informationFile.get(INDEX_ID_PRODUCT)));
			statistics.setIdproduct(idproduct);
		}
		statistics.setYear(Integer.valueOf(informationFile.get(INDEX_YEAR)));
		statistics.setMonth(informationFile.get(4) == null ? null : Integer.valueOf(informationFile.get(INDEX_MONTH)));
		headers = new String[] { "Precio de Venta", "Precio de compra", "Peso", "Pecio Promocion" };
		statistics.setVarnameone(headers[0]);
		statistics.setVarnametwo(headers[1]);
		statistics.setVarnamethree(headers[2]);
		statistics.setVarnamefour(headers[3]);
		statistics = statisticsRepository.save(statistics);
		headers = new String[] { "count", "mean", "std", "min", "25%", "50%", "75%", "max" };
		StatisticsVariableRelation variableRelation;
		int index = 0;
		for (final List<String> row : rows) {
			variableRelation = new StatisticsVariableRelation();
			variableRelation.setIdstatistics(statistics);
			variableRelation.setStatisticvar(headers[index]);
			variableRelation.setVarvalueone(row.get(0).length() > 20 ? row.get(0).substring(0, 20) : row.get(0));
			variableRelation.setVarvaluetwo(row.get(1).length() > 20 ? row.get(1).substring(0, 20) : row.get(1));
			variableRelation.setVarvaluethree(row.get(2).length() > 20 ? row.get(2).substring(0, 20) : row.get(2));
			variableRelation.setVarvaluefour(row.get(3).length() > 20 ? row.get(3).substring(0, 20) : row.get(3));
			statisticsVariableRelationRepository.save(variableRelation);
			index++;
		}

		file.renameTo(newFile);
		try {
			log.info("****  New PATH {} {} ", newFile, BACKUP_PATH + newFile.getName());
			Files.move(Paths.get(newFile.getAbsolutePath()), Paths.get(BACKUP_PATH + newFile.getName()),
					StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			log.error(":: Error moving file ", e);
		}
	}

	private void processGroupByReport(String informationFileName) {
		log.info(":: Processing group by report ");
		final List<String> informationFile = processFileNameInformation(informationFileName);
		String[] headers;
		List<List<String>> rows;
		Branch idbranch;
		Product idproduct;
		File file = new File(informationFileName);
		File newFile = new File(informationFileName + "_" + formatTimestamp.format(Calendar.getInstance().getTime()));

		headers = new String[] { "Provider Name Product", "Brand Name Product", "Purchase-Price-Product" };
		rows = utilService.processCsvFile(headers, informationFileName);

		StatisticsGroupby gropuBy = new StatisticsGroupby();
		idbranch = new Branch();
		idbranch.setId(Integer.valueOf(informationFile.get(INDEX_ID_BRANCH)));
		gropuBy.setIdbranch(idbranch);
		if (BooleanUtils.negate(informationFile.get(INDEX_ID_PRODUCT) == null)) {
			idproduct = new Product();
			idproduct.setId(Integer.valueOf(informationFile.get(INDEX_ID_PRODUCT)));
			gropuBy.setIdproduct(idproduct);
		}
		gropuBy.setYear(Integer.valueOf(informationFile.get(INDEX_YEAR)));
		gropuBy.setMonth(informationFile.get(4) == null ? null : Integer.valueOf(informationFile.get(INDEX_MONTH)));
		headers = new String[] { "Proveedor", "Marca", "Precio de compra" };
		gropuBy.setHorizontalvarnameone(headers[0]);
		gropuBy.setHorizontalvarnametwo(headers[1]);
		gropuBy.setHorizontalvarnamethree(headers[2]);
		gropuBy = statisticsGroupbyRepository.save(gropuBy);
		StatisticsGroupbyVariableRelation variableRelation;
		for (final List<String> row : rows) {
			variableRelation = new StatisticsGroupbyVariableRelation();
			variableRelation.setIdstatisticsgroupby(gropuBy);
			variableRelation.setRelationvalueone(row.get(0).length() > 20 ? row.get(0).substring(0, 20) : row.get(0));
			variableRelation.setRelationvaluetwo(row.get(1).length() > 20 ? row.get(1).substring(0, 20) : row.get(1));
			variableRelation.setRelationvaluethree(row.get(2).length() > 20 ? row.get(2).substring(0, 20) : row.get(2));
			if (row.size() > 3) {
				variableRelation
						.setRelationvaluefour(row.get(3).length() > 20 ? row.get(3).substring(0, 20) : row.get(3));
			}
			statisticsGroupbyVariableRelationRepository.save(variableRelation);
		}

		file.renameTo(newFile);
		try {
			log.info("****  New PATH {} {} ", newFile, BACKUP_PATH + newFile.getName());
			Files.move(Paths.get(newFile.getAbsolutePath()), Paths.get(BACKUP_PATH + newFile.getName()),
					StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			log.error(":: Error moving file ", e);
		}
	}

	private void processAnovaReport(String informationFileName) {
		final List<String> informationFile = processFileNameInformation(informationFileName);
		Branch idbranch;
		Product idproduct;
		File file = new File(informationFileName);
		File newFile = new File(informationFileName + "_" + formatTimestamp.format(Calendar.getInstance().getTime()));

		log.info(":: Anova processing file ");
		List<String> lines = null;
		try {
			lines = utilService.readFile(informationFileName);
		} catch (IOException e) {
			log.error(":: Error reading file " + informationFileName, e);
		}

		StatisticsAnova anova;
		idbranch = new Branch();
		idbranch.setId(Integer.valueOf(informationFile.get(INDEX_ID_BRANCH)));
		if (BooleanUtils.negate(informationFile.get(INDEX_ID_PRODUCT) == null)) {
			idproduct = new Product();
			idproduct.setId(Integer.valueOf(informationFile.get(INDEX_ID_PRODUCT)));
		} else {
			idproduct = null;
		}
		StringTokenizer token;
		String pValue;
		String fTestScore;
		for (final String line : lines) {
			anova = new StatisticsAnova();
			anova.setIdbranch(idbranch);
			anova.setIdproduct(idproduct);
			anova.setYear(Integer.valueOf(informationFile.get(INDEX_YEAR)));
			anova.setMonth(informationFile.get(4) == null ? null : Integer.valueOf(informationFile.get(INDEX_MONTH)));
			token = new StringTokenizer(line, TOKEN);
			anova.setVarsindname(token.nextToken());
			anova.setVardepname(token.nextToken());
			fTestScore = token.nextToken();
			anova.setFtestscore(fTestScore.length() > 20 ? fTestScore.substring(0, 20) : fTestScore);
			pValue = token.nextToken();
			anova.setPvalue(pValue.length() > 20 ? pValue.substring(0, 20) : pValue);
			statisticsAnovaRepository.save(anova);
		}

		file.renameTo(newFile);
		try {
			log.info("****  New PATH {} {} ", newFile, BACKUP_PATH + newFile.getName());
			Files.move(Paths.get(newFile.getAbsolutePath()), Paths.get(BACKUP_PATH + newFile.getName()),
					StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			log.error(":: Error moving file ", e);
		}
	}

	private void processBloxPlotReport(String informationFileName) {
		log.info(":: Blox plot reading file ");
		final List<String> informationFile = processFileNameInformation(informationFileName);
		Branch idbranch;
		Product idproduct;
		File file = new File(informationFileName);
		File newFile = new File(informationFileName + "_" + formatTimestamp.format(Calendar.getInstance().getTime()));
		File image;

		final StatisticsBloxPlot boxPlot = new StatisticsBloxPlot();
		idbranch = new Branch();
		idbranch.setId(Integer.valueOf(informationFile.get(INDEX_ID_BRANCH)));
		boxPlot.setIdbranch(idbranch);
		if (BooleanUtils.negate(informationFile.get(INDEX_ID_PRODUCT) == null)) {
			idproduct = new Product();
			idproduct.setId(Integer.valueOf(informationFile.get(INDEX_ID_PRODUCT)));
			boxPlot.setIdproduct(idproduct);
		}
		StringTokenizer token = new StringTokenizer(informationFile.get(INDEX_YEAR), PIPE_SEPARATOR);
		String year = token.nextToken();
		String varInd = token.nextToken();
		String varDep = token.nextToken();

		boxPlot.setYear(Integer.valueOf(year));
		boxPlot.setMonth(informationFile.get(4) == null ? null : Integer.valueOf(informationFile.get(INDEX_MONTH)));
		boxPlot.setVarindname(varInd);
		boxPlot.setVardepname(varDep);
		try {
			image = new File(informationFileName);
			boxPlot.setImage(Files.readAllBytes(image.toPath()));
		} catch (IOException e) {
			e.printStackTrace();
		}
		statisticsBloxPlotRepository.save(boxPlot);

		file.renameTo(newFile);
		try {
			log.info("****  New PATH {} {} ", newFile, BACKUP_PATH + newFile.getName());
			Files.move(Paths.get(newFile.getAbsolutePath()), Paths.get(BACKUP_PATH + newFile.getName()),
					StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			log.error(":: Error moving file ", e);
		}
	}

	private void processHeatMapReport(String informationFileName) {
		log.info(":: Blox plot reading file ");
		final List<String> informationFile = processFileNameInformation(informationFileName);
		Branch idbranch;
		Product idproduct;
		File file = new File(informationFileName);
		File newFile = new File(informationFileName + "_" + formatTimestamp.format(Calendar.getInstance().getTime()));
		File image;

		final StatisticsHeatmap heatMap = new StatisticsHeatmap();
		idbranch = new Branch();
		idbranch.setId(Integer.valueOf(informationFile.get(INDEX_ID_BRANCH)));
		heatMap.setIdbranch(idbranch);
		if (BooleanUtils.negate(informationFile.get(INDEX_ID_PRODUCT) == null)) {
			idproduct = new Product();
			idproduct.setId(Integer.valueOf(informationFile.get(INDEX_ID_PRODUCT)));
			heatMap.setIdproduct(idproduct);
		}
		StringTokenizer token = new StringTokenizer(informationFile.get(INDEX_YEAR), PIPE_SEPARATOR);
		String year = token.nextToken();
		String keyVar = token.nextToken();
		String verticalVarOne = token.nextToken();
		String verticalVarTwo = token.nextToken();

		heatMap.setYear(Integer.valueOf(year));
		heatMap.setMonth(informationFile.get(4) == null ? null : Integer.valueOf(informationFile.get(INDEX_MONTH)));
		heatMap.setKeyvar(keyVar);
		heatMap.setVerticalvarone(verticalVarOne);
		heatMap.setVerticalvartwo(verticalVarTwo);
		try {
			image = new File(informationFileName);
			heatMap.setImage(Files.readAllBytes(image.toPath()));
		} catch (IOException e) {
			e.printStackTrace();
		}
		statisticsHeatMapRepository.save(heatMap);

		file.renameTo(newFile);
		try {
			log.info("****  New PATH {} {} ", newFile, BACKUP_PATH + newFile.getName());
			Files.move(Paths.get(newFile.getAbsolutePath()), Paths.get(BACKUP_PATH + newFile.getName()),
					StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			log.error(":: Error moving file ", e);
		}
	}
	
	@Override
	public List<String> processFileNameInformation(String informationFileName) {
		final List<String> result = new ArrayList<>();
		informationFileName = informationFileName.substring(informationFileName.lastIndexOf("/") + 1);
		log.info(":: File name process information: {} ",
				informationFileName.replace(CSV_EXTENSION, "").replace(TXT_EXTENSION, "").replace(PNG_EXTENSION, ""));
		final StringTokenizer token = new StringTokenizer(
				informationFileName.replace(CSV_EXTENSION, "").replace(TXT_EXTENSION, "").replace(PNG_EXTENSION, ""),
				"_");

		String nameReport = null;
		String branch = null;
		String idBranch = null;
		String year = null;
		String productMonth = null;
		String idProduct = null;
		String month = null;
		int countTokens = token.countTokens();

		if (countTokens < 7) {
			nameReport = token.nextToken();
			branch = token.nextToken();
			if (BooleanUtils.negate(branch.equals("branch"))) {
				token.nextToken();
			}
			idBranch = token.nextToken();
			token.nextToken();
			year = token.nextToken();
		} else if (countTokens < 9) {
			nameReport = token.nextToken();
			branch = token.nextToken();
			if (BooleanUtils.negate(branch.equals("branch"))) {
				token.nextToken();
			}
			idBranch = token.nextToken();
			token.nextToken();
			year = token.nextToken();
			productMonth = token.nextToken();
			if (productMonth.equals("product")) {
				idProduct = token.nextToken();
			} else {
				month = token.nextToken();
			}
		} else {
			nameReport = token.nextToken();
			branch = token.nextToken();
			if (BooleanUtils.negate(branch.equals("branch"))) {
				token.nextToken();
			}
			idBranch = token.nextToken();
			token.nextToken();
			year = token.nextToken();
			token.nextToken();
			idProduct = token.nextToken();
			token.nextToken();
			month = token.nextToken();
		}
		log.info(":: File information: {} {} {} {} {} ", nameReport, idBranch, year, idProduct, month);
		result.add(nameReport);
		result.add(idBranch);
		result.add(year);
		result.add(idProduct);
		result.add(month);
		return result;
	}



}
