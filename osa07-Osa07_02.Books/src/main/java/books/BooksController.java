package books;

import java.util.Random;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class BooksController {

    @Autowired
    BookRepository bookRepository;

    @GetMapping("/")
    public String view(Model model) {
        return "books";
    }

    @ResponseBody
    @GetMapping("/books/random")
    public Book randomBook() {
        int pageIdx = (int) (new Random().nextDouble() * bookRepository.count());
        return bookRepository.findAll(PageRequest.of(pageIdx, 1)).getContent().get(0);
    }

    @PostMapping("/books")
    public String post(@ModelAttribute Book book) {
        bookRepository.save(book);
        return "redirect:/";
    }

}
