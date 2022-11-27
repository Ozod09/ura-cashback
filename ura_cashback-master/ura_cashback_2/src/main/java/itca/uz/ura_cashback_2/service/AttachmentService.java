package itca.uz.ura_cashback_2.service;

import itca.uz.ura_cashback_2.entity.Attachment;
import itca.uz.ura_cashback_2.payload.AttachmentResDto;
import itca.uz.ura_cashback_2.repository.AttachmentContentRepository;
import itca.uz.ura_cashback_2.repository.AttachmentRepository;
import lombok.SneakyThrows;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

@Service
public class AttachmentService {
    final
    AttachmentRepository attachmentRepository;
    final
    AttachmentContentRepository attachmentContentRepository;
    public static final Path root = Paths.get("D:\\test");

    public AttachmentService(AttachmentRepository attachmentRepository, AttachmentContentRepository attachmentContentRepository) {
        this.attachmentRepository = attachmentRepository;
        this.attachmentContentRepository = attachmentContentRepository;
    }

    public Long upload(MultipartFile request) {
        try {
//             Files.deleteIfExists(Path.of(root+"\\"+ request.getOriginalFilename()));
             Files.copy(request.getInputStream(), root.resolve(Objects.requireNonNull(request.getOriginalFilename())));
             Attachment save = attachmentRepository.save(Attachment.builder()
                    .contentType(request.getContentType())
                    .name(request.getOriginalFilename())
                    .size(request.getSize())
                    .build());
             return save.getId();
        }catch (Exception e){
            return null;
        }
    }
    @SneakyThrows
    public AttachmentResDto getFile(Long id){
        Attachment attachment = attachmentRepository.findById(id).orElseThrow(() -> new ResourceAccessException("GetAttachment"));
        Path file = root.resolve(attachment.getName());
        Resource resource = new UrlResource(file.toUri());
        AttachmentResDto attachmentResDto = new AttachmentResDto();
        attachmentResDto.setId(attachment.getId());
        attachmentResDto.setAttachment(attachment);
        attachmentResDto.setResource(resource);
        return attachmentResDto;
    }

}
