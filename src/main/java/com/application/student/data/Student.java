package com.application.student.data;
import com.application.server.data.Address;
import com.application.server.data.ResidenceDepartment;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;
import org.hibernate.Hibernate;
import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Objects;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
@Getter
@Setter
@ToString
@Table(name = "Student")
public class Student  implements Serializable {
    @Id
    private Long studentNumber;
    @Column(name="fullname", nullable = false)
    @NonNull
    @Size(min = 3, max = 50, message = "Fullname must contain 3 to 50 characters.")
    private  String fullName;
    @Size(min = 10, max = 10, message = "Phone number must contain 10 digit.")
    @Column(name = "contact",nullable = false)
    private String contact;
    @Column(name="accommodation", nullable = false)
    private  String accommodation;

    @JsonBackReference
    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name="residence_departmentId",referencedColumnName = "id")
    private ResidenceDepartment department;
    @ManyToOne
    @JoinColumn(name = "addressId", referencedColumnName = "id")
    private Address address;
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Student that = (Student) o;
        return studentNumber != null && Objects.equals(studentNumber, that.studentNumber) ;
    }
    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

}
