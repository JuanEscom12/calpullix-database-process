package com.calpullix.db.process.twitter.service.impl;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
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
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.StringTokenizer;

import javax.imageio.ImageIO;

import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.calpullix.db.process.catalog.model.TwitterTypeMessage;
import com.calpullix.db.process.twitter.model.Twitter;
import com.calpullix.db.process.twitter.model.TwitterMessages;
import com.calpullix.db.process.twitter.repository.TwitterMessagesRepository;
import com.calpullix.db.process.twitter.repository.TwitterRepository;
import com.calpullix.db.process.twitter.service.TwitterProcessService;
import com.calpullix.db.process.util.service.UtilService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TwitterProcessServiceImpl implements TwitterProcessService {

	private static final String BASE_TWITTER_DIRECTORY = "/Users/juancarlospedrazaalcala/Documents/TT/TwitterUser/";

	private static final String BASE_TWITTER_LABEL_DIRECTORY = "/Users/juancarlospedrazaalcala/Documents/TT/TwitterLabel/";

	private static final String CALPULLIX_TWITTERS_PATH = "/Users/juancarlospedrazaalcala/Documents/TT/twitters_calpullix.txt";

	private static final String STOP_WORDS_FILE = "/Users/juancarlospedrazaalcala/Documents/TT/StopWords.txt";

	private static final String PUNCTUATION_MARKS_REGEX = "[,.:!?)%$#|]";

	private static final String POSITIVE_LABEL = "positive";

	private static final String NEGATIVE_LABEL = "negative";

	private static final String NEUTRAL_LABEL = "neutral";

	@Autowired
	private TwitterRepository twitterRepository;

	@Autowired
	private TwitterMessagesRepository twitterMessagesRepository;

	@Autowired
	private UtilService utilService;

	private SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	@Override
	public void watchTwitterFiles() throws IOException, InterruptedException {
		Path faxFolder = Paths.get(BASE_TWITTER_DIRECTORY);
		WatchService watchService = FileSystems.getDefault().newWatchService();
		faxFolder.register(watchService, StandardWatchEventKinds.ENTRY_CREATE);
		boolean valid = true;
		do {
			WatchKey watchKey = watchService.take();
			for (WatchEvent<?> event : watchKey.pollEvents()) {
				if (StandardWatchEventKinds.ENTRY_CREATE.equals(event.kind())) {
					String fileName = event.context().toString();
					log.info(":: Twitter File Created: {} ", BASE_TWITTER_DIRECTORY + fileName);
					processTwitter(BASE_TWITTER_DIRECTORY, fileName);
				}
			}
			valid = watchKey.reset();
		} while (valid);
	}

	@Override
	public void watchTwitterMessagesFiles() throws IOException, InterruptedException {
		Path faxFolder = Paths.get(BASE_TWITTER_LABEL_DIRECTORY);
		WatchService watchService = FileSystems.getDefault().newWatchService();
		faxFolder.register(watchService, StandardWatchEventKinds.ENTRY_CREATE);
		boolean valid = true;
		String fileName;
		String fileMessages = null;
		String fileLabels = null;
		String fileCloudWords = null;
		String tokenOption;
		StringTokenizer token;
		do {
			WatchKey watchKey = watchService.take();
			for (WatchEvent<?> event : watchKey.pollEvents()) {
				if (StandardWatchEventKinds.ENTRY_CREATE.equals(event.kind())) {
					fileName = event.context().toString();
					log.info("**** Twitter file messages {} ", BASE_TWITTER_LABEL_DIRECTORY + fileName);
					token = new StringTokenizer(fileName, "_");
					tokenOption = token.hasMoreTokens() ? token.nextToken() : "";
					log.info(":: Token name message {} ", tokenOption);
					if (tokenOption.equals("messages")) {
						fileMessages = fileName;
					} else if (tokenOption.equals("nbresultlabels")) {
						fileLabels = fileName;
					} else if (tokenOption.equals("cloudwords")) {
						fileCloudWords = fileName;
					}
					if (BooleanUtils.negate(fileMessages == null) && BooleanUtils.negate(fileLabels == null)
							&& BooleanUtils.negate(fileCloudWords == null)) {
						processMessages(BASE_TWITTER_LABEL_DIRECTORY + fileMessages,
								BASE_TWITTER_LABEL_DIRECTORY + fileLabels,
								BASE_TWITTER_LABEL_DIRECTORY + fileCloudWords);
						fileMessages = null;
						fileLabels = null;
						fileCloudWords = null;
					}
				}
			}
			valid = watchKey.reset();
		} while (valid);
	}

	@Override
	public void processTwitter(String pathFile, String fileName) {
		try {
			log.info(":: File Twiiter {} {} ", pathFile, fileName);
			StringTokenizer token = new StringTokenizer(fileName, "_");
			if (BooleanUtils.negate(token.hasMoreTokens())
					|| BooleanUtils.negate(token.nextToken().equals("profile"))) {
				return;
			}
			Optional<Twitter> twitter = twitterRepository.findOneByIsactive(Boolean.TRUE);
			if (twitter.isPresent()) {
				twitter.get().setIsactive(Boolean.FALSE);
				twitterRepository.save(twitter.get());
			}

			Twitter twitterModel;
			List<String> rows = utilService.readFile(pathFile + fileName);
			File imageFile;
			BufferedImage image;
			URL url;
			for (final String row : rows) {
				token = new StringTokenizer(row, "****");
				twitterModel = new Twitter();
				twitterModel.setDate(formatDate.format(Calendar.getInstance().getTime()));
				twitterModel.setProfilename(token.nextToken());
				twitterModel.setProfilattname("@" + token.nextToken());
				twitterModel.setIsactive(Boolean.TRUE);
				url = new URL(token.nextToken());
				image = ImageIO.read(url);
				imageFile = new File(pathFile + "picture_profile.jpg");
				ImageIO.write(image, "jpg", imageFile);
				twitterModel.setProfilepicture(Files.readAllBytes(imageFile.toPath()));
				twitterRepository.save(twitterModel);
				imageFile.delete();
			}
			utilService.moveFile(pathFile + fileName);
		} catch (IOException e) {
			log.error(":: Error processing user twitter file ", e);
		}
	}

	@Override
	public void processMessages(String pathFileMessages, String pathFileLabels, String pathCloudWordsImage) {
		try {
			log.info(":: Twitter Messages {} {} {} ", pathFileMessages, pathFileLabels, pathCloudWordsImage);
			final List<String> rowsLabels = utilService.readFile(pathFileLabels);
			log.info(":: List label {} ", rowsLabels);
			final Map<String, List<Integer>> mapLabels = new HashMap<String, List<Integer>>();
			List<Integer> indexLabel;
			int index = 0;
			for (final String rowLabel : rowsLabels) {
				indexLabel = mapLabels.get(rowLabel);
				if (indexLabel == null) {
					indexLabel = new ArrayList<>();
					indexLabel.add(index);
					mapLabels.put(rowLabel, indexLabel);
				} else {
					indexLabel.add(index);
				}
				index++;
			}
			final List<String> rowsMessages = utilService.readFile(pathFileMessages);
			final List<String> rowsCalpulliMessages = utilService.readFile(CALPULLIX_TWITTERS_PATH);
			log.info(":: Map labels \n {} **** \n {} **** \n {} ", mapLabels, rowsMessages, rowsCalpulliMessages);

			Optional<Twitter> twitter = twitterRepository.findOneByIsactive(Boolean.TRUE);
			try {
				File image = new File(pathCloudWordsImage);
				twitter.get().setClowwords(Files.readAllBytes(image.toPath()));
				final List<String> mostRepeatedWords = getCloudWords(rowsCalpulliMessages);
				twitter.get().setKeywordone(mostRepeatedWords.get(0));
				twitter.get().setKeywordtwo(mostRepeatedWords.get(1));
				twitter.get().setKeywordthree(mostRepeatedWords.get(2));
				twitter.get().setKeywordfour(mostRepeatedWords.get(3));
				twitter.get().setKeywordfive(mostRepeatedWords.get(4));
				setPercentageClassifications(mapLabels, twitter.get());
				twitterRepository.save(twitter.get());
				saveMessages(mapLabels, rowsMessages, rowsCalpulliMessages, TwitterTypeMessage.NEUTRAL, twitter.get());
				saveMessages(mapLabels, rowsMessages, rowsCalpulliMessages, TwitterTypeMessage.NEGATIVE, twitter.get());
				saveMessages(mapLabels, rowsMessages, rowsCalpulliMessages, TwitterTypeMessage.POSITIVE, twitter.get());
			} catch (IOException e) {
				log.error(":: Error persisting image " + pathCloudWordsImage, e);
			}
			utilService.moveFile(pathFileMessages);
			utilService.moveFile(pathFileLabels);
			utilService.moveFile(pathCloudWordsImage);
		} catch (IOException e) {
			log.error(":: Error reading files " + pathFileMessages + ", " + pathFileLabels + "," + pathCloudWordsImage,
					e);
		}
	}

	private void setPercentageClassifications(Map<String, List<Integer>> mapLabels, Twitter twitter) {
		int totalLabels = 0;
		for (final String label : mapLabels.keySet()) {
			totalLabels += mapLabels.get(label).size();
		}
		twitter.setPercentagepositive(new BigDecimal(mapLabels.get(POSITIVE_LABEL).size())
				.divide(new BigDecimal(totalLabels)).multiply(BigDecimal.valueOf(100)).intValue());
		twitter.setPercentagenegative(new BigDecimal(mapLabels.get(NEGATIVE_LABEL).size())
				.divide(new BigDecimal(totalLabels)).multiply(BigDecimal.valueOf(100)).intValue());
		twitter.setPercentageneutral(new BigDecimal(mapLabels.get(NEUTRAL_LABEL).size())
				.divide(new BigDecimal(totalLabels)).multiply(BigDecimal.valueOf(100)).intValue());
	}

	private void saveMessages(Map<String, List<Integer>> mapLabels, List<String> rowsMessages,
			List<String> rowsCalpulliMessages, TwitterTypeMessage typeMessage, Twitter twitter) {
		TwitterMessages messageModel;
		StringTokenizer token;
		String rowMessage;
		for (final Integer itemLabel : mapLabels.get(typeMessage.name().toLowerCase())) {
			rowMessage = rowsMessages.get(itemLabel);
			token = new StringTokenizer(rowMessage, "****");
			messageModel = new TwitterMessages();
			messageModel.setUser(token.nextToken());
			messageModel.setAtuser("@" + token.nextToken());
			messageModel.setDate(token.nextToken());
			messageModel.setMessage(rowsCalpulliMessages.get(itemLabel));
			messageModel.setIdtwitter(twitter);
			messageModel.setTypemessage(typeMessage);
			twitterMessagesRepository.save(messageModel);
		}
	}

	@SuppressWarnings("unchecked")
	private List<String> getCloudWords(List<String> lines) throws IOException {
		final List<String> result = new ArrayList<>();
		final List<String> stopWords = Files.readAllLines(Paths.get(STOP_WORDS_FILE));

		final Map<String, Integer> mapWords = new HashMap<>();
		StringTokenizer token;
		String word;
		Integer counter;
		for (final String line : lines) {
			token = new StringTokenizer(line, " ");
			while (token.hasMoreTokens()) {
				word = token.nextToken();
				if (BooleanUtils.negate(stopWords.contains(word))) {
					word = word.replaceAll(PUNCTUATION_MARKS_REGEX, "");
					counter = mapWords.get(word);
					if (counter == null) {
						mapWords.put(word, 1);
					} else {
						++counter;
						mapWords.put(word, counter);
					}
				}
			}
		}
		log.info(":: Map Cloud-Words {} ", mapWords);
		Object[] orderMapValues = mapWords.entrySet().toArray();
		Arrays.sort(orderMapValues, new Comparator<Object>() {
			public int compare(Object o1, Object o2) {
				return ((Map.Entry<String, Integer>) o2).getValue()
						.compareTo(((Map.Entry<String, Integer>) o1).getValue());
			}
		});
		if (orderMapValues.length >= 5) {
			result.add(((Map.Entry<String, Integer>) orderMapValues[0]).getKey() + " ("
					+ ((Map.Entry<String, Integer>) orderMapValues[0]).getValue() + ")");
			result.add(((Map.Entry<String, Integer>) orderMapValues[1]).getKey() + " ("
					+ ((Map.Entry<String, Integer>) orderMapValues[1]).getValue() + ")");
			result.add(((Map.Entry<String, Integer>) orderMapValues[2]).getKey() + " ("
					+ ((Map.Entry<String, Integer>) orderMapValues[2]).getValue() + ")");
			result.add(((Map.Entry<String, Integer>) orderMapValues[3]).getKey() + " ("
					+ ((Map.Entry<String, Integer>) orderMapValues[3]).getValue() + ")");
			result.add(((Map.Entry<String, Integer>) orderMapValues[4]).getKey() + " ("
					+ ((Map.Entry<String, Integer>) orderMapValues[4]).getValue() + ")");
		}
		return result;
	}

	@Override
	public void setPercentages() throws IOException {
		final List<String> rowsLabels = utilService
				.readFile("/Users/juancarlospedrazaalcala/Documents/TT/TwitterLabel/nbresultlabels_twitter.csv");
		log.info(":: List label {} ", rowsLabels);
		final Map<String, List<Integer>> mapLabels = new HashMap<String, List<Integer>>();
		List<Integer> indexLabel;
		int index = 0;
		for (final String rowLabel : rowsLabels) {
			indexLabel = mapLabels.get(rowLabel);
			if (indexLabel == null) {
				indexLabel = new ArrayList<>();
				indexLabel.add(index);
				mapLabels.put(rowLabel, indexLabel);
			} else {
				indexLabel.add(index);
			}
			index++;
		}
		Optional<Twitter> twitter = twitterRepository.findById(2);
		if (twitter.isPresent()) {
			setPercentageClassifications(mapLabels, twitter.get());
		}

	}

}
