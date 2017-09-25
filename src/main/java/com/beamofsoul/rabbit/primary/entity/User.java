package com.beamofsoul.rabbit.primary.entity;

import java.util.HashMap;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "T_USER")
//@org.hibernate.annotations.Table(appliesTo = "t_user", comment = "用户表")
public class User extends BaseAbstractEntity {

	private static final long serialVersionUID = -6503407292877788715L;

	@Id
	@GeneratedValue
	private Long id;
	
	@Column(columnDefinition = "varchar(20) unique not null comment '用户名'")
	private String username;
	
	@Column(columnDefinition = "varchar(20) not null comment '密码'")
	private String password;
	
	@Column(columnDefinition = "varchar(20) not null comment '昵称'")
	private String nickname;
	
	@Column(columnDefinition = "varchar(20) not null comment '电子邮箱'")
	private String email;
	
	@Column(columnDefinition = "int default 1 comment '用户状态 - 1:正常,0:冻结,-1:删除'")
	private int status; //1:正常, 0:冻结, -1:删除
	
	public User(Long id) {
		this.id = id;
	}
	
	public Status getStatus() {
		return Status.getInstance(status);
	}
	
	@RequiredArgsConstructor(access=AccessLevel.PROTECTED)
	public static enum Status {
		NORMAL(1),LOCKED(0),DELETED(-1);
		@Getter private final int value;
		private static HashMap<Integer, Status> codeValueMap = new HashMap<>(3);
		static {
			for (Status status : Status.values()) {
				codeValueMap.put(status.value, status);
			}
		}
		public static Status getInstance(int code) {
			return codeValueMap.get(code);
		}
	}
}
