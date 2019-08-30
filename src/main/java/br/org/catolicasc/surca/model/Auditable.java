package br.org.catolicasc.surca.model;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
class Auditable {
    private @CreatedDate
    LocalDateTime createdDate;
    private @LastModifiedDate
    LocalDateTime lastModifiedDate;

    private @CreatedBy
    String createdBy;
    private @LastModifiedBy
    String lastModifiedBy;
}
