package com.nikitoe.stockdividend.scraper;

import com.nikitoe.stockdividend.model.Company;
import com.nikitoe.stockdividend.model.ScrapedResult;

public interface Scraper {

    ScrapedResult scrap(Company company);

    Company scrapCompanyByTicker(String ticker);

}
