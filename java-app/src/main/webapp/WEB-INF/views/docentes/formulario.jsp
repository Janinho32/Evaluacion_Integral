<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="es">
<head>
  <meta charset="UTF-8"><meta name="viewport" content="width=device-width, initial-scale=1">
  <title><c:choose><c:when test="${editar}">Editar</c:when><c:otherwise>Nuevo</c:otherwise></c:choose> Docente — UniSistema</title>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
  <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css" rel="stylesheet">
  <style>
    body{background:#f0f4f8;font-family:'Segoe UI',sans-serif}
    .form-card{border-radius:16px;border:none;box-shadow:0 4px 18px rgba(0,0,0,.07)}
    .form-control,.form-select{border-radius:10px}
    .form-label{font-weight:600;font-size:.88rem;color:#374151}
    .section-title{font-weight:700;color:#1b7a4a;border-left:4px solid #1b7a4a;padding-left:12px;margin-bottom:1.2rem}
  </style>
</head>
<body>
<%@ include file="/WEB-INF/views/partials/navbar.jspf" %>
<div class="container py-4" style="max-width:680px">

  <div class="d-flex align-items-center gap-2 mb-4">
    <a href="${pageContext.request.contextPath}/docentes" class="btn btn-outline-secondary btn-sm" style="border-radius:10px"><i class="bi bi-arrow-left"></i></a>
    <h5 class="fw-bold mb-0">
      <c:choose><c:when test="${editar}"><i class="bi bi-pencil-fill me-1"></i>Editar Docente</c:when>
      <c:otherwise><i class="bi bi-person-plus-fill me-1"></i>Registrar Nuevo Docente</c:otherwise></c:choose>
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
        <c:when test="${editar}"><form method="post" action="${pageContext.request.contextPath}/docentes/editar/${docente.id}"></c:when>
        <c:otherwise><form method="post" action="${pageContext.request.contextPath}/docentes"></c:otherwise>
      </c:choose>

        <div class="section-title">Datos Personales</div>
        <div class="row g-3 mb-4">
          <div class="col-md-6">
            <label class="form-label">Nombre *</label>
            <input type="text" name="nombre" class="form-control" required value="${not empty docente ? docente.nombre : ''}" placeholder="Ej: Ana">
          </div>
          <div class="col-md-6">
            <label class="form-label">Apellido *</label>
            <input type="text" name="apellido" class="form-control" required value="${not empty docente ? docente.apellido : ''}" placeholder="Ej: Torres">
          </div>
          <div class="col-md-8">
            <label class="form-label">Email *</label>
            <input type="email" name="email" class="form-control" required value="${not empty docente ? docente.email : ''}" placeholder="correo@universidad.edu">
          </div>
          <div class="col-md-4">
            <label class="form-label">Teléfono</label>
            <input type="tel" name="telefono" class="form-control" value="${not empty docente ? docente.telefono : ''}" placeholder="9XXXXXXXX">
          </div>
        </div>

        <div class="section-title">Datos Académicos</div>
        <div class="row g-3">
          <div class="col-md-4">
            <label class="form-label">Código *</label>
            <input type="text" name="codigo" class="form-control text-uppercase" required value="${not empty docente ? docente.codigo : ''}" placeholder="DOC001" maxlength="10">
          </div>
          <div class="col-md-8">
            <label class="form-label">Especialidad *</label>
            <input type="text" name="especialidad" class="form-control" required value="${not empty docente ? docente.especialidad : ''}" placeholder="Ej: Ingeniería de Software">
          </div>
          <div class="col-md-6">
            <label class="form-label">Categoría</label>
            <select name="categoria" class="form-select">
              <option value="Ordinario"  ${not empty docente && docente.categoria == 'Ordinario'  ? 'selected' : ''}>Ordinario</option>
              <option value="Contratado" ${not empty docente && docente.categoria == 'Contratado' ? 'selected' : ''}>Contratado</option>
              <option value="Invitado"   ${not empty docente && docente.categoria == 'Invitado'   ? 'selected' : ''}>Invitado</option>
            </select>
          </div>
          <div class="col-md-6">
            <label class="form-label">Horas de Cátedra</label>
            <input type="number" name="horasCatedra" class="form-control" min="0" max="40" value="${not empty docente ? docente.horasCatedra : 12}">
          </div>
        </div>

        <div class="d-flex gap-2 mt-4">
          <button type="submit" class="btn btn-success px-4" style="border-radius:10px">
            <i class="bi bi-save-fill me-1"></i>
            <c:choose><c:when test="${editar}">Actualizar</c:when><c:otherwise>Registrar</c:otherwise></c:choose>
          </button>
          <a href="${pageContext.request.contextPath}/docentes" class="btn btn-outline-secondary" style="border-radius:10px">Cancelar</a>
        </div>
      </form>
    </div>
  </div>
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
<script>document.querySelector('[name="codigo"]').addEventListener('input',function(){this.value=this.value.toUpperCase()});</script>
</body>
</html>
