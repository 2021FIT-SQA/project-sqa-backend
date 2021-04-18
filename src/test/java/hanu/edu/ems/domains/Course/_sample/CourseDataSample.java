package hanu.edu.ems.domains.Course._sample;

import hanu.edu.ems.domains.Course.dto.CreateCourseDTO;
import hanu.edu.ems.domains.Course.dto.UpdateCourseDTO;
import hanu.edu.ems.domains.Course.entity.Course;
import hanu.edu.ems.domains.Department.entity.Department;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

public class CourseDataSample {

    private static final Course course1 = Course.builder()
            .id(1l)
            .registrationCode("FIT2DMA")
            .name("DMA")
            .numberOfCredits(3)
            .requiredSchoolYear(2)
            .activeReleasesCount(0)
            .department(
                    Department.builder()
                            .id(0l)
                            .name("CNTT")
                            .code("FIT")
                            .createdAt(LocalDateTime.now())
                            .updatedAt(LocalDateTime.now())
                            .build()
            )
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build();


    private static final Course course2 = Course.builder()
            .id(1l)
            .registrationCode("FIT3SS2")
            .name("SS2")
            .numberOfCredits(3)
            .requiredSchoolYear(3)
            .activeReleasesCount(0)
            .department(
                    Department.builder()
                            .id(0l)
                            .name("CNTT")
                            .code("FIT")
                            .createdAt(LocalDateTime.now())
                            .updatedAt(LocalDateTime.now())
                            .build()
            )
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build();

    private static final Course course3 = Course.builder()
            .id(1l)
            .registrationCode("FIT3SQA")
            .name("SQA")
            .numberOfCredits(3)
            .requiredSchoolYear(3)
            .activeReleasesCount(0)
            .department(
                    Department.builder()
                            .id(0l)
                            .name("CNTT")
                            .code("FIT")
                            .createdAt(LocalDateTime.now())
                            .updatedAt(LocalDateTime.now())
                            .build()
            )
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build();

    private static final CreateCourseDTO course1DTO = CreateCourseDTO.builder()
            .registrationCode("FIT2DMA")
            .name("DMA")
            .numberOfCredits(3)
            .requiredSchoolYear(2)
            .departmentID(0l)
            .build();

    private static final CreateCourseDTO course2DTO = CreateCourseDTO.builder()
            .registrationCode("FIT3SS2")
            .name("SS2")
            .numberOfCredits(3)
            .requiredSchoolYear(3)
            .departmentID(0l)
            .build();

    private static final CreateCourseDTO course3DTO = CreateCourseDTO.builder()
            .registrationCode("FIT2SQA")
            .name("SQA")
            .numberOfCredits(3)
            .requiredSchoolYear(3)
            .departmentID(0l)
            .build();



    private static final UpdateCourseDTO updateDTO = UpdateCourseDTO.builder()
        .registrationCode("FIT2SQA")
            .departmentID(0l)
            .numberOfCredits(10)
            .requiredSchoolYear(3)
            .name("SQA")
        .build();

    public static Course getExampleValidCourse() {
        return course1;
    }

    public static CreateCourseDTO getExampleValidCreateCourseDTO() {
        return course1DTO.toBuilder().build();
    }

    public static UpdateCourseDTO getExampleValidUpdateCourseDTO() {
        return updateDTO.toBuilder().build();
    }

    public static List<Course> get3Courses() {
        return Arrays.asList(course1.toBuilder().build(), course2.toBuilder().build(), course3.toBuilder().build());
    }

    public static List<CreateCourseDTO> get3CreateCourseDTOs() {
        return Arrays.asList(course1DTO.toBuilder().build(), course2DTO.toBuilder().build(), course3DTO.toBuilder().build());
    }
}
