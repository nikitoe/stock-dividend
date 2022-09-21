package com.nikitoe.stockdividend.service;

import com.nikitoe.stockdividend.exception.impl.NoCompanyException;
import com.nikitoe.stockdividend.model.Company;
import com.nikitoe.stockdividend.model.Dividend;
import com.nikitoe.stockdividend.model.ScrapedResult;
import com.nikitoe.stockdividend.model.constants.CacheKey;
import com.nikitoe.stockdividend.persist.CompanyRepository;
import com.nikitoe.stockdividend.persist.DividendRepository;
import com.nikitoe.stockdividend.persist.entity.CompanyEntity;
import com.nikitoe.stockdividend.persist.entity.DividendEntity;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class FinanceService {

    private final CompanyRepository companyRepository;
    private final DividendRepository dividendRepository;

    @Cacheable(key = "#companyName", value = CacheKey.KEY_FINANCE)
    public ScrapedResult getDividendByCompanyName(String companyName) {

        log.info("Search company ->" + companyName);

        // 1. 회사명을 기준으로 회사 정보 조회
        CompanyEntity company = this.companyRepository.findByName(companyName)
            .orElseThrow(() -> new NoCompanyException());

        // 2. 조회된 회사Id로 배당금 정보를 조회
        List<DividendEntity> dividendEntities = this.dividendRepository.findAllByCompanyId(
            company.getId());

        // 3. 결과 조합 후 반환
        List<Dividend> dividends = dividendEntities.stream()
            .map(e -> new Dividend(e.getDate(), e.getDividend()))
            .collect(Collectors.toList());

        return new ScrapedResult(new Company(company.getTicker(), company.getName())
            , dividends);
    }
}
