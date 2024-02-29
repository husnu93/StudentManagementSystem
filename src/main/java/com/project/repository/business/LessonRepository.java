package com.project.repository.business;

import com.project.entity.concretes.business.Lesson;
import com.project.entity.concretes.business.LessonProgram;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LessonRepository extends JpaRepository<Lesson,Long> {
    boolean existsLessonByLessonNameEqualsIgnoreCase(String lessonName);
}
