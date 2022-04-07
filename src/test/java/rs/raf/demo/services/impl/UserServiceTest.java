package rs.raf.demo.services.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import rs.raf.demo.model.Permission;
import rs.raf.demo.model.User;
import rs.raf.demo.repositories.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    private static final Long MOCK_ID = 1L;
    private static final String MOCK_USERNAME = "MOCK_USERNAME";
    private static final String MOCK_PASSWORD = "MOCK_PASSWORD";

    @Test
    void testSave() {
        User user = new User();
        given(userRepository.save(user)).willReturn(user);

        assertEquals(user, userService.save(user));
    }

    @Test
    void testFindById() {
        Optional<User> userOptional = Optional.of(new User());
        given(userRepository.findByUserId(MOCK_ID)).willReturn(userOptional);

        assertEquals(userOptional, userService.findById(MOCK_ID));
    }

    @Test
    void testFindAll() {
        List<User> userList = new ArrayList<>();
        given(userRepository.findAll()).willReturn(userList);

        assertEquals(userList, userService.findAll());
    }

    @Test
    void testDeleteById() {
        userService.deleteById(MOCK_ID);

        then(userRepository).should(times(1)).deleteById(MOCK_ID);
    }

    @Test
    void testFindByUsernameSuccess() {
        Permission permission = new Permission();
        List<Permission> permissionList = new ArrayList<>(List.of(permission));
        User user = new User();
        user.setUsername(MOCK_USERNAME);
        user.setPassword(MOCK_PASSWORD);
        user.setPermissions(permissionList);
        Optional<User> optionalUser = Optional.of(user);
        given(userRepository.findByUsername(MOCK_USERNAME)).willReturn(optionalUser);

        UserDetails userDetails = userService.loadUserByUsername(MOCK_USERNAME);

        assertEquals(MOCK_USERNAME, userDetails.getUsername());
        assertEquals(MOCK_PASSWORD, userDetails.getPassword());
        assertEquals(permissionList.size(), userDetails.getAuthorities().size());
        List<String> authorityList = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
        assertTrue(permissionList.stream().map(Permission::getAuthority).collect(Collectors.toList()).containsAll(authorityList));
    }

    @Test
    void testFindByUsernameException() {
        given(userRepository.findByUsername(MOCK_USERNAME)).willReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> userService.loadUserByUsername(MOCK_USERNAME));
    }
}
