package by.afinny.apigateway.dto.credit;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter(AccessLevel.PUBLIC)
@ToString
@Schema(description = "DTO for uploading documents")
public class RequestCreditOrderDocumentDto {

    @Schema(description = "Document for credit order")
    private MultipartFile file;
}
