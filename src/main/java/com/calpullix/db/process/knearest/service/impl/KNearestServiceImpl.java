package com.calpullix.db.process.knearest.service.impl;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.StringTokenizer;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.calpullix.db.process.customer.model.CustomerProfile;
import com.calpullix.db.process.customer.model.Customers;
import com.calpullix.db.process.customer.repository.CustomerRepository;
import com.calpullix.db.process.knearest.service.KNearestService;
import com.calpullix.db.process.profile.model.ProfileKnearest;
import com.calpullix.db.process.profile.repository.ProfileKNearestRepository;
import com.calpullix.db.process.util.service.UtilService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class KNearestServiceImpl implements KNearestService {

	private static final String BASE_KNEAREST_DIRECTORY = "/Users/juancarlospedrazaalcala/Documents/TT/CSV_KNearest/";

	@Autowired
	private ProfileKNearestRepository profileKNearestRepository;

	@Autowired
	private CustomerRepository customerRepository;

	@Autowired
	private UtilService utilService;
	
	private SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	
	@Override
	public void createCustomersKNearestCSVFileIdprofileNull() {
		String pathCsv = "/Users/juancarlospedrazaalcala/Documents/TT/CSV_MYSQL/customers_knearest_2020_04_10.csv";

		String[] headersCsv = { "Id", "Age", "State", "Education Level", "Job", "Income", "Debt", "Marital Status", "Id Profile" };

		try {
			createCustomersKNearestCSVFileIdprofileNull(headersCsv, pathCsv);
		} catch (IOException e) {
			log.error(":: Error creating customers csv ", e);
		}
	}
	
	@Override
	public void createCustomersKNearestCSVFile() {
		final String pathCsv = "/Users/juancarlospedrazaalcala/Documents/TT/CSV_MYSQL/customers_knearest_classified_2020_04_10.csv";

		final String[] headersCsv = { "Id", "Age", "State", "Education Level", "Job", "Income", "Debt", "Marital Status", "Id Profile" };

		try {
			createCustomersKNearestCSVFile(headersCsv, pathCsv);
		} catch (IOException e) {
			log.error(":: Error creating customers csv ", e);
		}
	}
	
	
	@Override
	public void createCustomersKNearestCSVFileIdprofileNull(String[] headers, String path) throws IOException {
		File fileToDelete = new File(path);
		fileToDelete.delete();
		log.info(":: Writing file {} {} ", path, headers);
		final List<Customers> customers = customerRepository.findAllCustomersByIdprofileIsNull();
		log.info(":: Customers Profile null {} ", customers.size());
		FileWriter out = new FileWriter(path);
		try (CSVPrinter printer = new CSVPrinter(out, CSVFormat.DEFAULT.withHeader(headers))) {
			customers.forEach(item -> {
				try {
					printer.printRecord(item.getId(), item.getAge(), item.getState().getId(),
							item.getEducationlevel().getId(), item.getJob(), item.getIncome(), item.getDebt(),
							item.getMaritalstatus().getId(), 0);
				} catch (IOException e) {
					log.error(":: Error al escribir el archivo " + path, e);
				}
			});
		}
		log.info(":: Ends up writing file K-Nearest {} ", path);
	}

	@Override
	public void createCustomersKNearestCSVFile(String[] headers, String path) throws IOException {
		File fileToDelete = new File(path);
		fileToDelete.delete();
		log.info(":: Writing file {} {} ", path, headers);
		final List<Customers> customers = customerRepository.findAllCustomersByIdprofileIsNotNull();
		log.info(":: Customers profile classified {} ", customers.size());
		FileWriter out = new FileWriter(path);
		try (CSVPrinter printer = new CSVPrinter(out, CSVFormat.DEFAULT.withHeader(headers))) {
			customers.forEach(item -> {
				try {
					printer.printRecord(item.getId(), item.getAge(), item.getState().getId(),
							item.getEducationlevel().getId(), item.getJob(), item.getIncome(), item.getDebt(),
							item.getMaritalstatus().getId(), item.getIdprofile().getId());
				} catch (IOException e) {
					log.error(":: Error al escribir el archivo " + path, e);
				}
			});
		}
		log.info(":: Ends up writing file K-Nearest classified {} ", path);
	}

	@Override
	public void watchKNearestFiles() throws IOException, InterruptedException {
		Path faxFolder = Paths.get(BASE_KNEAREST_DIRECTORY);
		WatchService watchService = FileSystems.getDefault().newWatchService();
		faxFolder.register(watchService, StandardWatchEventKinds.ENTRY_CREATE);
		boolean valid = true;
		String fileName;
		String fileClassification = null;
		String fileIdCustomers = null;
		String fileGraphic = null;
		String fileKValues = null;
		String tokenOption;
		StringTokenizer token;
		do {
			WatchKey watchKey = watchService.take();
			for (WatchEvent<?> event : watchKey.pollEvents()) {
				if (StandardWatchEventKinds.ENTRY_CREATE.equals(event.kind())) {
					fileName = event.context().toString();
					log.info("**** K-Nearest file messages {} ", BASE_KNEAREST_DIRECTORY + fileName);
					token = new StringTokenizer(fileName, "_");
					tokenOption = token.hasMoreTokens() ? token.nextToken() : "";
					if (tokenOption.equals("classification")) {
						fileClassification = fileName;
					} else if (tokenOption.equals("idcustomer")) {
						fileIdCustomers = fileName;
					} else if (tokenOption.equals("accuracy")) {
						fileGraphic = fileName;
					} else if (tokenOption.equals("kvalue")) {
						fileKValues = fileName;
					}
					if (BooleanUtils.negate(fileClassification == null) && BooleanUtils.negate(fileIdCustomers == null) &&
							BooleanUtils.negate(fileGraphic == null) && BooleanUtils.negate(fileKValues == null)) {
						processKNearest(BASE_KNEAREST_DIRECTORY + fileClassification,
								BASE_KNEAREST_DIRECTORY + fileIdCustomers, BASE_KNEAREST_DIRECTORY + fileGraphic,
								BASE_KNEAREST_DIRECTORY + fileKValues);
						fileClassification = null;
						fileIdCustomers = null;
						fileGraphic = null;
						fileKValues = null;
					}
				}
			}
			valid = watchKey.reset();
		} while (valid);
	}
	
	@Override
	public void processKNearest(String pathClassificationFile, String pathIdCustomersFile,
			String pathKNearestAccuracyImage, String pathKValuesFile) throws IOException {
		log.info(":: Processing K-Nearest {} {} {} ", pathClassificationFile, pathIdCustomersFile,
				pathKNearestAccuracyImage);
		final List<String> idCustomer = utilService.readFile(pathIdCustomersFile);
		final List<String> profiles = utilService.readFile(pathClassificationFile);
		Optional<Customers> customer;
		CustomerProfile idprofile;
		int index = 0;
		for (final String id : idCustomer) {
			customer = customerRepository.findById(Integer.valueOf(id));
			if (customer.isPresent()) {
				idprofile = new CustomerProfile();
				idprofile.setId(Integer.valueOf(profiles.get(index)));
				customer.get().setIdprofile(idprofile);
				customerRepository.save(customer.get());
			}
			index++;
		}
		final Map<String, Integer> mapProfile = new HashMap<>();
		Integer profileCounter;
		for (final String profile : profiles) {
			profileCounter = mapProfile.get(profile);
			if (profileCounter == null) {
				mapProfile.put(profile, 1);
			} else {
				profileCounter++;
				mapProfile.put(profile, profileCounter);
			}
		}
		final List<String> kValues = utilService.readFile(pathKValuesFile);
		ProfileKnearest kNearest;
		File image;
		StringTokenizer token;
		for (final String profile : mapProfile.keySet()) {
			idprofile = new CustomerProfile();
			idprofile.setId(Integer.valueOf(profile));
			kNearest = profileKNearestRepository.findOneByIdprofileAndIsactive(idprofile, Boolean.TRUE);
			if (BooleanUtils.negate(kNearest == null)) {
				kNearest.setIsactive(Boolean.FALSE);
				profileKNearestRepository.save(kNearest);
			}
			kNearest = new ProfileKnearest();
			kNearest.setDate(formatDate.format(Calendar.getInstance().getTime()));
			kNearest.setIdprofile(idprofile);
			try {
				image = new File(pathKNearestAccuracyImage);
				kNearest.setImage(Files.readAllBytes(image.toPath()));
			} catch (IOException e) {
				log.error(":: Error persisting image " + pathKNearestAccuracyImage, e);
			}
			kNearest.setIsactive(Boolean.TRUE);
			kNearest.setNumbercustomers(mapProfile.get(profile));
			token = new StringTokenizer(kValues.get(0), ",");
			kNearest.setKaccuracy(token.hasMoreTokens() ? token.nextToken() : "");
			kNearest.setKneighborg(token.hasMoreTokens() ? token.nextToken() : "");
			profileKNearestRepository.save(kNearest);
		}

		utilService.moveFile(pathClassificationFile);
		utilService.moveFile(pathIdCustomersFile);
		utilService.moveFile(pathKNearestAccuracyImage);
		utilService.moveFile(pathKValuesFile);

		log.info(":: Ends up K-Nearest processing ");
	}


	
}
