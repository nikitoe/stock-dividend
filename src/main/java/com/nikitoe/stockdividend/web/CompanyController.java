package com.nikitoe.stockdividend.web;

import com.nikitoe.stockdividend.model.Company;
import com.nikitoe.stockdividend.model.CompanyPage;
import com.nikitoe.stockdividend.model.constants.CacheKey;
import com.nikitoe.stockdividend.persist.entity.CompanyEntity;
import com.nikitoe.stockdividend.service.CompanyService;
import lombok.AllArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/company")
public class CompanyController {

    private final CompanyService companyService;
    private final CacheManager redisCacheManager;

    /**
     * 키워드 자동완성
     */
    @GetMapping("/autocomplete")
    public ResponseEntity<?> autocomplete(@RequestParam String keyword) {
        // Trie 자료구조 사용
        var result = this.companyService.autoComplete(keyword);

        // Like 연산자 사용
        //var result = this.companyService.getCompanyNamesByKeyword(keyword);

        return ResponseEntity.ok(result);
    }


    /**
     * 회사 정보 조회(페이징 처리)
     *
     * @param pageable
     * @return
     */
    @GetMapping
    @PreAuthorize("hasRole('READ')")
    public ResponseEntity<?> searchCompany(final Pageable pageable) {

        CompanyPage companies = this.companyService.getAllCompany(pageable);
        return ResponseEntity.ok(companies);
    }

    /**
     * 회사 및 배당금 정보 추가
     *
     * @param request
     * @return
     */
    @PostMapping
    @PreAuthorize("hasRole('WRITE')")
    public ResponseEntity<?> addCompany(@RequestBody Company request) {

        String ticker = request.getTicker().trim();
        if (ObjectUtils.isEmpty(ticker)) {
            throw new RuntimeException("ticker is empty");
        }

        Company company = this.companyService.save(ticker);
        this.companyService.addAutocompleteKeyword(company.getName());

        return ResponseEntity.ok(company);
    }

    /**
     * 회사 및 배당금, 키워드 삭제
     *
     * @return
     */
    @DeleteMapping("/{ticker}")
    @PreAuthorize("hasRole('WRITE')")
    public ResponseEntity<?> deleteCompany(@PathVariable String ticker) {
        String companyName = this.companyService.deleteCompany(ticker);
        // 캐시 정보 삭제
        this.clearFinanceCache(companyName);

        return ResponseEntity.ok(companyName);
    }


    /**
     * 캐시 정보 삭제
     *
     * @param companyName
     */
    public void clearFinanceCache(String companyName) {

        this.redisCacheManager.getCache(CacheKey.KEY_FINANCE).evict(companyName);

    }

}
