package com.telerikacademy.socialnetwork.models;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;

@MappedSuperclass
@ApiModel(description = "Details about create and update Datetime")
public abstract class AbstractTimestampEntity {

  @CreationTimestamp
  @Column(name = "create_date", nullable = false, updatable = false)
  @ApiModelProperty(notes = "Datetime when created")
  private Date createDate;

  @UpdateTimestamp
  @Column(name = "update_date", nullable = false)
  @ApiModelProperty(notes = "Datetime when updated")
  private Date updateDate;

  public Date getCreateDate() {
    return createDate;
  }

  public void setCreateDate(Date createDate) {
    this.createDate = createDate;
  }

  public Date getUpdateDate() {
    return updateDate;
  }

  public void setUpdateDate(Date updateDate) {
    this.updateDate = updateDate;
  }
}
