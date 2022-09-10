package com.application.student.model;
import com.application.student.data.Student;
import com.application.student.repostory.StudentRepository;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Service
public class StudentService implements OnSearchStudent{
    private  TreeStudents treeStudents;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private OnStudentChanges studentChanges;
    
    /**
     * Save student to database
     * @param student - to save to database
     */
    public  boolean saveStudent(Student student){
        if(student !=null) {
            Student student1 =studentRepository.save(student);
            studentChanges.addedStudent(student1);
        }
        else throw new RuntimeException("Can not save null student");
        return true;
    }

    /**
     * Update Student  fullname or contact
     * @param student - to be updated
     * @return true if successfully updated else false
     */
    @Transactional
    @Modifying
    public  boolean updateStudent(Student student){
        boolean updated =false;
        if(student !=null){
                Student student1 = getStudent(student.getStudentNumber());
                student1.setFullName(student.getFullName());
                student1.setContact(student.getContact());
                student1.setAccommodation(student.getAccommodation());
                saveStudent(student1);
                updated =true;


        }
        return updated;
    }

    /**
     * Delete student from students entity
     * @param student  to be deleted from students entity
     * @return deleted student if it's present on students entity else null
     */
    public Student deleteStudent(Student student){
        if(student ==null)return null;
        else {
            Student student1 = getStudent(student.getStudentNumber());
            if(student1 !=null) {
                studentChanges.deletedStudent(student);
                studentRepository.deleteById(student1.getStudentNumber());
                return  student1;
            }
            else return null;
        }
    }

    /**
     * Update student department
     * @param student  to update its department
     */
    public  void updateStudentDepartment(Student student){
        studentRepository.save(student);
    }



    /**
     * Fetch all students then insert into binary tree
     * @return parent node of all the tree
     */
    public   Node fetchAllStudents(){
        treeStudents = new TreeStudents();
        studentRepository.findAll().forEach(
                student1 ->
                treeStudents.root= treeStudents.insert(treeStudents.root,student1)

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

    /**
     * Get students with accommodation
     * @return - list of students with accommodation
     */
    public List<Student> getStudentsWithResOffer() {
       return studentRepository.getStudentsWithResOffer();
    }

    /**
     * Search for student given student number
     * @param studentNumber - student oof the student
     * @return - Student if student is valid
     * @throws  - student with given student number does not exist
     */
    public Student getStudent(long studentNumber) {
        Student student = studentRepository.getReferenceById(studentNumber);
        if(student !=null) return student;
        else throw new RuntimeException("Student with student number "+studentNumber+" does not exist");
    }

    /**
     * Make Student Node
     */
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

    /**
     * Make Student binary tree
     */
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





}
