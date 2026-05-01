package com.hotel.msvhuespedes;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.hotel.msvhuespedes","com.fernando.commons" })
public class MsvHuespedesApplication {

    public static void main(String[] args) {
        SpringApplication.run(MsvHuespedesApplication.class, args);
    }

}
