<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ include file="../shared/header.jspf" %>
<h1>Dossier patient</h1><%@ include file="../shared/patient.jspf" %>
<c:if test="${patient.waiting}"><form class="card card-body mb-4" method="post" action="${ctx}/doctor/consultations"><input type="hidden" name="csrf" value="${sessionScope.csrf}"><input type="hidden" name="action" value="create"><input type="hidden" name="patientId" value="${patient.id}"><h2 class="h5">Créer une consultation · 150 DH</h2><label class="form-label">Motif</label><textarea class="form-control mb-3" name="reason" maxlength="1000" required></textarea><label class="form-label">Observations / Examen clinique</label><textarea class="form-control mb-3" name="observations" maxlength="3000"></textarea><div><button class="btn btn-primary">Créer la consultation</button></div></form></c:if>
<h2 class="h5">Mes consultations précédentes</h2><c:forEach items="${patient.consultations}" var="c"><p><a href="${ctx}/doctor/consultations?action=details&id=${c.id}">Consultation #${c.id}</a> · <c:out value="${c.status}"/></p></c:forEach><a href="${ctx}/doctor/consultations">Retour</a>
<%@ include file="../shared/footer.jspf" %>
