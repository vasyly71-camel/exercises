package xyz.vasyly.camel.exercises;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.apache.camel.builder.endpoint.EndpointRouteBuilder;
import org.apache.camel.builder.endpoint.dsl.DirectEndpointBuilderFactory.DirectEndpointBuilder;

@ApplicationScoped
public class Sink extends EndpointRouteBuilder {

  @Inject
  DlqProcessor dlqProcessor;

  @Override
  public void configure() throws Exception {
    errorHandler(noErrorHandler());
    from(getEndpoint())
        .to(log(Sink.class.getName()).showAll(true).multiline(true))
        .throwException(AppException.class, "Sink exception");
  }

  public DirectEndpointBuilder getEndpoint() {
    return direct("sink");
  }

}
