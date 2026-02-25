package com.github.myfreeit;

import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class SensorEmulator {
    public static void main(String[] args) {

        WebClient webClient = WebClient.builder()
                .baseUrl("http://localhost:8080")
                .build();

        List<String> sensors = List.of("sensor-1", "sensor-2", "sensor-3");

        Flux.fromIterable(sensors)
                .flatMap(sensorName -> {
                    SensorDto sensor = new SensorDto(sensorName);

                    Mono<Void> registration = webClient.post()
                            .uri("/sensors/registration")
                            .bodyValue(sensor)
                            .retrieve()
                            .bodyToMono(String.class)
                            .doOnNext(resp -> System.out.printf("%s registered: %s%n", sensorName, resp))
                            .doOnError(WebClientResponseException.class, ex -> {
                                if (ex.getStatusCode().value() == 400) {
                                    System.out.printf("%s already exists, continuing...%n", sensorName);
                                } else {
                                    System.err.printf("Registration error for %s: %s%n", sensorName, ex.getMessage());
                                }
                            })
                            .onErrorResume(WebClientResponseException.class, ex -> Mono.empty())
                            .then();

                    Flux<String> measurements = Flux.range(1, 1000)
                            .map(i -> new MeasurementDto(
                                    BigDecimal.valueOf(ThreadLocalRandom.current().nextDouble(-20.0, 40.0))
                                            .setScale(2, RoundingMode.HALF_UP)
                                            .doubleValue(),
                                    ThreadLocalRandom.current().nextBoolean(),
                                    sensor
                            ))
                            .flatMap(dto -> webClient.post()
                                            .uri("/measurements/add")
                                            .bodyValue(dto)
                                            .retrieve()
                                            .bodyToMono(String.class)
                                            .doOnNext(resp -> System.out.printf("%s measurement sent: %s%n", sensorName, resp))
                                            .onErrorResume(WebClientResponseException.class, ex -> {
                                                System.err.printf("Measurement error for %s: %s%n", sensorName, ex.getMessage());
                                                return Mono.empty();
                                            }),
                                    20
                            );

                    return registration.thenMany(measurements);
                })
                .doOnComplete(() -> System.out.println("All sensors finished sending measurements."))
                .blockLast();
    }
}