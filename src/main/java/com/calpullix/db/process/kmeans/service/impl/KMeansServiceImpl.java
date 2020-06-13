package com.calpullix.db.process.kmeans.service.impl;

import java.io.File;
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.calpullix.db.process.customer.model.CustomerProfile;
import com.calpullix.db.process.customer.model.Customers;
import com.calpullix.db.process.customer.repository.CustomerProfileRepository;
import com.calpullix.db.process.customer.repository.CustomerRepository;
import com.calpullix.db.process.kmeans.service.KMeansService;
import com.calpullix.db.process.profile.model.ProfileKmeans;
import com.calpullix.db.process.profile.repository.ProfileKMeansRepository;
import com.calpullix.db.process.util.service.UtilService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class KMeansServiceImpl implements KMeansService {

	private static final String BASE_KMEANS_DIRECTORY = "/Users/juancarlospedrazaalcala/Documents/TT/CSV_KMeans/";

	private static final String CSV_EXTENSION_NO_FULLSTOP = "csv";

	private static final String PNG_EXTENSION_NO_FULL_STOP = "png";

	@Autowired
	private CustomerRepository customerRepository;

	@Autowired
	private CustomerProfileRepository customerProfileRepository;

	@Autowired
	private ProfileKMeansRepository profileKMeansRepository;

	@Autowired
	private UtilService utilService; 
	
	private SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	@Override
	public void watchKMeansFiles() throws IOException, InterruptedException {
		Path faxFolder = Paths.get(BASE_KMEANS_DIRECTORY);
		WatchService watchService = FileSystems.getDefault().newWatchService();
		faxFolder.register(watchService, StandardWatchEventKinds.ENTRY_CREATE);
		boolean valid = true;
		String pathCsv = null;
		String pathImage = null;
		do {
			WatchKey watchKey = watchService.take();
			for (WatchEvent<?> event : watchKey.pollEvents()) {
				if (StandardWatchEventKinds.ENTRY_CREATE.equals(event.kind())) {
					String fileName = event.context().toString();
					log.info(":: Extension K-Means file {} ",
							fileName.substring(fileName.length() - 3, fileName.length()));
					if (fileName.substring(fileName.length() - 3, fileName.length()).equals(CSV_EXTENSION_NO_FULLSTOP)) {
						pathCsv = fileName;
					} else if (fileName.substring(fileName.length() - 3, fileName.length()).equals(PNG_EXTENSION_NO_FULL_STOP)) {
						pathImage = fileName;
					}
					log.info(":: K-Means file created: {} ", BASE_KMEANS_DIRECTORY + fileName);
				}
			}
			if (StringUtils.isNotEmpty(pathCsv) && StringUtils.isNotEmpty(pathImage)) {
				log.info(":: Files to Send {} {} ", BASE_KMEANS_DIRECTORY + pathCsv, BASE_KMEANS_DIRECTORY + pathImage);
				processKmeans(BASE_KMEANS_DIRECTORY + pathCsv, BASE_KMEANS_DIRECTORY + pathImage);
				pathCsv = null;
				pathImage = null;
			}
			valid = watchKey.reset();
		} while (valid);
	}

	@Override
	public void processKmeans(String pathFile, String pathImage) {

		final List<Customers> customers = customerRepository.findAll();
		final List<Integer> index = new ArrayList<Integer>();
		index.add(0);
		final List<List<String>> rows = utilService.processCsvFileByIndex(pathFile, index);
		List<String> row;
		Optional<CustomerProfile> profile;
		int i = 0;
		int countLow = 0, countAverage = 0, countVip = 0, countPremium = 0;

		for (final Customers customer : customers) {
			row = rows.get(i);
			profile = customerProfileRepository.findById(Integer.valueOf(row.get(0)) + 1);
			if (profile.isPresent()) {
				customer.setIdprofile(profile.get());
				customerRepository.save(customer);
			}
			if (profile.isPresent() && profile.get().getId()
					.equals(com.calpullix.db.process.catalog.model.CustomerProfile.LOW.getId())) {
				countLow++;
			} else if (profile.isPresent() && profile.get().getId()
					.equals(com.calpullix.db.process.catalog.model.CustomerProfile.AVERAGE.getId())) {
				countAverage++;
			} else if (profile.isPresent() && profile.get().getId()
					.equals(com.calpullix.db.process.catalog.model.CustomerProfile.VIP.getId())) {
				countVip++;
			} else {
				countPremium++;
			}
			i++;
		}

		ProfileKmeans kMeans = new ProfileKmeans();
		profile = customerProfileRepository
				.findById(com.calpullix.db.process.catalog.model.CustomerProfile.LOW.getId());
		kMeans.setIdprofile(profile.get());
		kMeans.setColor("indigo");
		kMeans.setDate(formatDate.format(Calendar.getInstance().getTime()));
		File image;
		try {
			image = new File(pathImage);
			kMeans.setImage(Files.readAllBytes(image.toPath()));
		} catch (IOException e) {
			e.printStackTrace();
		}
		kMeans.setIsactive(Boolean.TRUE);
		kMeans.setNumbercustomers(countLow);
		profileKMeansRepository.save(kMeans);

		kMeans = new ProfileKmeans();
		profile = customerProfileRepository
				.findById(com.calpullix.db.process.catalog.model.CustomerProfile.AVERAGE.getId());
		kMeans.setIdprofile(profile.get());
		kMeans.setColor("cornflowerblue");
		kMeans.setDate(formatDate.format(Calendar.getInstance().getTime()));
		try {
			image = new File(pathImage);
			kMeans.setImage(Files.readAllBytes(image.toPath()));
		} catch (IOException e) {
			e.printStackTrace();
		}
		kMeans.setIsactive(Boolean.TRUE);
		kMeans.setNumbercustomers(countAverage);
		profileKMeansRepository.save(kMeans);

		kMeans = new ProfileKmeans();
		profile = customerProfileRepository
				.findById(com.calpullix.db.process.catalog.model.CustomerProfile.VIP.getId());
		kMeans.setIdprofile(profile.get());
		kMeans.setColor("limegreen");
		kMeans.setDate(formatDate.format(Calendar.getInstance().getTime()));
		try {
			image = new File(pathImage);
			kMeans.setImage(Files.readAllBytes(image.toPath()));
		} catch (IOException e) {
			e.printStackTrace();
		}
		kMeans.setIsactive(Boolean.TRUE);
		kMeans.setNumbercustomers(countVip);
		profileKMeansRepository.save(kMeans);

		kMeans = new ProfileKmeans();
		profile = customerProfileRepository
				.findById(com.calpullix.db.process.catalog.model.CustomerProfile.PREMIUM.getId());
		kMeans.setIdprofile(profile.get());
		kMeans.setColor("yellow");
		kMeans.setDate(formatDate.format(Calendar.getInstance().getTime()));
		try {
			image = new File(pathImage);
			kMeans.setImage(Files.readAllBytes(image.toPath()));
		} catch (IOException e) {
			e.printStackTrace();
		}
		kMeans.setIsactive(Boolean.TRUE);
		kMeans.setNumbercustomers(countPremium);
		profileKMeansRepository.save(kMeans);

		utilService.moveFile(pathFile);
		utilService.moveFile(pathImage);
	}

	
}
