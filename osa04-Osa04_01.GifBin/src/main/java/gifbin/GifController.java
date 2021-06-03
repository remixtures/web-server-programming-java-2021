/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gifbin;

import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author miguel
 */
@Controller
public class GifController {
    
    @Autowired
    private GifRepository gifRepository;
    
    @GetMapping("/gifs")
    public String redirectToGif() {
        return "redirect:/gifs/1";
    }
    
    @GetMapping("/gifs/{id}")
    public String getPictures(Model model, @PathVariable Long id) {
        long count = gifRepository.count();
        model.addAttribute("count", count);
        if (id >= 1 && id < count) {
            model.addAttribute("next", id + 1);
        }
        
        if (id > 1 && id <= count) {
            model.addAttribute("previous", id - 1);
        } 
        
        if (id >= 1 && id <= count) {
            model.addAttribute("current", id);
        }
        
        return "gifs";
    }
    
    @GetMapping(path = "/gifs/{id}/content", produces = "image/gif")
    @ResponseBody
    public byte[] getPicture(@PathVariable Long id) {
        return gifRepository.getOne(id).getContent();
    }
    
    @PostMapping("/gifs")
    public String createPicture(@RequestParam("file") MultipartFile file) throws IOException {
        if (file.getContentType().equals("image/gif")) {
            Gif picture = new Gif();
            picture.setContent(file.getBytes());
            gifRepository.save(picture);
        }
        
        return "redirect:/gifs";
    }
}