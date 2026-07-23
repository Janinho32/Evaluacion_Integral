<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="es">
<head>
  <meta charset="UTF-8"><meta name="viewport" content="width=device-width, initial-scale=1">
  <title>Registrar Nota — UniSistema</title>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
  <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css" rel="stylesheet">
  <style>
    body{background:#f0f4f8;font-family:'Segoe UI',sans-serif}
    .form-card{border-radius:16px;border:none;box-shadow:0 4px 18px rgba(0,0,0,.07)}
    .nota-input{font-size:2.5rem;font-weight:700;text-align:center;border-radius:16px;width:160px}
    .form-label{font-weight:600;font-size:.88rem;color:#374151}
  </style>
</head>
<body>
<%@ include file="/WEB-INF/views/partials/navbar.jspf" %>
<div class="container py-4" style="max-width:500px">

  <div class="d-flex align-items-center gap-2 mb-4">
    <a href="${pageContext.request.contextPath}/matriculas" class="btn btn-outline-secondary btn-sm" style="border-radius:10px"><i class="bi bi-arrow-left"></i></a>
    <h5 class="fw-bold mb-0"><i class="bi bi-pencil-square me-1"></i>Registrar Nota</h5>
  </div>

  <c:if test="${not empty error}">
    <div class="alert alert-danger" style="border-radius:12px">${error}</div>
  </c:if>

  <div class="card form-card">
    <div class="card-body p-4">
      <!-- Info de la matrícula -->
      <div class="rounded-3 p-3 mb-4" style="background:#f9fafb;border:1px solid #e5e7eb">
        <div class="row g-2 text-sm">
          <div class="col-6">
            <div class="text-muted" style="font-size:.78rem">ESTUDIANTE</div>
            <div class="fw-semibold">${matricula.nombreEstudiante}</div>
            <small class="text-muted">${matricula.codigoEstudiante}</small>
          </div>
          <div class="col-6">
            <div class="text-muted" style="font-size:.78rem">CURSO</div>
            <div class="fw-semibold">${matricula.nombreCurso}</div>
            <span class="badge bg-warning text-dark">${matricula.codigoCurso}</span>
          </div>
          <div class="col-6 mt-2">
            <div class="text-muted" style="font-size:.78rem">NOTA ACTUAL</div>
            <div class="fw-bold fs-5 ${matricula.nota >= 11 ? 'text-success' : matricula.nota > 0 ? 'text-danger' : 'text-muted'}">
              ${matricula.notaFormateada}
            </div>
          </div>
          <div class="col-6 mt-2">
            <div class="text-muted" style="font-size:.78rem">ESTADO</div>
            <div class="fw-semibold">${matricula.estado}</div>
          </div>
        </div>
      </div>

      <form method="post" action="${pageContext.request.contextPath}/matriculas/nota/${matricula.id}">
        <div class="d-flex flex-column align-items-center mb-4">
          <label class="form-label mb-2">Nueva Nota (0 – 20)</label>
          <input type="number" name="nota" class="form-control nota-input"
                 min="0" max="20" step="0.5"
                 value="${matricula.nota > 0 ? matricula.nota : ''}"
                 placeholder="00" required>
          <small class="text-muted mt-2">Nota mínima aprobatoria: <strong>11.00</strong></small>
        </div>

        <div class="d-flex gap-2 justify-content-center">
          <button type="submit" class="btn px-4 text-white fw-semibold" style="background:#6a2c8e;border-radius:10px">
            <i class="bi bi-save-fill me-1"></i>Guardar Nota
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
