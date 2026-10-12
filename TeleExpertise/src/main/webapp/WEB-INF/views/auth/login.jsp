<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ include file="../shared/header.jspf" %>
<div class="login-box"><p class="eyebrow">ACCÈS AU PERSONNEL</p><h1>Connexion</h1><p class="text-muted">Accédez à votre espace de prise en charge.</p><form method="post" action="${ctx}/login"><input type="hidden" name="csrf" value="${sessionScope.csrf}">
<label class="form-label">Adresse e-mail</label><input class="form-control mb-3" type="email" name="email" maxlength="200" autocomplete="username" required>
<label class="form-label">Mot de passe</label><input class="form-control mb-4" type="password" name="password" maxlength="72" autocomplete="current-password" required><button class="btn btn-primary w-100">Se connecter</button></form></div>
<%@ include file="../shared/footer.jspf" %>
