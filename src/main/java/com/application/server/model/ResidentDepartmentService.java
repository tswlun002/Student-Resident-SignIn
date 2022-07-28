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

    @Autowired
    private ResidenceDepartmentRepository residenceDepartmentRepository;
    @Autowired private StudentService studentService;

    @Autowired
    ResidenceService residenceService;

    @Autowired
    private  ResidentStudentService residentStudentService;
    private List<Student> students;
    private String accommodation_status;
    private List<ResidentStudent> resident;


    public void setStudents(List<Student> students) {
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
    }
    public  void saveStudents(){
        students.forEach(
                student->{
                    String status  = student.getAccommodation()==null?"no":student.getAccommodation();
                    ResidenceDepartment residenceDepartment = ResidenceDepartment.builder().students(student).accommodation(status).build();
                    residenceDepartmentRepository.save(residenceDepartment);
                }
        );
    }

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

    /**
     * Place student at residence
     */
    public  boolean  placeStudentsResidence(){
        List<ResidenceDepartment>studentList = residenceDepartmentRepository.getStudentWithNoRes(1000);
        List<ResidentStudent>roomsAvailable  = residentStudentService.availableRoom();
        boolean placed =false;
        try {
            for (int index = 0; index < roomsAvailable.size(); index++) {
                if (index < studentList.size()) {
                    ResidenceDepartment department = studentList.get(index);
                    Residence residence = roomsAvailable.get(index).getResidence();
                    residenceDepartmentRepository.placeStudent(department.getId(), residence.getResidenceName(),
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
     * Save student
     * @param studentService - student service object get student to be saved
     */
    public  void saveStudent(StudentService studentService){
        residenceDepartmentRepository.save(ResidenceDepartment.builder().students(studentService.getStudent()).build());
    }

    /**
     * All Student placed at forest hill by residence department
     * @return list of student
     */
    protected  List<Student> studentsPlacedAtForestHill(){
        List<Long> studentId= residenceDepartmentRepository.getStudentAtResidence("Forest Hill");
        List<Student>studentList = new ArrayList<>();
        for(Student student : studentService.getAllStudent()){
            for(long studentID : studentId){
                   if(student.getStudentNumber()==studentID)studentList.add(student);
            }
        }
        return studentList;
    }

}
