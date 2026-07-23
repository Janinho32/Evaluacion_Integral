<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="es">
<head>
  <meta charset="UTF-8"><meta name="viewport" content="width=device-width, initial-scale=1">
  <title>Cursos — UniSistema</title>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
  <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css" rel="stylesheet">
  <style>
    body{background:#f0f4f8;font-family:'Segoe UI',sans-serif}
    .page-header{background:#b45309;color:#fff;border-radius:16px;padding:1.5rem 2rem}
    .table-card{border-radius:16px;border:none;box-shadow:0 4px 18px rgba(0,0,0,.07)}
    .table thead th{background:#b45309;color:#fff;border:none;font-weight:600;font-size:.85rem}
    .table tbody tr:hover{background:#fffbeb}
    .cupo-bar{height:6px;border-radius:99px;background:#e5e7eb}
    .cupo-fill{height:6px;border-radius:99px;transition:width .3s}
  </style>
</head>
<body>
<%@ include file="/WEB-INF/views/partials/navbar.jspf" %>
<div class="container-lg py-4">

  <div class="page-header mb-4 d-flex justify-content-between align-items-center flex-wrap gap-2">
    <div>
      <h5 class="fw-bold mb-1"><i class="bi bi-journal-bookmark-fill me-2"></i>Cursos</h5>
      <small class="opacity-75">Total registrados: <strong>${total}</strong></small>
    </div>
    <a href="${pageContext.request.contextPath}/cursos/nuevo" class="btn btn-light fw-semibold">
      <i class="bi bi-journal-plus me-1"></i>Nuevo Curso
    </a>
  </div>

  <c:if test="${param.msg == 'creado'}"><div class="alert alert-success alert-dismissible fade show"><i class="bi bi-check-circle-fill me-2"></i>Curso creado.<button type="button" class="btn-close" data-bs-dismiss="alert"></button></div></c:if>
  <c:if test="${param.msg == 'actualizado'}"><div class="alert alert-info alert-dismissible fade show"><i class="bi bi-pencil-fill me-2"></i>Curso actualizado.<button type="button" class="btn-close" data-bs-dismiss="alert"></button></div></c:if>
  <c:if test="${param.msg == 'eliminado'}"><div class="alert alert-warning alert-dismissible fade show"><i class="bi bi-trash-fill me-2"></i>Curso eliminado.<button type="button" class="btn-close" data-bs-dismiss="alert"></button></div></c:if>

  <div class="card table-card">
    <div class="table-responsive">
      <table class="table table-hover align-middle mb-0">
        <thead>
          <tr>
            <th>Código</th>
            <th>Nombre del Curso</th>
            <th class="text-center">Créditos</th>
            <th>Docente</th>
            <th>Ocupación</th>
            <th class="text-center">Acciones</th>
          </tr>
        </thead>
        <tbody>
          <c:choose>
            <c:when test="${empty cursos}">
              <tr><td colspan="6" class="text-center py-5 text-muted"><i class="bi bi-inbox fs-2 d-block mb-2"></i>No hay cursos registrados.</td></tr>
            </c:when>
            <c:otherwise>
              <c:forEach var="cur" items="${cursos}">
                <c:set var="matKey" value="mat_${cur.id}"/>
                <c:set var="matriculados" value="${requestScope[matKey]}"/>
                <c:set var="pct" value="${cur.cupoMaximo > 0 ? (matriculados * 100 / cur.cupoMaximo) : 0}"/>
                <tr>
                  <td><span class="badge bg-warning text-dark fw-bold">${cur.codigo}</span></td>
                  <td>
                    <div class="fw-semibold">${cur.nombre}</div>
                  </td>
                  <td class="text-center"><span class="badge bg-secondary">${cur.creditos} cr.</span></td>
                  <td>
                    <c:choose>
                      <c:when test="${not empty cur.nombreDocente}">${cur.nombreDocente}</c:when>
                      <c:otherwise><span class="text-muted fst-italic">Sin asignar</span></c:otherwise>
                    </c:choose>
                  </td>
                  <td style="min-width:130px">
                    <div class="d-flex justify-content-between mb-1" style="font-size:.78rem">
                      <span>${matriculados} / ${cur.cupoMaximo}</span>
                      <span>${pct}%</span>
                    </div>
                    <div class="cupo-bar">
                      <div class="cupo-fill ${pct >= 90 ? 'bg-danger' : pct >= 70 ? 'bg-warning' : 'bg-success'}" style="width:${pct}%"></div>
                    </div>
                  </td>
                  <td class="text-center">
                    <a href="${pageContext.request.contextPath}/cursos/editar/${cur.id}" class="btn btn-sm btn-outline-warning me-1" title="Editar"><i class="bi bi-pencil-fill"></i></a>
                    <a href="${pageContext.request.contextPath}/cursos/eliminar/${cur.id}" class="btn btn-sm btn-outline-danger"
                       onclick="return confirm('¿Eliminar el curso ${cur.nombre}?')" title="Eliminar"><i class="bi bi-trash-fill"></i></a>
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
