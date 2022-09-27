package com.application.register.data;

import com.application.visitor.data.Visitor;
import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.time.LocalTime;
import java.util.Objects;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Getter
@Setter
@Table(name = "VisitorsRegister")
public class VisitorsRegister {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;
    @ManyToOne
    @JoinColumn(name="visitorsId", referencedColumnName="id")
    private Visitor visitor;
    @Column(name = "signinTime")
    private LocalTime signInTime ;
    @Column(name = "signoutTime")
    private  LocalTime signOutTime;
    @Column(name = "signingStatus")
    private  String signingStatus;
    @ManyToOne
    @JoinColumn(name="registerId", referencedColumnName="id")
    private  Register register;
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        VisitorsRegister that = (VisitorsRegister) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "Time in: "+signInTime+", Time out: "+signOutTime+", status: "+signingStatus +", "+register.getHostStudent()+
                ", "+visitor;
    }
}
