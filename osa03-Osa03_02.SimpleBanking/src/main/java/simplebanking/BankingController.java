package simplebanking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class BankingController {

    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private AccountRepository accountRepository;

    @GetMapping("/")
    public String list(Model model) {
        model.addAttribute("clients", clientRepository.findAll());
        return "index";
    }

    @PostMapping("/")
    public String add(@RequestParam String name, @RequestParam String iban) {
        if (name.trim().isEmpty() || iban.trim().isEmpty()) {
            return "redirect:/";
        }

        Client client = clientRepository.findByName(name);
        if (client == null) {
            client = new Client();
            client.setName(name);
        }
        Account account = accountRepository.findByIban(iban);
        if (account == null) {
            account = new Account();
            account.setIban(iban);
            account.setBalance(100);
        }

        // DO SOMETHING HERE
        client.getAccounts().add(account);
        clientRepository.save(client);
        account.setClient(client);
        accountRepository.save(account);
        return "redirect:/";
    }
}
