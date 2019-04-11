package pl.setblack.fibo;


import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Controller
public class FibController {

    @RequestMapping(value="/fib/{n}",method= RequestMethod.GET)
    @ResponseBody
    public String fib(@PathVariable("n") int n) {

        if (n < 2) {
            return String.valueOf(n);
        } else {
            Mono<Integer> n_1 = WebClient.create("http://localhost:8080")
                    .get().uri("/fib/{n}", (n - 1))
                    .accept(MediaType.TEXT_HTML)
                    .exchange().flatMap(resp -> resp.bodyToMono(String.class))
                    .map(Integer::parseInt);
            Mono<Integer> n_2 = WebClient.create("http://localhost:8080")
                    .get().uri("/fib/{n}", (n - 2))
                    .accept(MediaType.TEXT_HTML)
                    .exchange().flatMap(resp -> resp.bodyToMono(String.class))
                    .map(Integer::parseInt);

            Integer a = n_1.block(Duration.ofMinutes(1));
            Integer b = n_2.block(Duration.ofMinutes(1));

            return  String.valueOf(a + b);
        }
    }
}
