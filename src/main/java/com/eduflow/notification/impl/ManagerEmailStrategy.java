package com.eduflow.notification.impl;

import com.eduflow.notification.AccountMailContext;
import com.eduflow.notification.interfaces.AccountEmailStrategy;
import com.eduflow.people.domain.PersonType;
import org.springframework.stereotype.Component;

@Component
public class ManagerEmailStrategy implements AccountEmailStrategy {

    @Override
    public boolean supports(AccountMailContext ctx) {
        return ctx.personType() == null; // manager case in your model
    }

    @Override
    public PersonType supports() {
        return null;
    }


    @Override
    public String subject(AccountMailContext ctx) {
        return "UniFlow - Votre compte manager";
    }

    @Override
    public String html(AccountMailContext ctx) {
        return """
        <div style="font-family:Arial;line-height:1.5">
          <h2>Compte Manager UniFlow</h2>
          <p>Bonjour <b>%s</b>,</p>
          <ul>
            <li><b>Username :</b> %s</li>
            <li><b>Mot de passe :</b> %s</li>
          </ul>
          <p>Connexion : <a href="%s">%s</a></p>
        </div>
        """.formatted(esc(ctx.displayName()), esc(ctx.username()), esc(ctx.generatedPassword()),
                ctx.loginUrl(), ctx.loginUrl());
    }

    private String esc(String s) {
        if (s == null) return "";
        return s.replace("&","&amp;").replace("<","&lt;").replace(">","&gt;");
    }
}
