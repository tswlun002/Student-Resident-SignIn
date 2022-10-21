package com.application.server.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.Objects;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Getter
@ToString
@Setter
@Table(name = "Address")
@JsonIgnoreProperties({"hibernateLazyInitializer","handler"})
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;
    @NonNull
    private  String streetNumber;
    @NonNull
    private  String streetName;
    @NonNull
    private  String suburbs;
    @NonNull
    private  int postcode;
    @NonNull
    private  String city;
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Address that = (Address) o;
        return Objects.equals(streetNumber, that.streetNumber) &&
                streetName.equalsIgnoreCase(that.streetName) &&
                suburbs.equalsIgnoreCase(that.suburbs);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}