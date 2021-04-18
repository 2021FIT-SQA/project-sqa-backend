package hanu.edu.ems.domains.Course;

import hanu.edu.ems.domains.Course.entity.Course;
import hanu.edu.ems.domains.CourseRelease.entity.CourseRelease;
import hanu.edu.ems.domains.Department.entity.Department;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class CourseTest {
    @Mock
    Department department;

    @Mock
    List<CourseRelease> courseReleases;

    @InjectMocks
    Course course;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testSetId() {
        course.setId(1L);
    }

    @Test
    void testSetRegistrationCode() {
        course.setRegistrationCode("FIT2DMA");
    }

    @Test
    void testSetName() {
        course.setName("DMA");
    }

    @Test
    void testSetNumberOfCredits() {
        course.setNumberOfCredits(3);
    }

    @Test
    void testSetRequiredSchoolYear() {
        course.setRequiredSchoolYear(0);
    }

//    @Test
//    void testSetActiveReleasesCount() {
//        course.setActiveReleasesCount(0);
//    }

    @Test
    void testSetCreatedAt() {
        course.setCreatedAt(LocalDateTime.now());
    }

    @Test
    void testSetUpdatedAt() {
        course.setUpdatedAt(LocalDateTime.now());
    }

    public static Course getExampleCourse() {
        return Course.builder().build();
    }
}
