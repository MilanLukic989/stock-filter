
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MakeMeRich {
	public static final List<String> symbols = Arrays.asList("AMD", "HPQ", "IBM", "TXN", "VMW", "XRX", "AAPL", "ADBE",
			"AMZN", "CRAY", "CSCO", "SNE", "GOOG", "INTC", "INTU", "MSFT", "ORCL", "TIBX", "VRSN", "YHOO");

	private static String rootURL = "https://www.alphavantage.co/query";
	private static String API_KEY = "LN9GE2EHPSRVTYL8";

	public static void main(String[] args) throws IOException {

		getCryptoData();

		// 1. Print these symbols using a Java 8 for-each and lambdas
		symbols.stream().forEach((sym) -> System.out.print(sym + " "));
		System.out.println("");

		// 2. Use the StockUtil class to print the price of Bitcoin
//		System.out.println("\nBTC-USD Price: " + StockUtil.getPrice("BTC-USD"));
		System.out.println("\nBTC-USD Price: " + BitcoinPrice());
		System.out.println("");

		// 3. Create a new List of StockInfo that includes the stock price
		List<StockInfo> stockPrice = StockUtil.prices.entrySet().stream()
				.map(x -> new StockInfo(x.getKey(), x.getValue())).collect(Collectors.toList());
		stockPrice.forEach(System.out::println);
		System.out.println("");

		// 4. Find the highest-priced stock under $500
		StockInfo highest = stockPrice.stream().filter(StockUtil.isPriceLessThan(500)).reduce(StockUtil::pickHigh)
				.get();
		System.out.println("The highest-priced stock under $500 is " + highest);

		// 1. Adding Copyright
		String canonicalPath = new java.io.File("src").getCanonicalPath();
		File dir = new File(canonicalPath);
		String[] extensions = new String[] { "java" };
		String myCopyright = "// Copyright MilanLukic, 2019";

		List<File> files = (List<File>) FileUtils.listFiles(dir, extensions, true);
		for (File sourceFile : files) {
//					System.out.println("file: " + sourceFile.getCanonicalPath());
			String fileContent = FileUtils.readFileToString(sourceFile);
			if (!fileContent.endsWith(myCopyright)) {
				FileUtils.writeStringToFile(sourceFile, "\n" + myCopyright, true);
			}
		}

	}

	public static void getCryptoData() throws IOException {

		String urlAddr = rootURL + "?function=BATCH_STOCK_QUOTES";
		urlAddr += "&apikey=" + API_KEY;
		urlAddr += "&symbols=";
		for (int i = 0; i < symbols.size(); i++) {
			if (i == symbols.size() - 1) {
				urlAddr += symbols.get(i);
			} else {
				urlAddr += symbols.get(i) + ",";
			}
		}

		URL request = new URL(urlAddr);
		InputStream openStream = request.openStream();
		String response = IOUtils.toString(openStream);

		JSONObject root = new JSONObject(response);
		JSONArray stockQuotes = (JSONArray) root.get("Stock Quotes");
		for (int i = 0; i < stockQuotes.length(); i++) {
			JSONObject jsonObj = (JSONObject) stockQuotes.get(i);
			StockInfo si = new StockInfo(jsonObj.getString("1. symbol"), jsonObj.getDouble("2. price"));
			StockUtil.addStock(si);
		}

//		for(String symbol : symbols) {
//			if(StockUtil.doesExist(symbol)) {
//				StockInfo siTmp = StockUtil.getPrice(symbol);
//				System.out.println(siTmp);	
//			}
//		}

	}

	private static double BitcoinPrice() throws IOException{
		String urlAddr = rootURL + "?function=CURRENCY_EXCHANGE_RATE";
		urlAddr += "&apikey=" + API_KEY;
		urlAddr += "&from_currency=BTC";
		urlAddr += "&to_currency=USD";

		URL request = new URL(urlAddr);
		InputStream openStream = request.openStream();
		String response = IOUtils.toString(openStream);

		JSONObject root = new JSONObject(response);
		JSONObject exchangeRate = root.getJSONObject("Realtime Currency Exchange Rate");
		return exchangeRate.getDouble("5. Exchange Rate");

	}
}

// Copyright MilanLukic, 2019