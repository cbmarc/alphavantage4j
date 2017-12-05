package co.alphavantage.api.parser;

import co.alphavantage.exception.AlphaVantageException;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Map;

public abstract class TimeSeriesParser<Data> extends JsonParser<Data> {

  protected abstract Data resolve(Map<String, String> metaData,
                        Map<String, Map<String, String>> stockData) throws AlphaVantageException;

  protected abstract String getStockDataKey();

  @Override
  public Data resolve(JsonObject rootObject) throws AlphaVantageException {
    Type metaDataType = new TypeToken<Map<String, String>>() {
    }.getType();
    Type dataType = new TypeToken<Map<String, Map<String, String>>>() {
    }.getType();
    try {
      Map<String, String> metaData = GSON.fromJson(rootObject.get("Meta Data"), metaDataType);
      Map<String, Map<String, String>> stockData = GSON.fromJson(rootObject.get(getStockDataKey()), dataType);
      return resolve(metaData, stockData);
    } catch (JsonSyntaxException e) {
      throw new AlphaVantageException("time series api change", e);
    }
  }
}
