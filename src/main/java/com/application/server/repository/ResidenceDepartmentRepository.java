package com.application.server.repository;
import com.application.server.data.ResidenceDepartment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
@Repository
public interface ResidenceDepartmentRepository extends JpaRepository<ResidenceDepartment,Long> {
    @Query(value = "select * from ResidenceDepartment where studentId <:id and accommodation='yes' and residence is null" +
            " and blocks is null", nativeQuery = true)
    List<ResidenceDepartment> getStudentWithNoRes(long id);
    @Transactional
    @Modifying
    @Query(value = "update  ResidenceDepartment set residence=:residenceName,blocks=:blocks, accommodation=:accommodationStatus where id=:Id",
            nativeQuery = true)
    void placeStudent(@Param("Id") long Id, @Param("residenceName") String residenceName,@Param("blocks") String blocks,
                      @Param("accommodationStatus") String accommodationStatus);
    @Query(value = "select ResidenceDepartment.studentId from ResidenceDepartment where residence=:residence",
            nativeQuery = true)
    List<Long> getStudentAtResidence(@Param("residence") String residence);
}
