package xyz.vasyly.camel.exercises;

import javax.enterprise.context.ApplicationScoped;

import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.endpoint.EndpointRouteBuilder;
import org.apache.camel.builder.endpoint.dsl.DirectEndpointBuilderFactory.DirectEndpointBuilder;
import org.apache.camel.builder.endpoint.dsl.GoogleSheetsEndpointBuilderFactory.GoogleSheetsEndpointBuilder;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@ApplicationScoped
public class DlqProcessor extends EndpointRouteBuilder {

  private static final String DLQ_ROUTE = "dlqRoute";

  @ConfigProperty(name = "app.dlq.max.redeliveries", defaultValue = "2")
  int maxRedeliveries;

  @ConfigProperty(name = "app.dlq.max.redelivery.delay", defaultValue = "30000")
  long maxRedeliverrDelay;

  @ConfigProperty(name = "app.dlq.redelivery.delay", defaultValue = "5000")
  long redeliveryDelay;

  @ConfigProperty(name = "app.dlq.redelivery.backOff.multiplier", defaultValue = "1.5")
  double backoffMultiplier;

  @Override
  public void configure() throws Exception {
    onException(Throwable.class)
        .maximumRedeliveries(maxRedeliveries)
        .maximumRedeliveryDelay(null)
        .redeliveryDelay(redeliveryDelay)
        .backOffMultiplier(backoffMultiplier)
        .retriesExhaustedLogLevel(LoggingLevel.ERROR);

    from(this.getEndPoint())
        .to(log(DlqProcessor.class.getName()));
  }

  public DirectEndpointBuilder getEndPoint() {
    return direct(DLQ_ROUTE);
  }

  public String getEndPointUri() {
    return getEndPoint().getUri();
  }

}
