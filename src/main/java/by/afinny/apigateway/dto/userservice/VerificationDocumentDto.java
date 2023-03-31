package by.afinny.apigateway.dto.userservice;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;
@Getter
@Setter(AccessLevel.PUBLIC)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VerificationDocumentDto {
    private UUID id;
    private UUID clientId;
    private LocalDate creationDate;
    private String documentName;
    private String fileFormat;
    private byte[] file;
}
