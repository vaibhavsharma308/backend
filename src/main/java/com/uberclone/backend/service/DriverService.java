package com.uberclone.backend.service;

import com.uberclone.backend.model.Driver;
import org.springframework.stereotype.Service;

@Service
public class DriverService {


    public Driver workHardToFindBestDriver() {
        return new Driver("123",
                "John Doe",4.9) ;
    }
}
