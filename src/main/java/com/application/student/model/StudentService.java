package com.application.student.model;
import com.application.server.data.Residence;
import com.application.student.data.Student;
import com.application.student.repostory.StudentRepository;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Service
public class StudentService implements OnSearchStudent{
    private  TreeStudents treeStudents;
    @Autowired
    protected StudentRepository studentRepository;

    public  void saveStudent(){
        Student student = Student.builder().studentNumber(getHostNumber()).
                fullName(getFullName()).contact(getContact()).accommodation(getAccommodation()).build();
        studentRepository.save(student);
    }
    public Student getStudent(){
        return  Student.builder().studentNumber(getHostNumber()).
                fullName(getFullName()).contact(getContact()).accommodation(getAccommodation()).build();
    }
    public  List<Student> getAllStudent(){
       return studentRepository.findAll();
    }


    /**
     *
     * @return true if accommodation status yes else false
     */
    public  boolean accommodationStatus(){
        return  this.accommodation.equalsIgnoreCase("yes");

    }

    /**
     * Fetch all students then insert into binary tree
     * @return parent node of all the tree
     */
    public   Node fetchAllStudents(){
        treeStudents = new TreeStudents();
        studentRepository.findAll().forEach(
                student1 -> {
                treeStudents.root= treeStudents.insert(treeStudents.root,student1);
                }
        );
        return treeStudents.root;
    }

    /**
     * search for student in students
     * @param student - object of student to search
     * @return true if found else false
     */
    @Override
    public boolean search(Student student) {
        return treeStudents.search(treeStudents.root,student);
    }

    public List<Student> getStudentsWithResOffer() {
       return studentRepository.getStudentsWithResOffer();
    }


    private static class Node implements  Comparable{
        Student student;
        Node left;
        Node right;
        Node(Student student){
            this.student=student;
            this.left=null;
            this.right=null;
        }
        @Override
        public int compareTo(Object otherNode) {
            int value =-1;
            if(otherNode instanceof Node node){
                value = Long.compare(this.student.getStudentNumber(), node.student.getStudentNumber());
            }else  throw new ClassCastException();
            return value;
        }
    }

    static class TreeStudents{

        Node root;
        /**
         * insert student in binary tree
         * @param node - parent node
         */
        protected Node insert(Node node,Student student){
            if(node ==null){node = new Node(student);
            }
            else{

                int value  = node.compareTo(new Node(student));
                if(value<=0){
                  node.left= insert(node.left,student);
                }else{
                   node.right =insert(node.right,student);

                }
            }
            return node;
        }

        /**
         * Search for student in the tree
         * @param node - node of the tree search on
         * @param student - student searching
         * @return true if found else false
         */
        protected  boolean search(Node node,Student student){
            if(node==null || student==null) return  false;
            if(node.student.equals(student))return true;

            return node.compareTo(new Node(student))<0? search(node.left,student) : search(node.right, student);
        }
    }



    private long studentNumber;
    private  String fullName;
    private String contact;
    private  String accommodation;
    public long getHostNumber() {
        return studentNumber;
    }

    public void setHostNumber(long hostNumber) {
        this.studentNumber = hostNumber;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getAccommodation() {
        return accommodation;
    }

    public void setAccommodation(String accommodation) {
        this.accommodation = accommodation;
    }
    /**
     *
     * @return to string for StudentService class
     */
    @Override
    public String toString() {
        return "StudentService{" +
                "studentNumber='" + studentNumber + '\'' +
                ", fullName='" + fullName + '\'' +
                ", contact='" + contact + '\'' +
                ", accommodation =" + accommodation +
                '}';
    }
}
