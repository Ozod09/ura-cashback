package itca.uz.ura_cashback_2.controller;

import itca.uz.ura_cashback_2.service.AttachmentService;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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
    public HttpEntity<?> upload(MultipartHttpServletRequest request) {
        Long upload = attachmentService.upload(request);
        return ResponseEntity.ok(upload);
    }

    @GetMapping("/getFile/{id}")
    public HttpEntity<?> download(@PathVariable Long id){
        return attachmentService.getFile(id);
    }
}
