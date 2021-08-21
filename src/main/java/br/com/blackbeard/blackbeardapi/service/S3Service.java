package br.com.blackbeard.blackbeardapi.service;

import br.com.blackbeard.blackbeardapi.exceptions.FileException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.UUID;

@Service
public class S3Service {

    @Autowired
    private AmazonS3 s3;

    @Value("${s3.bucket}")
    private String bucket;

    public URI uploadFile(MultipartFile multipartFile, String fileName, String contentType) {
        var meta = new ObjectMetadata();
        meta.setContentType(contentType);
        var inputStream = getPNGImageFromFile(multipartFile);
        s3.putObject(bucket, fileName, inputStream, meta);
        try {
            return s3.getUrl(bucket, fileName).toURI();
        } catch (URISyntaxException e) {
            throw new FileException("fail to convert URL to URI");
        }
    }

    public void deleteFile(UUID id) {
        s3.deleteObject(bucket, id.toString());
    }

    private InputStream getPNGImageFromFile(MultipartFile multipartFile) {
        var extension = FilenameUtils.getExtension(multipartFile.getOriginalFilename());
        if (!"png".equalsIgnoreCase(extension) && !"jpg".equalsIgnoreCase(extension)) {
            throw new FileException("only accept images PNG and JPG");
        }
        try {
            var image = ImageIO.read(multipartFile.getInputStream());
            return getInputStream(image);

        } catch (IOException e) {
            throw new FileException("error to read archive");
        }
    }

    private InputStream getInputStream(BufferedImage img) {
        try {
            var os = new ByteArrayOutputStream();
            ImageIO.write(img, "png", os);
            return new ByteArrayInputStream(os.toByteArray());
        } catch (IOException e) {
            throw new FileException("error to read archive");
        }
    }
}
