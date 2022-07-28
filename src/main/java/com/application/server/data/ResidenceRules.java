package com.application.server.data;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.time.LocalTime;
import java.util.Objects;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
@Getter
@Setter
@ToString
@Table(name = "ResidenceRules", uniqueConstraints = @UniqueConstraint(columnNames = {"id"}))
public class ResidenceRules {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;
    @NonNull
    @Column(name = "signingInTime", nullable = false)
    private LocalTime startSigningTime;
    @Column(name = "signingOutTime", nullable = true)
    private  LocalTime endSigningTime;
    @Column(name = "maximumVisitors",nullable = false)
    private  int numberVisitor;
    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "residence", referencedColumnName = "residenceName"),
            @JoinColumn(name = "blocks", referencedColumnName = "blocks")
    })
    private Residence residence;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        ResidenceRules that = (ResidenceRules) o;
        return id != null && Objects.equals(id, that.id) &&
                residence.residenceName !=null &&
                Objects.equals(residence.getResidenceName(),that.residence.getResidenceName()) &&
                residence.getBlocks() !=null &&
                Objects.equals(residence.getBlocks(),that.residence.getBlocks());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
