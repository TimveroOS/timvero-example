package com.timvero.example.portal.client;

import static com.timvero.example.portal.InternalApiSecurityConfig.BASIC_AUTH;

import com.timvero.example.portal.client.form.CreateClientRequest;
import com.timvero.example.portal.client.form.CreateClientResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("clients")
@SecurityRequirement(name = BASIC_AUTH)
@Tag(name = "Client Management", description = "API for managing clients and their applications")
public class ClientController {

    @Autowired
    private ClientService clientService;

    @PostMapping
    @Operation(
        summary = "Create a new client",
        description = "Creates a new client with the provided personal and contact information"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Client created successfully",
            content = @Content(schema = @Schema(implementation = CreateClientResponse.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid request data",
            content = @Content()
        )
    })
    public ResponseEntity<CreateClientResponse> createClient(
        @RequestBody @Valid
        @Parameter(description = "Client creation request with personal and contact details")
        CreateClientRequest form) {
        UUID clientId = clientService.createClient(form);
        return ResponseEntity.ok(new CreateClientResponse(clientId));
    }
}
