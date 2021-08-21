package br.com.blackbeard.blackbeardapi.service;

import br.com.blackbeard.blackbeardapi.exceptions.ObjectAlreadyCreatedException;
import br.com.blackbeard.blackbeardapi.exceptions.ObjectNotFoundException;
import br.com.blackbeard.blackbeardapi.models.Address;
import br.com.blackbeard.blackbeardapi.models.BarberShop;
import br.com.blackbeard.blackbeardapi.repositories.BarberShopRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.net.URI;
import java.util.UUID;

import static java.util.Objects.nonNull;

@Service
@AllArgsConstructor
public class BarberShopService {

    private final BarberShopRepository repository;
    private final ImageService imageService;
    private final S3Service s3Service;

    @Transactional
    public BarberShop save(BarberShop barberShop) {
        barberShop.generateId();
        return repository.save(barberShop);
    }

    @Transactional
    public void update(BarberShop barberShop, UUID barberShopId) {
        var persistedBarberShop = findById(barberShopId);
        persistedBarberShop.update(barberShop);

        repository.save(persistedBarberShop);
    }

    public Page<BarberShop> listAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public BarberShop findById(UUID id) {
        return repository.findById(id)
                .orElseThrow(ObjectNotFoundException::new);
    }

    public void saveAddress(Address address, UUID barberShopId) {
        var barberShop = findById(barberShopId);

        if (nonNull(barberShop.getAddress())) {
            throw new ObjectAlreadyCreatedException("Address already created. Try update it");
        }

        barberShop.setAddress(address);
        repository.save(barberShop);
    }

    public URI saveLogo(UUID barberShopId, MultipartFile multipartFile) {
        var barberShop = findById(barberShopId);
        var uriLogo = s3Service.uploadFile(multipartFile, barberShop.getId().toString(), "logo");
        barberShop.setUrlLogo(uriLogo.toString());
        repository.save(barberShop);
        return uriLogo;
    }

    public void deleteLogo(UUID barberShopId) {
        var barberShop = findById(barberShopId);
        s3Service.deleteFile(barberShop.getId());
        barberShop.setUrlLogo(null);
        repository.save(barberShop);
    }

    public URI saveImages(UUID barberShopId, MultipartFile multipartFile) {
        var barberShop = findById(barberShopId);
        return imageService.saveImages(barberShop, multipartFile);
    }

    public void deleteImage(UUID barberShopId, UUID imageId) {
        var barberShop = findById(barberShopId);
        imageService.deleteImage(barberShop, imageId);
    }
}
