package com.application.server.data;
import com.application.student.data.Student;
import lombok.*;
import org.hibernate.Hibernate;
import javax.persistence.*;
import java.util.Objects;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name ="ResidenceRegister" )
public class ResidenceRegister {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private  int floor;
    private  String flat;
    private  String room;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name = "residenceId", referencedColumnName = "id"),

    })
    private  Residence residence;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name = "studentNumber",referencedColumnName = "studentNumber"),
    })
    private Student student;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        ResidenceRegister residenceRegister = (ResidenceRegister) o;
        return id!=null && Objects.equals(id, residenceRegister.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
