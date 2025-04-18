package vn.hoangdung.restAPI.controller;

import jakarta.validation.Valid;
import org.apache.el.stream.Optional;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.hoangdung.restAPI.domain.Permission;
import vn.hoangdung.restAPI.domain.response.ResultPaginationDTO;
import vn.hoangdung.restAPI.service.PermissionService;
import vn.hoangdung.restAPI.util.anotation.ApiMessage;
import vn.hoangdung.restAPI.util.error.IdInvalidException;

import java.nio.file.OpenOption;

@RestController
@RequestMapping("/api/v1")
public class PermissionController {

    private final PermissionService permissionService;

    public PermissionController(PermissionService permissionService) {
        this.permissionService = permissionService;
    }

    //create
    @PostMapping("/permissions")
    @ApiMessage("Create a Permission")
    public ResponseEntity<Permission> create(@RequestBody @Valid Permission permission) throws IdInvalidException {
        //check exists
        if(this.permissionService.isPermissionExist(permission)) {
            throw new IdInvalidException("Permission đã tồn tại");
        }
        Permission newPermission = this.permissionService.create(permission);
        return ResponseEntity.status(HttpStatus.CREATED).body(newPermission);
    }

    //update
    @PutMapping("/permissions")
    @ApiMessage("Update a Permission")
    public ResponseEntity<Permission> update(@RequestBody @Valid Permission permission) throws IdInvalidException {
        //check id exists
        if(this.permissionService.fetchById(permission.getId()) == null) {
            throw new IdInvalidException("Permission với Id = " + permission.getId() + " không tồn tại");
        }

        // check exist by module, apiPath and method
        if (this.permissionService.isPermissionExist(permission)) {
            throw new IdInvalidException("Permission đã tồn tại.");
        }
        Permission newPermission = this.permissionService.update(permission);
        return ResponseEntity.ok().body(newPermission);
    }

    @DeleteMapping("/permissions/{id}")
    @ApiMessage("delete a permission")
    public ResponseEntity<Void> delete(@PathVariable("id") long id) throws IdInvalidException {
        // check exist by id
        if (this.permissionService.fetchById(id) == null) {
            throw new IdInvalidException("Permission với id = " + id + " không tồn tại.");
        }
        this.permissionService.delete(id);
        return ResponseEntity.ok().body(null);
    }

    @GetMapping("/permissions")
    @ApiMessage("List permissions")
    public ResponseEntity<ResultPaginationDTO> getList(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "current", defaultValue = "1") int current,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {

        Pageable pageable = PageRequest.of(current - 1, pageSize);
        return ResponseEntity.ok(this.permissionService.getListPermission(name, pageable));
    }

}
