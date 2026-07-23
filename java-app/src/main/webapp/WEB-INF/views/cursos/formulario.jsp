<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="es">
<head>
  <meta charset="UTF-8"><meta name="viewport" content="width=device-width, initial-scale=1">
  <title><c:choose><c:when test="${editar}">Editar</c:when><c:otherwise>Nuevo</c:otherwise></c:choose> Curso — UniSistema</title>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
  <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css" rel="stylesheet">
  <style>
    body{background:#f0f4f8;font-family:'Segoe UI',sans-serif}
    .form-card{border-radius:16px;border:none;box-shadow:0 4px 18px rgba(0,0,0,.07)}
    .form-control,.form-select{border-radius:10px}
    .form-label{font-weight:600;font-size:.88rem;color:#374151}
    .section-title{font-weight:700;color:#b45309;border-left:4px solid #b45309;padding-left:12px;margin-bottom:1.2rem}
  </style>
</head>
<body>
<%@ include file="/WEB-INF/views/partials/navbar.jspf" %>
<div class="container py-4" style="max-width:640px">

  <div class="d-flex align-items-center gap-2 mb-4">
    <a href="${pageContext.request.contextPath}/cursos" class="btn btn-outline-secondary btn-sm" style="border-radius:10px"><i class="bi bi-arrow-left"></i></a>
    <h5 class="fw-bold mb-0">
      <c:choose><c:when test="${editar}"><i class="bi bi-pencil-fill me-1"></i>Editar Curso</c:when>
      <c:otherwise><i class="bi bi-journal-plus me-1"></i>Crear Nuevo Curso</c:otherwise></c:choose>
    </h5>
  </div>

  <c:if test="${not empty errores}">
    <div class="alert alert-danger" style="border-radius:12px">
      <strong><i class="bi bi-exclamation-triangle-fill me-1"></i>Corrija los errores:</strong>
      <ul class="mb-0 mt-1"><c:forEach var="err" items="${errores}"><li>${err}</li></c:forEach></ul>
    </div>
  </c:if>

  <div class="card form-card">
    <div class="card-body p-4">
      <c:choose>
        <c:when test="${editar}"><form method="post" action="${pageContext.request.contextPath}/cursos/editar/${curso.id}"></c:when>
        <c:otherwise><form method="post" action="${pageContext.request.contextPath}/cursos"></c:otherwise>
      </c:choose>

        <div class="section-title">Información del Curso</div>
        <div class="row g-3 mb-4">
          <div class="col-md-4">
            <label class="form-label">Código *</label>
            <input type="text" name="codigo" class="form-control text-uppercase" required
                   value="${not empty curso ? curso.codigo : ''}" placeholder="POO101" maxlength="10">
          </div>
          <div class="col-md-8">
            <label class="form-label">Nombre del Curso *</label>
            <input type="text" name="nombre" class="form-control" required
                   value="${not empty curso ? curso.nombre : ''}" placeholder="Ej: Programación Orientada a Objetos">
          </div>
          <div class="col-md-4">
            <label class="form-label">Créditos *</label>
            <select name="creditos" class="form-select">
              <c:forEach var="cr" begin="1" end="8">
                <option value="${cr}" ${not empty curso && curso.creditos == cr ? 'selected' : cr == 3 ? 'selected' : ''}>${cr} crédito${cr > 1 ? 's' : ''}</option>
              </c:forEach>
            </select>
          </div>
          <div class="col-md-4">
            <label class="form-label">Cupo Máximo *</label>
            <input type="number" name="cupoMaximo" class="form-control" min="1" max="100"
                   value="${not empty curso ? curso.cupoMaximo : 30}">
          </div>
          <div class="col-md-4">
            <label class="form-label">Docente</label>
            <select name="docenteId" class="form-select">
              <option value="0">-- Sin asignar --</option>
              <c:forEach var="doc" items="${docentes}">
                <option value="${doc.id}" ${not empty curso && curso.docenteId == doc.id ? 'selected' : ''}>${doc.nombreCompleto}</option>
              </c:forEach>
            </select>
          </div>
        </div>

        <div class="d-flex gap-2">
          <button type="submit" class="btn px-4 text-white fw-semibold" style="background:#b45309;border-radius:10px">
            <i class="bi bi-save-fill me-1"></i>
            <c:choose><c:when test="${editar}">Actualizar</c:when><c:otherwise>Crear Curso</c:otherwise></c:choose>
          </button>
          <a href="${pageContext.request.contextPath}/cursos" class="btn btn-outline-secondary" style="border-radius:10px">Cancelar</a>
        </div>
      </form>
    </div>
  </div>
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
<script>document.querySelector('[name="codigo"]').addEventListener('input',function(){this.value=this.value.toUpperCase()});</script>
</body>
</html>
