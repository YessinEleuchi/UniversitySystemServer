package com.eduflow.notification.interfaces;

import com.eduflow.notification.AccountMailContext;
import com.eduflow.people.domain.PersonType;

public interface AccountEmailStrategy {
    boolean supports(AccountMailContext ctx);

    PersonType supports();
    String subject(AccountMailContext ctx);
    String html(AccountMailContext ctx);
}
