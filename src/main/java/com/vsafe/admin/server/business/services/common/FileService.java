package com.vsafe.admin.server.business.services.common;

import com.vsafe.admin.server.business.response.UploadFileResponse;
import com.vsafe.admin.server.core.exceptions.FileStorageException;
import com.vsafe.admin.server.core.exceptions.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class FileService {
    @Value("${resource.upload.private.dir:upload-private}")
    private String privateFolder = "";

    @Value("${resource.upload.public.dir:upload-public}")
    private String publicFolder = "";

    @Value("${resource.download.private.uri:/app/file}")
    private String privateDownloadFileUri = "";

    @Value("${resource.download.public.uri:/open/file}")
    private String publicDownloadFileUri = "";

    private final Path privateFolderLocation;
    private final Path publicFolderLocation;

    @Value("${url.public.cdn:/}")
    private String pathCdn;

    FileService() {
        this.privateFolderLocation = Paths.get(privateFolder)
                .toAbsolutePath().normalize();
        this.publicFolderLocation = Paths.get(publicFolder)
                .toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.privateFolderLocation);
            Files.createDirectories(this.publicFolderLocation);
        } catch (Exception ex) {
            throw new FileStorageException("Could not create the directory where the uploaded files will be stored.");
        }
    }

    public List<UploadFileResponse> storeMultipleFile(List<MultipartFile> files, String path, String folder) {
        List<UploadFileResponse> uploadFileResponses = new ArrayList<>();
        files.forEach(e -> {
            String fileName = e.getOriginalFilename();
            UploadFileResponse uploadFileResponse = storeFile(e, path, folder, fileName);
            uploadFileResponses.add(uploadFileResponse);
        });
        return uploadFileResponses;
    }

    public UploadFileResponse storeFile(MultipartFile file, String pathDir, String folder, String fileName) {

        Path path = Paths.get(pathDir, folder);
        try {

            Files.createDirectories(path);
        } catch (Exception ex) {
            throw new FileStorageException("Could not create the directory where the uploaded files will be stored.");
        }

        if (fileName == null) fileName = new Date().getTime() + "";

        try {
            if (fileName.contains("..")) {
                throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
            }
            fileName = fileName.trim().replace("/", "");

            String fileExtension = FilenameUtils.getExtension(file.getOriginalFilename());
            String fileUpload = fileName.contains(".") ? fileName : (fileName + "." + fileExtension);

            String fileDownloadUri = folder + "/" + fileUpload;

            Path targetLocation = path.toAbsolutePath().normalize().resolve(fileUpload);

            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            return new UploadFileResponse(fileName, fileDownloadUri, file.getContentType(), file.getSize());
        } catch (IOException ex) {
            throw new FileStorageException("Could not store file " + fileName + ". Please try again!");
        } catch (Exception e) {
            throw new FileStorageException("Sorry! File invalid " + fileName);
        }

    }

    public UploadFileResponse storeFile(MultipartFile file, String pathDir, String folder, String fileName, boolean includeCdn) {

        Path path = Paths.get(pathDir, folder);
        try {

            Files.createDirectories(path);
        } catch (Exception ex) {
            throw new FileStorageException("Could not create the directory where the uploaded files will be stored.");
        }

        if (fileName == null) fileName = new Date().getTime() + "";

        try {
            if (fileName.contains("..")) {
                throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
            }
            fileName = fileName.trim().replace("/", "");

            String fileExtension = FilenameUtils.getExtension(file.getOriginalFilename());
            String fileUpload = fileName.contains(".") ? fileName : (fileName + "." + fileExtension);

            String fileDownloadUri = includeCdn ? pathCdn : "" + "/" + folder + "/" + fileUpload;

            Path targetLocation = path.toAbsolutePath().normalize().resolve(fileUpload);

            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            return new UploadFileResponse(fileName, fileDownloadUri, file.getContentType(), file.getSize());
        } catch (IOException ex) {
            throw new FileStorageException("Could not store file " + fileName + ". Please try again!");
        } catch (Exception e) {
            throw new FileStorageException("Sorry! File invalid " + fileName);
        }

    }

    public UploadFileResponse storeFile(MultipartFile file, String folder, String fileName, boolean isPublic) {

        Path path = Paths.get(isPublic ? this.publicFolder : this.privateFolder, folder);
        try {

            Files.createDirectories(path);
        } catch (Exception ex) {
            throw new FileStorageException("Could not create the directory where the uploaded files will be stored.");
        }

        if (fileName == null) fileName = new Date().getTime() + "";

        try {
            if (fileName.contains("..")) {
                throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
            }
            fileName = fileName.trim().replace("/", "");

            String fileExtension = FilenameUtils.getExtension(file.getOriginalFilename());
            String fileUpload = fileName.contains(".") ? fileName : (fileName + "." + fileExtension);
            String fileDownloadUri = String.join("/", isPublic ? publicDownloadFileUri : privateDownloadFileUri,
                    new String(new Base64().encode((folder + "/" + fileUpload).getBytes())) + "." + fileExtension);

            Path targetLocation = path.toAbsolutePath().normalize().resolve(fileUpload);

            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            return new UploadFileResponse(fileName, fileDownloadUri, file.getContentType(), file.getSize());
        } catch (IOException ex) {
            throw new FileStorageException("Could not store file " + fileName + ". Please try again!");
        } catch (Exception e) {
            throw new FileStorageException("Sorry! File invalid " + fileName);
        }

    }

    public Resource loadFileAsResource(String fileName, boolean isPublic) {
        try {
            Path fileStorageLocation = Paths.get(isPublic ? this.publicFolder : this.privateFolder);
            Path filePath = fileStorageLocation.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists()) {
                return resource;
            } else {
                throw new NotFoundException("File not found.");
            }
        } catch (MalformedURLException ex) {
            throw new NotFoundException("File not found.");
        }
    }

}
