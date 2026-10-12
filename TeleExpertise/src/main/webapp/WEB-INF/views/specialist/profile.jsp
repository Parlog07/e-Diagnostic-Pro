<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ include file="../shared/header.jspf" %>
<h1>Mon profil spécialiste</h1><form class="card card-body narrow" method="post" action="${ctx}/specialist/requests"><input type="hidden" name="csrf" value="${sessionScope.csrf}"><input type="hidden" name="action" value="profile"><label class="form-label">Spécialité</label><input class="form-control mb-3" name="specialty" maxlength="100" required value="<c:out value='${profile.specialty}'/>"><label class="form-label">Tarif de l'expertise (DH)</label><input class="form-control mb-3" name="tariff" type="number" min="0" step="0.01" required value="${profile.tariff}"><p class="text-muted">Les demandes existantes conservent leur tarif initial. Les créneaux ne sont pas gérés dans cette version.</p><div><button class="btn btn-primary">Enregistrer le profil</button></div></form>
<%@ include file="../shared/footer.jspf" %>
