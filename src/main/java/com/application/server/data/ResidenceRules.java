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
    @Column(name = "signing_in_time", nullable = false)
    private LocalTime startSigningTime;
    @Column(name = "signing_out_time", nullable = true)
    private  LocalTime endSigningTime;
    @Column(name = "maximum_visitor_per_host",nullable = false)
    private  int numberVisitor;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        ResidenceRules that = (ResidenceRules) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
