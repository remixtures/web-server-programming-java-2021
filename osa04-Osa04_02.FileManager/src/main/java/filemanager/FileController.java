/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package filemanager;

import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author miguel
 */

@Controller
public class FileController {
    
    @Autowired
    private FileRepository fileRepository;
    
    @GetMapping("/files")
    public String viewFiles(Model model) {
        model.addAttribute("files", fileRepository.findAll());
        return "files";
    }    
    
    @PostMapping("/files")
    public String addFile(@RequestParam("file") MultipartFile file) throws IOException {
        File ficheiro = new File();
        ficheiro.setName(file.getOriginalFilename());
        ficheiro.setContent(file.getBytes());
        ficheiro.setContentLength(file.getSize());
        ficheiro.setContentType(file.getContentType());
        fileRepository.save(ficheiro);
        return "redirect:/files";
    }
    
    @GetMapping("/files/{id}")
    public ResponseEntity<byte[]> viewFile(@PathVariable Long id) {
        File ficheiro = fileRepository.getOne(id);
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentLength(ficheiro.getContentLength());
        headers.setContentType(MediaType.parseMediaType(ficheiro.getContentType()));
        headers.add("Content-Disposition", "attachment; filename =" + ficheiro.getName());
        return new ResponseEntity<>(ficheiro.getContent(), headers, HttpStatus.CREATED);
    }
    
    @DeleteMapping("/files/{id}")
    public String deleteFile(@PathVariable Long id) {
        fileRepository.deleteById(id);
        return "redirect:/files";
    }
}