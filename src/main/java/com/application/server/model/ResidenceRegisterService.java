package com.application.server.model;
import com.application.server.data.Residence;
import com.application.server.data.ResidenceDepartment;
import com.application.server.data.ResidenceRegister;
import com.application.server.repository.ResidenceRegisterRepository;
import com.application.student.data.Student;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
    private  ResidentDepartmentService departmentService;

    /**
     * Fetch residence by name and blocks
     * @param name of the residence
     * @param block of the residence being fetched
     * @return residence matched name and block
     */
    public  Residence getResidence(String name,String block){
        return residenceService.getResidence(name,block);
    }

    /**
     * Fetch department by name and blocks of residence
     * @param name of the residence
     * @param block of the residence being fetched
     * @return department matched name and block of residence
     */
     public  ResidenceDepartment getDepartment(String name,String block){
         Residence residence  = getResidence(name,block);
         if(residence !=null && residence.getDepartment()!=null){
             return  departmentService.getDepartment(residence.getDepartment().getId());
         }
         return null;
     }

    /**
     * Label/name rooms of residence
     * Store  information   block, floor, flat for each room
     * Rooms are labels by range of letters from A-to range.
     * If there are 3 rooms in a flat then room will be labeled by A-C
     * @param name of the residence
     * @param block  at the residence
     */
     @Transactional
     @Modifying
    public  void residenceRoom(String name, String block){
         ResidenceDepartment department = getDepartment(name,block);
         if(department !=null && department.getStudents().size()>0 && department.getResidence()!=null){
             Residence residence = department.getResidence();
            int floors = residence.getNumberFloors();
            int flats  = residence.getNumberFlats();
            int rooms  = residence.getNumberRoom();
            IntStream.range(1,floors+1).forEach(floor->
                    IntStream.range(1,flats+1).forEach(flat-> IntStream.range(1,rooms+1).forEach(room-> {
                                try {

                                    ResidenceRegister residenceRegister =
                                            ResidenceRegister.builder().residence(residence).flat((floor * 100 + flat) + "").
                                                    floor(floor).room(getRoom(room)).build();

                                    boolean notExists = repository.getBy(residence.getBlocks(), floor,
                                            (floor * 100 + flat) + "", getRoom(room)) == null;

                                    if (notExists) repository.save(residenceRegister);


                                } catch (RuntimeException e) {
                                    throw new RuntimeException(residence.getResidenceName() + " " + residence.getBlocks() + ", " +
                                            floor + ", " + (floor * 100 + flat) + ", " + getRoom(room) + "\n" + e.getMessage());
                                } finally {
                                    System.out.printf("Block %s, floor %d, flat %d, room %s\n", residence.getBlocks(),
                                            floor, (floor * 100 + flat), getRoom(room));
                                }

                            })

                    )
            );
         }

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
     * Place or assign student to room
     * Check if the student is registered in the residence by department
     * Check if the room is empty or not assigned to other student
     * @param name of residence of the room where student is being assigned
     * @param blocks of residence of the room where student is being assigned
     * @param floor  of the room at the residence
     * @param flat   of room  assigned to student
     * @param room   assigned to student
     * @param studentId  being place in the room
     */
    @Transactional
    @Modifying
    public void placeStudentToRoom(String name, String blocks, int floor, String flat, String room, long studentId) {
        ResidenceRegister register = repository.getBy(blocks,floor,flat,room);
        ResidenceDepartment department =  getDepartment(name,blocks);
        Student student =  getStudent(department,studentId);
        checkIfStudentIsNotAssignedOtherRoom(student);
        if(register!=null){
            if(register.getStudent()==null) {
                try {
                    register.setStudent(student);
                    repository.save(register);

                } catch (Exception e) {
                    throw new RuntimeException(e.getMessage());
                }
            }else  throw new RuntimeException("Room "+blocks +" "+flat+""+room+" is assigned to other student");

        }else throw new RuntimeException("Residence "+name+" "+blocks +" is not found");
    }

    /**
     * Fetch student from students assigned to this residence  by res department
     * @param department of the contains residence of students
     * @param studentId of the student being fetched
     * @return student matches the id else throw exception
     */
    public  Student getStudent(ResidenceDepartment department,long studentId){
        Student student= department.getStudents().stream().filter(student1 ->
                student1.getStudentNumber()==studentId).toList().get(0);
        if(student!=null) return  student;
        else throw  new RuntimeException("The student: "+studentId+" is not found.");
    }

    /**
     * Check if student is not assigned in other room
     * @param student is being checked
     * @return true if student does not have room else throw an exception
     */
    public  boolean checkIfStudentIsNotAssignedOtherRoom(Student student){
        boolean assigned = repository.findAll().stream().anyMatch(register->
                register.getStudent() != null && register.getStudent().equals(student));

        if(assigned)throw  new RuntimeException(student+" is already has assigned to room");
        return true;
    }

}

