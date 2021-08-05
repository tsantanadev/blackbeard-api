package br.com.blackbeard.blackbeardapi.service;

import br.com.blackbeard.blackbeardapi.models.BarberShop;
import br.com.blackbeard.blackbeardapi.models.Image;
import br.com.blackbeard.blackbeardapi.repositories.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
public class ImageService {

    @Autowired
    private ImageRepository repository;

    public List<Image> saveImages(BarberShop barberShop) {
        var images = Arrays.asList(
                Image.builder()
                        .id(UUID.randomUUID())
                        .url("https://graces.com.br/wp-content/uploads/2019/02/o-que-nao-pode-faltar-na-sua-barbearia-equipamentos-1024x640.jpg")
                        .barberShop(barberShop)
                        .build(),
                Image.builder()
                        .id(UUID.randomUUID())
                        .url("http://fastcorpbr.com/wp-content/uploads/2019/04/reforma-de-barbearia.jpg")
                        .barberShop(barberShop)
                        .build(),
                Image.builder()
                        .id(UUID.randomUUID())
                        .url("https://www.barbeariasantome.com.br/wp-content/uploads/2017/12/IMG_4503-1.png")
                        .barberShop(barberShop)
                        .build()
        );
        return repository.saveAll(images);
    }
}
