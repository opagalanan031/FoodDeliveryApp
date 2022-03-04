package com.learning.payload.request;

import java.util.Set;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserRequest {
	@NotBlank
	private String name;
	@NotNull
	private String email;
	@NotBlank
	private String password;
	
	@NotEmpty
	private Set<Address> address;
}
