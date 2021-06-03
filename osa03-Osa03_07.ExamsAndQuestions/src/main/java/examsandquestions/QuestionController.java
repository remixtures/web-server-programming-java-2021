package examsandquestions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class QuestionController {

    @Autowired
    private QuestionRepository questionRepository;
    
    @GetMapping("/questions")
    public String list(Model model) {
        model.addAttribute("questions", questionRepository.findAll());
        return "questions";
    }

    @PostMapping("/questions")
    public String addQuestion(@RequestParam String title, @RequestParam String content) {
        Question question = new Question();
        question.setContent(content);
        question.setTitle(title);
        questionRepository.save(question);
        return "redirect:/questions";
    }

}
