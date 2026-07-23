<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="es">
<head>
  <meta charset="UTF-8"><meta name="viewport" content="width=device-width, initial-scale=1">
  <title><c:choose><c:when test="${editar}">Editar</c:when><c:otherwise>Nuevo</c:otherwise></c:choose> Estudiante — UniSistema</title>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
  <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css" rel="stylesheet">
  <style>
    body{background:#f0f4f8;font-family:'Segoe UI',sans-serif}
    .form-card{border-radius:16px;border:none;box-shadow:0 4px 18px rgba(0,0,0,.07)}
    .form-control,.form-select{border-radius:10px}
    .form-label{font-weight:600;font-size:.88rem;color:#374151}
    .section-title{font-weight:700;color:#1a3a6c;border-left:4px solid #1a3a6c;padding-left:12px;margin-bottom:1.2rem}
  </style>
</head>
<body>
<%@ include file="/WEB-INF/views/partials/navbar.jspf" %>
<div class="container py-4" style="max-width:680px">

  <div class="d-flex align-items-center gap-2 mb-4">
    <a href="${pageContext.request.contextPath}/estudiantes" class="btn btn-outline-secondary btn-sm" style="border-radius:10px">
      <i class="bi bi-arrow-left"></i>
    </a>
    <h5 class="fw-bold mb-0">
      <c:choose>
        <c:when test="${editar}"><i class="bi bi-pencil-fill me-1"></i>Editar Estudiante</c:when>
        <c:otherwise><i class="bi bi-person-plus-fill me-1"></i>Registrar Nuevo Estudiante</c:otherwise>
      </c:choose>
    </h5>
  </div>

  <!-- Errores -->
  <c:if test="${not empty errores}">
    <div class="alert alert-danger" style="border-radius:12px">
      <strong><i class="bi bi-exclamation-triangle-fill me-1"></i>Corrija los siguientes errores:</strong>
      <ul class="mb-0 mt-1">
        <c:forEach var="err" items="${errores}"><li>${err}</li></c:forEach>
      </ul>
    </div>
  </c:if>

  <div class="card form-card">
    <div class="card-body p-4">
      <c:choose>
        <c:when test="${editar}">
          <form method="post" action="${pageContext.request.contextPath}/estudiantes/editar/${estudiante.id}">
        </c:when>
        <c:otherwise>
          <form method="post" action="${pageContext.request.contextPath}/estudiantes">
        </c:otherwise>
      </c:choose>

        <div class="section-title">Datos Personales</div>
        <div class="row g-3 mb-4">
          <div class="col-md-6">
            <label class="form-label">Nombre *</label>
            <input type="text" name="nombre" class="form-control" required
                   value="${not empty estudiante ? estudiante.nombre : ''}"
                   placeholder="Ej: María">
          </div>
          <div class="col-md-6">
            <label class="form-label">Apellido *</label>
            <input type="text" name="apellido" class="form-control" required
                   value="${not empty estudiante ? estudiante.apellido : ''}"
                   placeholder="Ej: García">
          </div>
          <div class="col-md-8">
            <label class="form-label">Email *</label>
            <input type="email" name="email" class="form-control" required
                   value="${not empty estudiante ? estudiante.email : ''}"
                   placeholder="correo@estudiante.edu">
          </div>
          <div class="col-md-4">
            <label class="form-label">Teléfono</label>
            <input type="tel" name="telefono" class="form-control"
                   value="${not empty estudiante ? estudiante.telefono : ''}"
                   placeholder="9XXXXXXXX">
          </div>
        </div>

        <div class="section-title">Datos Académicos</div>
        <div class="row g-3">
          <div class="col-md-4">
            <label class="form-label">Código *</label>
            <input type="text" name="codigo" class="form-control text-uppercase" required
                   value="${not empty estudiante ? estudiante.codigo : ''}"
                   placeholder="EST001" maxlength="10">
            <small class="text-muted">Formato: LETRAS + números</small>
          </div>
          <div class="col-md-8">
            <label class="form-label">Carrera *</label>
            <select name="carrera" class="form-select" required>
              <option value="">-- Seleccionar --</option>
              <option value="Ingeniería de Sistemas"      ${not empty estudiante && estudiante.carrera == 'Ingeniería de Sistemas'      ? 'selected':''}>Ingeniería de Sistemas</option>
              <option value="Ingeniería Industrial"       ${not empty estudiante && estudiante.carrera == 'Ingeniería Industrial'       ? 'selected':''}>Ingeniería Industrial</option>
              <option value="Ciencias de la Computación"  ${not empty estudiante && estudiante.carrera == 'Ciencias de la Computación'  ? 'selected':''}>Ciencias de la Computación</option>
              <option value="Matemáticas"                 ${not empty estudiante && estudiante.carrera == 'Matemáticas'                 ? 'selected':''}>Matemáticas</option>
              <option value="Ingeniería Electrónica"      ${not empty estudiante && estudiante.carrera == 'Ingeniería Electrónica'      ? 'selected':''}>Ingeniería Electrónica</option>
              <option value="Administración de Empresas"  ${not empty estudiante && estudiante.carrera == 'Administración de Empresas'  ? 'selected':''}>Administración de Empresas</option>
              <option value="Contabilidad"                ${not empty estudiante && estudiante.carrera == 'Contabilidad'                ? 'selected':''}>Contabilidad</option>
            </select>
          </div>
          <div class="col-md-4">
            <label class="form-label">Semestre *</label>
            <select name="semestre" class="form-select">
              <c:forEach var="s" begin="1" end="12">
                <option value="${s}" ${not empty estudiante && estudiante.semestre == s ? 'selected' : ''}>${s}° Semestre</option>
              </c:forEach>
            </select>
          </div>
        </div>

        <div class="d-flex gap-2 mt-4">
          <button type="submit" class="btn btn-primary px-4" style="border-radius:10px">
            <i class="bi bi-save-fill me-1"></i>
            <c:choose><c:when test="${editar}">Actualizar</c:when><c:otherwise>Registrar</c:otherwise></c:choose>
          </button>
          <a href="${pageContext.request.contextPath}/estudiantes" class="btn btn-outline-secondary" style="border-radius:10px">Cancelar</a>
        </div>
      </form>
    </div>
  </div>
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
<script>
  document.querySelector('[name="codigo"]').addEventListener('input', function(){
    this.value = this.value.toUpperCase();
  });
</script>
</body>
</html>
