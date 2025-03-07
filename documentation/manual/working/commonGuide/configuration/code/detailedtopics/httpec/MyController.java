/*
 * Copyright (C) from 2022 The Play Framework Contributors <https://github.com/playframework>, 2011-2021 Lightbend Inc. <https://www.lightbend.com>
 */

package detailedtopics.httpec;

// #http-execution-context
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import javax.inject.Inject;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.*;

public class MyController extends Controller {

  private HttpExecutionContext httpExecutionContext;

  @Inject
  public MyController(HttpExecutionContext ec) {
    this.httpExecutionContext = ec;
  }

  public CompletionStage<Result> index() {
    // Use a different task with explicit EC
    return calculateResponse()
        .thenApplyAsync(
            answer -> {
              return ok("answer was " + answer).flashing("info", "Response updated!");
            },
            httpExecutionContext.current());
  }

  private static CompletionStage<String> calculateResponse() {
    return CompletableFuture.completedFuture("42");
  }
}
// #http-execution-context
