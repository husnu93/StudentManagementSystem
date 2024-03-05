package com.project.payload.request.business;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class StudentInfoRequest {

    @NotNull(message = "Please select Education Term")
    private Long educationTermId;

    @DecimalMax("100.0")
    @DecimalMax("0.0")
    @NotNull(message = "Please enter midtermExam")
    private Double midtermExam;

    @DecimalMax("100.0")
    @DecimalMax("0.0")
    @NotNull(message = "Please enter finalExam")
    private Double finalExam;


    @NotNull(message = "Please Enter absentee")
    private Integer absentee;

    @NotNull(message = "Please enter info")
    @Size(min = 10, max = 200, message = "Info should be at least 10 chars")
    @Pattern(regexp = "\\A(?!\\s*\\Z).+" ,message="Info must consist of the characters .")
    private String infoNote;

    @NotNull(message = "Please select lesson")
    private Long lessonId;

    @NotNull(message = "Please select student")
    private Long studentId;

}
