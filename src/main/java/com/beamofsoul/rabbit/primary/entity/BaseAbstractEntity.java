package com.beamofsoul.rabbit.primary.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@MappedSuperclass
public abstract class BaseAbstractEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Column(updatable = false, columnDefinition = "datetime comment '最后修改时间'")
	protected LocalDateTime createdDate;

	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Column(columnDefinition = "datetime comment '创建时间'")
	protected LocalDateTime updatedDate;

	public BaseAbstractEntity() {
		this.createdDate = this.updatedDate = LocalDateTime.now();
	}
}
