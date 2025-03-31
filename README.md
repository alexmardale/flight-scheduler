# flight-scheduler

To execute application run one of the following:

`./mvnw exec:java -D"exec.mainClass"="com.example.flight_scheduler.FlightSchedulerApplication"`

```
docker build -t flight-scheduler .
docker run -p 127.0.0.1:8080:8080 flight-scheduler
```

`docker compose up --build`

To access the Swagger UI, go to:
http://localhost:8080/swagger-ui/index.html