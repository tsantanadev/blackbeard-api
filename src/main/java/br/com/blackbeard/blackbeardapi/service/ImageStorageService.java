package br.com.blackbeard.blackbeardapi.service;

import org.springframework.web.multipart.MultipartFile;

import java.net.URI;
import java.util.UUID;

public interface ImageStorageService {

    URI uploadFile(MultipartFile multipartFile, String fileName);

    void deleteFile(UUID id);

}
