package vn.hoangdung.restAPI.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import vn.hoangdung.restAPI.domain.response.File.ResUploadFileDTO;
import vn.hoangdung.restAPI.service.FileService;
import vn.hoangdung.restAPI.util.anotation.ApiMessage;
import vn.hoangdung.restAPI.util.error.StorageException;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class FileController {

    @Value("${hoangdung.upload-file.base-uri}")
    private String baseURI;

    private final FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping("/files")
    @ApiMessage("Upload file")
    public ResponseEntity<ResUploadFileDTO> upload(@RequestParam(name = "file", required = false) MultipartFile file,
                                                   @RequestParam("folder") String folder)
            throws URISyntaxException,
            IOException, StorageException {

        if(file == null || file.isEmpty()) {
            throw new StorageException("File is empty. Please upload a file");
        }

        //validator
        String fileName = file.getOriginalFilename();
        List<String> allowedExtensions = Arrays.asList("pdf", "jpg", "jpeg", "png", "doc", "docx");

        boolean isValidExtension = allowedExtensions.stream().anyMatch(ext ->
        {
            assert fileName != null;
            return fileName.toLowerCase().endsWith("." + ext);
        });
        if (!isValidExtension) {
//            return ResponseEntity.badRequest().body("Invalid file type based on extension.");
            throw new StorageException("Invalid file type based on extension.");
        }

        // Validate MIME type
//        String contentType = file.getContentType();
//        if (!allowedExtensions.contains(contentType)) {
//            throw new StorageException("Invalid file type based on MIME type.");
//        }

        //create directory
        this.fileService.createUploadFolder(baseURI + folder);
        //store file
        String uploadFile = this.fileService.store(file, folder);

        ResUploadFileDTO res = new ResUploadFileDTO(uploadFile, Instant.now());

        return ResponseEntity.ok().body(res);
    }

}
