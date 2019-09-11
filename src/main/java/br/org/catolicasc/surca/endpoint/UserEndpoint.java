package br.org.catolicasc.surca.endpoint;

import br.org.catolicasc.surca.email.EmailMessage;
import br.org.catolicasc.surca.email.Mailer;
import br.org.catolicasc.surca.model.*;
import br.org.catolicasc.surca.repository.UserRepository;
import br.org.catolicasc.surca.repository.VetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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
import static br.org.catolicasc.surca.endpoint.hateoas.UserHateos.*;

@RestController
@RequestMapping("v1")
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

    @GetMapping(path = "/admin/usuario")
    public ResponseEntity<?> listAll(Pageable pageable, PagedResourcesAssembler assembler){
        Page<User> users = userDao.findByStatus(pageable, Status.VISIBLE);
        if(!users.isEmpty())
            users.forEach(user -> createLinkById(user, assembler));
        return new ResponseEntity<>(createLinkFindBy(users, assembler), HttpStatus.OK);
    }

    @GetMapping(path = "/admin/usuario/{id}")
    public ResponseEntity<?> getUserById(@PathVariable("id") Long id, PagedResourcesAssembler assembler){
        Optional<User> user =  userDao.findById(id);
        if(user.isPresent())
            createLink(user, assembler);
        return new ResponseEntity<>(user.get(), HttpStatus.OK);
    }

    @GetMapping(path = "/admin/usuario/nome/{nome}")
    public ResponseEntity<?> getUserName(@PathVariable("nome") String name, Pageable pageable,
                                         PagedResourcesAssembler assembler){
        Page<User> users= userDao.findByNameAndStatus(pageable, name, Status.VISIBLE);
        if(!users.isEmpty())
            users.forEach(user -> createLinkById(user, assembler));
        return new ResponseEntity<>(createLinkFindBy(users, assembler), HttpStatus.OK);
    }

    @GetMapping(path = "/admin/usuario/nome/like/{nome}")
    public ResponseEntity<?> getUserNameLike(@PathVariable("nome") String name, Pageable pageable,
                                             PagedResourcesAssembler assembler){
        Page<User> users = userDao.findByNameStartingWithAndStatus(pageable, name, Status.VISIBLE);
        if(!users.isEmpty())
            users.forEach(user -> createLinkById(user, assembler));
        return new ResponseEntity<>(createLinkFindBy(users, assembler), HttpStatus.OK);
    }

    @GetMapping(path = "/admin/usuario/email/{email}")
    public ResponseEntity<?> getUserEmail(@PathVariable("email") String email, PagedResourcesAssembler assembler){
        User user = userDao.findByEmailAndStatus(email, Status.VISIBLE);
        if(user == null){
            user = new User();
        }else {
            createLinkById(user, assembler);
        }
        createLinkFindBy(user, assembler);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping(path = "/user/usuario")
    public ResponseEntity<?> getUserEmailAuth(@AuthenticationPrincipal Authentication auth){
        String email = auth.getName();
        User user = null;
        if(email != null){
            user = userDao.findByEmailAndStatus(email, Status.VISIBLE);
            createLink(user, auth);
        }
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping(path = "/login/usuario")
    public ResponseEntity<?> getLogin(@RequestBody User user){
        User findUser = userDao.findByEmailAndStatus(user.getEmail(), Status.VISIBLE);
        if(findUser != null) {
            if (BCrypt.checkpw(user.getPassword(), findUser.getPassword())) {
                findUser.setPassword(user.getPassword());
            }else{
                findUser = null;
            }
        }
        return new ResponseEntity<>(findUser, HttpStatus.OK);
    }

    @GetMapping(path = "/admin/usuario/nivelDeAcesso/{levelsOfAccessString}")
    public ResponseEntity<?> getUserIdNivel(@PathVariable("levelsOfAccessString") String levelsOfAccessString,
                                            Pageable pageable, PagedResourcesAssembler assembler){
        levelsOfAccessString = levelsOfAccessString.toUpperCase();
        Page<User> users;
        switch (levelsOfAccessString) {
            case "U":
            case "USUÁRIO":
            case "USUARIO":
            case "USER":
                users = userDao.findByLevelsOfAccessAndStatus(LevelsOfAccess.USUARIO, Status.VISIBLE, pageable);
                break;
            case "V":
            case "VETERINARIO":
            case "VETERINÁRIO":
            case "VET":
                users = userDao.findByLevelsOfAccessAndStatus(LevelsOfAccess.VETERINARIO, Status.VISIBLE, pageable);
                break;
            case "A":
            case "ADMINISTRADOR":
            case "ADMIN":
                users = userDao.findByLevelsOfAccessAndStatus(LevelsOfAccess.ADMIN, Status.VISIBLE, pageable);
                break;
            default:
                users = userDao.findByLevelsOfAccessAndStatus(null, Status.VISIBLE, pageable);
        }
        if(!users.isEmpty())
            users.forEach(user -> createLinkById(user, assembler));
        return new ResponseEntity<>(createLinkFindBy(users, assembler), HttpStatus.OK);
    }

    @PostMapping(path = "/login/usuario")
    public ResponseEntity<?> saveLogin(@RequestBody User user){
        user.setLevelsOfAccess(LevelsOfAccess.USUARIO);
        user.setBcryptPassword();
        return new ResponseEntity<>(userDao.save(user), HttpStatus.OK);
    }

    @PostMapping(path = "/admin/usuario")
    public ResponseEntity<?> save(@RequestBody Vet vet){
        Vet findVet = null;
        User user = vet.getUser();
        String password = getPassword();
        if(user.getLevelsOfAccess().equals(LevelsOfAccess.VETERINARIO) && vet.getCrmv() != null){
            findVet = vetDao.findByCrmv(vet.getCrmv());
            if(findVet != null){
                vet.setCode(findVet.getCode());
                vet.getUser().setStatus(Status.VISIBLE);
                vet.getUser().setPassword(findVet.getUser().getPassword());
                vet.getUser().setCode(findVet.getUser().getCode());
            }else{
                vet.getUser().setPassword(password);
                vet.getUser().setBcryptPassword();
            }
            vetDao.save(vet);
        }else{
            user.setPassword(password);
            user.setBcryptPassword();
            userDao.save(user);
        }
        if(findVet == null){
            ArrayList<String> recipients = new ArrayList<>();
            recipients.add("Eduardo Poerner <eduardo.poerner@catolicasc.org.br>");
            sendEmail(recipients, password);
        }
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

    @DeleteMapping(path = "/admin/usuario")
    public ResponseEntity<?> deleteAll(@RequestBody List<User> users){
        for(User user : users){
            Vet vet = vetDao.findByUserCode(user.getCode());
            if(vet == null)
                userDao.deleteById(user.getCode());
            else {
                user.setStatus(Status.INVISIBLE);
                userDao.save(user);
            }
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping(path = "/user/usuario")
    public ResponseEntity<?> updateLogin(@AuthenticationPrincipal Authentication auth, @RequestBody User user){
        String email = auth.getName();
        User userSave = null;
        if(email != null){
            User findUser = userDao.findByEmail(email);
            user.setCode(findUser.getCode());
            user.setLevelsOfAccess(findUser.getLevelsOfAccess());
            user.setBcryptPassword();
            userSave = userDao.save(user);
            createLink(user, auth);
        }
        return new ResponseEntity<>(userSave, HttpStatus.OK);
    }

    @PutMapping(path = "/admin/usuario")
    public ResponseEntity<?> update(@RequestBody User user){
        user.setBcryptPassword();
        return new ResponseEntity<>(userDao.save(user), HttpStatus.OK);
    }

    private void sendEmail(ArrayList<String> recipients, String body){
        try{
            mailer.submit(new EmailMessage("Eduardo Aguiar <emailtestesurca@gmail.com>",
                    recipients, "Senha", "Senha -> " + body));
        }catch (MailException ignored){

        }
    }

}
