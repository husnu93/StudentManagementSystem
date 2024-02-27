package com.project.service.business;

import com.project.exception.BadRequestException;
import com.project.payload.messages.ErrorMessages;
import com.project.payload.request.business.EducationTermRequest;
import com.project.payload.response.business.EducationTermResponse;
import com.project.payload.response.business.ResponseMessage;
import com.project.repository.business.EducationTermRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EducationTermService {
    private final EducationTermRepository educationTermRepository;

    public ResponseMessage<EducationTermResponse> saveEducationTerm(EducationTermRequest educationTermRequest) {
return null;// burası yazılcak


    }



    private void validateEducationTermDatesRequest(EducationTermRequest educationTermRequest){

        if (educationTermRequest.getLastRegistrationDate().isAfter(educationTermRequest.getStartDate())){
            throw new BadRequestException(ErrorMessages.EDUCATION_START_DATE_IS_EARLIER_THAN_LAST_REGISTRATION_DATE);
        }


    }

}
