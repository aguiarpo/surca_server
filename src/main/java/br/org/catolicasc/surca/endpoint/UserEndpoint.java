package br.org.catolicasc.surca.endpoint;

import br.org.catolicasc.surca.model.LevelsOfAccess;
import br.org.catolicasc.surca.model.User;
import br.org.catolicasc.surca.model.Vet;
import br.org.catolicasc.surca.repository.UserRepository;
import br.org.catolicasc.surca.repository.VetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("v1")
public class UserEndpoint {

    private UserRepository userDao;
    private VetRepository vetDao;

    @Autowired
    public UserEndpoint(UserRepository userDao, VetRepository vetDao) {
        this.userDao = userDao;
        this.vetDao = vetDao;
    }

    @GetMapping(path = "/admin/usuario")
    public ResponseEntity<?> listAll(Pageable pageable){
        return new ResponseEntity<>(userDao.findAll(pageable), HttpStatus.OK);
    }

    @GetMapping(path = "/admin/usuario/{id}")
    public ResponseEntity<?> getUserById(@PathVariable("id") Long id){
        Optional<User> user =  userDao.findById(id);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping(path = "/admin/usuario/cidade/like/{city}")
    public ResponseEntity<?> getUserByCity(@PathVariable("city") String city, Pageable pageable){
        Page<User> user =  userDao.findByCityStartingWith(pageable, city);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping(path = "/admin/usuario/cidade/{city}")
    public ResponseEntity<?> getUserByCityLike(@PathVariable("city") String city, Pageable pageable){
        Page<User> user =  userDao.findByCity(pageable, city);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping(path = "/admin/usuario/estado/{state}")
    public ResponseEntity<?> getUserByState(@PathVariable("state") String state, Pageable pageable){
        Page<User> user =  userDao.findByState(pageable, state);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping(path = "/admin/usuario/nome/{nome}")
    public ResponseEntity<?> getUserName(@PathVariable("nome") String name, Pageable pageable){
        Page<User> users= userDao.findByName(pageable, name);
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping(path = "/admin/usuario/nome/like/{nome}")
    public ResponseEntity<?> getUserNameLike(@PathVariable("nome") String name, Pageable pageable){
        Page<User> users = userDao.findByNameStartingWith(pageable, name);
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping(path = "/admin/usuario/email/{email}")
    public ResponseEntity<?> getUserEmail(@PathVariable("email") String email, Pageable pageable){
        Page<User> users = userDao.findByEmail(pageable, email);
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping(path = "/user/usuario/email")
    public ResponseEntity<?> getUserEmail(@AuthenticationPrincipal Authentication auth){
        String email = auth.getName();
        User user = null;
        if(email != null){
            user = userDao.findByEmail(email);
        }
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping(path = "/admin/usuario/nivelDeAcesso/{levelsOfAccessString}")
    public ResponseEntity<?> getUserIdNivel(@PathVariable("levelsOfAccessString") String levelsOfAccessString, Pageable pageable){
        levelsOfAccessString = levelsOfAccessString.toUpperCase();
        Page<User> users;
        switch (levelsOfAccessString) {
            case "U":
            case "USUÁRIO":
            case "USUARIO":
            case "USER":
                users = userDao.findByLevelsOfAccess(LevelsOfAccess.USUARIO, pageable);
                break;
            case "V":
            case "VETERINARIO":
            case "VETERINÁRIO":
            case "VET":
                users = userDao.findByLevelsOfAccess(LevelsOfAccess.VETERINARIO, pageable);
                break;
            case "A":
            case "ADMINISTRADOR":
            case "ADMIN":
                users = userDao.findByLevelsOfAccess(LevelsOfAccess.ADMIN, pageable);
                break;
            default:
                users = userDao.findByLevelsOfAccess(null, pageable);
        }
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping(path = "/admin/usuario/criadoPor/{createdBy}")
    public ResponseEntity<?> getUserCreatedBy(@PathVariable("createdBy")String name, Pageable pageable){
        Page<User> users =  userDao.findByCreatedByStartingWith(pageable, name);
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping(path = "/admin/usuario/modificadoPor/{lastModifiedBy}")
    public ResponseEntity<?> getUserLastModifiedBy(@PathVariable("lastModifiedBy")String name, Pageable pageable){
        Page<User> users =  userDao.findByLastModifiedByStartingWith(pageable, name);
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @PostMapping(path = "/login/usuario")
    public ResponseEntity<?> saveLogin(@RequestBody User user){
        user.setLevelsOfAccess(LevelsOfAccess.USUARIO);
        user.setBcryptPassword();
        return new ResponseEntity<>(userDao.save(user), HttpStatus.OK);
    }

    @PostMapping(path = "/admin/usuario")
    public ResponseEntity<?> save(@RequestBody User user){
        user.setLevelsOfAccess(LevelsOfAccess.ADMIN);
        user.setBcryptPassword();
        return new ResponseEntity<>(userDao.save(user), HttpStatus.OK);
    }

    @DeleteMapping(path = "/user/usuario")
    public ResponseEntity<?> deleteLogin(@AuthenticationPrincipal Authentication auth, @RequestBody User userForDelete){
        if(auth.getName().equals(userForDelete.getEmail())) {
            User user = userDao.findByEmailWithReturnPassword(userForDelete.getEmail());
            if (BCrypt.checkpw(userForDelete.getPassword(), user.getPassword())) {
                userDao.deleteByEmailAndPassword(user.getEmail(), user.getPassword());
            }
        }
        return new ResponseEntity<>(userDao.findByEmail(auth.getName()), HttpStatus.OK);
    }

    @DeleteMapping(path = "/admin/usuario/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id){
        Vet vet = vetDao.findByUserId(id);
        if(vet == null)
            userDao.deleteById(id);
        else
            vetDao.deleteById(vet.getId());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping(path = "/user/usuario")
    public ResponseEntity<?> updateLogin(@AuthenticationPrincipal Authentication auth, @RequestBody User user){
        String email = auth.getName();
        User userSave = null;
        if(email != null){
            User findUser = userDao.findByEmailWithReturnPassword(email);
            user.setId(findUser.getId());
            user.setLevelsOfAccess(LevelsOfAccess.USUARIO);
            user.setBcryptPassword();
            userSave = userDao.save(user);
        }
        return new ResponseEntity<>(userSave, HttpStatus.OK);
    }

    @PutMapping(path = "/admin/usuario")
    public ResponseEntity<?> update(@RequestBody User user){
        user.setBcryptPassword();
        return new ResponseEntity<>(userDao.save(user), HttpStatus.OK);
    }

}
