package br.com.blackbeard.blackbeardapi.dtos.image;

import lombok.Data;

import java.util.UUID;

@Data
public class ImageResponse {
    private UUID id;
    private String url;
}
