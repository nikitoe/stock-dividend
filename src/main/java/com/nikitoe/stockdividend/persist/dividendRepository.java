package com.nikitoe.stockdividend.persist;

import com.nikitoe.stockdividend.persist.entity.DividendEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface dividendRepository extends JpaRepository<DividendEntity, Long> {

}
