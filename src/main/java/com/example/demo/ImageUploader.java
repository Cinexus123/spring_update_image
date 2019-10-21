package com.example.demo;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.demo.model.Image;
import com.example.demo.repo.ImageRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.example.demo.repo.ImageRepo;

import java.io.File;
import java.io.IOException;
import java.util.Map;

@Service
public class ImageUploader  {

    private Cloudinary cloudinary;

    private ImageRepo imageRepo;


    @Value("dfy7udwfu")
    private String cloudNameValue;
    @Value("515389927851221")
    private String apiKeyValue;
    @Value("LsdGTd19_ZANdjTfVfOv8RfRI38")
    private String apiSecretValue;

    @Autowired
    public ImageUploader(ImageRepo imageRepo,
                         @Value("dfy7udwfu") String cloudNameValue,
                         @Value("515389927851221") String apiKeyValue,
                         @Value("LsdGTd19_ZANdjTfVfOv8RfRI38") String apiSecretValue) {
        this.imageRepo = imageRepo;
        cloudinary = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", cloudNameValue,
                "api_key", apiKeyValue,
                "api_secret", apiSecretValue));
    }

    public String uploadFileAndSaveToDb(String path) {
        File file = new File(path);
        Map uploadResult = null;
        try {
            uploadResult = cloudinary.uploader().upload(file, ObjectUtils.emptyMap());
            imageRepo.save(new Image(uploadResult.get("url").toString()));
        } catch (IOException e) {
            // todo
        }
        return uploadResult.get("url").toString();
    }
}
