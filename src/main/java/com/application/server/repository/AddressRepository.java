package com.application.server.repository;
import com.application.server.data.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {
    @Query("select a from Address a " +
            " where a.streetNumber=:streetNumber and a.streetName=:streetName and a.suburbs=:suburbs")
    Address getAddress(String streetNumber, String streetName,String suburbs);
}
