package br.org.catolicasc.surca.endpoint;

import br.org.catolicasc.surca.email.Mailer;
import br.org.catolicasc.surca.model.User;
import br.org.catolicasc.surca.model.UserLevel;
import br.org.catolicasc.surca.model.Vet;
import br.org.catolicasc.surca.repository.UserLevelRepository;
import br.org.catolicasc.surca.repository.UserRepository;
import br.org.catolicasc.surca.repository.VetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("v1")
public class VetEndpoint {

    private VetRepository vetDao;
    private UserRepository userDao;
    private UserLevelRepository levelDao;
    private Mailer mailer;

    @Autowired
    public VetEndpoint(VetRepository vetDao, UserRepository userDao, UserLevelRepository levelDao) {
        this.vetDao = vetDao;
        this.userDao = userDao;
        this.levelDao = levelDao;
    }

    @GetMapping(path = "/admin/veterinario")
    public ResponseEntity<?> listAll(Pageable pageable){
        return new ResponseEntity<>(vetDao.findAll(pageable), HttpStatus.OK);
    }

    @GetMapping(path = "/admin/veterinario/{id}")
    public ResponseEntity<?> getVeterinarioById(@PathVariable("id") Long id){
        Optional<Vet> vet =  vetDao.findById(id);
        return new ResponseEntity<>(vet, HttpStatus.OK);
    }


    @GetMapping(path = "/admin/veterinario/nome/{nome}")
    public ResponseEntity<?> getVeterinarioName(@PathVariable("nome") String name, Pageable pageable){
        Page<Vet> vet = vetDao.findByUserName(pageable, name);
        return new ResponseEntity<>(vet, HttpStatus.OK);
    }

    @GetMapping(path = "/admin/veterinario//crmv/{crmv}")
    public ResponseEntity<?> getVeterinarioCrmv(@PathVariable("crmv") String crmv, Pageable pageable){
        Page<Vet> vet = vetDao.findByCrmv(pageable, crmv);
        return new ResponseEntity<>(vet, HttpStatus.OK);
    }

    @GetMapping(path = "/admin/veterinario/nome/like/{nome}")
    public ResponseEntity<?> getVeterinarioNameLike(@PathVariable("nome") String name, Pageable pageable){
        Page<Vet> vet = vetDao.findByUserNameStartingWith(pageable, name);
        return new ResponseEntity<>(vet, HttpStatus.OK);
    }

    @GetMapping(path = "/admin/veterinario/email/{email}")
    public ResponseEntity<?> getVeterinarioEmail(@PathVariable("email") String email, Pageable pageable){
        Page<Vet> vet = vetDao.findByUserEmail(pageable, email);
        return new ResponseEntity<>(vet, HttpStatus.OK);
    }

    @PostMapping(path = "/admin/veterinario")
    public ResponseEntity<?> save(@RequestBody Vet vet){
        UserLevel userLevel = levelDao.findByName(vet.getUser().getUserLevel().getName());
        vet.getUser().setUserLevel(userLevel);
//        ArrayList<String> recipients = new ArrayList<>();
//        recipients.add("Eduardo Poerner <eduardo.poerner@catolicasc.org.br>");
//        sendEmail("Eduardo Aguair <eduardo.aguiarpo@gmail.com>",recipients,
//                "Senha", password);
        return new ResponseEntity<>(vetDao.save(vet), HttpStatus.OK);
    }

    @DeleteMapping(path = "/admin/veterinario/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id){
        vetDao.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping(path = "/admin/veterinario")
    public ResponseEntity<?> update(@RequestBody Vet vet){
        UserLevel userLevel = levelDao.findByName(vet.getUser().getUserLevel().getName());
        vet.getUser().setUserLevel(userLevel);
        Vet vetCrmv = vetDao.findByCrmv(vet.getCrmv());
        vet.getUser().setId(vetCrmv.getUser().getId());
        return new ResponseEntity<>(vetDao.save(vet), HttpStatus.OK);
    }

//    public String generatePassword(){
//        StringBuilder password = new StringBuilder();
//        Random generator = new Random();
//        int size;
//        char letra;
//        int number;
//        for(int i = 0; i < 8; i++){
//            int random = generator.nextInt(10);
//            if(random % 2 == 0)
//                size = 65;
//            else
//                size = 97;
//
//            if(random > 4){
//                letra = (char) (generator.nextInt(25) + size);
//                password.append(letra);
//            } else{
//                number = generator.nextInt(9);
//                password.append(number);
//            }
//        }
//        return password.toString();
//    }
//
//    public void sendEmail(String sender, ArrayList<String> recipients, String subject, String body ){
//        try{
//            mailer.submit(new EmailMessage(sender,
//                    recipients, subject, "Senha -> " + body));
//        }catch (MailException e){
//            throw new ResourceNotFoundException("Erro ao enviar o email");
//        }
//    }
}