package com.application.server.model;
import com.application.server.data.Residence;
import com.application.server.data.ResidenceRegister;
import com.application.server.repository.ResidenceRegisterRepository;
import com.application.student.data.Student;
import com.application.student.model.StudentService;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.IntStream;
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Service
public  class ResidenceRegisterService {
    @Autowired
    private ResidenceRegisterRepository repository;
    @Autowired
    private ResidenceService residenceService;
    @Autowired
    private StudentService studentService;
    @Autowired
    private  ResidentDepartmentService departmentService;

    /***
     * Get Residence details and store them
     */

    public  void fillResidentFeature(String name){
        List<Residence>residences  =residenceService.getAllResidence(name);
        residences.forEach(
                residence-> {                                       // block

                    IntStream.range(1, residence.getNumberFloors()+1).forEach(    // each floor
                            floor -> {

                                IntStream.range(1, residence.getNumberFlats()+1).forEach( //each flat
                                        flat -> {

                                            IntStream.range(1, residence.getNumberRoom()+1).forEach(    // each room
                                                    room->{
                                                        try {

                                                            ResidenceRegister residenceRegister = ResidenceRegister.builder().residence(residence).flat((floor * 100 + flat) + "").
                                                            floor(floor).room(getRoom(room)).build();
                                                            boolean notExists =repository.getBy(residence.getBlocks(),floor,(floor * 100 + flat) + "",getRoom(room))==null;
                                                            if(notExists) repository.save(residenceRegister);
                                                        }catch (RuntimeException e){
                                                            throw  new RuntimeException(residence.getResidenceName()+" "+residence.getBlocks()+", "+ floor+", "+(floor*100+flat)+", "+getRoom(room)+"\n"+e.toString());
                                                        }
                                                        finally {
                                                            System.out.printf("Block %s, floor %d, flat %d, room %s\n",residence.getBlocks(), floor, (floor*100+flat),getRoom(room));
                                                        }
                                                    }
                                             );

                                        }
                                );


                            }
                    );

                }

        );
    }

    /**
     * Get letter from  ASCII number
     * @param index - index to be converted to ASCII , range 1 - 26
     * @return letter of the ASCII
     */
    private  String getRoom(int index){
        if(index>=0 && index<27)
            return  (((char)(64+index))+"").toUpperCase();
        else throw  new RuntimeException("Can not find asc  of negative  or value greater than 25 value");
    }

    /**
     * @return list of available room
     */
    public  List<ResidenceRegister> availableRoom(){
       return repository.getAllAvailableRooms();
    }

    /**
     * Places student(s) available rooms
     * Student is checked if has been placed to room already
     * If student has been placed to room, then its placed to available room
     * @return  true if student(s) is placed else false

    public  boolean placeStudentRoom(String residence){
        List<ResidenceRegister>roomsAvailable  = availableRoom();
        List<Student> students = departmentService.studentsPlacedAt(residence);
        boolean placed = false;
        for(int index=0 ; index<roomsAvailable.size(); index++){
                    if(index<students.size()){
                        ResidenceRegister room  = roomsAvailable.get(index);
                        Student student = students.get(index);
                        if(! isPlacedAtRoom(student)) {
                            repository.placeStudent(student.getStudentNumber(), student.getFullName(),
                                    room.getId(), room.getResidence().getResidenceName(), room.getResidence().getBlocks(),
                                    room.getFlat(), room.getRoom());
                            placed = true;
                        }
                    }
                    else break;
        }

        return placed;
    }*/

    /**
     * Check if the student is not already allocated or placed to room at the residence
     * Student has no room if we get null when is being retrieved from database
     * @param student  - student is being checked if the student has room
     * @return false if the has been placed already else  true
     */
    private boolean isPlacedAtRoom(Student student) {
        return true;  //repository.checkStudentHasRoom(student.getStudentNumber(), student.getFullName()) !=null;
    }


}

