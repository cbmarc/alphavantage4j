package co.alphavantage.api.connector;

import co.alphavantage.api.params.ApiParameter;
import co.alphavantage.exception.AlphaVantageException;

/**
 * Connection to api endpoint.
 */
@FunctionalInterface
public interface ApiConnector {
  /**
   * Sends request to api
   * @param apiParameters the api parameters (required/optional) to the api call
   * @return Either the raw Json string or IOExcpetion
   */
  String getRequest(ApiParameter... apiParameters) throws AlphaVantageException;
}
