# 🌡️ Sensor Emulator

![Java](https://img.shields.io/badge/Java-22-orange?logo=openjdk)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.0.2-brightgreen?logo=springboot)
![Maven](https://img.shields.io/badge/Maven-Build-blue?logo=apachemaven)
![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)

A tutorial project to emulate weather sensors that send data to the
main [Meteo API](https://github.com/MyFreeIT/meteoapi.git).

---

## 🚀 Technologies

- ⚡ Java 22 — actual version of the platform
- 🌱 Spring Boot 4.0.2 (WebFlux) — asynchronous HTTP client
- 📦 Maven — building the project
- 🧩 Jackson Databind — DTO serialization
- ✅ Validation — input data checking

---

## 📡 Functional

- Registers sensors (sensor-1, sensor-2, sensor-3)
- Generates random temperature and precipitation values
- Sends measurements asynchronously (up to 20 requests simultaneously)
- Stops after all data has been sent

---

## 🛠️ Launching

### Prerequisite

The emulator requires the [Meteo API](https://github.com/MyFreeIT/meteoapi) to be running locally at 👉 http://localhost:8080.  
Make sure the API service is started before launching the emulator, otherwise requests will fail.


### Assembly

```bash
mvn clean install
```

### Launching the emulator

```bash
java -jar target/sensor-emulator-1.0-SNAPSHOT.jar
```

The emulator will send data to the API at:  
👉 http://localhost:8080

---

## 📜 License

This project is licensed under the **MIT** License.  
Author: **Denis Odesskiy (MyFreeIT)**  
Website: [myfreeit.github.io](https://myfreeit.github.io/)  
See the full license text in the [LICENSE](./LICENSE) file.