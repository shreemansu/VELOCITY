package com.car.dto;

import java.io.Serializable;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddUserDto implements Serializable {
	private String name;
	private String email;
	private String phone;
	@Size(min = 5, max = 25, message = "Password length must be greater than 5")
	private String password;
}
