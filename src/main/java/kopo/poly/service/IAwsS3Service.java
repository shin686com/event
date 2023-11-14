package kopo.poly.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IAwsS3Service {
    List<String> uploadImage(List<MultipartFile> multipartFile);
    void deleteImage(String fileName);
    String createFileName(String fileName);
    String getFileExtension(String fileName);
}
