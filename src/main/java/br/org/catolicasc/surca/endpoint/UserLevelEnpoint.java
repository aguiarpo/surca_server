package br.org.catolicasc.surca.endpoint;

import br.org.catolicasc.surca.model.UserLevel;
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
public class UserLevelEnpoint {

    private UserLevelRepository levelDao;
    private UserRepository userDao;
    private VetRepository vetDao;

    @Autowired
    public UserLevelEnpoint(UserLevelRepository levelDao, UserRepository userDao, VetRepository vetDao) {
        this.levelDao = levelDao;
        this.userDao = userDao;
        this.vetDao = vetDao;
    }

    @GetMapping(path = "/admin/nivelUsuario")
    public ResponseEntity<?> listAll(Pageable pageable){
        return new ResponseEntity<>(levelDao.findAll(pageable), HttpStatus.OK);
    }

    @GetMapping(path = "/admin/nivelUsuario/{id}")
    public ResponseEntity<?> getNivelUsuarioById(@PathVariable("id")Long id){
        Optional<UserLevel> levelUser =  levelDao.findById(id);
        return new ResponseEntity<>(levelUser, HttpStatus.OK);
    }

    @GetMapping(path = "/admin/nivelUsuario/nivel/{name}")
    public ResponseEntity<?> getNivelUsuarioById(@PathVariable("name") String name, Pageable pageable){
        Page<UserLevel> levelUser = levelDao.findByName(pageable, name);
        return new ResponseEntity<>(levelUser, HttpStatus.OK);
    }

    @PostMapping(path = "/admin/nivelUsuario")
    public ResponseEntity<?> save(@RequestBody UserLevel userLevelModel){
        return new ResponseEntity<>( levelDao.save(userLevelModel), HttpStatus.OK);
    }

    @DeleteMapping(path = "/admin/nivelUsuario/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id){
        vetDao.removeByUserUserLevelId(id);
        userDao.removeByUserLevelId(id);
        levelDao.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @PutMapping(path = "/admin/nivelUsuario")
    public ResponseEntity<?> update(@RequestBody UserLevel userLevelModel){
        return new ResponseEntity<>(levelDao.save(userLevelModel), HttpStatus.OK);
    }
}
