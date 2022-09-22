package com.nikitoe.stockdividend.model;

import com.nikitoe.stockdividend.persist.entity.CompanyEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Company {

    private String ticker;
    private String name;

    public static Company  of(CompanyEntity company){
        return Company.builder()
            .ticker(company.getTicker())
            .name(company.getName())
            .build();
    }


}
