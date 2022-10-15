package xyz.vasyly.camel.exercises;

import javax.enterprise.context.ApplicationScoped;

import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.endpoint.EndpointRouteBuilder;

@ApplicationScoped
public class ProcessorWithError extends EndpointRouteBuilder {

  static final String PROCESSOR_WITH_ERROR = "processorWithError";

  @Override
  public void configure() throws Exception {

    onException(AppException.class)
    .log(LoggingLevel.ERROR, ProcessorWithError.class.getName(), "Caught exception ${exception}")
    .handled(true);

    from(direct(PROCESSOR_WITH_ERROR))
        .throwException(AppException.class, "Test exception");
  }

}
