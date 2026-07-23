<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="es">
<head>
  <meta charset="UTF-8"><meta name="viewport" content="width=device-width, initial-scale=1">
  <title>Matrículas — UniSistema</title>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
  <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css" rel="stylesheet">
  <style>
    body{background:#f0f4f8;font-family:'Segoe UI',sans-serif}
    .page-header{background:#6a2c8e;color:#fff;border-radius:16px;padding:1.5rem 2rem}
    .table-card{border-radius:16px;border:none;box-shadow:0 4px 18px rgba(0,0,0,.07)}
    .table thead th{background:#6a2c8e;color:#fff;border:none;font-weight:600;font-size:.85rem}
    .table tbody tr:hover{background:#faf5ff}
    .nota-cell{font-size:1.05rem;font-weight:700}
  </style>
</head>
<body>
<%@ include file="/WEB-INF/views/partials/navbar.jspf" %>
<div class="container-lg py-4">

  <div class="page-header mb-4 d-flex justify-content-between align-items-center flex-wrap gap-2">
    <div>
      <h5 class="fw-bold mb-1"><i class="bi bi-clipboard-check-fill me-2"></i>Matrículas</h5>
      <small class="opacity-75">
        Total: <strong>${total}</strong> &nbsp;|&nbsp;
        Promedio: <strong>${promedio}</strong> &nbsp;|&nbsp;
        Aprobadas: <strong>${aprobadas}</strong>
      </small>
    </div>
    <a href="${pageContext.request.contextPath}/matriculas/nueva" class="btn btn-light fw-semibold">
      <i class="bi bi-clipboard-plus me-1"></i>Nueva Matrícula
    </a>
  </div>

  <c:if test="${param.msg == 'creado'}"><div class="alert alert-success alert-dismissible fade show"><i class="bi bi-check-circle-fill me-2"></i>Matrícula registrada.<button type="button" class="btn-close" data-bs-dismiss="alert"></button></div></c:if>
  <c:if test="${param.msg == 'nota_actualizada'}"><div class="alert alert-info alert-dismissible fade show"><i class="bi bi-pencil-fill me-2"></i>Nota actualizada correctamente.<button type="button" class="btn-close" data-bs-dismiss="alert"></button></div></c:if>
  <c:if test="${param.msg == 'eliminado'}"><div class="alert alert-warning alert-dismissible fade show"><i class="bi bi-trash-fill me-2"></i>Matrícula eliminada.<button type="button" class="btn-close" data-bs-dismiss="alert"></button></div></c:if>

  <div class="card table-card">
    <div class="table-responsive">
      <table class="table table-hover align-middle mb-0">
        <thead>
          <tr>
            <th>#</th>
            <th>Estudiante</th>
            <th>Curso</th>
            <th class="text-center">Nota</th>
            <th class="text-center">Estado</th>
            <th>Fecha</th>
            <th class="text-center">Acciones</th>
          </tr>
        </thead>
        <tbody>
          <c:choose>
            <c:when test="${empty matriculas}">
              <tr><td colspan="7" class="text-center py-5 text-muted"><i class="bi bi-inbox fs-2 d-block mb-2"></i>No hay matrículas registradas.</td></tr>
            </c:when>
            <c:otherwise>
              <c:forEach var="m" items="${matriculas}">
                <tr>
                  <td><small class="text-muted">#${m.id}</small></td>
                  <td>
                    <div class="fw-semibold">${m.nombreEstudiante}</div>
                    <small class="text-muted">${m.codigoEstudiante}</small>
                  </td>
                  <td>
                    <div>${m.nombreCurso}</div>
                    <span class="badge bg-warning text-dark" style="font-size:.7rem">${m.codigoCurso}</span>
                  </td>
                  <td class="text-center">
                    <span class="nota-cell ${m.nota >= 11 ? 'text-success' : m.nota > 0 ? 'text-danger' : 'text-muted'}">
                      ${m.notaFormateada}
                    </span>
                  </td>
                  <td class="text-center">
                    <c:choose>
                      <c:when test="${m.estado == 'Aprobada'}"><span class="badge bg-success">Aprobada</span></c:when>
                      <c:when test="${m.estado == 'Desaprobada'}"><span class="badge bg-danger">Desaprobada</span></c:when>
                      <c:when test="${m.estado == 'Retirada'}"><span class="badge bg-secondary">Retirada</span></c:when>
                      <c:otherwise><span class="badge bg-primary">Activa</span></c:otherwise>
                    </c:choose>
                  </td>
                  <td><small>${m.fechaFormateada}</small></td>
                  <td class="text-center">
                    <a href="${pageContext.request.contextPath}/matriculas/nota/${m.id}" class="btn btn-sm btn-outline-purple me-1"
                       style="border-color:#6a2c8e;color:#6a2c8e" title="Registrar nota">
                      <i class="bi bi-pencil-square"></i>
                    </a>
                    <a href="${pageContext.request.contextPath}/matriculas/eliminar/${m.id}" class="btn btn-sm btn-outline-danger"
                       onclick="return confirm('¿Eliminar esta matrícula?')" title="Eliminar">
                      <i class="bi bi-trash-fill"></i>
                    </a>
                  </td>
                </tr>
              </c:forEach>
            </c:otherwise>
          </c:choose>
        </tbody>
      </table>
    </div>
  </div>
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
