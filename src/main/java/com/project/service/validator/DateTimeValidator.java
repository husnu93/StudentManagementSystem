package com.project.service.validator;

import com.project.exception.BadRequestException;
import com.project.payload.messages.ErrorMessages;

import java.time.LocalDate;
import java.time.LocalTime;

public class DateTimeValidator {
    public boolean checkTime(LocalTime start, LocalTime stop){
        return start.isAfter(stop) || start.equals(stop) ;
    }

    public void checkTimeWithException(LocalTime start, LocalTime stop){
        if(checkTime(start, stop)){
            throw new BadRequestException(ErrorMessages.TIME_NOT_VALID_MESSAGE);
        }
    }
}
