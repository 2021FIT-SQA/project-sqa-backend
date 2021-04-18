package hanu.edu.ems.domains.Course;

import com.fasterxml.jackson.databind.ObjectMapper;
import hanu.edu.ems.domains.Authority.entity.Authority;
import hanu.edu.ems.domains.Authority.entity.AuthorityName;
import hanu.edu.ems.domains.Course._sample.CourseDataSample;
import hanu.edu.ems.domains.Course.dto.CreateCourseDTO;
import hanu.edu.ems.domains.Course.dto.UpdateCourseDTO;
import hanu.edu.ems.domains.Course.entity.Course;
import hanu.edu.ems.domains.Department.DepartmentRepository;
import hanu.edu.ems.domains.Department._sample.DepartmentDataSample;
import hanu.edu.ems.domains.Department.entity.Department;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.RequestPostProcessor;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.properties")
@Slf4j
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class CourseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CourseService courseService;

    @MockBean
    private DepartmentRepository departmentRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void whenCreateValid_thenReturnNewCourse() throws Exception {

        CreateCourseDTO createCourseDTO = CourseDataSample.getExampleValidCreateCourseDTO();

        createCourseDTO.setDepartmentID(1L);

        Course expectedResult = CourseDataSample.getExampleValidCourse();

        Department fit = DepartmentDataSample.getOneValidDepartment();
        createCourseDTO.setDepartmentID(fit.getId());

        given(departmentRepository.findById(1L)).willReturn(Optional.of(fit));
        given(courseService.create(Mockito.any(CreateCourseDTO.class))).willReturn(expectedResult);

        String content = objectMapper.writeValueAsString(createCourseDTO);

        mockMvc.perform(post("/courses")
            .contentType(MediaType.APPLICATION_JSON)
            .with(adminAuthority())
            .content(content)
        )
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//            .andExpect(jsonPath("$.username").value(expectedResult.getUsername()))
//            .andExpect(jsonPath("$.phoneNumber").value(expectedResult.getPhoneNumber()))
//            .andExpect(jsonPath("$.email").value(expectedResult.getEmail()))
//            .andExpect(jsonPath("$.firstName").value(expectedResult.getFirstName()))
//            .andExpect(jsonPath("$.lastName").value(expectedResult.getLastName()))
//            .andExpect(jsonPath("$.gender").value("Male"))
//            .andExpect(jsonPath("$.authorities").isArray())
////            .andExpect(jsonPath("$.authorities[0].authority").value("course"))
////            .andExpect(jsonPath("$.authorities[1]").doesNotExist())
//            .andExpect(jsonPath("$.dob").isArray())
//            .andExpect(jsonPath("$.createdAt").isArray())
//            .andExpect(jsonPath("$.updatedAt").isArray())
//            .andExpect(jsonPath("$.sinceYear").value(expectedResult.getSinceYear()))
////            .andExpect(jsonPath("$.department").isMap())
////            .andExpect(jsonPath("$.department.id").value(1L))
                .andExpect(jsonPath("$.registrationCode").value(expectedResult.getRegistrationCode()))
                .andExpect(jsonPath("$.name").value(expectedResult.getName()))
                .andExpect(jsonPath("$.numberOfCredit").value(expectedResult.getNumberOfCredits()))
                .andExpect(jsonPath("$.requiredSchoolYear").value(expectedResult.getRequiredSchoolYear()))
                .andExpect(jsonPath("$.department.id").value(expectedResult.getDepartment().getId()))
            .andDo(print());
    }

    @Test
    void whenAdminGetAll_thenReturnAllCourses() throws Exception {

        List<Course> courseList = CourseDataSample.get3Courses();

        Department department = DepartmentDataSample.getOneValidDepartment();
        department.setId(1L);

        Course course0 = courseList.get(0);
        Course course1 = courseList.get(1);
        Course course2 = courseList.get(2);

//        Authority authority = Authority.builder()
//            .id(1L)
//            .name(AuthorityName.course)
//            .build();

//        cous.setAuthorities(Collections.singletonList(authority));
        course0.setDepartment(department);
//
//        course1.setAuthorities(Collections.singletonList(authority));
        course1.setDepartment(department);
//
//        course2.setAuthorities(Collections.singletonList(authority));
        course2.setDepartment(department);

        given(courseService.findAll()).willReturn(courseList);

        Department fit = DepartmentDataSample.getOneValidDepartment();

        given(departmentRepository.findById(1L)).willReturn(Optional.of(fit));

        mockMvc.perform(get("/courses/all").with(adminAuthority()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))

            .andExpect(jsonPath("$[0].registrationCode").value(course0.getRegistrationCode()))
            .andExpect(jsonPath("$[0].name").value(course0.getName()))
            .andExpect(jsonPath("$[0].numberOfCredit").value(course0.getNumberOfCredits()))
            .andExpect(jsonPath("$[0].requiredSchoolYear").value(course0.getRequiredSchoolYear()))
            .andExpect(jsonPath("$[0].department").isMap())
            .andExpect(jsonPath("$[0].department.id").value(1l))
            .andExpect(jsonPath("$[0].createdAt").isArray())
            .andExpect(jsonPath("$[0].updatedAt").isArray())

            .andExpect(jsonPath("$[1].registrationCode").value(course0.getRegistrationCode()))
            .andExpect(jsonPath("$[1].name").value(course0.getName()))
            .andExpect(jsonPath("$[1].numberOfCredit").value(course0.getNumberOfCredits()))
            .andExpect(jsonPath("$[1].requiredSchoolYear").value(course0.getRequiredSchoolYear()))
            .andExpect(jsonPath("$[1].department").isMap())
            .andExpect(jsonPath("$[1].department.id").value(1l))
            .andExpect(jsonPath("$[1].createdAt").isArray())
            .andExpect(jsonPath("$[1].updatedAt").isArray())

            .andExpect(jsonPath("$[2].registrationCode").value(course0.getRegistrationCode()))
            .andExpect(jsonPath("$[2].name").value(course0.getName()))
            .andExpect(jsonPath("$[2].numberOfCredit").value(course0.getNumberOfCredits()))
            .andExpect(jsonPath("$[2].requiredSchoolYear").value(course0.getRequiredSchoolYear()))
            .andExpect(jsonPath("$[2].department").isMap())
            .andExpect(jsonPath("$[2].department.id").value(1l))
            .andExpect(jsonPath("$[2].createdAt").isArray())
            .andExpect(jsonPath("$[2].updatedAt").isArray())
            .andDo(print());
    }

    private RequestPostProcessor adminAuthority() {
        return user("admin").authorities(new SimpleGrantedAuthority("ADMIN"));
    }

    @Test
    void whenAdminGetManyCourses_thenReturnCoursePage() throws Exception {
        Page<Course> coursePage = Page.empty();
        Pageable pageable = Pageable.unpaged();

        given(courseService.findAll(pageable)).willReturn(coursePage);

        mockMvc.perform(get("/courses").with(adminAuthority()))
            .andExpect(status().isOk())
            .andDo(print());
    }

    @Test
    void whenAdminGetCourseWithID_thenReturnCourse() throws Exception {
        Course course = CourseDataSample.getExampleValidCourse();

        Authority authority = Authority.builder()
            .id(1L)
            .name(AuthorityName.TEACHER)
            .build();

//        course.setAuthorities(Collections.singletonList(authority));
        Department department = DepartmentDataSample.getOneValidDepartment();

        course.setDepartment(department);
        department.setId(1L);

        given(courseService.getById(1L)).willReturn(course);

        mockMvc.perform(get("/courses/1").with(adminAuthority()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[2].registrationCode").value(course.getRegistrationCode()))
                .andExpect(jsonPath("$[2].name").value(course.getName()))
                .andExpect(jsonPath("$[2].numberOfCredit").value(course.getNumberOfCredits()))
                .andExpect(jsonPath("$[2].requiredSchoolYear").value(course.getRequiredSchoolYear()))
                .andExpect(jsonPath("$[2].department").isMap())
                .andExpect(jsonPath("$[2].department.id").value(1l))
                .andExpect(jsonPath("$[2].createdAt").isArray())
                .andExpect(jsonPath("$[2].updatedAt").isArray())
            .andDo(print());
    }

    @Test
    void whenAdminUpdateByID_thenReturnUpdatedResult() throws Exception {

        UpdateCourseDTO createCourseDTO = CourseDataSample.getExampleValidUpdateCourseDTO();

        createCourseDTO.setDepartmentID(1L);

        Course expectedResult = CourseDataSample.getExampleValidCourse();

        Department fit = DepartmentDataSample.getOneValidDepartment();
        createCourseDTO.setDepartmentID(fit.getId());

        given(departmentRepository.findById(eq(1L))).willReturn(Optional.of(fit));
        given(courseService.updateById(eq(1L), Mockito.any(UpdateCourseDTO.class))).willReturn(expectedResult);

        String content = objectMapper.writeValueAsString(createCourseDTO);

        mockMvc.perform(put("/courses/1")
            .contentType(MediaType.APPLICATION_JSON)
            .with(adminAuthority())
            .content(content)
        )
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[2].registrationCode").value(expectedResult.getRegistrationCode()))
                .andExpect(jsonPath("$[2].name").value(expectedResult.getName()))
                .andExpect(jsonPath("$[2].numberOfCredit").value(expectedResult.getNumberOfCredits()))
                .andExpect(jsonPath("$[2].requiredSchoolYear").value(expectedResult.getRequiredSchoolYear()))
                .andExpect(jsonPath("$[2].department").isMap())
                .andExpect(jsonPath("$[2].department.id").value(1l))
                .andExpect(jsonPath("$[2].createdAt").isArray())
                .andExpect(jsonPath("$[2].updatedAt").isArray())
//            .andExpect(jsonPath("$.department").isMap())
//            .andExpect(jsonPath("$.department.id").value(1L))
            .andDo(print());
    }

    @Test
    void whenAdminDeleteByID_thenSuccess() throws Exception {
        mockMvc.perform(delete("/courses/1").with(adminAuthority()))
            .andExpect(status().isOk());
    }
}