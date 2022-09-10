package com.application.server.model;
import com.application.server.data.Residence;
import com.application.server.data.ResidenceDepartment;
import com.application.student.data.Student;
import com.application.server.repository.ResidenceDepartmentRepository;
import com.application.student.model.OnStudentChanges;
import com.application.student.model.StudentService;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import javax.annotation.PostConstruct;
import java.util.List;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Service
public class ResidentDepartmentService implements OnStudentChanges,OnChangesResidence {
    @Autowired private ResidenceDepartmentRepository repository;
    @Autowired private StudentService studentService;
    @Autowired private ResidenceService residenceService;
    @Autowired private ResidenceRegisterService residenceRegisterService;
    private List<Student> students;
    private String accommodation_status;
    private  List<ResidenceDepartment>residenceDepartmentList;

    /**
     * Fetch all  department residences data from database
     * @return List of ResidenceDepartment
     */
    @PostConstruct
    List<ResidenceDepartment> getDepartmentWithResidence(){
        residenceDepartmentList= repository.getDepartments();
        return  residenceDepartmentList;
    }

    /**
     * notify about student added to students entity
     */
    @Override
    public void addedStudent(Student student) {

        if(student !=null)saveStudentWithResidences(student);
    }

    /**
     * notify about student deleted to students entity
     * @param student was deleted on students entity
     * */
    @Override
    public void deletedStudent(Student student) {
        if(student !=null)deleteStudent(student);

    }

    /**
     * Notify when residence is a deleted
     *
     * @param residence deleted  or to be deleted
     */
    @Override
    public void deletedResidence(Residence residence) {
           if(residence !=null) removeResidence(residence);
    }

    /**
     * Save all registered students
     * First  fetch all students that have residence
     * Group them according to their residence
     * Then save these students
     */
    public void saveStudentWithResidences(Student student){
        HashMap<String ,Set<Student>>  setHashMap = new HashMap<>();
        categoriseStudentByRes(setHashMap, student);

        setHashMap.forEach(
                (key, students)->{

                    String []residenceInfo = key.split(",");
                    String name  = StringUtils.capitalize(residenceInfo[0].trim());
                    String block  = (residenceInfo.length==2)?StringUtils.capitalize(residenceInfo[1].trim()):name;
                    Residence residence = getResidence(name, block);

                    if(residence != null) {
                        if(!existsSchoolResidence(residence)) {
                          addNewDepartment(residence,students,name,block);

                        }else {
                            //insert into existing set
                            addOnExistingDepartment(residence,students,key);
                        }
                    }else throw new RuntimeException("Residence "+key+" does not exist");

                }
        );
    }

    /**
     * Create new department instance or record and save it to database
     * @param residence - of the department
     * @param students - of the residence of the department
     * @param resName - is the residence name
     * @param block - blocks of  residence
     */
   private  void addNewDepartment(Residence residence,Set<Student>students, String resName, String block){
       students.forEach(student -> student.setAddress(residence.getAddress()));
       ResidenceDepartment residenceDepartment = ResidenceDepartment.builder().students(students)
               .residence(residence).accommodation(resName + "," + block).build();

       repository.save(residenceDepartment);
       setDepartmentResidence(residence,residenceDepartment);
       setDepartmentStudent(students,residenceDepartment);
   }

    /**
     * Add student into existing residence department
     * @param residence of the student of  department
     * @param students to be added to department
     * @param group -  string name & blocks of the residence
     */
   private void addOnExistingDepartment(Residence residence,Set<Student>students,String group){
       students.forEach(student -> student.setAddress(residence.getAddress()));
       students.forEach(
               student1->{
                   ResidenceDepartment residenceDepartment =addToStudentSet(student1,residence);
                   if(residenceDepartment != null) {
                       setDepartmentResidence(residence,residenceDepartment);
                       setDepartmentStudent(students,residenceDepartment);
                   }else  throw new RuntimeException("Residence "+group+" does not exist");
               }
       );
   }

    /**
     * Remove residence with no students
     *-remove residence from set of residence
     *-remove department reference on residence
     *-remove department instance itself
     */
    @Transactional
    @Modifying
    public  void removeDepartmentWithNoStudents(){
        List<ResidenceDepartment>list = getDepartmentWithNoStudents();
        list.forEach( department -> {
            setDepartmentResidence(department.getResidence(), null);
            department.setResidence(null);
            repository.delete(department);


        });
    }

    /**
     * Fetch all departments with residence but no student
     * @return list of departments with no students
     */
    public List<ResidenceDepartment> getDepartmentWithNoStudents(){
        return repository.getDepartmentWithNoStudents();
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
                    studentService.updateStudentDepartment(student);
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
            if (residenceDepartment1.getResidence().equals(residence)) {
                residenceDepartment1.getStudents().add(student);
                residenceDepartment = residenceDepartment1;
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
        List<ResidenceDepartment>list = getDepartmentWithResidence();
        if(list!=null && list.size()>0) {
            return list.stream().anyMatch(
                    residenceDepartment -> residenceDepartment.getResidence().equals(residence)
            );
        }
        return false;
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
     *
     * @param setHashMap - hashmap to categorise/group these students
     * @param student    -  student to group or map
     */
    public void categoriseStudentByRes(HashMap<String, Set<Student>> setHashMap, Student student){
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


    /**
     * Fetch   department residences data from database
     * @return  of ResidenceDepartment
     */
    ResidenceDepartment getDepartment(Student student, Residence residence){

        ResidenceDepartment residenceDepartment= repository.getDepartments(student.getStudentNumber(),residence.getId(),
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

    /**
     * Get department using id of that department
     * @param id - of the department to fetch from database
     * @return  ResidenceDepartment
     */
    public ResidenceDepartment getDepartmentWithResidence(Long id) {
        return  repository.getDepartments(id);
    }


    /**
     * Filter out all students that not yet stored at department residence
     * Get students not yet placed.
     * Then check if they are not placed at any residence
     * @return set of students that not yet placed at any residence
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
              student.setDepartment(null);
              student.setAccommodation(nextResName);
              studentService.saveStudent(student);
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
   private ResidenceDepartment removeStudent(ResidenceDepartment residenceDepartment, Student student){
       residenceDepartment.getStudents().remove(student);
       return residenceDepartment;

   }
    /**
     * Remove residence from housing/residence department
     * @param nameResidence - name of the residence to remove
     * @param blockResidence - block of residence to remove
     * @return true if residence successfully removed  else false
     */
    public  boolean removeResidence(String nameResidence ,String blockResidence){
        Residence residence  = getResidence(nameResidence,blockResidence);
        return removeResidence(residence);
    }

    /**
     * Remove residence from housing/residence department
     * @param residence to be removed
     * @return true if residence successfully removed  else false
     */
   private   boolean removeResidence(Residence residence){
       boolean removed  = false;
       if(existsSchoolResidence(residence) && residence.getDepartment()!=null){
           ResidenceDepartment department  = getDepartment(residence.getDepartment().getId());
           setDepartmentStudent(department.getStudents(),null);
           setDepartmentResidence(department.getResidence(),null);
           department.getStudents().clear();
           department.setResidence(null);
           repository.delete(department);
           removed =true;
       }
       return removed;
   }

    /**
     * Get residence department  by id
     * @param id - of the department to remove
     * @return -residence department
     */
   ResidenceDepartment getDepartment(Long id){
       return repository.getDepartmentById(id);
   }



    /**
     * Remove student from residence data
     */
   public  void  deleteStudent(Student student ) {
       residenceDepartmentList.forEach(
               dept-> dept.getStudents().remove(student)
       );
   }


    /**
     * Fetch All students that stays at residences
     * @return  string list of students
     */
    List<ResidenceDepartment> getDepartmentWithStudents(){
       return repository.getDepartmentWithStudents();
    }




}
