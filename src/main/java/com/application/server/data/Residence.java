package com.application.server.data;
import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Residence implements Serializable {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    @Column(name = "residenceName")
    private String residenceName;
    private String blocks;
    private  int numberFloors ;
    private int numberFlats;
    private int numberRoom;
    private  int capacity;
    @OneToOne(fetch = FetchType.LAZY,optional = false,orphanRemoval = true,cascade = {CascadeType.MERGE,CascadeType.PERSIST})
    @JoinColumns({
            @JoinColumn(name = "addressId", referencedColumnName = "id"),
    })
    private Address address;
    @OneToOne(fetch = FetchType.LAZY,optional = false,orphanRemoval = true,cascade = {CascadeType.PERSIST})
    @JoinColumn(name = "residenceRules", referencedColumnName = "id")
    private ResidenceRules residenceRules;
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Residence residence = (Residence) o;
        return id !=null && Objects.equals(id,residence.id)&&
                residenceName != null && Objects.equals(residenceName, residence.residenceName)
                && blocks != null && Objects.equals(blocks, residence.blocks);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id,residenceName, blocks);
    }
}
