package com.application.server.data;
import com.application.student.data.Student;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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
    @JsonManagedReference
    @OneToMany(mappedBy = "department",fetch = FetchType.LAZY)
    private Set<Student> students = new HashSet<>();
    @JsonManagedReference
    @OneToOne(mappedBy = "department",fetch = FetchType.LAZY)
    private Residence residence ;

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