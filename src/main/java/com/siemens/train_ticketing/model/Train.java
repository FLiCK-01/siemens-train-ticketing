package com.siemens.train_ticketing.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Train {
    private String id;
    private String name;
    private int capacity;
}
