package io.duto.springbootexample.router;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

import java.util.Map;

@Component
public class WebRequestHandler {
    public Mono<ServerResponse> index(ServerRequest request) {
        return HttpClient.create()
                .baseUrl("https://api.hangang.msub.kr/")
                .headers(header -> {
                    header.add("Content-Type", "*/*");
                })
                .secure()
                .get()
                .responseSingle((httpClientResponse, byteBufMono) -> {
                    var code = httpClientResponse.status().code();
                    if(code / 100 != 2) { // 2xx가 아닐 시
                        return Mono.error(new RuntimeException(String.valueOf(code)));
                    }
                    return byteBufMono.asString();
                })
                .retry(1)
                .flatMap(body -> Mono.fromSupplier(ObjectMapper::new)
                        .map(mapper -> {
                            try {
                                return mapper
                                        .readTree(body)
                                        .get("temp")
                                        .asText();
                            } catch (JsonProcessingException e) {
                                e.printStackTrace();
                                return "0.0";
                            }
                        }))
                .map(temp -> Map.of("temperature", temp))
                .flatMap(data ->
                        ServerResponse.ok()
                                .contentType(MediaType.TEXT_HTML)
                                .render("index", data));
    }
}
