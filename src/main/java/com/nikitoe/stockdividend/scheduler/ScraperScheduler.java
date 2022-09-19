package com.nikitoe.stockdividend.scheduler;

import com.nikitoe.stockdividend.model.Company;
import com.nikitoe.stockdividend.model.ScrapedResult;
import com.nikitoe.stockdividend.persist.CompanyRepository;
import com.nikitoe.stockdividend.persist.DividendRepository;
import com.nikitoe.stockdividend.persist.entity.CompanyEntity;
import com.nikitoe.stockdividend.persist.entity.DividendEntity;
import com.nikitoe.stockdividend.scraper.Scraper;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@AllArgsConstructor
public class ScraperScheduler {

    private final CompanyRepository companyRepository;
    private final DividendRepository dividendRepository;

    private final Scraper yahooFinanceScraper;

    // 일정 주기마다 실행
    @Scheduled(cron = "${scheduler.scrap.yahoo}")
    public void yahooFinanceScheduling() {

        log.info("scraping scheduler is started");

        // 저장된 회사 목록을 조회
        List<CompanyEntity> companies = this.companyRepository.findAll();

        // 회사마다 배당금 정보를 새로 스크래핑
        for (var company : companies) {
            log.info("scraping scheduler is started - > " + company.getName());
            ScrapedResult scrapedResult = this.yahooFinanceScraper.scrap(Company.builder()
                .name(company.getName())
                .ticker(company.getTicker())
                .build());

            // 스크래핑한 배당금 정보 중 데이터베이스에 없는 값은 저장
            scrapedResult.getDividendEntities().stream()
                // dividend 모델을 dividend entity로 매핑
                .map(e -> new DividendEntity(company.getId(), e))
                // element를 하나씩 dividend 레파지토리에 저장
                .forEach(e -> {
                    boolean exists = this.dividendRepository.existsByCompanyIdAndDate(
                        e.getCompanyId(), e.getDate());
                    if (!exists) {
                        this.dividendRepository.save(e);
                    }
                });

            // 연속적으로 스크래핑 대상 사이트 서버에 요청을 날리지 않도록 일시정지
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

    }


}
