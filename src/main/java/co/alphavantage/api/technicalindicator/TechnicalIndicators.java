package co.alphavantage.api.technicalindicator;

import co.alphavantage.api.connector.ApiConnector;
import co.alphavantage.api.params.Symbol;
import co.alphavantage.api.technicalindicator.params.Function;
import co.alphavantage.api.technicalindicator.params.Interval;
import co.alphavantage.api.technicalindicator.params.SeriesType;
import co.alphavantage.api.technicalindicator.params.TimePeriod;
import co.alphavantage.exception.AlphaVantageException;

/**
 * Technical indicator values are updated realtime: the latest data point is derived from the current trading day of a given equity.
 */
public class TechnicalIndicators {
  private final ApiConnector apiConnector;
  /**
   * Constructs a Technical Indicator Data api endpoint with the help of an {@link ApiConnector}
   * @param apiConnector the connection to the api
   */
  public TechnicalIndicators(ApiConnector apiConnector) {
    this.apiConnector = apiConnector;
  }

  public SMA sma(String symbol, Interval interval, TimePeriod timePeriod, SeriesType seriesType) throws AlphaVantageException {
    String json = apiConnector.getRequest(new Symbol(symbol), Function.SMA, timePeriod, interval, seriesType);
    return SMA.from(json);
  }
}
