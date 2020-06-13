package com.calpullix.db.process.service.impl;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;
import java.util.StringTokenizer;

import javax.imageio.ImageIO;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.calpullix.db.process.branch.model.Branch;
import com.calpullix.db.process.product.model.Product;
import com.calpullix.db.process.regression.model.ForecastSales;
import com.calpullix.db.process.regression.model.ForecastVariables;
import com.calpullix.db.process.regression.repository.ForecastSalesRepository;
import com.calpullix.db.process.regression.repository.ForecastVariablesRepository;
import com.calpullix.db.process.service.ForecastService;
import com.calpullix.db.process.util.service.UtilService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ForecastServiceImpl implements ForecastService {

	private static final String SALES_FORECAST_DIRECTORY_PATH = "/Users/juancarlospedrazaalcala/Documents/TT/Regression_OUTPUT";
		
	private static final String PATH_PRODUCT_BRANCH_SALES = "/Users/juancarlospedrazaalcala/Documents/TT/Regression_INPUT/product_branch_sales_";

	private static final String QUERY_PRODUCT_BRANCH_SALES = "SELECT YEAR(PB.saleDate) as year, MONTH(PB.saleDate) as month, COUNT(PB.id) as sales FROM Product_Branch as PB, Product_History as PH, Product as P WHERE PB.idBranch = ? AND "
			+ " PB.idProductHistory = PH.id AND PH.idProduct = P.id AND P.id = ? group by YEAR(PB.saleDate), MONTH(PB.saleDate) ORDER BY YEAR(PB.saleDate) ASC;";

	private static final String QUERY_PRODUCT_SALES_DATE = "SELECT COUNT(*) FROM Product_Branch as PB, Product_History as PH, Product as P WHERE PB.idProductHistory = PH.id AND PH.idProduct = P.id AND P.id = ? "
			+ "AND PB.idBranch = ? AND PB.saledate between ? AND ?;";

	private static final String CSV_EXTENSION = ".csv";
		
	private static final String SEPARATOR_PATH = "/";

	private static final String PIPE_SEPARATOR = "|";

	private static final String UNDERSCORE_SEPARATOR = "_";

	private static final int IMG_WIDTH = 400;

	private static final int IMG_HEIGHT = 200;

	@Autowired
	private ForecastSalesRepository forecastSalesRepository;

	@Autowired
	private ForecastVariablesRepository forecastVariablesRepository;

	@Autowired
	private UtilService utilService;

	@Autowired
	private JdbcTemplate jdbcTemplate;
	

	@SuppressWarnings("unused")
	private void processImages() {
		String pathFile = "/Users/juancarlospedrazaalcala/Documents/TT/CSV_Jupyetr/boxplot_two_branch_2_year_2019.png";
		String newPath = "/Users/juancarlospedrazaalcala/Documents/TT/CSV_Jupyetr/boxplot_two_branch_2_year_2019_COPY.png";
		try {
			processImage(pathFile, newPath, "png");
		} catch (IOException e) {
			log.error(":: Process Image Error ", e);
		}
		File image = new File(newPath);
		log.info(":: File exists {} ", image.exists());
	}

	private boolean processImage(final String pathImage, final String newPath, final String imageExtension)
			throws IOException {
		log.info(":: Resize Image ");
		final BufferedImage originalImage = ImageIO.read(new File(pathImage));
		int type = originalImage.getType() == 0 ? BufferedImage.TYPE_BYTE_BINARY : originalImage.getType();
		final BufferedImage resizeImageJpg = resizeImage(originalImage, type);
		ImageIO.write(resizeImageJpg, imageExtension, new File(newPath));
		log.info(":: Type image {} ", type);
		return Boolean.TRUE;
	}

	private BufferedImage resizeImage(BufferedImage originalImage, int type) {
		BufferedImage resizedImage = new BufferedImage(IMG_WIDTH, IMG_HEIGHT, type);
		Graphics2D g = resizedImage.createGraphics();
		g.drawImage(originalImage, 0, 0, IMG_WIDTH, IMG_HEIGHT, null);
		g.dispose();
		return resizedImage;
	}

	@SuppressWarnings("unused")
	private void editFiles(String pathFile) {
		log.info(":: Edit file {} ", pathFile);
		File fileToBeModified = new File(pathFile);
		String oldContent = "";
		BufferedReader reader = null;
		FileWriter writer = null;

		try {
			reader = new BufferedReader(new FileReader(fileToBeModified));
			String line = reader.readLine();
			line = "";
			while (BooleanUtils.negate(line == null)) {
				if (BooleanUtils.negate(line.equals(""))) {
					oldContent = oldContent + line + System.lineSeparator();
				}
				line = reader.readLine();
			}
			writer = new FileWriter(fileToBeModified);
			writer.write(oldContent);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				reader.close();
				if (BooleanUtils.negate(writer == null)) {
					writer.close();
				}
			} catch (IOException e) {
				log.error(":: Error edit file {} ", e);
			}
		}
	}

	@Override
	public void createProductBranchSalesCSVFile() throws IOException {
		final List<Integer> products = new ArrayList<>();
		products.add(1);
		products.add(10);

		List<ProductBranchSalesDTO> result;
		for (final Integer product : products) {
			result = jdbcTemplate.query(QUERY_PRODUCT_BRANCH_SALES, new Object[] { 1, product },
					new ProductBranchSalesRowMapper());
			writeFileBranchProductSales(result, PATH_PRODUCT_BRANCH_SALES + product + CSV_EXTENSION);
		}
	}

	private void writeFileBranchProductSales(List<ProductBranchSalesDTO> fileList, String pathFile) throws IOException {
		final String[] headers = { "Month", "Sales" };
		File file = new File(pathFile);
		file.delete();
		FileWriter out = new FileWriter(pathFile);
		try (CSVPrinter printer = new CSVPrinter(out, CSVFormat.DEFAULT.withHeader(headers))) {
			fileList.forEach(item -> {
				try {
					printer.printRecord(item.getYear() + "-" + item.getMonth(), item.getSales());
				} catch (IOException e) {
					log.error(":: Error writing file " + pathFile, e);
				}
			});
		}
	}
	
	@Override
	public void watchSalesForecastMessagesFiles() throws IOException, InterruptedException {
		Path faxFolder = Paths.get(SALES_FORECAST_DIRECTORY_PATH);
		WatchService watchService = FileSystems.getDefault().newWatchService();
		faxFolder.register(watchService, StandardWatchEventKinds.ENTRY_CREATE);
		boolean valid = true;

		String fileName;
		String tokenOption;
		String fileExpectedSales = null;
		String fileForecastSales = null;
		String fileSalesResults = null;
		String fileAutocorrelation = null;
		String fileHistogram = null;
		String filePlot = null;
		String fileVariablesResults = null;
		String fileStationary = null;
		StringTokenizer token;

		do {
			WatchKey watchKey = watchService.take();
			for (WatchEvent<?> event : watchKey.pollEvents()) {
				if (StandardWatchEventKinds.ENTRY_CREATE.equals(event.kind())) {
					fileName = event.context().toString();
					log.info("**** Sales forecast file {} ", SALES_FORECAST_DIRECTORY_PATH + fileName);
					token = new StringTokenizer(fileName, "_");
					tokenOption = token.hasMoreTokens() ? token.nextToken() : "";
					log.info(":: Token name message {} ", tokenOption);

					if (tokenOption.equals("expected")) {
						fileExpectedSales = fileName;
					} else if (tokenOption.equals("forecast")) {
						fileForecastSales = fileName;
					} else if (tokenOption.equals("autocorrelation")) {
						fileAutocorrelation = fileName;
					} else if (tokenOption.equals("histogram")) {
						fileHistogram = fileName;
					} else if (tokenOption.equals("plot")) {
						filePlot = fileName;
					} else if (tokenOption.equals("variableresults")) {
						fileVariablesResults = fileName;
					} else if (tokenOption.equals("results")) {
						fileSalesResults = fileName;
					} else if (tokenOption.equals("stationary")) {
						fileStationary = fileName;
					}

					if (BooleanUtils.negate(fileExpectedSales == null) && BooleanUtils.negate(fileForecastSales == null)
							&& BooleanUtils.negate(fileSalesResults == null)
							&& BooleanUtils.negate(fileAutocorrelation == null)
							&& BooleanUtils.negate(fileHistogram == null) && BooleanUtils.negate(filePlot == null)
							&& BooleanUtils.negate(fileVariablesResults == null)
							&& BooleanUtils.negate(fileStationary == null)) {

						processForecastFiles(SALES_FORECAST_DIRECTORY_PATH + SEPARATOR_PATH + fileExpectedSales,
								SALES_FORECAST_DIRECTORY_PATH + SEPARATOR_PATH + fileForecastSales, fileSalesResults,
								SALES_FORECAST_DIRECTORY_PATH + SEPARATOR_PATH + fileAutocorrelation,
								SALES_FORECAST_DIRECTORY_PATH + SEPARATOR_PATH + fileHistogram,
								SALES_FORECAST_DIRECTORY_PATH + SEPARATOR_PATH + filePlot,
								SALES_FORECAST_DIRECTORY_PATH + SEPARATOR_PATH + fileVariablesResults,
								SALES_FORECAST_DIRECTORY_PATH + SEPARATOR_PATH + fileStationary);

						fileExpectedSales = null;
						fileForecastSales = null;
						fileSalesResults = null;
						fileAutocorrelation = null;
						fileHistogram = null;
						filePlot = null;
						fileVariablesResults = null;
						fileStationary = null;

					}
				}
			}
			valid = watchKey.reset();
		} while (valid);
	}

	@SuppressWarnings("unused")
	private void processForecastFiles(String fileExpectedSales, String fileForecastSales, String fileSalesResults,
			String fileAutocorrelation, String fileHistogram, String filePlot, String fileVariablesResults,
			String fileStationary) throws IOException {
		ForecastSales forecastSales = new ForecastSales();
		List<String> lines = utilService.readFile(fileVariablesResults);

		StringTokenizer token = new StringTokenizer(lines.get(0), PIPE_SEPARATOR);

		forecastSales.setBestarima(token.hasMoreTokens() ? token.nextToken() : "");
		forecastSales.setRmsetraining(
				token.hasMoreTokens() ? new BigDecimal(token.nextToken()).setScale(2, RoundingMode.HALF_EVEN)
						: BigDecimal.ZERO);
		forecastSales.setRmseprediction(
				token.hasMoreTokens() ? new BigDecimal(token.nextToken()).setScale(2, RoundingMode.HALF_EVEN)
						: BigDecimal.ZERO);

		token = new StringTokenizer(fileSalesResults, UNDERSCORE_SEPARATOR);
		token.nextToken();
		final Branch idBranch = new Branch();
		idBranch.setId(token.hasMoreTokens() ? Integer.valueOf(token.nextToken()) : 0);
		forecastSales.setIdbranch(idBranch);
		final Product idProduct = new Product();
		idProduct.setId(token.hasMoreTokens() ? Integer.valueOf(token.nextToken()) : 0);
		forecastSales.setIdproduct(idProduct);
		forecastSales.setYear(token.hasMoreTokens() ? Integer.valueOf(token.nextToken()) : 0);

		Optional<ForecastSales> forecast = forecastSalesRepository.findOneByIdbranchAndIdproductAndYearAndIsactive(idBranch,
				idProduct, forecastSales.getYear(), Boolean.TRUE);
		if (forecast.isPresent()) {
			forecast.get().setIsactive(Boolean.FALSE);
			forecastSalesRepository.save(forecast.get());
		}

		File image;
		try {
			image = new File(fileAutocorrelation);
			forecastSales.setImageautocorrelation(Files.readAllBytes(image.toPath()));
			image = new File(fileHistogram);
			forecastSales.setImagehistogram(Files.readAllBytes(image.toPath()));
			image = new File(filePlot);
			forecastSales.setImageplot(Files.readAllBytes(image.toPath()));
			image = new File(SALES_FORECAST_DIRECTORY_PATH + SEPARATOR_PATH + fileSalesResults);
			forecastSales.setImageresults(Files.readAllBytes(image.toPath()));
			image = new File(fileStationary);
			forecastSales.setImagestationary(Files.readAllBytes(image.toPath()));
		} catch (IOException e) {
			log.error(":: Error image sales forecast ", e);
		}
		forecastSales.setIsactive(Boolean.TRUE);
		forecastSales = forecastSalesRepository.save(forecastSales);

		ForecastVariables forecastVariables;
		List<String> linesExpected = utilService.readFile(fileExpectedSales);
		List<String> linesPredicted = utilService.readFile(fileForecastSales);
		List<Integer> salesProduct;
		Calendar date = Calendar.getInstance();
		String startDate;
		String endDate;
		int month = Calendar.getInstance().get(Calendar.MONTH);
		int index = 0;
		for (final String linePredicted : linesPredicted) {
			forecastVariables = new ForecastVariables();
			if (index <= month) {
				date.set(Calendar.MONTH, index);
				startDate = forecastSales.getYear() + "-" + (index + 1) + "-"
						+ Calendar.getInstance().getMinimum(Calendar.DATE);
				endDate = forecastSales.getYear() + "-" + (index + 1) + "-"
						+ Calendar.getInstance().getMaximum(Calendar.DATE);

				salesProduct = jdbcTemplate.query(QUERY_PRODUCT_SALES_DATE,
						new Object[] { idProduct.getId(), idBranch.getId(), startDate, endDate },
						new ProductSalesRowMapper());
				
			} else {
				forecastVariables.setExpected(null);
			}
			forecastVariables.setIdforecastsales(forecastSales);
			forecastVariables.setPrediction(new BigDecimal(linePredicted).setScale(2, RoundingMode.HALF_EVEN));
			forecastVariables.setExpected(new BigDecimal((linesExpected.get(index))).intValue());
			index++;
			forecastVariablesRepository.save(forecastVariables);
		}

		utilService.moveFile(fileAutocorrelation);
		utilService.moveFile(fileExpectedSales);
		utilService.moveFile(fileForecastSales);
		utilService.moveFile(fileHistogram);
		utilService.moveFile(filePlot);
		utilService.moveFile(SALES_FORECAST_DIRECTORY_PATH + SEPARATOR_PATH + fileSalesResults);
		utilService.moveFile(fileStationary);
		utilService.moveFile(fileVariablesResults);

	}

}
