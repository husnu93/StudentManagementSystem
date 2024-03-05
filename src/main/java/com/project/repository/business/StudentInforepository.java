package com.project.repository.business;

import com.project.entity.concretes.business.StudentInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentInforepository extends JpaRepository<StudentInfo,Long> {

}
