package com.calpullix.db.process.regression.service.impl;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.StringTokenizer;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.calpullix.db.process.customer.model.CustomerProfile;
import com.calpullix.db.process.customer.model.Customers;
import com.calpullix.db.process.customer.repository.CustomerRepository;
import com.calpullix.db.process.profile.model.ProfileRegression;
import com.calpullix.db.process.profile.repository.ProfileRegressionRepository;
import com.calpullix.db.process.regression.service.RegressionProcessService;
import com.calpullix.db.process.util.service.UtilService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class RegressionProcessServiceImpl implements RegressionProcessService {

	private static final String BASIS_LOGISTIC_REGRESSION_DIRECTORY = "/Users/juancarlospedrazaalcala/Documents/TT/Logistic_Regression_OUTPUT";

	private static final String CLASSIFICATION_NULL_PATH_FILE = "/Users/juancarlospedrazaalcala/Documents/TT/Logistic_Regression_INPUT/regression_classification_null.csv";

	private static final String PATH_CUSTOMERS_LOGISTIC_REGRESSION_CSV_FILE = "/Users/juancarlospedrazaalcala/Documents/TT/Logistic_Regression_INPUT/logistic_regression_customers.csv";

	private static final String SEPARATOR_PATH = "/";

	private static final int LEFT_CLASSIFICATION = 0;

	private static final int LOYAL_CLASSIFICATION = 1;

	@Autowired
	private CustomerRepository customerRepository;

	@Autowired
	private ProfileRegressionRepository profileRegressionRepository;

	@Autowired
	private UtilService utilService;
	
	private SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	@Override
	public void exportCustomersClassificationNull() throws IOException {
		File fileToDelete = new File(CLASSIFICATION_NULL_PATH_FILE);
		fileToDelete.delete();
		final String[] headers = { "Id", "Age", "Sex", "Education Level", "State", "Marital Status", "Job" };
		final List<Customers> customers = customerRepository.findAllCustomersByClassificationIsNull();
		File file = new File(CLASSIFICATION_NULL_PATH_FILE);
		file.delete();
		FileWriter out = new FileWriter(CLASSIFICATION_NULL_PATH_FILE);
		try (CSVPrinter printer = new CSVPrinter(out, CSVFormat.DEFAULT.withHeader(headers))) {
			customers.forEach(item -> {
				try {
					printer.printRecord(BigDecimal.valueOf(item.getId()).setScale(1, RoundingMode.FLOOR),
							BigDecimal.valueOf(item.getAge()).setScale(1, RoundingMode.FLOOR),
							BigDecimal.valueOf(getSex(item.getSex())).setScale(1, RoundingMode.FLOOR),
							BigDecimal.valueOf(item.getEducationlevel().getId()).setScale(1, RoundingMode.FLOOR),
							BigDecimal.valueOf(item.getState().getId()).setScale(1, RoundingMode.FLOOR),
							BigDecimal.valueOf(item.getMaritalstatus().getId()).setScale(1, RoundingMode.FLOOR),
							BigDecimal.valueOf(getIdJob(item.getJob())).setScale(1, RoundingMode.FLOOR));
				} catch (IOException e) {
					log.error(":: Error al escribir el archivo " + CLASSIFICATION_NULL_PATH_FILE, e);
				}
			});
		}
	}
	
	@Override
	public void createCsvCustomerByProfileFile() throws IOException {
		final String[] headers = { "Id", "Age", "Sex", "Education Level", "State", "Marital Status", "Job",
				"Classification" };
		List<Customers> customers = customerRepository.findAllCustomersByClassificationIsNotNull();
		File file = new File(PATH_CUSTOMERS_LOGISTIC_REGRESSION_CSV_FILE);
		file.delete();
		FileWriter out = new FileWriter(PATH_CUSTOMERS_LOGISTIC_REGRESSION_CSV_FILE);
		try (CSVPrinter printer = new CSVPrinter(out, CSVFormat.DEFAULT.withHeader(headers))) {
			customers.forEach(item -> {
				try {
					printer.printRecord(BigDecimal.valueOf(item.getId()).setScale(1, RoundingMode.FLOOR),
							BigDecimal.valueOf(item.getAge()).setScale(1, RoundingMode.FLOOR),
							BigDecimal.valueOf(getSex(item.getSex())).setScale(1, RoundingMode.FLOOR),
							BigDecimal.valueOf(item.getEducationlevel().getId()).setScale(1, RoundingMode.FLOOR),
							BigDecimal.valueOf(item.getState().getId()).setScale(1, RoundingMode.FLOOR),
							BigDecimal.valueOf(item.getMaritalstatus().getId()).setScale(1, RoundingMode.FLOOR),
							BigDecimal.valueOf(getIdJob(item.getJob())).setScale(1, RoundingMode.FLOOR),
							BigDecimal.valueOf(item.getClassification()).setScale(1, RoundingMode.FLOOR));
				} catch (IOException e) {
					log.error(":: Error al escribir el archivo " + PATH_CUSTOMERS_LOGISTIC_REGRESSION_CSV_FILE, e);
				}
			});
		}
	}


	private int getSex(String sex) {
		int result = 0;
		if (sex.equals("M")) {
			result = 1;
		} else {
			result = 0;
		}
		return result;
	}

	
	private int getIdJob(String job) {
		final List<String> jobs = new ArrayList<>();
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
		int result = 0;
		for (int index = 0; index < jobs.size(); index++) {
			if (jobs.get(index).equals(job)) {
				result = index + 1;
				break;
			}
		}
		return result;
	}
	
	@Override
	public void watchLogisticRegressionFiles() throws IOException, InterruptedException {
		log.info(":: Service watchLogisticRegressionFiles ");
		Path faxFolder = Paths.get(BASIS_LOGISTIC_REGRESSION_DIRECTORY);
		WatchService watchService = FileSystems.getDefault().newWatchService();
		faxFolder.register(watchService, StandardWatchEventKinds.ENTRY_CREATE);
		StringTokenizer token;
		String fileName;
		String tokenOption;
		String classificationFile = null;
		String confusionFile = null;
		boolean valid = true;

		do {
			WatchKey watchKey = watchService.take();
			for (WatchEvent<?> event : watchKey.pollEvents()) {
				if (StandardWatchEventKinds.ENTRY_CREATE.equals(event.kind())) {

					fileName = event.context().toString();
					log.info("**** Logistic Regression file messages {} ",
							BASIS_LOGISTIC_REGRESSION_DIRECTORY + SEPARATOR_PATH + fileName);
					token = new StringTokenizer(fileName, "_");
					tokenOption = token.hasMoreTokens() ? token.nextToken() : "";

					if (tokenOption.equals("classification")) {
						classificationFile = fileName;
					} else if (tokenOption.equals("confusion")) {
						confusionFile = fileName;
					}

					if (BooleanUtils.negate(classificationFile == null) && BooleanUtils.negate(confusionFile == null)) {
						processLogisticRegressionFiles(
								BASIS_LOGISTIC_REGRESSION_DIRECTORY + SEPARATOR_PATH + confusionFile,
								BASIS_LOGISTIC_REGRESSION_DIRECTORY + SEPARATOR_PATH + classificationFile);
						classificationFile = null;
						confusionFile = null;
					}

				}
			}
			valid = watchKey.reset();
		} while (valid);
	}

	@Override
	public void processLogisticRegressionFiles(String pathConfusion, String pathCsvClassification) throws IOException {
		final Map<Integer, List<Integer>> mapProfile = new HashMap<>();
		final String[] headers = { "Id", "Age", "Sex", "Education Level", "State", "Marital Status", "Job" };
		final Iterable<CSVRecord> rows = utilService.readCsvFile(headers, CLASSIFICATION_NULL_PATH_FILE);
		final List<String> rowsClassification = utilService.readFile(pathCsvClassification);
		Optional<Customers> customer;
		List<Integer> recordClassification;
		int index = 0;
		Customers initialCustomer = null;
		for (final CSVRecord row : rows) {
			customer = customerRepository.findById(new BigDecimal(row.get("Id")).intValue());
			if (customer.isPresent()) {
				if (initialCustomer == null) {
					initialCustomer = customer.get();
				}
				customer.get().setClassification(new BigDecimal(rowsClassification.get(index)).intValue());
				index++;
				customerRepository.save(customer.get());
				recordClassification = mapProfile.get(customer.get().getIdprofile().getId());
				if (recordClassification == null) {
					recordClassification = new ArrayList<>();
					recordClassification.add(customer.get().getClassification());
					mapProfile.put(customer.get().getIdprofile().getId(), recordClassification);
				} else {
					recordClassification.add(customer.get().getClassification());
				}
			}
		}
		log.info(":: Map Profile {} ", mapProfile);

		ProfileRegression profileRegression;
		CustomerProfile idprofile;
		File image;
		for (final Integer idProfile : mapProfile.keySet()) {
			idprofile = new CustomerProfile();
			idprofile.setId(idProfile);
			profileRegression = profileRegressionRepository.findOneByIdprofileAndIsactive(idprofile, Boolean.TRUE);
			profileRegression.setInitialcustomer(initialCustomer);
			if (BooleanUtils.negate(profileRegression == null)) {
				profileRegression.setIsactive(Boolean.FALSE);
				profileRegressionRepository.save(profileRegression);
			}
			profileRegression = new ProfileRegression();
			profileRegression.setDate(formatDate.format(Calendar.getInstance().getTime()));
			profileRegression.setIdprofile(idprofile);
			try {
				image = new File(pathConfusion);
				profileRegression.setImageconfusion(Files.readAllBytes(image.toPath()));
			} catch (IOException e) {
				log.info(":: Error saving regression profile {} ", e);
			}
			profileRegression.setIsactive(Boolean.TRUE);
			profileRegression.setNumbercustomersleft(
					getNumberCustomersClassification(mapProfile.get(idProfile), LEFT_CLASSIFICATION));
			profileRegression.setNumbercustomersloyal(
					getNumberCustomersClassification(mapProfile.get(idProfile), LOYAL_CLASSIFICATION));
			profileRegressionRepository.save(profileRegression);
		}

		File file = new File(pathCsvClassification);
		file.delete();
		file = new File(pathConfusion);
		file.delete();
		log.info(":: Ends up logistic regression process ");
	}

	private int getNumberCustomersClassification(List<Integer> classificationProfile, int classification) {
		int result = 0;
		for (final Integer item : classificationProfile) {
			if (item.equals(classification)) {
				result++;
			}
		}
		return result;
	}

	
}

