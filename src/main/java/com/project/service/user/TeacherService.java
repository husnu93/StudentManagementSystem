package com.project.service.user;

import com.project.entity.concretes.user.User;
import com.project.entity.concretes.user.UserRole;
import com.project.entity.enums.RoleType;
import com.project.exception.ConflictException;
import com.project.payload.mappers.UserMapper;
import com.project.payload.messages.ErrorMessages;
import com.project.payload.messages.SuccessMessages;
import com.project.payload.request.user.TeacherRequest;
import com.project.payload.response.business.ResponseMessage;
import com.project.payload.response.user.StudentResponse;
import com.project.payload.response.user.TeacherResponse;
import com.project.payload.response.user.UserResponse;
import com.project.repository.user.UserRepository;
import com.project.repository.user.UserRoleRepository;
import com.project.service.business.LessonProgramService;
import com.project.service.helper.MethodHelper;
import com.project.service.validator.UniquePropertyValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TeacherService {

    private final UserRoleRepository userRoleRepository;
    private final UniquePropertyValidator uniquePropertyValidator;
    private final UserMapper userMapper;
    private final UserRoleService userRoleService;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final MethodHelper methodHelper;
    private final LessonProgramService lessonProgramService;
    public ResponseMessage<TeacherResponse> saveTeacher(TeacherRequest teacherRequest) {

        lessonProgramService.getLessonProgramById(teacherRequest.getLessonsIdList());


        //!!! unique konntrolu
        uniquePropertyValidator.checkDuplicate(teacherRequest.getUsername(),teacherRequest.getSsn()
                ,teacherRequest.getPhoneNumber(),teacherRequest.getEmail());

        //!!! DTO -> POJO
        User  teacher = userMapper.mapTeacherRequestToUser(teacherRequest);

        // rolu setledik dto da olup da pojo da olmadığı için rolu ekledik

        teacher.setUserRole(userRoleService.getUserRole(RoleType.TEACHER));

        lessonProgramService.getLessonProgramById(teacherRequest.getLessonsIdList());

        teacher.setPassword(passwordEncoder.encode(teacherRequest.getPassword()));


        if (teacherRequest.getIsAdvisorTeacher()){
            teacher.setIsAdvisor(Boolean.TRUE);
        }else teacher.setIsAdvisor(Boolean.FALSE);


        User savedTeacher = userRepository.save(teacher);
        return ResponseMessage.<TeacherResponse> builder()
                .message(SuccessMessages.TEACHER_SAVED)
                .httpStatus(HttpStatus.CREATED)
                .object(userMapper.mapUserToTeacherResponse(savedTeacher))
                .build();

    }

    public ResponseMessage<TeacherResponse> updateTeacherForManagers(TeacherRequest teacherRequest, Long userId) {
        //!!! id kontrolu
       User user =  methodHelper.isUserExist(userId);
       // parametrede gelen User gerçekten Teacher mı kontrolu
        methodHelper.checkRole(user,RoleType.TEACHER);

        // TODO : Lesson programlar getiriliyor

        //!!!! unique kontrolu

        uniquePropertyValidator.checkUniqueProperties(user,teacherRequest);

        //DTO -> POJO

        User updatedTeacher =  userMapper.mapTeacherRequsetToUpdatedUser(teacherRequest,userId);

        updatedTeacher.setPassword(passwordEncoder.encode(teacherRequest.getPassword()));

        // TODO : lesson programlar setlenecek

        updatedTeacher.setUserRole(userRoleService.getUserRole(RoleType.TEACHER));

       User savedTeacher =  userRepository.save(updatedTeacher);

       return ResponseMessage.<TeacherResponse>builder()
               .object(userMapper.mapUserToTeacherResponse(savedTeacher))
               .message(SuccessMessages.TEACHER_UPDATE)
               .httpStatus(HttpStatus.OK)
               .build();
    }

    public List<StudentResponse> getAllStudentByAdvisorUsername(String userName) {


        // önce böyle bi username ile birisi var mı kontrolu
      User teacher =   methodHelper.isUserExistByUsername(userName);
        //!!!! isAdvisor kontrolü
        methodHelper.checkAdvisor(teacher);

        return userRepository.findByAdvisorTeacherId(teacher.getId())
                .stream()
                .map(userMapper::mapUserToStudentResponse)
                .collect(Collectors.toList());
    }


    public ResponseMessage<UserResponse> saveAdvisorTeacher(Long teacherId) {
       User teacher =  methodHelper.isUserExist(teacherId);

       methodHelper.checkRole(teacher,RoleType.TEACHER);

       // id ile gelen teacher zaten advisor mı kontrolu
       if (Boolean.TRUE.equals(teacher.getIsAdvisor())) {
           throw new ConflictException(String.format(ErrorMessages.ALREADY_EXIST_ADVISOR_MESSAGE,teacherId));
       }

       teacher.setIsAdvisor(Boolean.TRUE);
       userRepository.save(teacher);

       return  ResponseMessage.<UserResponse>builder()
               .message(SuccessMessages.ADVISOR_TEACHER_SAVED)
               .object(userMapper.mapUserToUserResponse(teacher))
               .httpStatus(HttpStatus.OK)
               .build();

    }

    public ResponseMessage<UserResponse> deleteAdvisorTeacherById(Long id) {
        User teacher = methodHelper.isUserExist(id);
        methodHelper.checkRole(teacher,RoleType.TEACHER);
        methodHelper.checkAdvisor(teacher);

        teacher.setIsAdvisor(Boolean.FALSE);
        userRepository.save(teacher);

        //silinen rehber öğretmenin  öğrencileri boşta kaldı öğrencisi varsa koparmamız lazım

        List<User> allStudents = userRepository.findByAdvisorTeacherId(id);
        if (!allStudents.isEmpty()){
            allStudents.forEach(student->student.setAdvisorTeacherId(null));
        }

        return ResponseMessage.<UserResponse>builder()
                .message(SuccessMessages.ADVISOR_TEACHER_DELETED)
                .object(userMapper.mapUserToUserResponse(teacher))
                .httpStatus(HttpStatus.OK)
                .build();


    }

    public List<UserResponse> getAllAdvisorTeacher() {
        return userRepository.findAllByAdvisor(Boolean.TRUE)
                .stream()
                .map(userMapper::mapUserToUserResponse)
                .collect(Collectors.toList());




    }
}
