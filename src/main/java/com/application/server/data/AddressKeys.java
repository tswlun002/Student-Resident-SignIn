package com.application.server.data;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class AddressKeys implements Serializable {
    private  String streetNumber;
    private  String streetName;

    public String getSuburbs() {
        return suburbs;
    }

    public void setSuburbs(String suburbs) {
        this.suburbs = suburbs;
    }

    private  String suburbs;

    public String getStreetNumber() {
        return streetNumber;
    }

    public void setStreetNumber(String streetNumber) {
        this.streetNumber = streetNumber;
    }

    public String getStreetName() {
        return streetName;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }
}