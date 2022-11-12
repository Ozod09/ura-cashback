package itca.uz.ura_cashback_2.entity;

import itca.uz.ura_cashback_2.entity.template.AbsEntity;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class CompanyUserRole extends AbsEntity {

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private Long companyId;

    @Column(nullable = false)
    private Integer roleId;
}
