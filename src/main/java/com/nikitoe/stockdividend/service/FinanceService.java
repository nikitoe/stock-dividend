package com.nikitoe.stockdividend.service;

import com.nikitoe.stockdividend.model.Company;
import com.nikitoe.stockdividend.model.Dividend;
import com.nikitoe.stockdividend.model.ScrapedResult;
import com.nikitoe.stockdividend.persist.CompanyRepository;
import com.nikitoe.stockdividend.persist.DividendRepository;
import com.nikitoe.stockdividend.persist.entity.CompanyEntity;
import com.nikitoe.stockdividend.persist.entity.DividendEntity;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class FinanceService {

    private final CompanyRepository companyRepository;
    private final DividendRepository dividendRepository;


    public ScrapedResult getDividendByCompanyName(String companyName) {

        // 1. 회사명을 기준으로 회사 정보 조회
        CompanyEntity company = this.companyRepository.findByName(companyName)
            .orElseThrow(() -> new RuntimeException("존재하지 않는 회사명입니다."));

        // 2. 조회된 회사Id로 배당금 정보를 조회
        List<DividendEntity> dividendEntities = this.dividendRepository.findAllByCompanyId(
            company.getId());

        // 3. 결과 조합 후 반환
        List<Dividend> dividends = dividendEntities.stream()
            .map(e -> Dividend.builder()
                .date(e.getDate())
                .dividend(e.getDividend())
                .build())
            .collect(Collectors.toList()
            );

        return new ScrapedResult(Company.builder()
            .ticker(company.getTicker())
            .name(company.getName())
            .build(), dividends
        );
    }
}
