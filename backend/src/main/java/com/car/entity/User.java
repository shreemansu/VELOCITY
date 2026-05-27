package com.car.entity;

import java.time.LocalDateTime;
import java.util.List;

import com.car.enums.UserStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@NotBlank(message = "Name can't be empty")
	@Size(min = 5, max = 30, message = "Name length should be between 5 and 30")
	private String name;
	
	@NotBlank(message = "This field is mandatory")
	@Column(nullable = false, unique = true)
	@Email(message = "Please Provide Valid Email")
	private String email;
	
	@NotBlank(message = "Phone number can't be empty")
	@Column(nullable = false)
	@Pattern(regexp = "^[0-9]{10}$", message = "Enter a valid phone number")
	private String phone;
	
	@NotBlank(message = "Password can't be empty")
	@Column(nullable = false)
	//@Size(min = 5, max = 25, message = "Password length must be greater than 5")
	private String password;
	
	@Column(nullable = false)
	private String role;
	
	@Enumerated(EnumType.STRING)
	private UserStatus status=UserStatus.ACTIVE;
	
	private LocalDateTime createdAt;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
	private List<Rental> rentals;
}
