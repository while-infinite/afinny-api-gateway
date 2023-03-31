package by.afinny.apigateway.dto.infoservice;

import by.afinny.apigateway.dto.infoservice.constant.BankBranchType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.sql.Time;

@Builder
@Getter
@Setter(AccessLevel.PUBLIC)
@Schema(description = "DTO for response bank branch")
public class ResponseBankBranchDto {

    @Schema(description = "bank branch type")
    private BankBranchType bankBranchType;
    @Schema(description = "branch number")
    private String branchNumber;
    @Schema(description = "branch coordinates")
    private String branchCoordinates;
    @Schema(description = "city")
    private String city;
    @Schema(description = "branch address")
    private String branchAddress;
    @Schema(description = "is closed")
    private Boolean closed;
    @Schema(description = "opening time")
    private Time openingTime;
    @Schema(description = "closing time")
    private Time closingTime;
    @Schema(description = "work at weekends")
    private Boolean workAtWeekends;
    @Schema(description = "cash withdraw")
    private Boolean cashWithdraw;
    @Schema(description = "money transfer")
    private Boolean moneyTransfer;
    @Schema(description = "accept payment")
    private Boolean acceptPayment;
    @Schema(description = "currency exchange")
    private Boolean currencyExchange;
    @Schema(description = "exotic currency")
    private Boolean exoticCurrency;
    @Schema(description = "ramp")
    private Boolean ramp;
    @Schema(description = "replenish card")
    private Boolean replenishCard;
    @Schema(description = "replenish account")
    private Boolean replenishAccount;
    @Schema(description = "consultation")
    private Boolean consultation;
    @Schema(description = "insurance")
    private Boolean insurance;
    @Schema(description = "replenish without card")
    private Boolean replenishWithoutCard;
}