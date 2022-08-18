package com.application.server.data;

import com.application.visitor.data.Visitor;
import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Getter
@Setter
@Table(name = "Address")
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;
    private  String streetNumber;
    private  String streetName;
    private  String suburbs;
    private  int postcode;
    private  String city;
    @OneToMany(mappedBy = "address", fetch = FetchType.LAZY)
    private List<Visitor> visitors = new ArrayList<>();
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Address that = (Address) o;
        return streetNumber != null && Objects.equals(streetNumber, that.streetNumber) &&
                streetName !=null && streetName.equalsIgnoreCase(that.streetName) &&
                suburbs != null  && suburbs.equalsIgnoreCase(that.suburbs);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}