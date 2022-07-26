package com.application.server.repository;
import com.application.server.data.School;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface SchoolRepository  extends JpaRepository<School,Long> { }
