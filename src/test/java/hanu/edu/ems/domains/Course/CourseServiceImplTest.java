package hanu.edu.ems.domains.Course;

import hanu.edu.ems.domains.Course._sample.CourseDataSample;
import hanu.edu.ems.domains.Course.dto.CreateCourseDTO;
import hanu.edu.ems.domains.Course.entity.Course;
import hanu.edu.ems.domains.Department.DepartmentRepository;
import hanu.edu.ems.domains.Department._sample.DepartmentDataSample;
import hanu.edu.ems.domains.Department.entity.Department;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@SpringBootTest
@AutoConfigureMockMvc
@Slf4j
@TestPropertySource(locations = "classpath:application-test.properties")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class CourseServiceImplTest {

    @MockBean
    private CourseRepository courseRepository;

    @MockBean
    private DepartmentRepository departmentRepository;

    @Autowired
    private CourseService courseService;

    @Autowired
    private ModelMapper modelMapper;

    private final Department department = DepartmentDataSample.getOneValidDepartment();

    @Test
    void whenCreateWithValidData_thenReturnSuccessResult() {

        // Given
        Course course = CourseDataSample.getExampleValidCourse();
        course.setDepartment(department);
        given(courseRepository.save(any(Course.class))).willReturn(course);

        given(departmentRepository.findById(any())).willReturn(Optional.of(department));

        // When
        CreateCourseDTO createCourseDTO = CourseDataSample.getExampleValidCreateCourseDTO();
        createCourseDTO.setDepartmentID(department.getId());
        Course result = courseService.create(createCourseDTO);

        // Then
        assertNotNull(result);
    }

    @Test
    void whenUpdateExistingWithValidData_thenReturnSuccessResult() {
        // Given
        Course Course = CourseDataSample.getExampleValidCourse();

        given(courseRepository.save(any(Course.class))).willReturn(Course);

        given(departmentRepository.findById(any())).willReturn(Optional.of(department));
        // When

        // Then
    }

    @Test
    void whenDeleteExisting_thenSuccess() {
        // Given

        // When
        courseService.deleteById(1L);

        // Then
    }

    @Test
    void whenFindAll_thenReturnResult() {
        List<Course> expectedResults = CourseDataSample.get3Courses();
        // Given
        given(courseRepository.findAll()).willReturn(expectedResults);

        // When
        List<Course> receivedResults = courseService.findAll();

        // Then
        assertEquals(expectedResults.size(), receivedResults.size());

        for (int i = 0; i < expectedResults.size(); i++) {
            assertEquals(expectedResults.get(i).toString(), receivedResults.get(i).toString());
        }
    }

    @Test
    void whenFindMany_thenReturnPagedResult() {
        // Given
        Page<Course> expectedResults = new PageImpl<>(CourseDataSample.get3Courses());

        Pageable pageable = PageRequest.of(0, 10);

        // Given
        given(courseRepository.findAll(pageable)).willReturn(expectedResults);
        // When
        Page<Course> receivedResults = courseService.findAll(pageable);
        // Then
        assertEquals(expectedResults.getSize(), receivedResults.getSize());

        for (int i = 0; i < receivedResults.getSize(); i++) {
            assertEquals(receivedResults.getContent().get(i).toString(), expectedResults.getContent().get(i).toString());
        }
    }

    @Test
    void whenGetByIdOfExisting_thenReturnExistingResult() {
        // Given
        Course expected = CourseDataSample.getExampleValidCourse();
        given(courseRepository.findById(1L)).willReturn(Optional.of(expected));

        // When
        Course result = courseService.getById(1L);
        // Then

        assertEquals(result, expected);
    }

    @Test
    void testFindAllByDepartmentId() {
        // Given
        List<Course> courses = CourseDataSample.get3Courses();
        given(courseRepository.findAllByDepartmentId(1L)).willReturn(courses);
        // When
        List<Course> results = courseService.findAllByDepartmentID(1L);

        // Then
        assertEquals(courses.size(), results.size());

        for (int i = 0; i < courses.size(); i++) {
            assertEquals(courses.get(i).toString(), results.get(i).toString());
        }
    }

//    @Test
//    void testFindAllByCourseReleaseId() {
//        // Given
//        List<Course> Courses = CourseDataSample.get3Courses();
//
//        Page<Course> expectedResults = new PageImpl<>(Courses);
//        Pageable pageable = PageRequest.of(0, 10);
//
//        given(CourseRepository.findByCourseReleaseId(1L, pageable)).willReturn(expectedResults);
//        // When
//        Page<Course> results = CourseService.findByCourseReleaseId(1L, pageable);
//
//        // Then
//        assertEquals(expectedResults.getSize(), results.getSize());
//
//        for (int i = 0; i < results.getSize(); i++) {
//            assertEquals(Courses.get(i).toString(), results.getContent().get(i).toString());
//        }
//    }

    @Test
    void testFindByCourseId() {
        // Given
        List<Course> Courses = CourseDataSample.get3Courses();

        Page<Course> expectedResults = new PageImpl<>(Courses);
        Pageable pageable = PageRequest.of(0, 10);

        given(courseRepository.findAllByDepartmentId(1L, pageable)).willReturn(expectedResults);
        // When
        Page<Course> results = courseService.findAllByDepartmentID(1L, pageable);

        // Then
        assertEquals(expectedResults.getSize(), results.getSize());

        for (int i = 0; i < results.getSize(); i++) {
            assertEquals(Courses.get(i).toString(), results.getContent().get(i).toString());
        }
    }

    @Test
    void testFindByKeyWord() {
        // Given
        List<Course> courses = CourseDataSample.get3Courses();

        Page<Course> expectedResults = new PageImpl<>(courses);
        Pageable pageable = PageRequest.of(0, 10);

        given(courseRepository.findAllByNameLike("DMA", pageable)).willReturn(expectedResults);
        // When
        Page<Course> results = courseService.findAllByNameLike("DMA", pageable);

        // Then
        assertEquals(expectedResults.getSize(), results.getSize());

        for (int i = 0; i < results.getSize(); i++) {
            assertEquals(courses.get(i).toString(), results.getContent().get(i).toString());
        }
    }
}