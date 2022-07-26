package com.application.server.data;
import com.application.student.data.Student;
import com.application.register.data.RegisterEntity;
import lombok.*;
import org.hibernate.Hibernate;
import javax.persistence.*;
import java.util.Objects;

@Getter
@Setter
@ToString
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResidentStudent {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private  int floor;
    private  String flat;
    private  String room;
    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "blocks",referencedColumnName = "blocks"),
            @JoinColumn(name = "residence_name",referencedColumnName = "residence_name")
    })
    private  Residence residence;

    @ManyToOne
    @JoinColumns({
            @JoinColumn(name="student_id", referencedColumnName="studentNumber"),
            @JoinColumn(name="student_name", referencedColumnName="fullname")
    })
    private Student student;
    @OneToOne(mappedBy = "resident")
    RegisterEntity register;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        ResidentStudent residentStudent = (ResidentStudent) o;
        return  Objects.equals(id, residentStudent.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
