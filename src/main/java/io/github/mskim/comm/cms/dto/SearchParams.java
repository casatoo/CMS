package io.github.mskim.comm.cms.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class SearchParams extends BaseDTO {
    private String id;
    private String userId;
    private String month;
    private LocalDate startDate;
    private LocalDate endDate;
}
