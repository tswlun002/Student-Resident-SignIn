package com.application.server.Dao;
import com.application.server.model.ResidentStudentService;
import com.application.student.data.Student;
import com.application.student.model.StudentService;
import com.application.student.repostory.StudentRepository;
import com.application.server.repository.ResidentStudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ResidentDB{
    /**
     * @serialField  studentServiceList  - database of residentStudentService members
     */
    private final List<StudentService> studentServiceList =new ArrayList<>();

    @Autowired(required = true)
    ResidentStudentRepository repository;
    @Autowired
    ResidentStudentService residentStudentService;
    @Autowired
    StudentRepository studentRepository;


    /**
     * Defaulted  Construct
     */
    public  ResidentDB(){

    }

    public  void fillResidentFeature(){
          String[] blocks =    {"A",  "B", "C", "D", "E", "F", "G"};
          int [] NumberFloors ={  6,   6,   6,   6,   3,   3,   3};
         int [] NumberFlatsFloor ={8,  8,   8,   8,   8,   8,   8};
         String[] rooms ={"A","B","C","D"};

       /* residentStudentService = ResidentStudentService.builder().blocks(blocks).NumberFloors(NumberFloors).NumberFlatsFloor(NumberFlatsFloor)
                .room(rooms).residentName("Forest Hill").capacity(2500).build();
        int[] floor = {0};
        Arrays.stream(residentStudentService.getBlocks()).forEach(
                block-> {                                       // block

                    IntStream.range(1, residentStudentService.getNumberFloors()[floor[0]]+1).forEach(    // each floor
                            index -> {
                                int[] floorFlats = {0};
                                //flat
                                IntStream.range(1, residentStudentService.getNumberFlatsFloor()[floorFlats[0]]+1).forEach( //each flat
                                        flat -> {
                                            // each room
                                            Arrays.stream(residentStudentService.getRoom()).forEach(
                                                    room->{
                                                        try {
                                                            ResidentStudent resDB = ResidentStudent.builder().name(residentStudentService.getResidentName()).
                                                                    blocks(block).flat((index * 100 + flat) + "").room(room).floor(index).build();
                                                            repository.save(resDB);
                                                        }catch (RuntimeException e){
                                                            throw  new RuntimeException("Block %s, floor %d, flat %d, room %s , "+block+", "+ index+", "+(index*100+flat)+", "+room);
                                                        }
                                                        finally {
                                                            System.out.printf("Block %s, floor %d, flat %d, room %s\n",block, index, (index*100+flat),room);
                                                        }
                                                    }
                                             );

                                            floorFlats[0] += 1;
                                        }
                                );


                            }
                    );

                    floor[0] += 1;
                }

        );*/
                            //residentStudentService.getNumberFloors()
    }

    public void saveStudents(){
         final List<Student> studentList = studentRepository.findAll();


           /*studentServiceList.forEach(
                    student->{

                        String block  = student.getRoomNumber().substring(0,1).toUpperCase();
                        String flat  = student.getRoomNumber().substring(1,4).toUpperCase();
                        String room  = student.getRoomNumber().substring(4).toUpperCase();
                        StudentService student = StudentService.builder().studentNumber(student.getStudentNumber()).
                                fullName(student.getFullName()).contact(student.getContact()).
                                roomNumber(student.getRoomNumber()).build();
                        //System.out.print(student);
                        repository.updateWhereStudent_student_numberIsNull(student, block,flat,room);
                    }
            );*/
    }
    /**
     * @return List of the  members(Hosts) of the residentStudentService
     */
    public List<Student> getHostList() {
        return studentRepository.findAll();
    }
}
