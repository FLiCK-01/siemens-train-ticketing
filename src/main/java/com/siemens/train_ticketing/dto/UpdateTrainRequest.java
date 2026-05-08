package com.siemens.train_ticketing.dto;

public record UpdateTrainRequest(
        String name,
        int capacity
) {}