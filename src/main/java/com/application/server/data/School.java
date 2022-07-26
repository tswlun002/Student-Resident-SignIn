package com.application.server.data;
import com.application.student.data.Student;
import lombok.*;
import org.hibernate.Hibernate;
import javax.persistence.*;
import java.util.Objects;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Getter
@Setter
@ToString
public class School {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "school_seq")
    @SequenceGenerator(name = "school_seq")
    @Column(name = "id", nullable = false)
    private Integer id;
    public Integer getId() {
        return id;
    }
    @ManyToOne
    @JoinColumns({
            @JoinColumn(name="student_id", referencedColumnName="studentNumber"),
            @JoinColumn(name="student_name", referencedColumnName="fullname")
    })
    private Student students;
    @Column(nullable = false)
    private String accommodation;
    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "blocks",referencedColumnName = "blocks"),
            @JoinColumn(name = "residence_name",referencedColumnName = "residence_name")
    })
    private Residence residence;


    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        School that = (School) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}