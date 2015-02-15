package com.jtbdevelopment.TwistedHangman.quartz

import com.jtbdevelopment.TwistedHangman.players.reset.PlayerFreeGameResetJobDetailFactoryBean
import com.jtbdevelopment.TwistedHangman.players.reset.PlayerFreeGameResetScheduleFactoryBean
import groovy.transform.CompileStatic
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.quartz.SchedulerFactoryBean
import org.springframework.stereotype.Component

import javax.annotation.PostConstruct

/**
 * Date: 2/15/15
 * Time: 8:01 AM
 */
@Component
@CompileStatic
class TwistedHangmanSchedulerFactoryBean extends SchedulerFactoryBean {
    @Autowired
    PlayerFreeGameResetScheduleFactoryBean scheduleFactoryBean

    @Autowired
    PlayerFreeGameResetJobDetailFactoryBean jobDetailFactoryBean

    @PostConstruct
    void setup() {
        super.setJobDetails(jobDetailFactoryBean.object)
        super.setTriggers(scheduleFactoryBean.object)
    }
}
