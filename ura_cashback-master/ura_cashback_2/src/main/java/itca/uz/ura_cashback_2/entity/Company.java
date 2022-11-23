package itca.uz.ura_cashback_2.entity;

import itca.uz.ura_cashback_2.entity.template.AbsEntity;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Company extends AbsEntity {

    @Column(nullable = false, unique = true)
    private String name;

    private String bio;

    private String description;

    private int clientPercentage;

    @OneToOne
    private Attachment attachment;

    private byte active;
}
