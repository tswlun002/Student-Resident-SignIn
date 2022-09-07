package com.application.server.model;

import com.application.server.repository.AddressRepository;
import com.application.server.data.Address;
import com.application.visitor.data.Visitor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Service
public class AddressService {
    @Autowired
    private AddressRepository repository;
    private TreeAddress treeAddress;

    /**
     * Save given  address
     * check first if not already saved
     * @param address - address being saved
     * @return true if address saved  else false
     */
    public Address saveAddress(Address address){
        Address address1 = repository.getAddress(address.getStreetNumber(),
                address.getStreetName(),address.getSuburbs());
        if(address1==null) {
            repository.save(address);

        }
        return address1;
    }
    /**
     * Update address of the residence.
     * @param address - updated Address (Address with new properties)
     * @return - true if successfully updated otherwise false
     */
    public boolean updateAddress(Address address) {
        try{
            Address address1 = repository.getReferenceById(address.getId());
            address1.setStreetNumber(address.getStreetNumber());
            address1.setStreetName(address.getStreetName());
            address1.setSuburbs(address.getSuburbs());
            address1.setCity(address.getCity());
            address1.setPostcode(address.getPostcode());
            repository.save(address);
        }catch (Exception e){return  false;}
        return true;
    }

    private static class Node implements  Comparable{
        Address address;
        Node left;
        Node right;
        Node(Address address){
            this.address = address;
            this.left=null;
            this.right=null;
        }
        @Override
        public int compareTo(Object othersAddress) {
            int value =-1;
            if(othersAddress instanceof Address address1){
                value = address.getStreetNumber().toLowerCase().compareTo(address1.getStreetNumber().toLowerCase()) +
                        address.getStreetName().toLowerCase().compareTo(address1.getStreetName().toLowerCase());
            }else  throw new RuntimeException("other object is not Address type or instance");
            return value;
        }
    }

    static class TreeAddress {
       Node root;
        /**
         * insert student in binary tree
         * @param node - parent node
         * @param address - student insert into tree
         */
        protected Node insert(Node node, Address address){
            if(node ==null){
                node= new Node(address);
            }
            else{
                int value  = node.compareTo(new Node(address));
                if(value<=0){
                   node.left= insert(node.left,address);
                }else{
                    node.right =insert(node.right,address);
                }
            }
            return node;
        }

        /**
         * Search for address in the tree
         * @param node - node of the tree search on
         * @param address - address searching
         * @return true if found else false
         */
        protected  boolean search(Node node, Address address){
            if(node==null || address==null) return  false;
            if(node.address.equals(address))return true;
            return node.compareTo(new Node(address))<0? search(node.left,address) : search(node.right, address);
        }

        /**
         * get for address in the tree of visitor
         * @param node - node of the tree search on
         * @param visitor - address searching
         * @return  address if found else null
         */
        protected Address getAddress(Node node, Visitor visitor){
           if(node==null || visitor==null) return  null;
           if( node.address.getVisitors().stream().anyMatch(
                    existingVisitor ->existingVisitor.equals(visitor)
           )) return node.address;

           return node.compareTo(new Node(Address.builder().visitors(List.of(visitor)).build()))<0?
                    getAddress(node.left,visitor) : getAddress(node.right, visitor);
        }
    }
}


