package com.application.server.model;
import com.application.server.data.Residence;
import com.application.server.data.ResidenceDepartment;
import com.application.student.data.Student;
import com.application.server.repository.ResidenceDepartmentRepository;
import com.application.student.model.StudentService;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Service
public class ResidentDepartmentService {
    @Autowired private ResidenceDepartmentRepository residenceDepartmentRepository;
    @Autowired private StudentService studentService;
    @Autowired private ResidenceService residenceService;
    @Autowired private  ResidentStudentService residentStudentService;
    private List<Student> students;
    private String accommodation_status;

    private  List<ResidenceDepartment>residenceDepartmentList;

    /**
     * Save all registered students
     * First  fetch all students that have residence
     * Group them according to their residence
     * Then save these students
     * @return hashmap of residence their set of students
     */
    public HashMap<String, Set<Student>> saveStudentWithResidences(){
        Set<Student> studentSet= getStudentsNotPlaced();
        HashMap<String ,Set<Student>>  setHashMap = new HashMap<>();
        categoriseStudentByRes(setHashMap, studentSet);

        setHashMap.forEach(
               (key, value)->{

                   String []residenceInfo = key.split(",");
                   String name  = StringUtils.capitalize(residenceInfo[0].trim());
                   String block  = (residenceInfo.length==2)?StringUtils.capitalize(residenceInfo[1].trim()):name;
                   Residence residence = getResidence(name, block);
                   getDepartmentWithResidence();

                   if(residence != null) {

                          if(!existsSchoolResidence(residence)) {
                              ResidenceDepartment residenceDepartment = ResidenceDepartment.builder().students(value)
                                      .residence(Set.of(residence)).accommodation(name + "," + block).build();
                              residenceDepartmentRepository.save(residenceDepartment);
                              setDepartmentResidence(residence,residenceDepartment);
                              setDepartmentStudent(value,residenceDepartment);

                          }else {
                              //insert into existing set
                              value.forEach(
                                      student ->{

                                          ResidenceDepartment residenceDepartment =addToStudentSet(student,residence);
                                          if(residenceDepartment != null) {
                                              residenceDepartment.getResidence().add(residence);
                                              residenceDepartmentRepository.save(residenceDepartment);
                                              setDepartmentResidence(residence,residenceDepartment);
                                              setDepartmentStudent(value,residenceDepartment);
                                          }else  throw new RuntimeException("Residence "+key+" does not exist");
                                      }
                              );

                          }
                   }else throw new RuntimeException("Residence "+key+" does not exist");

               }
       );
        return setHashMap;
    }

    /**
     * Assign residence to department
     * @param residence - to assign to department
     * @param department - of the residence
     */
    private  void setDepartmentResidence(Residence residence,ResidenceDepartment department){
       residence.setDepartment(department);
       residenceService.updateResidence(residence);

    }

    /**
     * Assign student to department
     * @param students - to be assigned to department of residence
     * @param department - housing/residence of student
     */
    private void  setDepartmentStudent(Set<Student> students, ResidenceDepartment department){
        students.forEach(
                student -> {
                    student.setDepartment(department);
                    studentService.saveStudent(student);
                }
        );
    }


    /**
     *  Added student to set of student of the particular residence
     * @param student - student to add into set of student of residence in department on residences
     * @param residence - residence student of the student
     * @return  residenceDepartment student added to if student residenceDepartment contains student residence else null
     */
    public ResidenceDepartment addToStudentSet(Student student,Residence residence){
        ResidenceDepartment residenceDepartment= null;
        for(ResidenceDepartment residenceDepartment1 : residenceDepartmentList) {
                if (residenceDepartment1.getResidence().contains(residence)) {
                    residenceDepartment1.getStudents().clear();
                    residenceDepartment1.getStudents().add(student);
                    residenceDepartment= residenceDepartment1;
                    break;
                }
        }
        return residenceDepartment;
    }

    /**
     * Check if residence exists
     * @param name - residence name to be checked if it exists
     * @param blocks - block of the residence to be checked if it exists
     * @return true if the residence exists else false
     */
    public  boolean residenceExists(String name ,String blocks){
        return  residenceService.isExists(name,blocks);
    }
    /**
     * Check if residence already added into department
     * @param residence - residence to check if added to department
     * @return - true if added already else false
     */
    public boolean existsSchoolResidence(Residence residence){
        return residenceDepartmentList.stream().anyMatch(
                residenceDepartment -> residenceDepartment.getResidence().stream().anyMatch(
                        residence1 ->  residence1.equals(residence)
                )
        );
    }


    /**
     * Get residence from database with name and block
     * @param name - name of the residence
     * @param block - block of the residence
     * @return residence of the given name and block
     */
    public Residence getResidence(String name, String block){
        return  residenceService.getResidence(name.trim(), block.trim());
    }

    /**
     * Group students according to their residence/accommodation  status .
     * Accommodation status map to set of students belongs to it
     * @param setHashMap - hashmap to categorise/group these students
     * @param studentSet - set of all student to group or map
     * @return - hashmap of accommodation to set of students
     */
    public   HashMap<String, Set<Student>> categoriseStudentByRes(HashMap<String, Set<Student>> setHashMap, Set<Student> studentSet){
        studentSet.forEach(
                student -> {

                        String[] residenceInfo = student.getAccommodation().split(",");
                        String name = residenceInfo[0].trim();
                        String block = (residenceInfo.length == 2) ? residenceInfo[1].trim() : name;
                        String key = name.toLowerCase() + "," + block.toLowerCase();
                        if(residenceExists(name, block)) {
                            if (setHashMap.isEmpty()) {
                                Set<Student> studentSet1 = new HashSet<>();
                                studentSet1.add(student);
                                setHashMap.put(key, studentSet1);
                            } else if (setHashMap.containsKey(key)) {
                                setHashMap.get(key).add(student);
                            } else {
                                Set<Student> studentSet1 = new HashSet<>();
                                studentSet1.add(student);
                                setHashMap.put(key, studentSet1);
                            }
                        }
                    }


        );
        return  setHashMap;
    }

    /**
     * Fetch all  department residences data from database
     * @return List of ResidenceDepartment
     */
    List<ResidenceDepartment> getDepartmentWithResidence(){
        residenceDepartmentList= residenceDepartmentRepository.getDepartmentWithResidences();
        return  residenceDepartmentList;
    }

    /**
     * Fetch   department residences data from database
     * @return  of ResidenceDepartment
     */
    ResidenceDepartment getDepartment(Student student, Residence residence){

        ResidenceDepartment residenceDepartment=
                residenceDepartmentRepository.getDepartmentWithResidences(student.getStudentNumber(),residence.getId(),
                        residence.getResidenceName()+","+residence.getBlocks());
        if(residenceDepartment !=null)return  residenceDepartment;
        else{
              residenceDepartment = getDepartmentWithResidence(residence.getId());
              if(residenceDepartment ==null)
                  throw  new RuntimeException(student +" and  "+residence.getResidenceName()+","+residence.getBlocks()
                          +" id="+residence.getId()+" is not under department of residence");
        }
        return residenceDepartment;

    }

    public ResidenceDepartment getDepartmentWithResidence(Long id) {
        return  residenceDepartmentRepository.getDepartmentWithResidences(id);
    }


    /**
     * Filter out all students that not yet stored at department residence
     * Get students not yet placed.
     * Then check if they are not placed at any residence
     * @return set of students that not yet placed
     */
    public   Set<Student> getStudentsNotPlaced(){
        List<ResidenceDepartment> deptResidences = getDepartmentWithResidence();
        return new HashSet<>(studentService.getStudentsWithResOffer()).stream().filter(
                student -> !existsSchoolResidence(student, deptResidences)
        ).collect(Collectors.toSet());
    }

    /**
     * Check if student added already into department
     * @param student  - student to check if It's added to department
     * @param residenceDepartments - department residences  to check if it's not containing student
     * @return true if student is already added to   department residences else false
     */
  public   boolean existsSchoolResidence(Student student, List<ResidenceDepartment> residenceDepartments ){
        return residenceDepartments.stream().anyMatch(
                residenceDepartment -> residenceDepartment.getStudents().stream().anyMatch(
                        student1 -> student1.equals(student)

                )
        );
    }

    /**
     * Change student residence
     * @param student - student to change residence
     * @param currentResidenceName -  current student residence name
     * @param block - block of  residence
     * @return - true if successfully change student residence else false
     */
    @Transactional
    @Modifying
   public  boolean changeStudentResidence(Student student,String currentResidenceName, String block,
                                          String nextResName){

      boolean updated = false;
      try {
          Residence residence = getResidence(currentResidenceName,block);
          Student student1 = getStudent(student);
          ResidenceDepartment residenceDepartment = getDepartment(student1,residence);
          ResidenceDepartment residenceDepartment1 = removeStudent(residenceDepartment,student1);

          if(! residenceDepartment1.getStudents().contains(student))
          {
                 student.setAccommodation(nextResName);
                 studentService.saveStudent(student);
                 //saveStudentWithResidences();
                 updated=true;
          }
      }catch (Exception e){
          throw  new RuntimeException(e.getMessage());

      }

      return  updated;
   }


    /**
     * Get Student  from database
     * @param student  - student to fetch
     * @return student from database
     */
   Student getStudent(Student student){
       return  studentService.getStudent(student.getStudentNumber());
   }

    /**
     * Remove student from set of students in department of residence
     * @param residenceDepartment - department of residence we remove student from
     * @param student - student to remove from department of residence
     * @return  residenceDepartment without given student
     */
   ResidenceDepartment removeStudent(ResidenceDepartment residenceDepartment, Student student){
       residenceDepartment.getStudents().remove(student);
       return residenceDepartment;

   }

}
