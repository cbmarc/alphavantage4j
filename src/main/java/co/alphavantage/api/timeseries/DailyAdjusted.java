package co.alphavantage.api.timeseries;

import co.alphavantage.api.parser.TimeSeriesParser;
import co.alphavantage.api.timeseries.data.StockData;
import co.alphavantage.exception.AlphaVantageException;
import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DailyAdjusted {

  private final Map<String, String> metaData;
  private final List<StockData> stocks;

  private DailyAdjusted(Map<String, String> metaData, List<StockData> stocks) {
    this.metaData = metaData;
    this.stocks = stocks;
  }

  public Map<String, String> getMetaData() {
    return metaData;
  }

  public List<StockData> getStockData() {
    return stocks;
  }

  public static DailyAdjusted from(String json) throws AlphaVantageException {
    Parser parser = new Parser();
    return parser.parseJson(json);
  }

  private static class Parser extends TimeSeriesParser<DailyAdjusted> {

    @Override
    protected String getStockDataKey() {
      return "Time Series (Daily)";
    }

    @Override
    protected DailyAdjusted resolve(Map<String, String> metaData,
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
                Double.parseDouble(values.get("7. dividend amount")),
                Double.parseDouble(values.get("8. split coefficient"))

        )));
      } catch (Exception e) {
        throw new AlphaVantageException("Daily adjusted api change", e);
      }
      return new DailyAdjusted(metaData, stocks);
    }
  }
}
