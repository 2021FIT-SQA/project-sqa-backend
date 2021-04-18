package hanu.edu.ems.domains.Course;

import hanu.edu.ems.domains.Course._sample.CourseDataSample;
import hanu.edu.ems.domains.Course.entity.Course;
import hanu.edu.ems.domains.Department.DepartmentRepository;
import hanu.edu.ems.domains.Department._sample.DepartmentDataSample;
import hanu.edu.ems.domains.Department.entity.Department;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.DirtiesContext;

import javax.transaction.Transactional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@AutoConfigureTestEntityManager
@Slf4j
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class CourseRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    private Department department;

    @BeforeEach
    void setUp() {
        Department department = DepartmentDataSample.getOneValidDepartment();
//        given(departmentRepository.findById(1L)).willReturn(Optional.of(department));
        this.department = departmentRepository.saveAndFlush(department);

        assertNotNull(this.department);
    }

    @Test
    @Transactional
    void testFindAllByDepartmentId() {

        log.info("DEPARTMENT");
        log.info(department.toString());
        // Given
        List<Course> sampleCourses = CourseDataSample.get3Courses();

        Course course0 = sampleCourses.get(0);
        course0.setDepartment(department);
        course0.setId(null);

        Course course1 = sampleCourses.get(1);
        course1.setDepartment(department);
        course1.setId(null);

        Course course2 = sampleCourses.get(2);
        course2.setDepartment(department);
        course2.setId(null);

        entityManager.persistAndFlush(course0);
        entityManager.persistAndFlush(course1);
        entityManager.persistAndFlush(course2);

        // When
        List<Course> courses = courseRepository.findAllByDepartmentId(department.getId());
        // Then
        assertEquals(courses.size(), 3);

        courseRepository.deleteAll();
    }

    @Test
    @Transactional
    void findByCourseReleaseId() {

        List<Course> sampleCourses = CourseDataSample.get3Courses();

        Course course0 = sampleCourses.get(0);
        course0.setDepartment(department);
        course0.setId(null);

        Course course1 = sampleCourses.get(1);
        course1.setDepartment(department);
        course1.setId(null);

        Course course2 = sampleCourses.get(2);
        course2.setDepartment(department);
        course2.setId(null);

        entityManager.persistAndFlush(course0);
        entityManager.persistAndFlush(course1);
        entityManager.persistAndFlush(course2);

        Pageable pageable = PageRequest.of(0, 10);
        Page<Course> coursesPage = courseRepository.findAllByDepartmentId(1L, pageable);

        assertEquals(coursesPage.getContent().size(), 0);
    }

    @Test
    @Transactional
    void findByCourseId() {

        List<Course> sampleCourses = CourseDataSample.get3Courses();

        Course course0 = sampleCourses.get(0);
        course0.setDepartment(department);
        course0.setId(null);

        Course course1 = sampleCourses.get(1);
        course1.setDepartment(department);
        course1.setId(null);

        Course course2 = sampleCourses.get(2);
        course2.setDepartment(department);
        course2.setId(null);

        entityManager.persistAndFlush(course0);
        entityManager.persistAndFlush(course1);
        entityManager.persistAndFlush(course2);

        Pageable pageable = PageRequest.of(0, 10);
        Page<Course> coursesPage = courseRepository.findAllByDepartmentId(1L, pageable);

        assertEquals(coursesPage.getContent().size(), 0);
    }

    @Test
    @Transactional
    void findAllByKeyword() {

        List<Course> sampleCourses = CourseDataSample.get3Courses();

        Course course0 = sampleCourses.get(0);
        course0.setDepartment(department);
        course0.setId(null);

        Course course1 = sampleCourses.get(1);
        course1.setDepartment(department);
        course1.setId(null);

        Course course2 = sampleCourses.get(2);
        course2.setDepartment(department);
        course2.setId(null);

        entityManager.persistAndFlush(course0);
        entityManager.persistAndFlush(course1);
        entityManager.persistAndFlush(course2);

        Pageable pageable = PageRequest.of(0, 10);
        Page<Course> coursesPage = courseRepository.findAllByNameLike("1801", pageable);

        assertEquals(coursesPage.getContent().size(), 3);
    }
}