package co.alphavantage.api.timeseries;

import co.alphavantage.api.parser.TimeSeriesParser;
import co.alphavantage.api.timeseries.data.StockData;
import co.alphavantage.api.timeseries.params.Interval;
import co.alphavantage.exception.AlphaVantageException;
import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class IntraDay {
  private final Map<String, String> metaData;
  private final List<StockData> stocks;

  private IntraDay(Map<String, String> metaData, List<StockData> stocks) {
    this.metaData = metaData;
    this.stocks = stocks;
  }

  public Map<String, String> getMetaData() {
    return metaData;
  }

  public List<StockData> getStockData() {
    return stocks;
  }

  public static IntraDay from(Interval interval, String json) throws AlphaVantageException {
    Parser parser = new Parser(interval);
    return parser.parseJson(json);
  }

  private static class Parser extends TimeSeriesParser<IntraDay> {
    private final Interval interval;

    Parser(Interval interval) {
      this.interval = interval;
    }

    @Override
    protected String getStockDataKey() {
      return "Time Series (" + interval.getValue() + ")";
    }

    @Override
    protected IntraDay resolve(Map<String, String> metaData,
                     Map<String, Map<String, String>> stockData) throws AlphaVantageException {
      List<StockData> stocks = new ArrayList<>();
      try {
        stockData.forEach((key, values) -> stocks.add(new StockData(
                DateTime.parse(key, DATE_WITH_TIME_FORMAT),
                Double.parseDouble(values.get("1. open")),
                Double.parseDouble(values.get("2. high")),
                Double.parseDouble(values.get("3. low")),
                Double.parseDouble(values.get("4. close")),
                Long.parseLong(values.get("5. volume"))
        )));
      } catch (Exception e) {
        throw new AlphaVantageException("Intraday api change", e);
      }
      return new IntraDay(metaData, stocks);
    }
  }

}
