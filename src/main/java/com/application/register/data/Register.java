package com.application.register.data;
import com.application.server.data.Residence;
import com.application.student.data.Student;
import com.application.visitor.data.Visitor;
import lombok.*;
import org.hibernate.Hibernate;
import org.springframework.lang.Nullable;
import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Getter
@Setter
@ToString
@Table(name = "Register")
public class Register {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "registerSequence")
    @Column(name = "id", nullable = false)
    private Integer id;
    @OneToOne(optional = false)
    @JoinColumn(name = "residenceId")
    private Residence residence;
    @Column(name = "date")
    @Nullable
    private LocalDate  signingDate;
    @Column(name = "numberVisitors")
    private  int numberVisitors;
    @ManyToOne
    @JoinColumn(name="studentNumber", referencedColumnName="studentNumber")
    private Student hostStudent;

    public Integer getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Register that = (Register) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}