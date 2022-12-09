package com.courses.senla.services.impl;

import com.courses.senla.exceptions.ResourceNotFoundException;
import com.courses.senla.exceptions.ResourceRepetitionException;
import com.courses.senla.models.*;
import com.courses.senla.repositories.UserRepository;
import com.courses.senla.services.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.courses.senla.enums.ActivityStatus.ACTIVE;
import static com.courses.senla.enums.ERole.ROLE_ADMIN;
import static com.courses.senla.enums.ERole.ROLE_USER;

@Log4j2
@Transactional
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final RoleService roleService;

    private final DiscountService discountService;

    private final UserAccountService userAccountService;

    private final UserAccountHistoryService accountHistoryService;

    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           RoleService roleService,
                           DiscountService discountService,
                           UserAccountService userAccountService,
                           UserAccountHistoryService accountHistoryService) {
        this.userRepository = userRepository;
        this.roleService = roleService;
        this.discountService = discountService;
        this.userAccountService = userAccountService;
        this.accountHistoryService = accountHistoryService;
    }

    @Override
    public Page<User> getAll(Integer pageNo, Integer pageSize, String sortBy) {
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));

        log.info("get all users on page {}", pageNo);
        return userRepository.findAll(paging);
    }

    @Override
    public User getByUsername(String username) {
       return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));
    }

    @Override
    public User userRegistration(User user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            log.error("user already exists with username -- {}", user.getUsername());
            throw new ResourceRepetitionException
                    ("such a user already exists with username = ", user.getUsername());
        } else {
            UserAccount userAccount = generatedUserAccount();
            User registeredUser = saveUserForRegistration(user, userAccount);
            discountService.generateDiscounts(registeredUser );
            generatedUserAccountHistory(userAccount);
            log.info("IN register - user: {} successfully registered",
                    registeredUser.getUsername());

            return registeredUser;
        }
    }

    @Override
    public User getById(Long id) {
        log.info("get a user by id {}", id);

        return userRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("user not found by id = " + id));
    }

    public User addRole(Long id) {
        User user = getById(id);
        if (user.getRoles().contains(roleService.getByName(ROLE_ADMIN))) {
            log.debug("user isn't admin by id -- {}", user.getId());

            throw new ResourceRepetitionException
                    ("user already has this role = ", "ROLE_ADMIN");
        } else {
            Role role = roleService.getByName(ROLE_ADMIN);
            Set<Role> roles = user.getRoles();
            roles.add(role);
            user.setRoles(roles);
            log.info("add role -- {}", "ROLE_ADMIN");

            return userRepository.save(user);
        }
    }

    public User update(User updateUser, Long id) {
        User user = getById(id);
        log.info("update users data");
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        user.setPassword(passwordEncoder.encode(updateUser.getPassword()));
        user.setFirstName(updateUser.getFirstName());
        user.setLastName(updateUser.getLastName());
        user.setEmail(updateUser.getEmail());
        user.setUsername(updateUser.getUsername());

        return userRepository.save(user);
    }

    @Override
    public UserAccount getUserAccountByUserId(Long id) {
        return getById(id).getUserAccount();
    }

    @Transactional
    @Override
    public UserAccount replenishBalance(Long id, BigDecimal sum) {
        User user = getById(id);
        UserAccount userAccount = user.getUserAccount();
        BigDecimal newBalance = getNewBalance(userAccount, sum);
        userAccount.setBalance(newBalance);
        userAccountService.update(userAccount, userAccount.getId());
        accountHistoryService.balanceHistoryRecalculation(userAccount, newBalance);

        return userAccount;
    }

    private User saveUserForRegistration(User user, UserAccount userAccount) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        Role roleUser = roleService.getByName(ROLE_USER);
        Set<Role> userRoles = new HashSet<>();
        userRoles.add(roleUser);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(userRoles);
        user.setStatus(ACTIVE);
        user.setDeleted(false);
        user.setUserAccount(userAccount);

        return userRepository.save(user);
    }

    private BigDecimal getNewBalance(UserAccount userAccount, BigDecimal sum) {
        return userAccount.getBalance().add(sum);
    }

    private UserAccount generatedUserAccount() {
        UserAccount userAccount = new UserAccount();
        userAccount.setBalance(BigDecimal.valueOf(0.00));
        return userAccountService.save(userAccount);
    }

    private void generatedUserAccountHistory(UserAccount userAccount) {
        UserAccountHistory history = new UserAccountHistory();
        history.setSaldo((userAccount.getBalance()));
        history.setUserAccount(userAccount);
        accountHistoryService.save(history);
    }
}