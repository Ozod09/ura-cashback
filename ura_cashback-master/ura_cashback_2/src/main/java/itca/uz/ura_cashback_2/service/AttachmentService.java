package itca.uz.ura_cashback_2.service;

import itca.uz.ura_cashback_2.entity.Attachment;
import itca.uz.ura_cashback_2.entity.AttachmentContent;
import itca.uz.ura_cashback_2.repository.AttachmentContentRepository;
import itca.uz.ura_cashback_2.repository.AttachmentRepository;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.Objects;

@Service
public class AttachmentService {
    final
    AttachmentRepository attachmentRepository;
    final
    AttachmentContentRepository attachmentContentRepository;

    public AttachmentService(AttachmentRepository attachmentRepository, AttachmentContentRepository attachmentContentRepository) {
        this.attachmentRepository = attachmentRepository;
        this.attachmentContentRepository = attachmentContentRepository;
    }

    public static final Path root = Paths.get("D:\\UraCashback save attachment");


    public Long upload(MultipartHttpServletRequest request) {
        try {
            Iterator<String> fileNames = request.getFileNames();
            while (fileNames.hasNext()) {
                MultipartFile file = request.getFile(fileNames.next());
                assert file != null;
                Attachment attachment = new Attachment(
                        file.getOriginalFilename(),
                        file.getContentType(),
                        file.getSize());
                Attachment save = attachmentRepository.save(attachment);
                AttachmentContent attachmentContent = new AttachmentContent(
                        save,
                        file.getBytes());
                attachmentContentRepository.save(attachmentContent);
                return save.getId();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public ResponseEntity<byte[]> getFile(Long id){
        Attachment attachment = attachmentRepository.findById(id).orElseThrow(() -> new ResourceAccessException("GetAttachment"));
        AttachmentContent attachmentContent = attachmentContentRepository.findByAttachment(attachment);
        return ResponseEntity.ok()
                .contentType(MediaType.valueOf(attachment.getContentType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\""+attachment.getName()+"\"")
                .body(attachmentContent.getContentSize());
    }

    public Long uploadd(MultipartHttpServletRequest request){
        Iterator<String> fileNames = request.getFileNames();
        try {
            while (fileNames.hasNext()) {
                MultipartFile file = request.getFile(fileNames.next());
                assert file != null;
                String path = root + "\\" + file.getOriginalFilename();
                Attachment saveAttachment = attachmentRepository.save(
                        Attachment.builder()
                                .contentType(file.getContentType())
                                .fileName(file.getName())
                                .size(file.getSize())
                                .url(path)
                                .build());
                return saveAttachment.getId();
                Files.deleteIfExists(Path.of(root+"\\"+file.getOriginalFilename()));
                Files.copy(file.getInputStream(),root.resolve(Objects.requireNonNull(file.getOriginalFilename())));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return null;
    }



//    public String addProduct(ProductDto dto) {
//        AuthUser user = sessionUser.getUser();
//        MultipartFile multipartFile = dto.getMultipartFile();
//        Category category = categoryRepository.findById(dto.getCategoryId()).orElseThrow(() -> new GenericNotFoundException("Category not found",404));
//        String path = root + "\\" + dto.getMultipartFile().getOriginalFilename();
//        Attachment  saveAttachment = attachmentRepository.save(
//                Attachment.builder()
//                        .contentType(dto.getMultipartFile().getContentType())
//                        .fileName(dto.getMultipartFile().getOriginalFilename())
//                        .size(dto.getMultipartFile().getSize())
//                        .url(path)
//                        .build());
//        try {
//            Files.deleteIfExists(Path.of(root+"\\"+dto.getMultipartFile().getOriginalFilename()));
//            Files.copy(multipartFile.getInputStream(),root.resolve(Objects.requireNonNull(multipartFile.getOriginalFilename())));
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//        Product build = Product.builder()
//                .category(category)
//                .color(dto.getColor())
//                .name(dto.getName())
//                .description(dto.getDescription())
//                .count(dto.getCount())
//                .photo(saveAttachment)
//                .price(dto.getPrice())
//                .size(dto.getSize())
//                .createdBy(user.getId())
//                .createdAt(LocalDateTime.now())
//                .build();
//        productRepository.save(build);
//        return "Product successfully saved";
//    }



}
