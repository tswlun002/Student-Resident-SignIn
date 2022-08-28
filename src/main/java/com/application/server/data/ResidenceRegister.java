package com.application.server.data;
import com.application.student.data.Student;
import lombok.*;
import org.hibernate.Hibernate;
import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@ToString
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
    @OneToOne
    @JoinColumns({
            @JoinColumn(name = "residenceId", referencedColumnName = "id"),
            @JoinColumn(name = "blocks",referencedColumnName = "blocks"),
            @JoinColumn(name = "residence",referencedColumnName = "residenceName")
    })
    private  Residence residence;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "StudentsOfResidence",
            joinColumns = {@JoinColumn(name="Id")},
            inverseJoinColumns =  {@JoinColumn(name = "studentId",referencedColumnName = "studentNumber"),
                    @JoinColumn(name ="studentName", referencedColumnName = "fullname")}


    )
    private Set<Student> students = new HashSet<>();

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
