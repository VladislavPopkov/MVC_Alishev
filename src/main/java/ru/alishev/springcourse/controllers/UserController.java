package ru.alishev.springcourse.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.alishev.springcourse.dao.UserDao;
import ru.alishev.springcourse.model.User;

import java.sql.SQLException;

@Controller
@RequestMapping("/users")
public class UserController {
    private final UserDao userDao;
    @Autowired
    public UserController(UserDao userDao) {
        this.userDao = userDao;
    }

    @GetMapping
    public String showUser (Model model) {
        model.addAttribute("allUsers", userDao.showUser());
        return "showUser";
    }
    @GetMapping("{id}")
    public String getUserFromID(@PathVariable("id") long id, Model model) throws SQLException {
        model.addAttribute("myUser", userDao.showUserWhereId(id));
        return "getUserFromID";
    }
    @GetMapping("/new")
    public String newUser(Model model) {
        model.addAttribute("user", new User());
        return "new";
    }
    @PostMapping
    public String create(@ModelAttribute("user") User user) throws SQLException {
        userDao.save(user);
        return "redirect:/users";
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable("id") long id, Model model) throws SQLException {
        model.addAttribute("user", userDao.showUserWhereId(id));
        return "edit";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("user") User user, @PathVariable("id") long id) throws SQLException {
        userDao.update(id, user);
        return "redirect:/users";
    }
    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") long id) throws SQLException {
        userDao.delete(id);
        return "redirect:/users";
    }
}
