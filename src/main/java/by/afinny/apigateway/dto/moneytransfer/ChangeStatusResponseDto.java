package by.afinny.apigateway.dto.moneytransfer;

import by.afinny.apigateway.dto.moneytransfer.constant.TransferStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.UUID;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class ChangeStatusResponseDto {
    private UUID transferId;
    private TransferStatus status;
}
