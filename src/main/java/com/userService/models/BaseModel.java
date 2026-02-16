package com.userService.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;

@Getter
@Setter
@MappedSuperclass//we use it when parent class is not a real entity-> we use it for storing the common attributes
@EntityListeners(AuditingEntityListener.class)//it enables jpa to listen to changes made to any entity
public class BaseModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @CreatedDate
    private Date createdAt;
    @LastModifiedDate
    private Date lastModifiedAt;
    private boolean isDeleted;

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }
}
/*
  to record who has made the changes and when the changes are been made we use
  1 enable the JpaAuditing in the main class-> @EnableJpaAuditing()
  2 enable the EntityListeners in the base class-> @EntityListeners(AuditingEntityListener.class)
  3 on attribute use annotation createdAt, modifiedAt-> @LastModifiedBy, @LastModifiedBy
 */