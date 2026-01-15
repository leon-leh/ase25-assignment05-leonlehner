package de.unibayreuth.se.taskboard.api.dtos;

//TODO: Add DTO for users.
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class UserDto {
	private UUID id;
	private LocalDateTime createdAt;
	@NotNull
	@NotBlank
	private String name;

	public UserDto() {}

	public UserDto(UUID id, LocalDateTime createdAt, String name) {
		this.id = id;
		this.createdAt = createdAt;
		this.name = name;
	}
}
