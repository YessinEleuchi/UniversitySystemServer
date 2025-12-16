package com.eduflow.notification;

import com.eduflow.notification.impl.ManagerEmailStrategy;
import com.eduflow.notification.interfaces.AccountEmailStrategy;
import com.eduflow.notification.interfaces.EmailSender;
import com.eduflow.people.domain.PersonType;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountEmailDispatcher {

    private final EmailSender emailSender;
    private final List<AccountEmailStrategy> strategies;
    private final ManagerEmailStrategy managerEmailStrategy;

    public AccountEmailDispatcher(EmailSender emailSender,
                                  List<AccountEmailStrategy> strategies,
                                  ManagerEmailStrategy managerEmailStrategy) {
        this.emailSender = emailSender;
        this.strategies = strategies;
        this.managerEmailStrategy = managerEmailStrategy;
    }

    public void sendAccountCreated(AccountMailContext ctx) {
        if (ctx.toEmail() == null || !ctx.toEmail().contains("@")) return;
        if (ctx.generatedPassword() == null) return;

        // ✅ Manager case (personType null)
        if (ctx.personType() == null) {
            emailSender.sendHtml(ctx.toEmail(),
                    managerEmailStrategy.subject(ctx),
                    managerEmailStrategy.html(ctx));
            return;
        }

        // ✅ Student / Teacher case
        AccountEmailStrategy strategy = strategies.stream()
                .filter(s -> s.supports() == ctx.personType())
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No email strategy for personType=" + ctx.personType()));

        emailSender.sendHtml(ctx.toEmail(), strategy.subject(ctx), strategy.html(ctx));
    }
}
