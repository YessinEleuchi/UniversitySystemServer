package com.eduflow.notification.impl;

import com.eduflow.notification.AccountMailContext;
import com.eduflow.notification.interfaces.AccountEmailStrategy;
import com.eduflow.people.domain.PersonType;
import org.springframework.stereotype.Component;

@Component
public class StudentAccountEmail implements AccountEmailStrategy {
    public boolean supports(AccountMailContext ctx) {
        return ctx.personType() == com.eduflow.people.domain.PersonType.STUDENT;
    }


    @Override
    public PersonType supports() {
        return PersonType.STUDENT;
    }

    @Override
    public String subject(AccountMailContext ctx) {
        return "UniFlow - Votre compte étudiant";
    }

    @Override
    public String html(AccountMailContext ctx) {
        return """
            <!DOCTYPE html>
            <html lang="fr">
            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <title>Votre compte étudiant UniFlow est prêt</title>
                <style type="text/css">
                    body { margin: 0; padding: 0; background-color: #f3f4f6; }
                    table { border-collapse: collapse; }
                    @media only screen and (max-width: 600px) {
                        .container { width: 100%% !important; }
                        .card { padding: 30px 20px !important; }
                        .title { font-size: 28px !important; }
                        .btn { padding: 16px 30px !important; font-size: 18px !important; }
                    }
                </style>
                <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700&display=swap" rel="stylesheet">
            </head>
            <body style="margin:0; padding:0; background-color:#f3f4f6; font-family:'Inter', Arial, sans-serif;">
                <table width="100%%" cellpadding="0" cellspacing="0" style="background-color:#f3f4f6; padding:40px 0;">
                    <tr>
                        <td align="center">
                            <table class="container" width="600" cellpadding="0" cellspacing="0" style="max-width:600px; width:100%%;">
                                <tr>
                                    <td align="center">
                                        <table class="card" width="100%%" cellpadding="0" cellspacing="0" style="background-color:#ffffff; border-radius:16px; box-shadow:0 10px 30px rgba(0,0,0,0.08); overflow:hidden;">
                                            <!-- Header -->
                                            <tr>
                                                <td style="background: linear-gradient(135deg, #3b82f6, #2563eb); padding:50px 40px; text-align:center; color:#ffffff;">
                                                    <h1 style="margin:0; font-size:32px; font-weight:700;">Bienvenue sur UniFlow</h1>
                                                    <p style="margin:10px 0 0; font-size:18px; opacity:0.95;">Votre compte étudiant est prêt !</p>
                                                </td>
                                            </tr>
                                            <!-- Corps -->
                                            <tr>
                                                <td style="padding:40px; text-align:center;">
                                                    <p style="font-size:18px; color:#374151; margin-bottom:30px;">
                                                        Bonjour <strong>%s</strong>,
                                                    </p>
                                                    <p style="font-size:16px; color:#4b5563; line-height:1.6; margin-bottom:40px;">
                                                        Votre compte étudiant a été créé avec succès. Voici vos identifiants pour vous connecter :
                                                    </p>

                                                    <!-- Username -->
                                                    <table width="100%%" cellpadding="15" cellspacing="0" style="background:#f8fafc; border-radius:12px; margin-bottom:20px;">
                                                        <tr>
                                                            <td width="40" valign="top" style="color:#2563eb; font-size:20px;">👤</td>
                                                            <td style="text-align:left;">
                                                                <p style="margin:0; color:#6b7280; font-size:14px;">Username</p>
                                                                <p style="margin:4px 0 0; font-size:18px; font-weight:600; color:#111827;">%s</p>
                                                            </td>
                                                        </tr>
                                                    </table>

                                                    <!-- Mot de passe -->
                                                    <table width="100%%" cellpadding="15" cellspacing="0" style="background:#f8fafc; border-radius:12px; margin-bottom:40px;">
                                                        <tr>
                                                            <td width="40" valign="top" style="color:#2563eb; font-size:20px;">🔒</td>
                                                            <td style="text-align:left;">
                                                                <p style="margin:0; color:#6b7280; font-size:14px;">Mot de passe</p>
                                                                <p style="margin:4px 0 0; font-size:18px; font-weight:600; color:#111827;">%s</p>
                                                            </td>
                                                        </tr>
                                                    </table>

                                                    <p style="margin:0 0 30px; font-size:16px; color:#4b5563;">
                                                        Connectez-vous dès maintenant pour découvrir la plateforme :
                                                    </p>

                                                    <a href="%s" target="_blank" style="display:inline-block; background:#2563eb; color:#ffffff; font-size:18px; font-weight:600; text-decoration:none; padding:14px 40px; border-radius:50px; box-shadow:0 4px 15px rgba(37,99,235,0.3);">
                                                        Se connecter à UniFlow
                                                    </a>

                                                    <!-- Message important -->
                                                    <p style="margin:40px 0 0; padding:16px; background:#fffbeb; border-left:4px solid #f59e0b; border-radius:8px; font-size:15px; color:#92400e; text-align:left; max-width:480px; margin-left:auto; margin-right:auto;">
                                                        🔐 <strong>Conseil de sécurité :</strong> Changez votre mot de passe dès votre première connexion pour plus de sécurité.
                                                    </p>
                                                </td>
                                            </tr>

                                            <!-- Footer -->
                                            <tr>
                                                <td style="background:#f9fafb; padding:30px; text-align:center; color:#9ca3af; font-size:14px;">
                                                    <p style="margin:0;">© 2025 UniFlow. Tous droits réservés.</p>
                                                    <p style="margin:10px 0 0;">Besoin d'aide ? Contactez-nous à support@uniflow.com</p>
                                                </td>
                                            </tr>
                                        </table>
                                    </td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                </table>
            </body>
            </html>
            """
                .formatted(
                        esc(ctx.displayName()),
                        esc(ctx.username()),
                        esc(ctx.generatedPassword()),
                        ctx.loginUrl()
                );
    }

    private String esc(String s) {
        if (s == null) return "";
        return s.replace("&","&amp;").replace("<","&lt;").replace(">","&gt;");
    }
}
