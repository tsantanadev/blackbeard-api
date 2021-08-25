package br.com.blackbeard.blackbeardapi.controllers;

import br.com.blackbeard.blackbeardapi.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequestMapping("/barberShop")
public class ImageController {

    @Autowired
    private ImageService service;

    @PostMapping("/image")
    public ResponseEntity<Void> saveImage(@RequestParam("barberShopId") UUID barberShopId,
                                          @RequestParam("image") MultipartFile multipartFile) {
        var uri = service.saveImage(barberShopId, multipartFile);
        return ResponseEntity.created(uri).build();
    }

    @DeleteMapping("/image")
    public ResponseEntity<Void> deleteImage(@RequestParam("barberShopId") UUID barberShopId,
                                            @RequestParam("imageId") UUID imageId) {
        service.deleteImage(barberShopId, imageId);
        return ResponseEntity.ok().build();
    }
}
