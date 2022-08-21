package com.application.server.model;
import com.application.server.data.Residence;
import com.application.server.data.ResidenceDepartment;
import com.application.student.data.Student;
import com.application.server.data.ResidentStudent;
import com.application.server.repository.ResidenceDepartmentRepository;
import com.application.student.model.StudentService;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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
    private List<ResidentStudent> resident;

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
        System.out.println(studentSet.size());
        setHashMap.forEach(
               (key, value)->{

                   String []residenceInfo = key.split(",");
                   String name  = StringUtils.capitalize(residenceInfo[0].trim());
                   String block  = (residenceInfo.length==2)?StringUtils.capitalize(residenceInfo[1].trim()):name;
                   if(name.trim().equalsIgnoreCase("Forest Hill")) {
                       Residence residence = getResidence(name, block);
                       ResidenceDepartment residenceDepartment = ResidenceDepartment.builder().students(value)
                               .residence(Set.of(residence)).accommodation(name+","+block).build();
                       System.out.println(key);
                      residenceDepartmentRepository.save(residenceDepartment);
                   }
               }
       );
        return setHashMap;


    }

    /**
     * Get residence from database with name and block
     * @param name - name of the residence
     * @param block - block of the residence
     * @return residence of the given name and block
     */
    private Residence getResidence(String name, String block){
        return  residenceService.getResidence(name.trim(), block.trim());
    }

    /**
     * Group students according to their residence/accommodation  status .
     * Accommodation status map to set of students belongs to it
     * @param setHashMap - hashmap to categorise/group these students
     * @param studentSet - set of all student to group or map
     * @return - hashmap of accommodation to set of students
     */
    private  HashMap<String, Set<Student>> categoriseStudentByRes(HashMap<String, Set<Student>> setHashMap, Set<Student> studentSet){
        studentSet.forEach(
                student -> {
                    String []residenceInfo = student.getAccommodation().split(",");
                    String name  = residenceInfo[0].trim();
                    String block  = (residenceInfo.length==2)?residenceInfo[1].trim():name;
                    String key =name.toLowerCase()+","+block.toLowerCase();
                    if(setHashMap.isEmpty()){
                        Set<Student> studentSet1 = new HashSet<>();
                        studentSet1.add(student);
                        setHashMap.put(key,studentSet1);
                    }
                    else if(! setHashMap.containsKey(key)){
                        Set<Student> studentSet1 = new HashSet<>();
                        studentSet1.add(student);
                        setHashMap.put(key,studentSet1);
                    }
                    else{
                        setHashMap.get(key).add(student);
                    }
                }
        );
        return  setHashMap;
    }

    /**
     * Filter out all students that not yet stored at department residence
     * Get students not yet placed.
     * Then check if they are not placed at any residence
     * @return set of students that not yet placed
     */
    private  Set<Student> getStudentsNotPlaced(){
        return studentService.getStudentsWithResOffer().stream().filter(
                student -> {
                    List<ResidenceDepartment> residenceDepartments = residenceDepartmentRepository.getDepartment();

                    //check in all residence if student is not placed before
                    return residenceDepartments.stream().noneMatch(
                            residenceDepartment -> residenceDepartment.getStudents().stream().noneMatch(
                                    student1 -> student1.getStudentNumber()==student.getStudentNumber()
                            )
                    );
                    //Node need to add student into set
                }
        ).collect(Collectors.toSet());
    }

    protected   List<ResidenceDepartment>studentList(int limit){
        return residenceDepartmentRepository.getFirstLimitStudents(limit);
    }
    protected List<ResidentStudent>roomsAvailable(){
       return residentStudentService.availableRoom();
    }
    /**
     * Place student at residence
     */
    public  boolean  placeStudentsResidence(int limit){
        List<ResidenceDepartment>departments = studentList(limit);
        List<ResidentStudent>roomsAvailable  = roomsAvailable();
        boolean placed =false;
        try {
            for (int index = 0; index < roomsAvailable.size(); index++) {
                if (index < departments.size()) {
                    ResidenceDepartment department = departments.get(index);
                    Residence residence = roomsAvailable.get(index).getResidence();
                    residenceDepartmentRepository.placeStudent(department.getId(),residence.getId(), residence.getResidenceName(),
                            residence.getBlocks(), "yes");
                    placed=true;
                } else break;
            }
        }catch (RuntimeException e){
              throw new RuntimeException("Not able to place student(s)");
        }
        return  placed;

    }



    /**
     * All Student placed at forest hill by residence department
     * @return list of student reside at Forest Hill residence
     */
    protected  List<Student> studentsPlacedAt(String residence){
        List<Long> studentId= residenceDepartmentRepository.getStudentAtResidence(residence);
        List<Student>studentList = new ArrayList<>();
        for(Student student : studentService.getAllStudent()){
            for(long studentID : studentId){
                   if(student.getStudentNumber()==studentID)studentList.add(student);
            }
        }
        return studentList;
    }

    /**
     * Check if the student stays at residence
     * @param student - student,the function check if it has a residence
     * @param  residenceName - name of residence student claim to stay at
     * @return true if the student has residence else false
     */
    public  boolean checkResidenceStatus(Student student, String residenceName){
       return residenceDepartmentRepository.getStudentAtResidence(residenceName).stream().anyMatch(
                studentId->studentId==student.getStudentNumber()
        );
    }

    /**
     * Save student
     * @param studentService - student service object get student to be saved
     */
    public  void saveStudent(StudentService studentService){
        //Note need add Student into set
        residenceDepartmentRepository.save(ResidenceDepartment.builder().build());
    }

    /**
     * helper method to get residence of the student
     * @param student - student used to find residence
     * @return residence of the student if found else return null
     */
    public Residence getResidenceStudent(Student student) {
        String stringDetails = residenceDepartmentRepository.getStudentId(student.getStudentNumber());
        if(stringDetails==null) return  null;

        String[] residenceDetails = stringDetails.split(",");
        return  residenceService.getResidence(residenceDetails[0], residenceDetails[1]);
    }



}
