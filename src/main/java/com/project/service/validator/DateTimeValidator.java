package com.project.service.validator;

import com.project.entity.concretes.business.LessonProgram;
import com.project.exception.BadRequestException;
import com.project.payload.messages.ErrorMessages;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;

@Component
public class DateTimeValidator {
    public boolean checkTime(LocalTime start, LocalTime stop){
        return start.isAfter(stop) || start.equals(stop) ;
    }

    public void checkTimeWithException(LocalTime start, LocalTime stop){
        if(checkTime(start, stop)){
            throw new BadRequestException(ErrorMessages.TIME_NOT_VALID_MESSAGE);
        }
    }

    // Talep edilen yeni Lesson Programlar arasinda cakisma var mi kontrolu

    private void checkDuplicateLessonPrograms(Set<LessonProgram> existLessonProgram,
                                              Set<LessonProgram> lessonProgramRequest) {
        for(LessonProgram requestLessonProgram : lessonProgramRequest ){

            String requestLessonProgramDay = requestLessonProgram.getDay().name();
            LocalTime requestStart = requestLessonProgram.getStartTime();
            LocalTime requestStop = requestLessonProgram.getStopTime();

            if(existLessonProgram.stream()
                    .anyMatch(lessonProgram ->
                            lessonProgram.getDay().name().equals(requestLessonProgramDay)
                                    && (lessonProgram.getStartTime().equals(requestStart)
                                    || (lessonProgram.getStartTime().isBefore(requestStart) && lessonProgram.getStopTime().isAfter(requestStart))
                                    || (lessonProgram.getStartTime().isBefore(requestStop) && lessonProgram.getStopTime().isAfter(requestStop))
                                    || (lessonProgram.getStartTime().isAfter(requestStart) && lessonProgram.getStopTime().isBefore(requestStop))))

            ) {
                throw new BadRequestException(ErrorMessages.LESSON_PROGRAM_ALREADY_EXIST);
            }
        }
    }
    public void checkLessonPrograms(Set<LessonProgram> existLessonProgram,
                                    Set<LessonProgram> lessonProgramRequest) {
        // !!! mevcut ders programi bos ise ve requestten gelen ders programi doluysa
        if(existLessonProgram.isEmpty() && lessonProgramRequest.size()>1){
            checkDuplicateLessonPrograms(lessonProgramRequest);
        } else {
            // !!! talep edilen icinde cakisma var mi
            checkDuplicateLessonPrograms(lessonProgramRequest);

            // !!! talep edilen ile mevcutta cakisma var mi
            checkDuplicateLessonPrograms(existLessonProgram, lessonProgramRequest);
        }
    }

}
