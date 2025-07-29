package com.timvero.example.portal.application;

import static com.timvero.example.portal.InternalApiSecurityConfig.BASIC_AUTH;

import com.timvero.example.admin.application.entity.ApplicationStatus;
import com.timvero.example.portal.application.form.CreateApplicationRequest;
import com.timvero.example.portal.application.form.CreateApplicationResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.io.IOException;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("applications")
@SecurityRequirement(name = BASIC_AUTH)
@Tag(name = "Application Management", description = "API for managing applications")
public class ApplicationPortalController {

    @Autowired
    private ApplicationPortalService applicationService;

    @PostMapping
    @Operation(
        summary = "Create an application",
        description = "Creates a new loan application"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Application created successfully",
            content = @Content(schema = @Schema(implementation = CreateApplicationResponse.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Client not found",
            content = @Content()
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid request data",
            content = @Content()
        )
    })
    public ResponseEntity<CreateApplicationResponse> createApplication(
        @RequestBody @Valid
        @Parameter(description = "Application creation request with employment and financial information")
        CreateApplicationRequest request) {
        UUID applicationId = applicationService.createApplication(request);
        return ResponseEntity.ok(new CreateApplicationResponse(applicationId));
    }

    @GetMapping("/{applicationId}/status")
    @Operation(
        summary = "Get application status",
        description = "Returns the current status of an application by application id"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Application status retrieved successfully",
            content = @Content(schema = @Schema(implementation = ApplicationStatus.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Application not found",
            content = @Content()
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid request parameters",
            content = @Content()
        )
    })
    public ResponseEntity<PortalApplicationStatus> getApplicationStatus(
        @PathVariable
        @Parameter(description = "The application id", example = "45b633aa-c59c-4ae0-9623-06a8379df8b2")
        UUID applicationId) {

        PortalApplicationStatus status = applicationService.getApplicationStatus(applicationId);
        return ResponseEntity.ok(status);
    }

    @GetMapping("/signature-url")
    @Operation(
        summary = "Get application signature url",
        description = "Returns the url of an application by application id"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Application status retrieved successfully",
            content = @Content(schema = @Schema(implementation = ApplicationStatus.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Application not found",
            content = @Content()
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid request parameters",
            content = @Content()
        ),
        @ApiResponse(
            responseCode = "412",
            description = "Application with incorrect status provided",
            content = @Content()
        )
    })
    public String getApplicationSignatureUrl(@RequestParam UUID applicationId, @RequestParam String returnUrl)
        throws IOException {
        return applicationService.getSignatureUrl(applicationId, returnUrl);
    }
}
