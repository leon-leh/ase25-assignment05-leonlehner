package de.unibayreuth.se.taskboard.api.controller;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import de.unibayreuth.se.taskboard.api.dtos.UserDto;
import de.unibayreuth.se.taskboard.business.exceptions.DuplicateNameException;
import de.unibayreuth.se.taskboard.business.exceptions.MalformedRequestException;
import de.unibayreuth.se.taskboard.business.exceptions.UserNotFoundException;

@OpenAPIDefinition(
        info = @Info(
                title = "TaskBoard",
                version = "0.0.1"
        )
)
@Tag(name = "Users")
@Controller
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
        private final de.unibayreuth.se.taskboard.business.ports.UserService userService;
        private final de.unibayreuth.se.taskboard.api.mapper.UserDtoMapper userDtoMapper;

        // TODO: Add GET /api/users endpoint to retrieve all users.
        @GetMapping
        public ResponseEntity<List<UserDto>> getAll() {
                return ResponseEntity.ok(
                        userService.getAll().stream()
                                .map(userDtoMapper::fromBusiness)
                                .toList()
                );
        }

        // TODO: Add GET /api/users/{id} endpoint to retrieve a user by ID.
        @GetMapping("/{id}")
        public ResponseEntity<UserDto> getById(@PathVariable UUID id) {
                try {
                        return ResponseEntity.ok(
                                userDtoMapper.fromBusiness(userService.getById(id).orElseThrow(() -> new UserNotFoundException("User with ID " + id + " does not exist.")))
                        );
                } catch (UserNotFoundException e) {
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
                }
        }

        // TODO: Add POST /api/users endpoint to create a new user based on a provided user DTO.
        @PostMapping
        public ResponseEntity<UserDto> create(@RequestBody @Valid UserDto userDto) {
                try {
                        return ResponseEntity.ok(
                                userDtoMapper.fromBusiness(
                                        userService.create(userDtoMapper.toBusiness(userDto))
                                )
                        );
                } catch (MalformedRequestException | DuplicateNameException e) {
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
                }
        }
}
