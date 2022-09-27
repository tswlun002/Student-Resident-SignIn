package com.application.visitor.data;

import com.application.server.data.Address;
import lombok.*;
import org.hibernate.Hibernate;
import javax.persistence.*;
import java.util.Objects;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Visitors")
@Getter
@Setter
//@ToString
@RequiredArgsConstructor

public class Visitor {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    @NonNull
    private long idNumber;
    @NonNull
    private  String fullname;
    @NonNull
    private  String contact;
    @Column(nullable = false)
    private  String visitorType ;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name = "addressId", referencedColumnName = "id"),
    })
    private  Address address;

    public  Long getId(){return id;}
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Visitor that = (Visitor) o;
        return idNumber >0 && Objects.equals(idNumber, that.idNumber);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}