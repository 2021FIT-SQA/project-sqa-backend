package hanu.edu.ems.domains.Course;

import hanu.edu.ems.domains.Course._sample.CourseDataSample;
import hanu.edu.ems.domains.Course.dto.CreateCourseDTO;
import hanu.edu.ems.domains.Course.dto.UpdateCourseDTO;
import hanu.edu.ems.domains.Course.entity.Course;
import hanu.edu.ems.domains.Department.DepartmentRepository;
import hanu.edu.ems.domains.Department._sample.DepartmentDataSample;
import hanu.edu.ems.domains.Department.entity.Department;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;

import javax.persistence.EntityNotFoundException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@AutoConfigureMockMvc
@Slf4j
@TestPropertySource(locations = "classpath:application-test.properties")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
public class CourseServiceImplIntegrationTest {

    @Autowired
    private CourseService courseService;

    @Autowired
    private CourseRepository courseRepository;

    private Department department;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private ModelMapper modelMapper;

    @BeforeEach
    public void setUp() {
        Department department = DepartmentDataSample.getOneValidDepartment();
        this.department = departmentRepository.saveAndFlush(department);
    }

    @Test
    void whenCreateWithValidData_thenReturnSuccessResult() {
        // Given
        // When
        CreateCourseDTO createCourseDTO = CourseDataSample.getExampleValidCreateCourseDTO();
        createCourseDTO.setDepartmentID(department.getId());
        Course course = courseService.create(createCourseDTO);

        // Then
        assertEquals(course.getName(), createCourseDTO.getName());
    }

    @Test
    void whenUpdateExistingWithValidData_thenReturnSuccessResult() {
        // Given
        Course existingCourse = CourseDataSample.getExampleValidCourse();
        existingCourse.setDepartment(department);

        existingCourse = courseRepository.save(existingCourse);
        // When
        UpdateCourseDTO updateCourseDTO = UpdateCourseDTO.builder()
            .name("FIT3HCI")
            .build();

        modelMapper.map(existingCourse, updateCourseDTO);

        Course result = courseService.updateById(existingCourse.getId(), updateCourseDTO);

        // Then
        assertEquals(result.getName(), existingCourse.getName());
    }

    @Test
    void whenDeleteExisting_thenSuccess() {
        // Given
        Course existingCourse = CourseDataSample.getExampleValidCourse();
        existingCourse.setDepartment(department);

        existingCourse = courseRepository.save(existingCourse);

        // When
        courseService.deleteById(existingCourse.getId());

        // Then
        Course finalExistingCourse = existingCourse;
        assertThrows(EntityNotFoundException.class, () -> {
            courseService.getById(finalExistingCourse.getId());
        });
    }

    @Test
    void whenFindAll_thenReturnResult() {
        // Given
        List<Course> courses = CourseDataSample.get3Courses();

        for (Course course: courses) {
            course.setDepartment(department);
        }
        courses = courseRepository.saveAll(courses);

        // When
        List<Course> results = courseService.findAll();

        // Then
        assertEquals(results.size(), courses.size());

        for (int i = 0; i < results.size(); i++) {
            assertEquals(results.get(i).toString(), courses.get(i).toString());
        }
    }

    @Test
    void whenFindMany_thenReturnPagedResult() {
        // Given
        List<Course> courses = CourseDataSample.get3Courses();
        for (Course course: courses) {
            course.setDepartment(department);
        }
        courses = courseRepository.saveAll(courses);

        Page<Course> coursePage = new PageImpl<>(courses);
        // When
        Pageable pageable = PageRequest.of(0, 10);
        Page<Course> resultsPage = courseService.findAll(pageable);

        // Then
        assertEquals(coursePage.getNumberOfElements(), resultsPage.getNumberOfElements());

        for (int i = 0; i < coursePage.getNumberOfElements(); i++) {
            assertEquals(coursePage.getContent().get(i).toString(), resultsPage.getContent().get(i).toString());
        }
    }

    @Test
    void whenGetByIdOfExisting_thenReturnExistingResult() {
        // Given
        Course course = CourseDataSample.getExampleValidCourse();
        course.setDepartment(department);
        course = courseRepository.save(course);

        // When
        Course result = courseService.getById(course.getId());

        // Then
        assertEquals(result.toString(), course.toString());
    }

    @Test
    void testFindAllByDepartmentId() {
        // Given
        List<Course> courses = CourseDataSample.get3Courses();

        for (Course course: courses) {
            course.setDepartment(department);
        }
        courses = courseRepository.saveAll(courses);
        // When
        List<Course> results = courseService.findAllByDepartmentID(this.department.getId());

        // Then
        assertEquals(courses.size(), results.size());

        for (int i = 0; i < results.size(); i++) {
            assertEquals(courses.get(i).toString(), results.get(i).toString());
        }
    }

    @Test
    void testFindByCourseReleaseId() {
        // Given
        List<Course> courses = CourseDataSample.get3Courses();

        for (Course course: courses) {
            course.setDepartment(department);
        }
        courses = courseRepository.saveAll(courses);
        // When

        // Then
    }

    @Test
    void testFindByCourseId() {
        // Given
        List<Course> courses = CourseDataSample.get3Courses();

        for (Course course: courses) {
            course.setDepartment(department);
        }
        courses = courseRepository.saveAll(courses);
        // When

        Pageable pageable = PageRequest.of(0, 10);
        courseService.findAllByDepartmentID(1L, pageable);

        // Then
    }

    @Test
    void testFindByKeyWord() {
        // Given
        List<Course> courses = CourseDataSample.get3Courses();

        for (Course course: courses) {
            course.setDepartment(department);
        }
        courses = courseRepository.saveAll(courses);
        // When
        Pageable pageable = PageRequest.of(0, 10);
        Page<Course> resultsPage = courseService.findAllByNameLike("DMA", pageable);
        // Then
        assertEquals(1, resultsPage.getContent().size());
        assertEquals(courses.get(0).toString(), resultsPage.getContent().get(0).toString());
    }
}
