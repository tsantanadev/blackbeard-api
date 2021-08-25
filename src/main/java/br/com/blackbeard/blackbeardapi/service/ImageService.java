package br.com.blackbeard.blackbeardapi.service;

import br.com.blackbeard.blackbeardapi.exceptions.BarberShopImageLimitException;
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

    private static final Integer IMAGE_LIMIT = 5;
    public static final String IMAGE_DOES_NOT_THIS_BARBERSHOP = "image does not belong to this barbershop";

    private final ImageRepository repository;
    private final ImageStorageService imageStorageService;
    private final BarberShopService barberShopService;

    public URI saveImage(UUID barberShopId, MultipartFile multipartFile) {
        var barberShop = barberShopService.findById(barberShopId);
        var listImages = repository.findAllByBarberShopId(barberShop.getId());
        if (listImages.size() >= IMAGE_LIMIT) {
            throw new BarberShopImageLimitException();
        }
        var image = createImage(barberShop);

        var uriImage = imageStorageService.uploadFile(multipartFile, image.getId().toString());
        image.setUrl(uriImage.toString());
        repository.save(image);
        return uriImage;
    }

    public void deleteImage(UUID barberShopId, UUID imageId) {
        var barberShop = barberShopService.findById(barberShopId);
        var image = findById(imageId);
        if (!image.getBarberShop().getId().equals(barberShop.getId())) {
            throw new FileException(IMAGE_DOES_NOT_THIS_BARBERSHOP);
        }
        imageStorageService.deleteFile(image.getId());
        repository.delete(image);
    }

    public Image findById(UUID id) {
        return repository.findById(id)
                .orElseThrow(ObjectNotFoundException::new);
    }

    public Image createImage(BarberShop barberShop) {
        return Image.builder()
                .id(UUID.randomUUID())
                .barberShop(barberShop)
                .build();
    }
}
