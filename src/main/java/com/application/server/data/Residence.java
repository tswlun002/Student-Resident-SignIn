package com.application.server.data;
import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.Objects;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@ToString
@IdClass(ResidencePrimaryKey.class)
public class Residence {

    @Id
    @Column(name = "residence_name")
    String residenceName;
    @Id
    private String blocks;
    private  int numberFloors ;
    private int numberFlats;
    private int numberRoom;
    private  int capacity;
    @ManyToOne
    @JoinColumn(name = "rules_id")
    private ResidenceRules residenceRules;



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Residence residence = (Residence) o;
        return residenceName != null && Objects.equals(residenceName, residence.residenceName)
                && blocks != null && Objects.equals(blocks, residence.blocks);
    }

    @Override
    public int hashCode() {
        return Objects.hash(residenceName, blocks);
    }
}
