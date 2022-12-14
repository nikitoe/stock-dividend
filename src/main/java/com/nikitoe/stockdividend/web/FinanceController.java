package com.nikitoe.stockdividend.web;

import com.nikitoe.stockdividend.service.FinanceService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@Slf4j
@RequestMapping("/finance")
public class FinanceController {

    private final FinanceService financeService;

    /**
     * 해당 회사(회사 이름)에 대한 배당금 조회
     *
     * @param companyName
     * @return
     */
    @GetMapping("/dividend/{companyName}")
    public ResponseEntity<?> searchFinance(@PathVariable String companyName) {

        log.info("searchFinance companyName vlaue : " + companyName);

        var result = this.financeService.getDividendByCompanyName(companyName);

        return ResponseEntity.ok(result);
    }

}
