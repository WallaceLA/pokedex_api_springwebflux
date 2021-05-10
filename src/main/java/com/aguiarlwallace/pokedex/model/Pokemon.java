package com.aguiarlwallace.pokedex.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Data
@AllArgsConstructor
public class Pokemon {

    @Id
    private String id;
    private String name;
    private String[] type;
    private String[] abilities;
    private double weight;
    private double height;

}
