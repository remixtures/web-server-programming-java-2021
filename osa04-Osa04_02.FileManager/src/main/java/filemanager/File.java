/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package filemanager;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.AbstractPersistable;

/**
 *
 * @author miguel
 */

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class File extends AbstractPersistable<Long> {
    
    private String name;
    private long contentLength;
    private String contentType;
    
    @Lob
    @Basic(fetch = FetchType.LAZY)
    private byte[] content;
    
}
