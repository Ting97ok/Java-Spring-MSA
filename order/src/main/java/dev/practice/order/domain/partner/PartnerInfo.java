package dev.practice.order.domain.partner;

import dev.practice.order.domain.partner.Partner.Status;
import lombok.Getter;

@Getter
public class PartnerInfo {

  private final Long id;
  private final String partnerToken;
  private final String partnerName;
  private final String businessNo;
  private final String email;
  private final Status status;

  public PartnerInfo(Partner partner) {
    this.id = partner.getId();
    this.partnerToken = partner.getPartnerToken();
    this.partnerName = partner.getPartnerName();
    this.businessNo = partner.getBusinessNo();
    this.email = partner.getEmail();
    this.status = partner.getStatus();
  }
}
