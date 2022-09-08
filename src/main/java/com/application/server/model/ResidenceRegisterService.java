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
    public void placeStudentToRoom(String name, String blocks, int floor, String flat, String room, long studentId) {
        ResidenceRegister register = getRegisterBy(blocks,floor,flat,room);
        ResidenceDepartment department =  getDepartment(name,blocks);
        Student student =  getStudent(department,studentId);

        if(register!=null){
            if(  !checkIfStudentIsAssignedOtherRoom(student)) {
                if (register.getStudent() == null) {
                    try {
                       saveStudent(register,student);
                    } catch (Exception e) {
                        throw new RuntimeException(e.getMessage());
                    }
                } else
                    throw new RuntimeException("Room " + blocks + " " + flat + "" + room + " is assigned to other student");

            }else throw new RuntimeException(student+ "is has room already");

        }else throw new RuntimeException("Residence "+name+" "+blocks +" is not found");
    }

    /**
     * Saves student residence register
     * @param register is the residence register to student to
     * @param student to be saved to register
     */
    private  void saveStudent(ResidenceRegister register,Student student){
        register.setStudent(student);
        repository.save(register);

    }


    /**
     * Get register room
     * @param blocks of the room
     * @param floor  of the room
     * @param flat  of the room
     * @param room to get register of
     * @return register if room is found else null
     */
    public ResidenceRegister getRegisterBy(String blocks,int floor,String flat,String room){
        return repository.getBy(blocks,floor,flat,room);
    }

    /**
     * Fetch student from students assigned to this residence  by res department
     * @param department of the contains residence of students
     * @param studentId of the student being fetched
     * @return student matches the id else throw exception
     */
    public  Student getStudent(ResidenceDepartment department,long studentId){
        List<Student> studentList= department.getStudents().stream().filter(student1 ->
                student1.getStudentNumber()==studentId).toList();

        if(studentList.size()>0) {
            return studentList.get(0);
        }
        else throw  new RuntimeException("The student with id: "+studentId+" is not found.");
    }


    /**
     * Fetch room/register record by student id from  student of the residence
     * @param studentId of student to be fetched from residence register
     * @return student if student with given student id is found else null
     */
    public  ResidenceRegister getStudentRoom(long studentId){
        for(ResidenceRegister register :getResidenceRegister()){
            if(register.getStudent().getStudentNumber()==studentId)return register;
        }
        return null;
    }


    /**
     * Check if student is assigned in other room
     * @param student is being checked
     * @return true if student  have room else throw an exception
     */
    public  boolean checkIfStudentIsAssignedOtherRoom(Student student){
        return getResidenceRegister().stream().anyMatch(register->
                register.getStudent().equals(student));
    }

    /**
     * Fetches all register data
     * @return list of  ResidenceRegister
     */
    public List<ResidenceRegister> getResidenceRegister(){
        return  repository.getResidenceRegister();
    }

    /***
     * Change current room for student  to another room,
     * it can be same flat or another.
     * Changes must occur on same residence
     * @param studentId of the student that changes room
     * @param block new block student changes to
     * @param floor new floor student changes to
     * @param flat  new flat student changes to
     * @param room new room student changes to
     * @return true if student changes room successfully else false
     */
    public boolean changeStudentRoom(long studentId,String block, int floor,String flat, String room){
        ResidenceRegister room1 = getStudentRoom(studentId);
        if(room1 !=null){

            ResidenceRegister newRoom = getRegisterBy(block,floor,flat,room);
            Student studentFromNewRoom =null;

            if(newRoom !=null) {

                if (newRoom.getStudent() != null){

                     studentFromNewRoom = newRoom.getStudent();

                    if(! removeStudentRoomNewRoom(studentFromNewRoom.getStudentNumber()))
                        throw new RuntimeException("Student "+ studentId+" was not successful removed.");
                }

                Student studentFromCurrentRoom = room1.getStudent();
                removeStudentCurrentRoom(room1);

                if(studentFromNewRoom !=null)saveStudent(room1,studentFromNewRoom);

                saveStudent(newRoom,studentFromCurrentRoom);

                return true;
            }else  throw new RuntimeException("Room "+flat+room+"at block "+block+" is not valid");
        }
        return false;

    }

    /**
     * Check if room is empty
     * Room is empty if no student assigned to it
     * @param block new block student changes to
     * @param floor new floor student changes to
     * @param flat  new flat student changes to
     * @param room new room student changes to
     * @return true if no student assigned to room else false
     */
    public  boolean checkIfRoomEmpty(String block, int floor,String flat, String room){
        ResidenceRegister register =getRegisterBy(block,floor,flat, room);
        return  register!=null && register.getStudent()==null;
    }

    /**
     * Remove student room by student id
     * @param studentId  of student to be removed from room
     * @return true if successfully removed else false
     */
    public  boolean removeStudentRoomNewRoom(long studentId){
        ResidenceRegister register = getStudentRoom(studentId);
        if(register !=null){
            removeStudentCurrentRoom(register);
            return  true;
        }
        return false;
    }

    /**
     * Removes all students from room
     */
    public  void removeAllStudents(){
        getResidenceRegister().forEach(
                register -> {
                    register.setStudent(null);
                    repository.save(register);
                }
        );
    }

    /**
     * Remove student from residence register
     * @param student to be removed from register
     * @return true if student successfully removed else false
     */
    public  boolean removeStudent(Student student){
        if(student ==null)return  false;

        ResidenceRegister register = getStudentRoom(student.getStudentNumber());
        if(register==null)return false;
        try {
            removeStudentCurrentRoom(register);
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
        return true;
    }
    /**
     * Remove student from the room
     * @param register record of the room here student is being removed from
     */
    private void removeStudentCurrentRoom(ResidenceRegister register){
        register.setStudent(null);
        repository.save(register);
    }

}

