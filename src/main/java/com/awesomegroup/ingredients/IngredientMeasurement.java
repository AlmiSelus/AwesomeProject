package com.awesomegroup.ingredients;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Michal on 2017-04-18.
 */
public enum IngredientMeasurement {
    ALL(0, "ALL"), TBLSPOON(1, "tbl spoon"), SPOON(2, "spoon"),
    PINCH(3, "pinch"), ML(4, "ml"), MG(5, "mg"),
    G(6, "g"), L(7, "l"), BREAST(8, "breast"), TBSP (9, "tbsp"),
    TSP(10, "tsp"), CUPS (11, "cups"), CUP (12, "cup"), TEASPOON (13, "teaspoon"), COUNT(14, "COUNT");

    private long measurementID;
    private String measurementName;

    IngredientMeasurement(long measurementID, String measurementName) {
        this.measurementID = measurementID;
        this.measurementName = measurementName;
    }

    public long getMeasurementID() {
        return measurementID;
    }

    public String getMeasurementName() {
        return measurementName;
    }

    public static List<IngredientMeasurement> except(IngredientMeasurement... measurements) {
        List<IngredientMeasurement> toFilter = Arrays.asList(measurements);
        return Arrays.stream(values()).filter(measurement -> !toFilter.contains(measurement)).collect(Collectors.toList());
    }

    @Override
    public String toString() {
        return "IngredientMeasurement{" +
                "measurementID=" + measurementID +
                ", measurementName='" + measurementName + '\'' +
                '}';
    }
}
