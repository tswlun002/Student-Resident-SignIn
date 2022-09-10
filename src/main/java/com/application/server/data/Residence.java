package com.application.server.data;
import lombok.*;
import org.hibernate.Hibernate;
import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

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
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name = "addressId", referencedColumnName = "id"),
    })
    private Address address;
    @OneToOne(fetch = FetchType.LAZY,optional = false,orphanRemoval = true,cascade = {CascadeType.PERSIST})
    @JoinColumn(name = "residenceRules", referencedColumnName = "id")
    private ResidenceRules residenceRules;
    @ManyToOne
    @JoinColumn(name="residence_departmentId",referencedColumnName = "id")
    private ResidenceDepartment department;
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Residence residence = (Residence) o;
        return residenceName != null && residenceName.equalsIgnoreCase(residence.residenceName)
                && blocks != null && blocks.equalsIgnoreCase(residence.blocks);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id,residenceName, blocks);
    }
}
