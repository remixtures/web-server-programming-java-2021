/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package filemanager;

/**
 *
 * @author miguel
 */
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author miguel
 */
public interface FileRepository extends JpaRepository<File, Long> {
    
}
