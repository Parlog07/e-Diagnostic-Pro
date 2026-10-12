<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ include file="../shared/header.jspf" %>
<h1>Dossier patient</h1><p><span class="badge text-bg-primary"><c:out value="${patient.waiting ? 'En attente' : 'Pris en charge'}"/></span></p><%@ include file="../shared/patient.jspf" %>
<c:if test="${not patient.waiting}"><form class="card card-body mb-4" method="post" action="${ctx}/nurse/patients"><input type="hidden" name="csrf" value="${sessionScope.csrf}"><input type="hidden" name="action" value="receive"><input type="hidden" name="id" value="${patient.id}"><h2 class="h5">Nouvelle arrivée</h2><p>Possible après clôture de la consultation précédente.</p><%@ include file="../shared/vitals.jspf" %><div class="mt-4"><button class="btn btn-primary">Enregistrer et ajouter à la file d'attente</button></div></form></c:if><a href="${ctx}/nurse/patients">Retour aux patients</a>
<%@ include file="../shared/footer.jspf" %>
