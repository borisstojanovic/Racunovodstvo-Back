package rs.raf.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.raf.demo.model.Permission;
import rs.raf.demo.repositories.PermissionRepository;

import java.util.List;
import java.util.Optional;

@Service
public class PermissionService implements IService<Permission, Long> {

    private final PermissionRepository permissionRepository;

    @Autowired
    public PermissionService(PermissionRepository permissionRepository) {
        this.permissionRepository = permissionRepository;
    }

    @Override
    public  Permission  save(Permission permission) {
        return permissionRepository.save(permission);
    }

    @Override
    public Optional<Permission> findById(Long id) {
        return permissionRepository.findById(id);
    }

    @Override
    public List<Permission> findAll() {
        return (List<Permission>) permissionRepository.findAll();
    }

    @Override
    public void deleteById(Long id) {
        permissionRepository.deleteById(id);
    }
}

