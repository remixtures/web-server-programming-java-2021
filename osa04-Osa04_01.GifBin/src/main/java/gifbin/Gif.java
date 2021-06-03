/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gifbin;

import javax.persistence.Entity;
import javax.persistence.Lob;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.AbstractPersistable;

/**
 *
 * @author miguel
 */
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Gif extends AbstractPersistable<Long> {
    
    @Lob
    private byte[] content;
    
}
