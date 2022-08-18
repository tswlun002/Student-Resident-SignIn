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
import java.util.ArrayList;
import java.util.List;
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
     */
    public  void saveAllStudent(){
        studentService.getAllStudent().forEach(
                student -> {
                    String status  = student.getAccommodation()==null?"no":student.getAccommodation();
                    List<ResidenceDepartment> residenceDepartments = residenceDepartmentRepository.findAll();
                    if(residenceDepartments.stream().noneMatch(
                            residenceDepartment ->
                                    residenceDepartment.getStudents().getStudentNumber()==student.getStudentNumber()
                    ))
                         residenceDepartmentRepository.save(ResidenceDepartment.builder().accommodation(status).students(student).build());
                }
        );
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
        residenceDepartmentRepository.save(ResidenceDepartment.builder().students(studentService.getStudent()).build());
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

 /*   public void setStudents(List<Student> students) {
        this.students = students;
    }

    public void setAccommodation_status(String accommodation_status) {
        this.accommodation_status = accommodation_status;
    }

    public void setResident(List<ResidentStudent> resident) {
        this.resident = resident;
    }

    public List<Student> getStudents() {
        return students;
    }

    public String getAccommodation_status() {
        return accommodation_status;
    }

    public List<ResidentStudent> getResident() {
        return resident;
    }*/

}
