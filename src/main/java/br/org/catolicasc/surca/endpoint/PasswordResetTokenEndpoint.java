package br.org.catolicasc.surca.endpoint;

import br.org.catolicasc.surca.email.EmailMessage;
import br.org.catolicasc.surca.email.Mailer;
import br.org.catolicasc.surca.model.PasswordResetToken;
import br.org.catolicasc.surca.model.User;
import br.org.catolicasc.surca.repository.PasswordResetTokenRepository;
import br.org.catolicasc.surca.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("v1")
public class PasswordResetTokenEndpoint {

    private Mailer mailer;
    private PasswordResetTokenRepository passwordResetTokenDao;
    private UserRepository userDao;

    @Autowired
    public PasswordResetTokenEndpoint(Mailer mailer, PasswordResetTokenRepository passwordResetTokenDao, UserRepository userDao) {
        this.mailer = mailer;
        this.passwordResetTokenDao = passwordResetTokenDao;
        this.userDao = userDao;
    }

    @GetMapping(path = "/login/recuperarSenha/{token}")
    public ResponseEntity<?> resetPassword(@RequestBody User user, @PathVariable("token") String token){

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(path = "/login/recuperarSenha")
    public ResponseEntity<?> createToken(@RequestBody User user, HttpServletRequest request){
        user = userDao.findByEmail(user.getEmail());
        if(user != null){
            String token = generateToken();
            createPasswordResetTokenForUser(user, token);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }


    private void sendEmail(ArrayList<String> recipients, String body){
        try{
            mailer.submit(new EmailMessage("Eduardo Aguiar <emailtestesurca@gmail.com>",
                    recipients, "Recuperar Senha", "Token -> " + body));
        }catch (MailException ignored){

        }
    }

    private void createPasswordResetTokenForUser(User user, String token) {
        PasswordResetToken myToken = new PasswordResetToken();
        myToken.setUser(user);
        myToken.setToken(token);
        passwordResetTokenDao.save(myToken);
        ArrayList<String> recipients = new ArrayList<>();
        recipients.add(user.getEmail());
        sendEmail(recipients, token);
    }

    private String generateToken(){
        StringBuilder token = new StringBuilder();
        Random generator = new Random();
        int number;
        for(int i = 0; i < 6; i++){
                number = generator.nextInt(10);
                token.append(number);
        }
        return token.toString();
    }
}
