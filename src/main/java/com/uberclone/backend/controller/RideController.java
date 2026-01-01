package com.uberclone.backend.controller;

import com.uberclone.backend.service.DriverService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import com.uberclone.backend.model.Driver;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RideController {
    private final DriverService driverService;
    public RideController(DriverService driverService) {
        this.driverService = driverService;
    }
    @GetMapping("/")
    public String home() {
        return "RideWave Backend is running 🚀";
    }
    @GetMapping("/getDriver")
    public Driver getDriver() {
        System.out.println("test");
        return driverService.workHardToFindBestDriver();
    }
}

