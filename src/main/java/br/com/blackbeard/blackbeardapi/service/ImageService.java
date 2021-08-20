package br.com.blackbeard.blackbeardapi.service;

import br.com.blackbeard.blackbeardapi.models.BarberShop;
import br.com.blackbeard.blackbeardapi.models.Image;
import br.com.blackbeard.blackbeardapi.repositories.ImageRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;
import java.util.UUID;

@Service
@AllArgsConstructor
public class ImageService {


    private final ImageRepository repository;
    private final S3Service s3Service;

    public URI saveImages(BarberShop barberShop, MultipartFile multipartFile) {
        var image = Image.builder()
                .id(UUID.randomUUID())
                .barberShop(barberShop)
                .build();

        var uriImage = s3Service.uploadFile(multipartFile, image.getId().toString(), "image");
        image.setUrl(uriImage.toString());
        repository.save(image);
        return uriImage;
    }

}
