package vn.hoangdung.restAPI.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import vn.hoangdung.restAPI.domain.Permission;
import vn.hoangdung.restAPI.domain.Role;
import vn.hoangdung.restAPI.domain.response.ResultPaginationDTO;
import vn.hoangdung.restAPI.repository.PermissionRepository;
import vn.hoangdung.restAPI.repository.RoleRepository;
import vn.hoangdung.restAPI.util.RoleSpecification;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RoleService {

    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;

    public RoleService(
            RoleRepository roleRepository,
            PermissionRepository permissionRepository) {
        this.roleRepository = roleRepository;
        this.permissionRepository = permissionRepository;
    }

    //check name
    public boolean existByName(String name) {
        return this.roleRepository.existsByName(name);
    }

    //create
    public Role create(Role role) {
        //check permission
        if(role.getPermissions() != null) {
            List<Long> reqPermissions = role.getPermissions()
                    .stream().map(Permission::getId)
                    .toList();
            List<Permission> dbPermission = this.permissionRepository.findByIdIn(reqPermissions);
            role.setPermissions(dbPermission);
        }
        return this.roleRepository.save(role);
    }

    public Role fetchById(long id) {
        Optional<Role> roleOptional = this.roleRepository.findById(id);
        return roleOptional.orElse(null);
    }

    public Role update(Role r) {
        Role roleDB = this.fetchById(r.getId());
        // check permissions
        if (r.getPermissions() != null) {
            List<Long> reqPermissions = r.getPermissions()
                    .stream().map(Permission::getId)
                    .collect(Collectors.toList());

            List<Permission> dbPermissions = this.permissionRepository.findByIdIn(reqPermissions);
            r.setPermissions(dbPermissions);
        }

        roleDB.setName(r.getName());
        roleDB.setDescription(r.getDescription());
        roleDB.setActive(r.isActive());
        roleDB.setPermissions(r.getPermissions());
        roleDB = this.roleRepository.save(roleDB);
        return roleDB;
    }

    public void delete(long id) {
        this.roleRepository.deleteById(id);
    }

    public ResultPaginationDTO getListRole(String name, Pageable pageable) {
        Specification<Role> spec = Specification.where(RoleSpecification.hasName(name));
        Page<Role> pageResume = this.roleRepository.findAll(spec, pageable);

        ResultPaginationDTO result = new ResultPaginationDTO();

        ResultPaginationDTO.Meta meta = new ResultPaginationDTO.Meta();
        meta.setPage(pageResume.getNumber() + 1);
        meta.setPageSize(pageResume.getSize());
        meta.setPages(pageResume.getTotalPages());
        meta.setTotal(pageResume.getTotalElements());
        result.setMeta(meta);

        result.setResult(pageResume.getContent());

        return result;
    }

}
