
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class MakeMeRich {
	public static final List<String> symbols = Arrays.asList("AMD", "HPQ",
			"IBM", "TXN", "VMW", "XRX", "AAPL", "ADBE", "AMZN", "CRAY", "CSCO",
			"SNE", "GOOG", "INTC", "INTU", "MSFT", "ORCL", "TIBX", "VRSN",
			"YHOO");

	public static void main(String[] args) {

		// 1. Print these symbols using a Java 8 for-each and lambdas
		symbols.stream().forEach((sym) -> System.out.print(sym + " "));
		System.out.println("");
		
		// 2. Use the StockUtil class to print the price of Bitcoin
		System.out.println("\nBTC-USD Price: " + StockUtil.getPrice("BTC-USD"));
		System.out.println("");
		
		// 3. Create a new List of StockInfo that includes the stock price
		List<StockInfo> stockPrice = StockUtil.prices.entrySet().stream().map(x -> new StockInfo(x.getKey(), x.getValue())).collect(Collectors.toList());
		stockPrice.forEach(System.out::println);
		System.out.println("");
	 
		// 4. Find the highest-priced stock under $500
		        StockInfo highest = stockPrice.stream().filter(StockUtil.isPriceLessThan(500)).reduce(StockUtil::pickHigh).get();
		        System.out.println("The highest-priced stock under $500 is " + highest);
	
	}

}
