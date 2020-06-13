package com.calpullix.db.process.util.service.impl;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Service;

import com.calpullix.db.process.util.service.UtilService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UtilServiceImpl implements UtilService {

	private SimpleDateFormat formatTimestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:SS.sss");

	private static final String BACKUP_PATH = "/Users/juancarlospedrazaalcala/Documents/TT/BACK_UP/";

	@Override
	public List<List<String>> processCsvFileByIndex(String pathFile, List<Integer> index) {
		log.info(":: Reading CSV file: {} ", pathFile);
		final Iterable<CSVRecord> csvFile = readCsvFileByIndex(pathFile);
		List<List<String>> result = new ArrayList<>();
		List<String> row;
		for (final CSVRecord record : csvFile) {
			row = new ArrayList<>();
			for (final Integer i : index) {
				row.add(record.get(i));
			}
			result.add(row);
		}
		return result;
	}

	@Override
	public Iterable<CSVRecord> readCsvFileByIndex(String path) {
		Reader in;
		Iterable<CSVRecord> records = null;
		try {
			in = new FileReader(path);
			records = CSVFormat.DEFAULT.withIgnoreEmptyLines().parse(in);
		} catch (Exception e) {
			log.error(":: Error csv read file " + path, e);
		}
		return records;
	}

	@Override
	public List<List<String>> processCsvFile(String[] headers, String pathFile) {
		log.info(":: Reading CSV file: {} ", pathFile);
		final Iterable<CSVRecord> csvFile = readCsvFile(headers, pathFile);
		List<List<String>> result = new ArrayList<>();
		List<String> row;
		for (final CSVRecord record : csvFile) {
			row = new ArrayList<>();
			for (final String nameColumn : headers) {
				row.add(record.get(nameColumn));
			}
			result.add(row);
		}
		return result;
	}

	@Override
	public List<String> readFile(String pathFile) throws IOException {
		Path path = Paths.get(pathFile);
		return Files.readAllLines(path);
	}

	@Override
	public Iterable<CSVRecord> readCsvFile(String[] headers, String path) {
		Reader in;
		Iterable<CSVRecord> records = null;
		try {
			in = new FileReader(path);
			records = CSVFormat.DEFAULT.withHeader(headers).withFirstRecordAsHeader().withIgnoreEmptyLines().parse(in);
		} catch (Exception e) {
			log.error(":: Error csv read file " + path, e);
		}
		return records;
	}

	@Override
	public void moveFile(String pathFile) {
		File newFile = new File(pathFile + "_" + formatTimestamp.format(Calendar.getInstance().getTime()));
		File file = new File(pathFile);
		file.renameTo(newFile);
		try {
			log.info("****  New PATH {} {} ", newFile, BACKUP_PATH + newFile.getName());
			Files.move(Paths.get(newFile.getAbsolutePath()), Paths.get(BACKUP_PATH + newFile.getName()),
					StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			log.error(":: Error moving file ", e);
		}
	}

}
