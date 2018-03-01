package com.beamofsoul.rabbit.primary.entity;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

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
	
	@Column(columnDefinition = "varchar(20) not null comment '电话号码'")
	private String phone;
	
	@Column(columnDefinition = "varchar(200) comment '头像照片'")
	private String photo;
	
	@Transient
	private String photoString;
	
	@Column(columnDefinition = "int default 1 comment '用户状态 - 1:正常,0:锁定'")
	private Integer status;
	
	@ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE}, fetch = FetchType.LAZY)
	@JoinTable(name = "T_USER_ROLE", joinColumns = {@JoinColumn(name = "user_id")}, inverseJoinColumns = {@JoinColumn(name = "role_id")})
	private Set<Role> roles = new HashSet<Role>(0);
	
	public User(Long id) {
		this.id = id;
	}
	
	public Status getStatusEnum() {
		return Status.getInstance(status);
	}
	
	@RequiredArgsConstructor(access=AccessLevel.PROTECTED)
	public static enum Status {
		NORMAL(1),LOCKED(0);
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

	@Override
	public String toString() {
		return "User [id=" + id + ", username=" + username + ", password=" + password + ", nickname=" + nickname
				+ ", email=" + email + ", phone=" + phone + ", photo=" + photo + ", photoString=" + photoString
				+ ", status=" + status + ", roles=" + roles + "]";
	}
}
