package br.org.catolicasc.surca.endpoint;

import br.org.catolicasc.surca.email.EmailMessage;
import br.org.catolicasc.surca.email.Mailer;
import br.org.catolicasc.surca.model.*;
import br.org.catolicasc.surca.repository.UserRepository;
import br.org.catolicasc.surca.repository.VetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static br.org.catolicasc.surca.endpoint.GeneratePassword.getPassword;

@RestController
@RequestMapping("v1")
@CrossOrigin(origins = "http://localhost:4200")
public class UserEndpoint{

    private UserRepository userDao;
    private VetRepository vetDao;
    private Mailer mailer;

    @Autowired
    public UserEndpoint(UserRepository userDao, VetRepository vetDao, Mailer mailer) {
        this.userDao = userDao;
        this.vetDao = vetDao;
        this.mailer = mailer;
    }

    @GetMapping(path = "/admin/usuario/{column}/{value}")
    public  ResponseEntity<?> findAllPage(@PathVariable String column,
                                          @PathVariable String value, Pageable pageable){
        if(value.equals("empty")) value = "";
        switch (column){
            case "name":
                return new ResponseEntity<>
                        (userDao.findByNameStartingWithAndStatus(value, Status.VISIBLE, pageable), HttpStatus.OK);
            case "email":
                return new ResponseEntity<>
                        (userDao.findByEmailStartingWithAndStatus(value, Status.VISIBLE, pageable), HttpStatus.OK);
            case "removed":
                return new ResponseEntity<>
                        (userDao.findByEmailStartingWithAndStatus(value, Status.INVISIBLE, pageable), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(path = "/admin/usuario/like/{column}/{value}")
    public  ResponseEntity<?> suggestionName(@PathVariable String value, @PathVariable String column){
        Pageable topTen = PageRequest.of(0,5);
        switch (column){
            case "name":
                return new ResponseEntity<>
                        (userDao.findByNameStartingWithAndStatusGroupByName(value, topTen), HttpStatus.OK);
            case "email":
                return new ResponseEntity<>
                        (userDao.findByEmailStartingWithAndStatusGroupByEmail(value, "VISIBLE", topTen), HttpStatus.OK);
            case "removed":
                return new ResponseEntity<>
                        (userDao.findByEmailStartingWithAndStatusGroupByEmail(value, "INVISIBLE", topTen), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(path = "/admin/usuario/{id}")
    public  ResponseEntity<?> getId(@PathVariable Long id){
        Optional<User> user = userDao.findById(id);
        user.get().setPassword(null);
        Vet vet;
        vet = vetDao.findByUserCode(id);
        if(vet == null) vet = new Vet();
        vet.setUser(user.get());
        return new ResponseEntity<>(vet, HttpStatus.OK);
    }

    @GetMapping(path = "/user/usuario")
    public ResponseEntity<?> getUserEmailAuth(@AuthenticationPrincipal Authentication auth){
        String email = auth.getName();
        User user = null;
        if(email != null){
            user = userDao.findByEmailAndStatus(email, Status.VISIBLE);
        }
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping(path = "/user/usuario/login")
    public ResponseEntity<?> getLogin(@AuthenticationPrincipal Authentication auth){
        User user = userDao.findByEmail(auth.getName());
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PostMapping(path = "/login/usuario")
    public ResponseEntity<?> saveLogin(@RequestBody User user){
        user.setLevelsOfAccess(LevelsOfAccess.USUARIO);
        user.setBcryptPassword();
        return new ResponseEntity<>(userDao.save(user), HttpStatus.OK);
    }

    @PostMapping(path = "/admin/usuario")
    public ResponseEntity<?> save(@RequestBody Vet vet){
        User user = vet.getUser();
        String password = getPassword();
        if(user.getLevelsOfAccess().equals(LevelsOfAccess.VETERINARIO)){
            vet.getUser().setStatus(Status.VISIBLE);
            vet.getUser().setPassword(password);
            vet.getUser().setBcryptPassword();
            vetDao.save(vet);
        }else if(!user.getLevelsOfAccess().equals(LevelsOfAccess.VETERINARIO)){
            user.setPassword(password);
            user.setBcryptPassword();
            userDao.save(user);
        }
        ArrayList<String> recipients = new ArrayList<>();
        recipients.add("Eduardo Poerner <eduardo.poerner@catolicasc.org.br>");
        sendEmail(recipients, password);
        return new ResponseEntity<>(vet, HttpStatus.OK);
    }

    @DeleteMapping(path = "/user/usuario")
    public ResponseEntity<?> deleteLogin(@AuthenticationPrincipal Authentication auth, @RequestBody User userForDelete){
            User user = userDao.findByEmail(auth.getName());
            if (user != null && BCrypt.checkpw(userForDelete.getPassword(), user.getPassword())) {
                if(user.getLevelsOfAccess() == LevelsOfAccess.VETERINARIO){
                    user.setStatus(Status.INVISIBLE);
                    userDao.save(user);
                }else{
                    userDao.deleteByEmailAndPassword(user.getEmail(), user.getPassword());
                }
            }
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @DeleteMapping(path = "/admin/usuario/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id){
        Vet vet = vetDao.findByUserCode(id);
        if(vet == null){
            userDao.deleteById(id);
        }
        else{
            vet.getUser().setStatus(Status.INVISIBLE);
            userDao.save(vet.getUser());
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping(path = "/admin/usuario/visible/{id}")
    public ResponseEntity<?> visible(@PathVariable Long id){
        Optional<User> user = userDao.findById(id);
        if(user.isPresent()){
            user.get().setStatus(Status.VISIBLE);
            userDao.save(user.get());
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping(path = "/admin/usuario")
    public ResponseEntity<?> deleteAll(@RequestBody List<User> users){
        for(User user : users){
            Optional<User> findUser = userDao.findById(user.getCode());
            if(findUser.get().equals(LevelsOfAccess.VETERINARIO)) {
                user = findUser.get();
                user.setStatus(Status.INVISIBLE);
                userDao.save(user);
            }
            else {
                userDao.deleteById(user.getCode());
            }
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping(path = "/user/usuario")
    public ResponseEntity<?> updateLogin(@AuthenticationPrincipal Authentication auth, @RequestBody User user){
        String email = auth.getName();
        User userSave = null;
        User findUser = userDao.findByEmail(email);
        user.setCode(findUser.getCode());
        user.setEmail(email);
        user.setLevelsOfAccess(findUser.getLevelsOfAccess());
        if(user.getPassword().equals("")){
            user.setPassword(findUser.getPassword());
        }else{
            user.setBcryptPassword();
        }
        userSave = userDao.save(user);
        return new ResponseEntity<>(userSave, HttpStatus.OK);
    }

    @PutMapping(path = "/admin/usuario")
    public ResponseEntity<?> update(@RequestBody Vet vet){
        Optional<User> findUser = userDao.findById(vet.getUser().getCode());
        if(findUser.isPresent()) {
            if(vet.getUser().getPassword() == null)
                vet.getUser().setPassword(findUser.get().getPassword());
            else
                vet.getUser().setBcryptPassword();
            if (findUser.get().getLevelsOfAccess() == LevelsOfAccess.VETERINARIO ||
                    vet.getUser().getLevelsOfAccess() == LevelsOfAccess.VETERINARIO) {
                Vet findVet = vetDao.findByUserCode(findUser.get().getCode());
                vet.setCode(findVet.getCode());
                return new ResponseEntity<>(vetDao.save(vet), HttpStatus.OK);
            } else{
                return new ResponseEntity<>(userDao.save(vet.getUser()), HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private void sendEmail(ArrayList<String> recipients, String body){
        try{
            mailer.submit(new EmailMessage("Eduardo Aguiar <emailtestesurca@gmail.com>",
                    recipients, "Senha", "Senha -> " + body));
        }catch (MailException ignored){

        }
    }

}
