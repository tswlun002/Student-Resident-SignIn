package com.application.server.model;

import com.application.server.data.Address;
import com.application.server.repository.AddressRepository;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Service
public class AddressService {
    @Autowired
    private AddressRepository repository;

    /**
     * Save given  address
     * check first if not already saved
     * @param address - address being saved
     * @return new address if address saved  else address already stored
     */
    public Address saveAddress(Address address){
        Address address1 = repository.getAddress(address.getStreetNumber(),
                address.getStreetName(),address.getSuburbs());
        if(address1==null) {
            return repository.save(address);

        }
        return address1;
    }
    /**
     * Update address of the residence.
     * @param address - updated Address (Address with new properties)
     * @return - true if successfully updated otherwise false
     */
    public Address updateAddress(Address address) {
         if (address==null) return null;
         Address address1 = repository.getAddress(address.getStreetNumber(),
                 address.getStreetName(), address.getSuburbs());
         if(address1 ==null) address1 =saveAddress(address);
         else try{
            address1.setStreetNumber(address.getStreetNumber());
            address1.setStreetName(address.getStreetName());
            address1.setSuburbs(address.getSuburbs());
            address1.setCity(address.getCity());
            address1.setPostcode(address.getPostcode());
            address1 =repository.save(address1);
        }catch (Exception e){return  null;}

        return address1;
    }

    /***
     * Get address by streetNumber, street name and suburbs
     * @param streetNumber  of the address
     * @param streetName   of the address
     * @param suburbs  of the address
     * @return address if found else null
     */
    public Address getAddress(String streetNumber, String streetName, String suburbs) {
        return repository.getAddress(streetNumber,streetName, suburbs);
    }

}


