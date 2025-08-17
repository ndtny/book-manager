package com.david.bookmanager.config;

import com.david.bookmanager.service.BorrowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * 定时任务配置类
 */
@Configuration
@EnableScheduling
public class ScheduleConfig {

    @Autowired
    private BorrowService borrowService;

    /**
     * 每天凌晨2点更新借阅状态
     */
    @Scheduled(cron = "0 0 2 * * ?")
    public void updateBorrowStatus() {
        borrowService.updateBorrowStatus();
    }
}