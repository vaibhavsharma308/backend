package com.uberclone.backend.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Driver {
    private String id;
    private String name;
    private double rating;
}