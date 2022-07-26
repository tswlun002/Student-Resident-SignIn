package com.application.server.model;
import com.application.student.data.Student;
import com.application.server.data.ResidentStudent;
import com.application.server.data.School;
import com.application.server.repository.SchoolRepository;
import com.application.student.model.StudentService;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Service
public class SchoolService {

    @Autowired
    private SchoolRepository schoolRepository;
    @Autowired private StudentService studentService;
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
                    School school = School.builder().students(student).accommodation(status).build();
                    schoolRepository.save(school);
                }
        );
    }
    public  void saveAllStudent(){
        studentService.getAllStudent().forEach(
                student -> {
                    String status  = student.getAccommodation()==null?"no":student.getAccommodation();
                    schoolRepository.save(School.builder().accommodation(status).students(student).build());
                }
        );
    }

    public  void saveStudent(StudentService studentService){
        schoolRepository.save(School.builder().students(studentService.getStudent()).build());
    }

}
