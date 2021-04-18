package hanu.edu.ems.domains.Course;

import com.fasterxml.jackson.databind.ObjectMapper;
import hanu.edu.ems.domains.Course._sample.CourseDataSample;
import hanu.edu.ems.domains.Course.dto.CreateCourseDTO;
import hanu.edu.ems.domains.Course.dto.UpdateCourseDTO;
import hanu.edu.ems.domains.Course.entity.Course;
import hanu.edu.ems.domains.Department.DepartmentService;
import hanu.edu.ems.domains.Department._sample.DepartmentDataSample;
import hanu.edu.ems.domains.Department.dto.CreateDepartmentDTO;
import hanu.edu.ems.domains.Department.entity.Department;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.RequestPostProcessor;

import java.util.List;

import static org.hamcrest.Matchers.contains;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Slf4j
@TestPropertySource(locations = "classpath:application-test.properties")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
public class CourseControllerIntegrationTest {

    private Department department;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CourseService courseService;

    @Autowired
    private DepartmentService departmentService;

    @BeforeEach
    public void setUp() {
        CreateDepartmentDTO createDepartmentDTO = DepartmentDataSample.getOneValidCreateDepartmentDTO();
        this.department = departmentService.create(createDepartmentDTO);
    }

    private RequestPostProcessor adminAuthority() {
        return user("admin").authorities(new SimpleGrantedAuthority("ADMIN"));
    }

    @Test
    void whenCreateWithValidDataAsAdmin_thenReturnCourse() throws Exception {
        CreateCourseDTO createCourseDTO = CreateCourseDTO.builder()
                .registrationCode("FIT2DMA")
                .name("DMA")
                .numberOfCredits(3)
                .requiredSchoolYear(2)
                .departmentID(1l)
                .build();

        CreateCourseDTO expectedCourseResult = CreateCourseDTO.builder()
                .registrationCode("FIT2DMA")
                .name("DMA")
                .numberOfCredits(3)
                .requiredSchoolYear(2)
                .departmentID(1l)
                .build();



        mockMvc.perform(post("/courses")
            .with(adminAuthority())
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(createCourseDTO))
        )
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.department.id").value(expectedCourseResult.getDepartmentID()))
//            .andExpect(jsonPath("$.email").value(expectedCourseResult.getEmail()))
//            .andExpect(jsonPath("$.dob").isArray())
//            .andExpect(jsonPath("$.firstName").value(expectedcourseResult.getFirstName()))
//            .andExpect(jsonPath("$.lastName").value(expectedcourseResult.getLastName()))
//            .andExpect(jsonPath("$.gender").value("Male"))
//            .andExpect(jsonPath("$.fatherName").value(expectedcourseResult.getFatherName()))
//            .andExpect(jsonPath("$.motherName").value(expectedcourseResult.getMotherName()))
//            .andExpect(jsonPath("$.fullAddress").value(expectedcourseResult.getFullAddress()))
//            .andExpect(jsonPath("$.sinceYear").value(expectedcourseResult.getSinceYear()))
//            .andExpect(jsonPath("$.phoneNumber").value(expectedcourseResult.getPhoneNumber()))
//            .andExpect(jsonPath("$.username").value(expectedcourseResult.getUsername()))
//            .andExpect(jsonPath("$.password").doesNotExist())
                .andExpect(jsonPath("$.registrationCode").value(expectedCourseResult.getRegistrationCode()))
                .andExpect(jsonPath("$.name").value(expectedCourseResult.getName()))
                .andExpect(jsonPath("$.numberOfCredits").value(expectedCourseResult.getNumberOfCredits()))
                .andExpect(jsonPath("$.requiredSchoolYear").value(expectedCourseResult.getRequiredSchoolYear()))
                .andDo(print());
    }

    @Test
    public void whenGetManyAsAdmin_thenReturnCoursesPage() throws Exception {
        // GIVEN
        List<CreateCourseDTO> sampleDTOs = CourseDataSample.get3CreateCourseDTOs();

        CreateCourseDTO course2DTO = sampleDTOs.get(0);
        course2DTO.setDepartmentID(this.department.getId());

        CreateCourseDTO course1DTO = sampleDTOs.get(1);
        course1DTO.setDepartmentID(this.department.getId());

        CreateCourseDTO course3DTO = sampleDTOs.get(2);
        course3DTO.setDepartmentID(this.department.getId());

        Course course2 = courseService.create(course2DTO);
        Course course1 = courseService.create(course1DTO);
        Course course3 = courseService.create(course3DTO);

        mockMvc.perform(get("/courses")
            .queryParam("size", "3")
            .queryParam("page", "0")
            .with(adminAuthority())
        )
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.content").isArray())
//            .andExpect(jsonPath("$.content[*].department.id", contains(course2.getDepartment().getId().intValue(),
//                course1.getDepartment().getId().intValue(), course3.getDepartment().getId().intValue())))
//            .andExpect(jsonPath("$.content[*].email", contains(course2.getEmail(), course1.getEmail(), course3.getEmail())))
//            .andExpect(jsonPath("$.content[*].dob").isArray())
//            .andExpect(jsonPath("$.content[*].firstName", contains(course2.getFirstName(), course1.getFirstName(),
//                course3.getFirstName())))
//            .andExpect(jsonPath("$.content[*].lastName", contains(course2.getLastName(), course1.getLastName(), course3.getLastName())))
//            .andExpect(jsonPath("$.content[*].gender", contains("Male", "Female", "Male")))
//            .andExpect(jsonPath("$.content[*].fatherName", contains(course2.getFatherName(), course1.getFatherName(),
//                course3.getFatherName())))
//            .andExpect(jsonPath("$.content[*].motherName", contains(course2.getMotherName(), course1.getMotherName(),
//                course3.getMotherName())))
//            .andExpect(jsonPath("$.content[*].fullAddress", contains(course2.getFullAddress(), course1.getFullAddress(),
//                course3.getFullAddress())))
//            .andExpect(jsonPath("$.content[*].sinceYear", contains(course2.getSinceYear(), course1.getSinceYear(),
//                course3.getSinceYear())))
//            .andExpect(jsonPath("$.content[*].phoneNumber", contains(course2.getPhoneNumber(), course1.getPhoneNumber(),
//                course3.getPhoneNumber())))
//            .andExpect(jsonPath("$.content[*].username", contains(course2.getUsername(), course1.getUsername(),
//                course3.getUsername())))
                .andExpect(jsonPath("$.content[*].departmentID", contains(course1.getDepartment().getId()
                        ,course2.getDepartment().getId()
                        ,course3.getDepartment().getId())))
                .andExpect(jsonPath("$.content[*].registrationCode", contains(course1.getRegistrationCode()
                        ,course2.getRegistrationCode()
                        ,course3.getRegistrationCode())))
                .andExpect(jsonPath("$.content[*].name", contains(course1.getName(), course2.getName(), course3.getName())))
                .andExpect(jsonPath("$.content[*].numberOfCredits", contains(
                        course1.getNumberOfCredits(),
                        course2.getNumberOfCredits(),
                        course3.getNumberOfCredits()
                )))
                .andExpect(jsonPath("$.content[*].requiredSchoolYear", contains(
                        course1.getRequiredSchoolYear(),
                        course2.getRequiredSchoolYear(),
                        course3.getRequiredSchoolYear()
                )))
            .andDo(print());
    }

    @Test
    public void whenGetAllAsAdmin_thenReturnAllCourses() throws Exception {

        List<CreateCourseDTO> sampleDTOs = CourseDataSample.get3CreateCourseDTOs();

        CreateCourseDTO course2DTO = sampleDTOs.get(0);
        course2DTO.setDepartmentID(this.department.getId());

        CreateCourseDTO course1DTO = sampleDTOs.get(1);
        course1DTO.setDepartmentID(this.department.getId());

        CreateCourseDTO course3DTO = sampleDTOs.get(2);
        course3DTO.setDepartmentID(this.department.getId());

        Course course2 = courseService.create(course2DTO);
        Course course1 = courseService.create(course1DTO);
        Course course3 = courseService.create(course3DTO);

        mockMvc.perform(get("/courses")
            .with(adminAuthority())
        )
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$[*].department.id", contains(course2.getDepartment().getId().intValue(),
                course1.getDepartment().getId().intValue(), course3.getDepartment().getId().intValue())))
            .andExpect(jsonPath("$[*].registrationCode", contains(course2.getRegistrationCode(), course1.getRegistrationCode(), course3.getRegistrationCode())))
            .andExpect(jsonPath("$[*].name", contains(course2.getName(), course1.getName(),
                course3.getName())))
            .andExpect(jsonPath("$[*].numberOfCredits", contains(course2.getNumberOfCredits(), course1.getNumberOfCredits(),
                course3.getNumberOfCredits())))
            .andExpect(jsonPath("$[*].requiredSchoolYear", contains(course2.getRequiredSchoolYear(), course1.getRequiredSchoolYear(),
                course3.getRequiredSchoolYear())))
            .andDo(print());
    }

    @Test
    public void whenUpdateAsAdmin_thenReturnUpdated() throws Exception {

        CreateCourseDTO createcourseDTO = CourseDataSample.getExampleValidCreateCourseDTO();

        createcourseDTO.setDepartmentID(this.department.getId());

        Course existingCourse = courseService.create(createcourseDTO);

        // Partial Update, will not work on null value
        UpdateCourseDTO updateCourseDTO = UpdateCourseDTO.builder()
            .name("SML")
            .build();

        UpdateCourseDTO merged = modelMapper.map(createcourseDTO, UpdateCourseDTO.class);

        modelMapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());
        modelMapper.map(updateCourseDTO, merged);

        String content = objectMapper.writeValueAsString(merged);

        mockMvc.perform(put("/courses/" + existingCourse.getId())
            .with(adminAuthority())
            .contentType(MediaType.APPLICATION_JSON)
            .content(content)
        )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.department.id").value(createcourseDTO.getDepartmentID()))
            .andExpect(jsonPath("$.registrationCode").value(createcourseDTO.getRegistrationCode()))
            .andExpect(jsonPath("$.name").value(createcourseDTO.getName()))
            .andExpect(jsonPath("$.numberOfCredits").value(createcourseDTO.getNumberOfCredits()))
            .andExpect(jsonPath("$.requiredSchoolYear").value(createcourseDTO.getRequiredSchoolYear()))
            .andDo(print());
    }

    @Test
    public void whenDeleteAsAdmin_thenSuccess() throws Exception {

        CreateCourseDTO createcourseDTO = CourseDataSample.getExampleValidCreateCourseDTO();

        createcourseDTO.setDepartmentID(this.department.getId());

        Course course = courseService.create(createcourseDTO);

        mockMvc.perform(delete("/courses/" + course.getId()).with(adminAuthority()))
            .andExpect(status().isOk())
            .andDo(print());
    }
}
