package com.kosa.kosafinalprojbackend.global.amazon.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.UUID;

@Log4j2
@Service
@RequiredArgsConstructor
public class S3Service {

  @Value("${cloud.aws.s3.bucket}")
  private String bucket;

  @Value("${cloud.aws.s3.dir}")
  private String dir;

  private final AmazonS3 amazonS3;

  // 이미지 여부 확인
  public boolean existsFile(String existingFileName) {

    try {
      String key = dir + "/" + existingFileName;
      amazonS3.getObjectMetadata(bucket, key);
      return true;
    } catch (Exception ex) {
      return false;
    }
  }


  // 이미지 저장
  public String uploadFile(MultipartFile multipartFile) {

    // 파일명
    String originalFileName = multipartFile.getOriginalFilename();
    String uploadFileName = getUuidFileName(Objects.requireNonNull(originalFileName));

    ObjectMetadata objectMetadata = new ObjectMetadata();
    objectMetadata.setContentLength(multipartFile.getSize());
    objectMetadata.setContentType(multipartFile.getContentType());


    // AWS S3에 저장
    try (InputStream inputStream = multipartFile.getInputStream()) {

      String keyName = dir + "/" + uploadFileName; // ex) /구분/파일.확장자

      // S3에 폴더 및 파일 업로드
      amazonS3.putObject(new PutObjectRequest(bucket, keyName, inputStream, objectMetadata));


      // 업로드된 파일의 URL 반환
      return amazonS3.getUrl(bucket, keyName).toString();

    } catch (IOException e) {
      log.error("Filed upload failed", e);
    }

    return "";
  }


  // 파일명 생성 (UUID)
  public String getUuidFileName(String fileName) {

    String ext = fileName.substring(fileName.indexOf(".") + 1);
    return UUID.randomUUID() + "." + ext;
  }


  // 파일을 덮어쓰지 않고 동일한 키로 업로드하면 수정과 동일하게 동작
  public String updateFile(MultipartFile file, String existingFileName) {

    String key = dir + "/" + existingFileName;
    ObjectMetadata objectMetadata = new ObjectMetadata();
    objectMetadata.setContentLength(file.getSize());
    objectMetadata.setContentType(file.getContentType());

    // S3에 파일 업로드 (기존 파일 덮어쓰기)
    try {
      amazonS3.putObject(new PutObjectRequest(bucket, key, file.getInputStream(), objectMetadata));
    } catch (IOException e) {
      throw new RuntimeException("이미지 업데이트 실패", e);
    }

    // 업로드된 파일의 URL 반환
    return amazonS3.getUrl(bucket, key).toString();
  }


  // 이미지 삭제
  public String deleteFile(String existingFileName) {

    String key = dir + "/" + existingFileName;

    // S3에서 파일 삭제
    amazonS3.deleteObject(new DeleteObjectRequest(bucket, key));

    return null;
  }
}
