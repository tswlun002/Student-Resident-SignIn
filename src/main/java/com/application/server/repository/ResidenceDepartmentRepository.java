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
    @Query(value = "select * from ResidenceDepartment where   accommodation='yes' and residence is null" +
            " and blocks is null order by id limit :limit", nativeQuery = true)
    List<ResidenceDepartment> getFirstLimitStudents(long limit);

    @Query(value = "select * from ResidenceDepartment where   accommodation='yes' and residence is null" +
            " and blocks is null order by id desc limit :limit", nativeQuery = true)
    List<ResidenceDepartment> getLastLimitStudents(long limit);
    @Transactional
    @Modifying
    @Query(value = "update  ResidenceDepartment set residenceId=:residenceId, residence=:residenceName,blocks=:blocks," +
            " accommodation=:accommodationStatus where id=:Id", nativeQuery = true)
    void placeStudent(@Param("Id") long Id, Long residenceId, @Param("residenceName") String residenceName, @Param("blocks") String blocks,
                      @Param("accommodationStatus") String accommodationStatus);
    @Query(value = "select ResidenceDepartment.studentId from ResidenceDepartment where residence=:residence",
            nativeQuery = true)
    List<Long> getStudentAtResidence(@Param("residence") String residence);
    @Query(value = "select Residence, blocks from ResidenceDepartment " +
            "where ResidenceDepartment.studentId=:studentNumber",nativeQuery = true)
    String getStudentId(long studentNumber);
}
