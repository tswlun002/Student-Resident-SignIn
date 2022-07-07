package com.visitor;

import org.springframework.stereotype.Component;

@Component
public class Relative extends  Visitor implements OnShowIdentityCard{

    private long idNumber;
    private  Address address;



    public Relative(String fullName, String contact ,long idNumber, Address address) {
        super(fullName, contact);
        this.idNumber = idNumber;
        this.address = address;
    }

    public Relative(Relative relative) {
        super(relative);
        this.idNumber = relative.idNumber;
        this.address = relative.address;
    }

    public long getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(long idNumber) {
        this.idNumber = idNumber;
    }


    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "Relative{" +
                "idNumber='" + idNumber + '\'' +
                ", fullName='" + super.getFullName() + '\'' +
                ", contact='" + super.getContact() + '\'' +
                ", address='" + address + '\'' +
                '}';
    }

    /**
     * @return  details of the identity card  of relative
     */
    @Override
    public String showIdentityCard() {
         return  toString();
    }
}
