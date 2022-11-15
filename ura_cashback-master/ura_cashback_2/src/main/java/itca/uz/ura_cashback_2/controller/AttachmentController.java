package itca.uz.ura_cashback_2.controller;

import itca.uz.ura_cashback_2.payload.AttachmentResDto;
import itca.uz.ura_cashback_2.service.AttachmentService;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

@RestController
@RequestMapping("/api/attachment")
public class AttachmentController {
    final
    AttachmentService attachmentService;

    public AttachmentController(AttachmentService attachmentService) {
        this.attachmentService = attachmentService;
    }

    @PostMapping
    public HttpEntity<?> upload(@RequestParam MultipartFile request) {
        return ResponseEntity.ok(attachmentService.upload(request));
    }

    @GetMapping("/getFile/{id}")
    public HttpEntity<?> download(@PathVariable Long id){
         AttachmentResDto attachmentResDto = attachmentService.getFile(id);
        return ResponseEntity.ok()
                .contentType(MediaType.valueOf(attachmentResDto.getAttachment().getContentType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + attachmentResDto.getAttachment().getName())
                .body(attachmentResDto.getResource());
    }
}
