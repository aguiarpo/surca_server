package br.org.catolicasc.surca.endpoint;

import br.org.catolicasc.surca.email.EmailMessage;
import br.org.catolicasc.surca.email.Mailer;
import br.org.catolicasc.surca.error.ResourceNotFoundException;
import br.org.catolicasc.surca.model.PasswordResetToken;
import br.org.catolicasc.surca.model.User;
import br.org.catolicasc.surca.repository.PasswordResetTokenRepository;
import br.org.catolicasc.surca.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping(path = "admin/token")
    public ResponseEntity<?> listAll(Pageable pageable){
        return new ResponseEntity<>(passwordResetTokenDao.findAll(pageable), HttpStatus.OK);
    }

    @GetMapping(path = "admin/token/{id}")
    public ResponseEntity<?> getById(@PathVariable("id") Long id){
        return new ResponseEntity<>(passwordResetTokenDao.findById(id), HttpStatus.OK);
    }

    @GetMapping(path = "admin/token/{expiryDate}")
    public ResponseEntity<?> getExpiryDate(@PathVariable("expiryDate")String expiryDate, Pageable pageable){
        return new ResponseEntity<>(passwordResetTokenDao.findByExpiryDate(expiryDate, pageable), HttpStatus.OK);
    }

    @GetMapping(path = "/login/recuperarSenha/{token}/{email}")
    public ResponseEntity<?> getToken(@PathVariable("token") String token, @PathVariable("email") String email){
        PasswordResetToken passwordResetToken;
        User user;
        LocalDateTime today = LocalDateTime.now();
        if(email!= null){
            user = userDao.findByEmail(email);
            if(user == null){
                throw new ResourceNotFoundException("Email não cadastrado");
            }else{
                passwordResetToken = passwordResetTokenDao.findByUserCodeAndToken(user.getCode(), token);
            }
            if(passwordResetToken == null){
                throw new ResourceNotFoundException("Código incorreto");
            }else{
                if(passwordResetToken.getExpiryDate().isBefore(today)){
                    throw new ResourceNotFoundException("Código expirou");
                }
            }
        }else{
            throw new ResourceNotFoundException("Email não pode ser nulo");
        }
        return new ResponseEntity<>(passwordResetToken, HttpStatus.OK);
    }

    @PutMapping(path = "/login/recuperarSenha/{id}/{token}")
    public ResponseEntity<?> resetPassword(@RequestBody User user, @PathVariable("id") Long id, @PathVariable("token") String token){
        PasswordResetToken passwordResetToken;
        Optional<User> findUser = userDao.findById(id);
        if(findUser.isPresent() && user.getPassword() != null){
            String password = user.getPassword();
            passwordResetToken = passwordResetTokenDao.findByUserCodeAndToken(id, token);
            if(passwordResetToken != null){
                LocalDateTime today = LocalDateTime.now();
                if(passwordResetToken.getExpiryDate().isBefore(today)){
                    throw new ResourceNotFoundException("Código expirou");
                }
                user = findUser.get();
                user.setPassword(password);
                user.setBcryptPassword();
                User saveUser = userDao.save(user);
                if(saveUser !=null)
                    passwordResetTokenDao.delete(passwordResetToken);
            }else{
                throw new ResourceNotFoundException("Código incorreto");
            }
        }else{
                throw new ResourceNotFoundException("Senha não pode ser nula");
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(path = "/login/recuperarSenha")
    public ResponseEntity<?> createToken(@RequestBody User user){
        user = userDao.findByEmail(user.getEmail());
        if(user != null){
            String token = generateToken();
            createPasswordResetTokenForUser(user, token);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping(path = "admin/token/{id}")
    public ResponseEntity<?> listAll(@PathVariable("id") Long id){
        passwordResetTokenDao.deleteById(id);
        return new ResponseEntity<>(true, HttpStatus.OK);
    }

    @DeleteMapping(path = "admin/token")
    public ResponseEntity<?> listAll(@RequestBody List<PasswordResetToken> passwordResetTokens){
        for(PasswordResetToken passwordResetToken : passwordResetTokens){
            passwordResetTokenDao.deleteById(passwordResetToken.getCode());
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
        myToken.setExpiryDate(createExpiryDate());
        passwordResetTokenDao.save(myToken);
        ArrayList<String> recipients = new ArrayList<>();
        recipients.add(user.getEmail());
        sendEmail(recipients, token);
    }

    private LocalDateTime createExpiryDate(){
        LocalDateTime today = LocalDateTime.now();
        return today.plusDays(1);
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
