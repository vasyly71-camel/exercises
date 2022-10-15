package xyz.vasyly.camel.exercises;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.apache.camel.builder.endpoint.EndpointRouteBuilder;

@ApplicationScoped
public class Source extends EndpointRouteBuilder {

  @Inject
  DlqProcessor dlqProcessor;

  @Inject
  Sink sink;

  @Override
  public void configure() throws Exception {

    errorHandler(deadLetterChannel(dlqProcessor.getEndPointUri()));

    rest("/").id("/")

        .post("record").id("record")
        .apiDocs(true).id("apiDocs")
        .consumes("application/json")
        .produces("application/json")
        .to("log:xyz.vasyly.source?showAll=true&multiline=true").id("log")
        .to(sink.getEndpoint().getUri())

        .post("error").id("error")
        .apiDocs(true).id("apiDocs")
        .consumes("application/json")
        .produces("application/json")
        .to("direct:" + ProcessorWithError.PROCESSOR_WITH_ERROR).id("to" + ProcessorWithError.PROCESSOR_WITH_ERROR)
        .to(sink.getEndpoint().getUri());
  }

}
