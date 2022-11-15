package itca.uz.ura_cashback_2.repository;

import itca.uz.ura_cashback_2.entity.Attachment;
import itca.uz.ura_cashback_2.entity.AttachmentContent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.CrossOrigin;

@CrossOrigin
public interface AttachmentContentRepository extends JpaRepository<AttachmentContent, String> {
//    @Query("select a from AttachmentContent a where a.attachment =: attachment")
//    AttachmentContent findByAttachment(Attachment attachment);
}
