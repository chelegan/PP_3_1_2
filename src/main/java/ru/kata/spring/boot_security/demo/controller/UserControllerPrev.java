package ru.kata.spring.boot_security.demo.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.kata.spring.boot_security.demo.entity.model.Role;
import ru.kata.spring.boot_security.demo.entity.model.User;
import ru.kata.spring.boot_security.demo.service.RoleServiceImpl;
import ru.kata.spring.boot_security.demo.service.UserServiceImpl;

import java.security.Principal;
import java.util.List;
import java.util.Objects;
import java.util.Set;


@Controller
public class UserControllerPrev {

    private final UserServiceImpl userService;
    private final RoleServiceImpl roleServiceImpl;

    @Autowired
    public UserControllerPrev(UserServiceImpl userService, RoleServiceImpl roleServiceImpl) {
        this.userService = userService;
        this.roleServiceImpl = roleServiceImpl;
    }

    @GetMapping(value = "/admin/users")
    public String getAllUsers(ModelMap model) {
        model.addAttribute("users", userService.getListOfAllUsers());
        return "allUsers";
    }

    @GetMapping(value = "/admin/addUser")
    public String getFormForAddUser(@ModelAttribute("user") User user) {
        return "addUser";
    }

    @PostMapping(value = "/admin/users")
    public String addUser(@ModelAttribute("user") User user,
                          @RequestParam("role") String role) {


        user.setRoles(roleServiceImpl.getSetOfRoles(role));
        userService.addUser(user);

        return "redirect:/admin/users";
    }

    @GetMapping("/admin/delete")
    public String deleteUser(@RequestParam("id") Long id) {

        userService.deleteUser(id);

        return "redirect:/admin/users";
    }

    @GetMapping("/admin/update")
    public String getFormForUpdateUser(Model model, @RequestParam("id") Long id) {

        model.addAttribute("user", userService.getUser(id));

        return "updateUser";
    }

    @PostMapping("/admin/update")
    public String updateUser(@ModelAttribute("user") User user,
                             @RequestParam("role") String role) {

            user.setRoles(roleServiceImpl.getSetOfRoles(role));
            userService.updateUser(user);

        return "redirect:/admin/users";
    }

    @GetMapping("/user")
    public String getUserPage(Principal principal, Authentication authentication, Model model, @RequestParam("id") Long id) {

        Set<String> roles = AuthorityUtils.authorityListToSet(authentication.getAuthorities());

        if (userService.getIdByUsername(principal.getName()).equals(id) | roles.contains("ROLE_ADMIN")) {
            model.addAttribute("user", userService.getUser(id));
            return "user";
        } else {
            throw new AccessDeniedException("You do not have permission to access this page");
        }

    }
}
