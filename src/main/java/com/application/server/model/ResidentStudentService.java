package com.application.server.model;
import com.application.server.data.Residence;
import com.application.server.data.ResidentStudent;
import com.application.server.repository.ResidentStudentRepository;
import com.application.student.data.Student;
import com.application.student.repostory.StudentRepository;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.annotation.PostConstruct;
import java.util.List;
import java.util.stream.IntStream;
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Service
public  class ResidentStudentService {
    @Autowired
    private ResidentStudentRepository repository;
    @Autowired
    private ResidenceService residenceService;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private  ResidentDepartmentService departmentService;

    /***
     * Get Residence details and store them
     */
    @PostConstruct
    public  void fillResidentFeature(){
        List<Residence>residences  =residenceService.getAllResidence();

        residences.forEach(
                residence-> {                                       // block

                    IntStream.range(1, residence.getNumberFloors()+1).forEach(    // each floor
                            floor -> {

                                IntStream.range(1, residence.getNumberFlats()+1).forEach( //each flat
                                        flat -> {

                                            IntStream.range(1, residence.getNumberRoom()+1).forEach(    // each room
                                                    room->{
                                                        try {

                                                            ResidentStudent residentStudent = ResidentStudent.builder().residence(residence).flat((floor * 100 + flat) + "").
                                                            floor(floor).room(getRoom(room)).build();

                                                            repository.save(residentStudent);

                                                        }catch (RuntimeException e){
                                                            throw  new RuntimeException("Block %s, floor %d, flat %d, room %s , "+residence+", "+ floor+", "+(floor*100+flat)+", "+getRoom(room));
                                                        }
                                                        finally {
                                                            System.out.printf("Block %s, floor %d, flat %d, room %s\n",residence, floor, (floor*100+flat),getRoom(room));
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
    public  List<ResidentStudent> availableRoom(){
       return repository.getAllAvailableRooms();
    }

    /**
     * Places student(s) available rooms
     * @return  true if student(s) is placed else false
     */
    public  boolean placeStudentRoom(){
        List<Student> students = departmentService.studentsPlacedAtForestHill();
        List<ResidentStudent>roomsAvailable  = availableRoom();
        boolean placed = false;
        for(int index=0 ; index<roomsAvailable.size(); index++){
                    if(index<students.size()){
                        ResidentStudent room  = roomsAvailable.get(index);
                        Student student = students.get(index);
                       repository.placeStudent(student.getStudentNumber(), student.getFullName(),
                               room.getId(), room.getResidence().getResidenceName(),room.getResidence().getBlocks(),
                               room.getFlat(),room.getRoom());
                       placed=true;
                    }
                    else break;
        }

        return placed;
    }


}

