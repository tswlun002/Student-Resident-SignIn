package com.application.student.data;
import com.application.server.data.Address;
import com.application.server.data.ResidenceDepartment;
import lombok.*;
import org.hibernate.Hibernate;
import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
@Getter
@Setter
@Table(name = "Student")
public class Student  implements Serializable  {
    @Id
    private Long studentNumber;
    @Column(name="fullname", nullable = false)
    @NonNull
    private  String fullName;
    @Column(name = "contact",nullable = false)
    private String contact;
    @Column(name="accommodation", nullable = false)
    private  String accommodation;
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

    @Override
    public String toString() {
        return "Student{" +
                "studentNumber=" + studentNumber +
                ", fullName='" + fullName + '\'' +
                ", contact='" + contact + '\'' +
                ", accommodation='" + accommodation +
                '}';
    }
}
