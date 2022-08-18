package com.application.server.data;
import com.application.student.data.Student;
import lombok.*;
import org.hibernate.Hibernate;
import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Getter
@Setter
@ToString
@Table(name = "ResidenceDepartment")
public class ResidenceDepartment implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;
    public Long getId() {
        return id;
    }
    @ManyToOne
    @JoinColumns({
            @JoinColumn(name="studentId", referencedColumnName="studentNumber"),
            @JoinColumn(name="studentFullname", referencedColumnName="fullname")
    })
    private Student students;
    @Column(nullable = false)
    private String accommodation;
    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "residenceId", referencedColumnName = "id"),
            @JoinColumn(name = "blocks",referencedColumnName = "blocks"),
            @JoinColumn(name = "residence",referencedColumnName = "residenceName")
    })
    private Residence residence;


    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        ResidenceDepartment that = (ResidenceDepartment) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}