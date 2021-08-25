package br.com.blackbeard.blackbeardapi.exceptions;

public class FileException extends RuntimeException {

    public static final String ERROR_TO_READ_FILE = "Error to read file";
    public static final String IMAGE_FORMAT_ERROR_MESSAGE = "The image format must be PNG or JPG";
    public static final String FAIL_TO_CONVERT_URL_TO_URI = "fail to convert URL to URI";

    public FileException(String msg) {
        super(msg);
    }

    public static FileException errorToReadFile(){
        return new FileException(ERROR_TO_READ_FILE);
    }

    public static FileException invalidImageFormat(){
        return new FileException(IMAGE_FORMAT_ERROR_MESSAGE);
    }

    public static FileException errorToGetImageURI(){
        return new FileException(FAIL_TO_CONVERT_URL_TO_URI);
    }
}
