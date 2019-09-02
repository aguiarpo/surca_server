package br.org.catolicasc.surca.endpoint;

import br.org.catolicasc.surca.model.User;
import br.org.catolicasc.surca.model.UserLevel;
import br.org.catolicasc.surca.repository.UserLevelRepository;
import br.org.catolicasc.surca.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("v1")
public class UserEndpoint {

    private UserRepository userDao;
    private UserLevelRepository levelDao;

    @Autowired
    public UserEndpoint(UserRepository userDao, UserLevelRepository levelDao) {
        this.userDao = userDao;
        this.levelDao = levelDao;
    }

    @GetMapping(path = "/user/usuario")
    public ResponseEntity<?> listAll(Pageable pageable){
        return new ResponseEntity<>(userDao.findAll(pageable), HttpStatus.OK);
    }

    @GetMapping(path = "/user/usuario/{id}")
    public ResponseEntity<?> getUserById(@PathVariable("id") Long id){
        Optional<User> user =  userDao.findById(id);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping(path = "/user/usuario/nome/{nome}")
    public ResponseEntity<?> getUserName(@PathVariable("nome") String name, Pageable pageable){
        Page<User> user = userDao.findByName(pageable, name);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping(path = "/user/usuario/nome/like/{nome}")
    public ResponseEntity<?> getUserNameLike(@PathVariable("nome") String name, Pageable pageable){
        Page<User> user = userDao.findByNameStartingWith(pageable, name);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping(path = "/user/usuario/email/{email}")
    public ResponseEntity<?> getUserEmail(@PathVariable("email") String email, Pageable pageable){
        Page<User> user = userDao.findByEmail(pageable, email);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping(path = "/user/usuario/idNivel/{idNivel}")
    public ResponseEntity<?> getUserIdNivel(@PathVariable("idNivel") String name, Pageable pageable){
        Page<User> user = userDao.findByUserLevelName(pageable, name);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PostMapping(path = "/login/usuario")
    public ResponseEntity<?> saveLogin(@RequestBody User user){
        UserLevel userLevel = levelDao.findByName("Pesquisador");
        user.setUserLevel(userLevel);
        return new ResponseEntity<>(userDao.save(user), HttpStatus.OK);
    }

    @PostMapping(path = "/admin/usuario")
    public ResponseEntity<?> save(@RequestBody User user){
        UserLevel userLevel = levelDao.findByName(user.getUserLevel().getName());
        user.setUserLevel(userLevel);
        return new ResponseEntity<>(userDao.save(user), HttpStatus.OK);
    }

    @DeleteMapping(path = "/login/usuario/{id}")
    public ResponseEntity<?> deleteLogin(@PathVariable Long id){
        userDao.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping(path = "/admin/usuario/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id){
        userDao.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/login/usuario")
    public ResponseEntity<?> updateLogin(@RequestBody User user){
        UserLevel userLevel = levelDao.findByName("Pesquisador");
        user.setUserLevel(userLevel);
        return new ResponseEntity<>(userDao.save(user), HttpStatus.OK);
    }

    @PutMapping("/admin/usuario")
    public ResponseEntity<?> update(@RequestBody User user){
        UserLevel userLevel = levelDao.findByName(user.getUserLevel().getName());
        user.setUserLevel(userLevel);
        return new ResponseEntity<>(userDao.save(user), HttpStatus.OK);
    }

}
