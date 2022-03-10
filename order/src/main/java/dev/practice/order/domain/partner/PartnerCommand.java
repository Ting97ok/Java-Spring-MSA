package dev.practice.order.domain.partner;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class PartnerCommand {

  private final String partnerName;
  private final String businessNo;
  private final String email;

  public Partner toEntity() {
    var initPartner = Partner.builder()
        .partnerName(partnerName)
        .businessNo(businessNo)
        .email(email)
        .build();

    return initPartner;
  }
}
