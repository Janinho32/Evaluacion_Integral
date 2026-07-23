<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="es">
<head>
  <meta charset="UTF-8"><meta name="viewport" content="width=device-width, initial-scale=1">
  <title>Nueva Matrícula — UniSistema</title>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
  <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css" rel="stylesheet">
  <style>
    body{background:#f0f4f8;font-family:'Segoe UI',sans-serif}
    .form-card{border-radius:16px;border:none;box-shadow:0 4px 18px rgba(0,0,0,.07)}
    .form-control,.form-select{border-radius:10px}
    .form-label{font-weight:600;font-size:.88rem;color:#374151}
    .section-title{font-weight:700;color:#6a2c8e;border-left:4px solid #6a2c8e;padding-left:12px;margin-bottom:1.2rem}
  </style>
</head>
<body>
<%@ include file="/WEB-INF/views/partials/navbar.jspf" %>
<div class="container py-4" style="max-width:600px">

  <div class="d-flex align-items-center gap-2 mb-4">
    <a href="${pageContext.request.contextPath}/matriculas" class="btn btn-outline-secondary btn-sm" style="border-radius:10px"><i class="bi bi-arrow-left"></i></a>
    <h5 class="fw-bold mb-0"><i class="bi bi-clipboard-plus me-1"></i>Nueva Matrícula</h5>
  </div>

  <c:if test="${not empty errores}">
    <div class="alert alert-danger" style="border-radius:12px">
      <strong><i class="bi bi-exclamation-triangle-fill me-1"></i>Corrija los errores:</strong>
      <ul class="mb-0 mt-1"><c:forEach var="err" items="${errores}"><li>${err}</li></c:forEach></ul>
    </div>
  </c:if>

  <div class="card form-card">
    <div class="card-body p-4">
      <form method="post" action="${pageContext.request.contextPath}/matriculas">
        <div class="section-title">Seleccionar Estudiante y Curso</div>
        <div class="row g-3">
          <div class="col-12">
            <label class="form-label">Estudiante *</label>
            <select name="estudianteId" class="form-select" required>
              <option value="">-- Seleccionar estudiante --</option>
              <c:forEach var="est" items="${estudiantes}">
                <option value="${est.id}">${est.nombreCompleto} (${est.codigo}) — ${est.carrera}</option>
              </c:forEach>
            </select>
          </div>
          <div class="col-12">
            <label class="form-label">Curso *</label>
            <select name="cursoId" class="form-select" required>
              <option value="">-- Seleccionar curso --</option>
              <c:forEach var="cur" items="${cursos}">
                <option value="${cur.id}">${cur.codigo} — ${cur.nombre} (${cur.creditos} cr.)</option>
              </c:forEach>
            </select>
          </div>
        </div>

        <div class="alert alert-info mt-3 mb-0" style="border-radius:10px;font-size:.85rem">
          <i class="bi bi-info-circle me-1"></i>La nota se podrá registrar después de crear la matrícula.
        </div>

        <div class="d-flex gap-2 mt-4">
          <button type="submit" class="btn px-4 text-white fw-semibold" style="background:#6a2c8e;border-radius:10px">
            <i class="bi bi-save-fill me-1"></i>Registrar Matrícula
          </button>
          <a href="${pageContext.request.contextPath}/matriculas" class="btn btn-outline-secondary" style="border-radius:10px">Cancelar</a>
        </div>
      </form>
    </div>
  </div>
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
