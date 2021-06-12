package my.suhd.util;

import my.suhd.model.DriverAvailable;
import my.suhd.model.DriverEvent;
import my.suhd.model.Ride;
import my.suhd.model.RideCurrent;

import java.util.Date;
import java.util.Random;

public class DataGenerator {
    private static Random rand = new Random();
    private static long rideIterator = 0;
    private static long rideCurrentIterator = 0;
    private static long driverEventIterator = 0;

    public static DriverAvailable randomDriverAvailable() {
        int status = rand.nextInt(3);

        DriverAvailable.DriverAvailableBuilder builder =
                DriverAvailable.builder()
                        .driverId((long)rand.nextInt(10000000))
                        .lat(rand.nextFloat() * 90)
                        .lon(rand.nextFloat() * 180)
                        .status(status);

        switch (status) {
            case 1:
            case 2:
                builder = builder.rideId(Math.abs(rand.nextLong()));
                break;
        }

        return builder.build();
    }

    public static DriverEvent randomDriverEvent() {
        int status = rand.nextInt(3);

        DriverEvent.DriverEventBuilder builder =
                DriverEvent.builder()
                        .id(driverEventIterator)
                        .driverId((long)rand.nextInt(10000000))
                        .lat(rand.nextFloat() * 90)
                        .lon(rand.nextFloat() * 180)
                        .status(status)
                        .timePublish(System.currentTimeMillis());

        switch (status) {
            case 1:
                builder = builder.rideId(Math.abs(rand.nextLong()));
                break;
            case 2:
                builder =
                        builder
                                .rideId((long)rand.nextInt(10000000))
                                .distance(rand.nextInt(100000))
                                .duration(rand.nextInt(1000))
                                .cost(rand.nextInt(15000));
        }

        driverEventIterator++;
        return builder.build();
    }

    public static Ride randomRide() {
        Date dateStart = new Date();
        Long timeTook = (long) rand.nextInt(1000);

        Ride.RideBuilder builder =
                Ride.builder()
                        .id(rideIterator)
                        .passengerId((long)rand.nextInt(10000000))
                        .latStart(rand.nextFloat() * 90)
                        .lonStart(rand.nextFloat() * 180)
                        .latEnd(rand.nextFloat() * 90)
                        .lonEnd(rand.nextFloat() * 180)
                        .timePublish(dateStart.getTime() - 304)
                        .timeStart(dateStart.getTime())
                        .timeEnd(dateStart.getTime() + timeTook)
                        .timeTook(timeTook)
                        .driverId((long)rand.nextInt(10000000))
                        .distance(rand.nextInt(100000))
                        .cost(rand.nextInt(15000));

        rideIterator++;
        return builder.build();
    }

    public static RideCurrent randomRideCurrent() {
        Date dateStart = new Date();
        Long timeTook = (long) rand.nextInt(1000);

        RideCurrent.RideCurrentBuilder builder =
                RideCurrent.builder()
                        .rideId(rideCurrentIterator)
                        .passengerId((long)rand.nextInt(10000000))
                        .driverId((long)rand.nextInt(10000000))
                        .latStart(rand.nextFloat() * 90)
                        .lonStart(rand.nextFloat() * 180)
                        .latEnd(rand.nextFloat() * 90)
                        .lonEnd(rand.nextFloat() * 180)
                        .timePublish(dateStart.getTime())
                        .timeTook(timeTook);

        rideCurrentIterator++;
        return builder.build();
    }
}

