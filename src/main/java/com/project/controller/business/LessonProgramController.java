package com.project.controller.business;

import com.project.payload.request.business.LessonProgramRequest;
import com.project.payload.response.business.EducationTermResponse;
import com.project.payload.response.business.LessonProgramResponse;
import com.project.payload.response.business.ResponseMessage;
import com.project.service.business.LessonProgramService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/lessonPrograms")
@RequiredArgsConstructor
public class LessonProgramController {

    private final LessonProgramService lessonProgramService;

    @PostMapping("/save") // http://localhost:8080/lessonPrograms/save  + POST  + JSON
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER','ASSISTANT_MANAGER')")
    public ResponseMessage<LessonProgramResponse> saveLessonProgram(@RequestBody @Valid
                                                                    LessonProgramRequest lessonProgramRequest) {
        return lessonProgramService.saveLessonProgram(lessonProgramRequest);

    }

    @GetMapping("/getAll") // http://localhost:8080/lessonPrograms/getAll
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER','ASSISTANT_MANAGER','TEACHER','STUDENT')")
    public List<LessonProgramResponse> getAllLesonProgramByList(){
        return lessonProgramService.getAllLesonProgramByList();
    }



    @GetMapping("/getById/{id}") //http://localhost:8080/lessonPrograms/
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER','ASSISTANT_MANAGER')")
    public LessonProgramResponse getLessonProgramById(@PathVariable Long id){
        return lessonProgramService.getLessonProgramById(id);
    }


    // herhangi bir kullanıcı ataması yapılmamış derss programı getirme
    @GetMapping("/getAllUnassigned")//
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER','ASSISTANT_MANAGER','TEACHER','STUDENT')")
    public List<LessonProgramResponse> getAllUnassigned(){
        return lessonProgramService.getAllUnassigned();
    }

    @GetMapping("/getAllAssigned")
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER','ASSISTANT_MANAGER','TEACHER','STUDENT')")

    public List<LessonProgramResponse> getAllAssigned(){
        return lessonProgramService.getAllAssigned();
    }

    // Odev : delete -->> /delete/{id}

//   @DeleteMapping("/delete/{id}")
//   @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER','ASSISTANT_MANAGER')")
//   public ResponseMessage deleteById(@PathVariable Long id){
//       return lessonProgramService.deleteById(id);
//   }


    @DeleteMapping("/delete/{id}") //http://localhost:8080/lessonPrograms/delete/1
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER','ASSISTANT_MANAGER')")
    public ResponseMessage deleteLessonProgramById(@PathVariable Long id){
        return lessonProgramService.deleteLessonProgramById(id);
    }




    // get AllWithPage -->> / getAllLessonProgramByPage

///   @GetMapping("/getAllLessonProgramByPage")// http:localhost:8080/LessonProgram/getAllLessonProgramByPage
///   @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER','ASSISTANT_MANAGER','TEACHER')")
///   public Page<LessonProgramResponse> getAllLessonProgramByPage(
///           @RequestParam(value = "page", defaultValue = "0") int page,
///           @RequestParam(value = "size",defaultValue = "10") int size,
///           @RequestParam(value = "sort",defaultValue = "startDate") String sort,
///           @RequestParam(value = "type",defaultValue = "desc") String type

///   ){
///       return lessonProgramService.getAllLessonProgramByPage(page,size,sort,type);
///   }

    @GetMapping("/getAllLessonProgramByPage") // http://localhost:8080/lessonPrograms/getAllLessonProgramByPage?page=0&size=1&sort=id&type=desc
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER','ASSISTANT_MANAGER','TEACHER','STUDENT')")
    public Page<LessonProgramResponse> getAllLessonProgramByPage (
            @RequestParam(value = "page") int page,
            @RequestParam(value = "size") int size,
            @RequestParam(value = "sort") String sort,
            @RequestParam(value = "type") String type){
        return lessonProgramService.getAllLessonProgramByPage(page,size,sort,type);
    }


    // bir öğretmenin kendine ait lesson programları getiriyor
    @GetMapping("/getAllLessonProgramByTeacher")
    @PreAuthorize("hasAnyAuthority('TEACHER')")
    public Set<LessonProgramResponse> getAllLessonProgramByTeacherUsername(HttpServletRequest httpServletRequest){
        return lessonProgramService.getAllLessonProgramByUser(httpServletRequest);
    }

    // bir Ogrenci kendine ait lesson Programlari getiriyor
    @GetMapping("/getAllLessonProgramByStudent") // http://localhost:8080/lessonPrograms/getAllLessonProgramByStudent
    @PreAuthorize("hasAnyAuthority('STUDENT')")
    public Set<LessonProgramResponse> getAllLessonProgramByStudent(HttpServletRequest httpServletRequest){
        return lessonProgramService.getAllLessonProgramByUser(httpServletRequest);
    }





    @GetMapping("/getAllLessonProgramByTeacherId/{teacherId}")
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER','ASSISTANT_MANAGER')")
    public Set<LessonProgramResponse> getByTeacherId(@PathVariable Long teacherId){
        return lessonProgramService.getByTeacherId(teacherId);
    }

    @GetMapping("/getAllLessonProgramByStudentId/{studentId}") // http://localhost:8080/lessonPrograms/getAllLessonProgramByStudentId/3
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER','ASSISTANT_MANAGER')")
    public Set<LessonProgramResponse> getByStudentId(@PathVariable Long studentId){
        return lessonProgramService.getByStudentId(studentId);
    }



}

