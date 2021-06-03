/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package newtables;

/**
 *
 * @author miguel
 */

import java.util.List;
import java.util.ArrayList;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
@Data @NoArgsConstructor @AllArgsConstructor
public class Student extends AbstractPersistable<Long> {
    
    private String first_name;
    private String last_name;
    
    @ManyToMany
    @JoinTable(
        name="Enrollment", joinColumns=@JoinColumn(name="STUDENT_ID", referencedColumnName="ID"),
            inverseJoinColumns=@JoinColumn(name="COURSE_ID", referencedColumnName="ID"))
    List<Course> courses = new ArrayList<>();

}