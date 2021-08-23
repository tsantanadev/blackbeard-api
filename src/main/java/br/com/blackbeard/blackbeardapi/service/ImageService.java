package br.com.blackbeard.blackbeardapi.service;

import br.com.blackbeard.blackbeardapi.exceptions.FileException;
import br.com.blackbeard.blackbeardapi.exceptions.ObjectNotFoundException;
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

    private static final Integer LIMITED_IMAGE = 5;

    private final ImageRepository repository;
    private final S3Service s3Service;

    public URI saveImages(BarberShop barberShop, MultipartFile multipartFile) {
        var listImages = repository.findAllByBarberShopId(barberShop.getId());
        if (listImages.size() >= LIMITED_IMAGE) {
            throw new FileException("limit of images exceeded");
        }
        var image = createdImage(barberShop);

        var uriImage = s3Service.uploadFile(multipartFile, image.getId().toString(), "image");
        image.setUrl(uriImage.toString());
        repository.save(image);
        return uriImage;
    }

    public void deleteImage(BarberShop barberShop, UUID imageId) {
        var image = findById(imageId);
        if (!image.getBarberShop().getId().equals(barberShop.getId())) {
            throw new FileException("image does not belong to this barbershop");
        }
        s3Service.deleteFile(image.getId());
        repository.delete(image);
    }

    public Image findById(UUID id) {
        return repository.findById(id)
                .orElseThrow(ObjectNotFoundException::new);
    }

    public Image createdImage(BarberShop barberShop) {
        return Image.builder()
                .id(UUID.randomUUID())
                .barberShop(barberShop)
                .build();

    }

}
