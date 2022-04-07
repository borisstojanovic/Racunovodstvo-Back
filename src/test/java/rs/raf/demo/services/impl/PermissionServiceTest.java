package rs.raf.demo.services.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import rs.raf.demo.model.Permission;
import rs.raf.demo.repositories.PermissionRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class PermissionServiceTest {

    @InjectMocks
    private PermissionService permissionService;

    @Mock
    private PermissionRepository permissionRepository;

    private static final Long MOCK_ID = 1L;

    @Test
    void testSave() {
        Permission permission = new Permission();
        given(permissionRepository.save(permission)).willReturn(permission);

        assertEquals(permission, permissionService.save(permission));
    }

    @Test
    void testDeleteById() {
        permissionService.deleteById(MOCK_ID);

        then(permissionRepository).should(times(1)).deleteById(MOCK_ID);
    }

    @Test
    void findById() {
        Optional<Permission> optionalPermission = Optional.of(new Permission());
        given(permissionRepository.findById(MOCK_ID)).willReturn(optionalPermission);

        assertEquals(optionalPermission, permissionService.findById(MOCK_ID));
    }

    @Test
    void testFindAll() {
        List<Permission> permissionList = new ArrayList<>();
        given(permissionRepository.findAll()).willReturn(permissionList);

        assertEquals(permissionList, permissionService.findAll());
    }
}
