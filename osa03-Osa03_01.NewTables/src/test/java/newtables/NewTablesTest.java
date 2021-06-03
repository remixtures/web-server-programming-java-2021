package newtables;

import fi.helsinki.cs.tmc.edutestutils.Points;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@Points("03-01")
@ActiveProfiles("test")
public class NewTablesTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    public void hasTableStudent() {
        jdbcTemplate.execute("SELECT id, first_name, last_name FROM Student");
    }

    @Test
    public void hasTableCourse() {
        jdbcTemplate.execute("SELECT id, name FROM Course");
    }

    @Test
    public void hasJoinTable() {
        jdbcTemplate.execute("SELECT student_id, course_id FROM Enrollment");
    }
}
