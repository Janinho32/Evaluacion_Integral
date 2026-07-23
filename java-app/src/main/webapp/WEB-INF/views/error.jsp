<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isErrorPage="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="es">
<head>
  <meta charset="UTF-8"><meta name="viewport" content="width=device-width, initial-scale=1">
  <title>Error — UniSistema</title>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
  <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css" rel="stylesheet">
  <style>body{background:#f0f4f8;font-family:'Segoe UI',sans-serif}</style>
</head>
<body>
<%@ include file="/WEB-INF/views/partials/navbar.jspf" %>
<div class="container py-5 text-center" style="max-width:500px">
  <i class="bi bi-exclamation-octagon-fill text-danger" style="font-size:4rem"></i>
  <h4 class="fw-bold mt-3">Ocurrió un error</h4>
  <p class="text-muted">
    <c:choose>
      <c:when test="${not empty error}">${error}</c:when>
      <c:otherwise>Se produjo un error inesperado en el sistema.</c:otherwise>
    </c:choose>
  </p>
  <a href="${pageContext.request.contextPath}/" class="btn btn-primary" style="border-radius:10px">
    <i class="bi bi-house-fill me-1"></i>Volver al Inicio
  </a>
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
