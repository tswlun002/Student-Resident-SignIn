package com.application.server.data;
import com.application.student.data.Student;
import lombok.*;
import org.hibernate.Hibernate;
import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Getter
@Setter
@Table(name = "ResidenceDepartment")
public class ResidenceDepartment implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;
    public Long getId() {
        return id;
    }
    @Column(nullable = false)
    private String accommodation;
    @OneToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "AccommodatedStudents",
            joinColumns = {@JoinColumn(name="ResidenceDepartId")},
            inverseJoinColumns =  {@JoinColumn(name = "studentId",referencedColumnName = "studentNumber"),
                    @JoinColumn(name ="studentName", referencedColumnName = "fullname")}


    )
    private Set<Student> students = new HashSet<>();
    @OneToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "SchoolResidences",
            joinColumns =  @JoinColumn(name = "ResidenceDepartId"),
            inverseJoinColumns = {@JoinColumn(name = "residenceId", referencedColumnName = "id"),
                                  @JoinColumn(name = "residenceName", referencedColumnName = "residenceName"),
                                  @JoinColumn(name = "blocks",referencedColumnName = "blocks")
            }

    )
    private Set<Residence> residence = new HashSet<>();

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

    @Override
    public String toString() {
        return "ResidenceDepartment{" +
                "id=" + id +
                ", accommodation='" + accommodation + '\'' +
                '}';
    }
}