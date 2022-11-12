package itca.uz.ura_cashback_2.repository;

import itca.uz.ura_cashback_2.entity.Attachment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.CrossOrigin;

@CrossOrigin
public interface AttachmentRepository extends JpaRepository<Attachment, Long> {
}
