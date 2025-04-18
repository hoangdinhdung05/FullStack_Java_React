package vn.hoangdung.restAPI.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import vn.hoangdung.restAPI.domain.Permission;
import vn.hoangdung.restAPI.domain.response.ResultPaginationDTO;
import vn.hoangdung.restAPI.repository.PermissionRepository;
import vn.hoangdung.restAPI.util.PermissionSpecification;

import java.util.Optional;

@Service
public class PermissionService {

    private final PermissionRepository permissionRepository;

    public PermissionService(PermissionRepository permissionRepository) {
        this.permissionRepository = permissionRepository;
    }

    //check exists Permission
    public boolean isPermissionExist(Permission permission) {
        return this.permissionRepository.existsByModuleAndApiPathAndMethod(
                permission.getModule(),
                permission.getApiPath(),
                permission.getMethod()
        );
    }

    //get permissione by id
    public Permission fetchById(long id) {
        Optional<Permission> permissionOptional = this.permissionRepository.findById(id);
        return permissionOptional.orElse(null);
    }

    //create permission
    public Permission create(Permission permission) {
        return this.permissionRepository.save(permission);
    }

    //update
    public Permission update(Permission permission) {
        //get permission in DB
        Permission permissionDB = this.fetchById(permission.getId());
        if(permissionDB != null) {
            permissionDB.setName(permission.getName());
            permissionDB.setApiPath(permission.getApiPath());
            permissionDB.setMethod(permission.getMethod());
            permissionDB.setModule(permission.getModule());

            //update
            permissionDB = this.permissionRepository.save(permissionDB);
            return permissionDB;
        }
        return null;
    }

    public void delete(long id) {
        Optional<Permission> permissionOptional = this.permissionRepository.findById(id);
        Permission currentPermission = permissionOptional.get();
        currentPermission.getRoles().forEach(
                role -> role.getPermissions().remove(
                        currentPermission
                )
        );
        //delete
        this.permissionRepository.delete(currentPermission);
    }

    //get list
    public ResultPaginationDTO getListPermission(String name, Pageable pageable) {
        Specification<Permission> spec = Specification.where(PermissionSpecification.hasName(name));
        Page<Permission> pageResume = this.permissionRepository.findAll(spec, pageable);

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

    //check name
    public boolean isSameName(Permission p) {
        Permission permissionDB = this.fetchById(p.getId());
        if(permissionDB != null) {
            return permissionDB.getName().equals(p.getName());
        }
        return false;
    }
}
