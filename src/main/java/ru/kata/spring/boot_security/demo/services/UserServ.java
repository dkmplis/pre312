package ru.kata.spring.boot_security.demo.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repositories.RoleRepository;
import ru.kata.spring.boot_security.demo.repositories.UserRepository;

import javax.persistence.EntityNotFoundException;
import java.util.HashSet;
import java.util.List;


@Service
@Transactional(readOnly = true)
public class UserServ implements Services<User> {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Autowired
    public UserServ(UserRepository userRep, RoleRepository roleRepository) {
        this.userRepository = userRep;
        this.roleRepository = roleRepository;
    }

    public List<User> getAll() {
        return userRepository.findAll();
    }

    public User findById(int id) {
        return userRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    @Transactional
    public void addNew(User user) {
        Role role = roleRepository.findByName("ROLE_USER")
                .orElseThrow(EntityNotFoundException::new);
        user.setRole(role);
        userRepository.save(user);
    }

    @Transactional
    public void deleteById(int id) {
        userRepository.deleteById(id);
    }

    @Transactional
    public void update(int id, User updatedUser) {
        User user = userRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        updatedUser.setPassword(user.getPassword());
        updatedUser.setId(id);
        userRepository.save(updatedUser);
    }

    public User findByUserName(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username not found!"));
    }

    public User addRoles(User user, List<Integer> rolesId) {
        HashSet<Role> selectedRoles = new HashSet<>();
        if(rolesId != null) {
            for (int roleId : rolesId) {
                Role role = roleRepository.findById(roleId)
                        .orElseThrow(EntityNotFoundException::new);
                selectedRoles.add(role);
            }
        }
        user.setRoles(selectedRoles);
        return user;
    }
}
