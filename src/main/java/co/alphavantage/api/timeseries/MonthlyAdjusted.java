package co.alphavantage.api.timeseries;

import co.alphavantage.api.parser.TimeSeriesParser;
import co.alphavantage.api.timeseries.data.StockData;
import co.alphavantage.exception.AlphaVantageException;
import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MonthlyAdjusted {

  private final Map<String, String> metaData;
  private final List<StockData> stocks;

  private MonthlyAdjusted(Map<String, String> metaData, List<StockData> stocks) {
    this.metaData = metaData;
    this.stocks = stocks;
  }

  public Map<String, String> getMetaData() {
    return metaData;
  }

  public List<StockData> getStockData() {
    return stocks;
  }

  public static MonthlyAdjusted from(String json) throws AlphaVantageException {
    Parser parser = new Parser();
    return parser.parseJson(json);  }

  private static class Parser extends TimeSeriesParser<MonthlyAdjusted> {

    @Override
    protected String getStockDataKey() {
      return "Monthly Adjusted Time Series";
    }

    @Override
    protected MonthlyAdjusted resolve(Map<String, String> metaData,
                            Map<String, Map<String, String>> stockData) throws AlphaVantageException {
      List<StockData> stocks = new ArrayList<>();
      try {
        stockData.forEach((key, values) -> stocks.add(new StockData(
                DateTime.parse(key, SIMPLE_DATE_FORMAT),
                Double.parseDouble(values.get("1. open")),
                Double.parseDouble(values.get("2. high")),
                Double.parseDouble(values.get("3. low")),
                Double.parseDouble(values.get("4. close")),
                Double.parseDouble(values.get("5. adjusted close")),
                Long.parseLong(values.get("6. volume")),
                Double.parseDouble(values.get("7. dividend amount"))

        )));
      } catch (Exception e) {
        throw new AlphaVantageException("Monthly adjusted api change", e);
      }
      return new MonthlyAdjusted(metaData, stocks);
    }
  }
}
