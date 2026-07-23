<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="es">
<head>
  <meta charset="UTF-8"><meta name="viewport" content="width=device-width, initial-scale=1">
  <title>Docentes — UniSistema</title>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
  <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css" rel="stylesheet">
  <style>
    body{background:#f0f4f8;font-family:'Segoe UI',sans-serif}
    .page-header{background:#1b7a4a;color:#fff;border-radius:16px;padding:1.5rem 2rem}
    .table-card{border-radius:16px;border:none;box-shadow:0 4px 18px rgba(0,0,0,.07)}
    .table thead th{background:#1b7a4a;color:#fff;border:none;font-weight:600;font-size:.85rem}
    .table tbody tr:hover{background:#f0fff7}
    .avatar-circle{width:36px;height:36px;border-radius:50%;display:flex;align-items:center;justify-content:center;font-weight:700;font-size:.85rem;color:#fff;background:#1b7a4a;flex-shrink:0}
  </style>
</head>
<body>
<%@ include file="/WEB-INF/views/partials/navbar.jspf" %>
<div class="container-lg py-4">

  <div class="page-header mb-4 d-flex justify-content-between align-items-center flex-wrap gap-2">
    <div>
      <h5 class="fw-bold mb-1"><i class="bi bi-person-workspace me-2"></i>Docentes</h5>
      <small class="opacity-75">Total registrados: <strong>${total}</strong></small>
    </div>
    <a href="${pageContext.request.contextPath}/docentes/nuevo" class="btn btn-light fw-semibold">
      <i class="bi bi-person-plus-fill me-1"></i>Nuevo Docente
    </a>
  </div>

  <c:if test="${param.msg == 'creado'}">
    <div class="alert alert-success alert-dismissible fade show"><i class="bi bi-check-circle-fill me-2"></i>Docente registrado correctamente.<button type="button" class="btn-close" data-bs-dismiss="alert"></button></div>
  </c:if>
  <c:if test="${param.msg == 'actualizado'}">
    <div class="alert alert-info alert-dismissible fade show"><i class="bi bi-pencil-fill me-2"></i>Docente actualizado.<button type="button" class="btn-close" data-bs-dismiss="alert"></button></div>
  </c:if>
  <c:if test="${param.msg == 'eliminado'}">
    <div class="alert alert-warning alert-dismissible fade show"><i class="bi bi-trash-fill me-2"></i>Docente eliminado.<button type="button" class="btn-close" data-bs-dismiss="alert"></button></div>
  </c:if>

  <div class="card table-card">
    <div class="table-responsive">
      <table class="table table-hover align-middle mb-0">
        <thead>
          <tr>
            <th>Docente</th>
            <th>Código</th>
            <th>Especialidad</th>
            <th>Categoría</th>
            <th class="text-center">Horas</th>
            <th>Email</th>
            <th class="text-center">Acciones</th>
          </tr>
        </thead>
        <tbody>
          <c:choose>
            <c:when test="${empty docentes}">
              <tr><td colspan="7" class="text-center py-5 text-muted"><i class="bi bi-inbox fs-2 d-block mb-2"></i>No hay docentes registrados.</td></tr>
            </c:when>
            <c:otherwise>
              <c:forEach var="doc" items="${docentes}">
                <tr>
                  <td>
                    <div class="d-flex align-items-center gap-2">
                      <div class="avatar-circle">${doc.iniciales}</div>
                      <div>
                        <div class="fw-semibold">${doc.nombreCompleto}</div>
                        <small class="text-muted">${doc.telefono}</small>
                      </div>
                    </div>
                  </td>
                  <td><span class="badge bg-success bg-opacity-10 text-success fw-semibold">${doc.codigo}</span></td>
                  <td>${doc.especialidad}</td>
                  <td>
                    <c:choose>
                      <c:when test="${doc.categoria == 'Ordinario'}"><span class="badge bg-success">Ordinario</span></c:when>
                      <c:when test="${doc.categoria == 'Invitado'}"><span class="badge bg-info text-dark">Invitado</span></c:when>
                      <c:otherwise><span class="badge bg-secondary">${doc.categoria}</span></c:otherwise>
                    </c:choose>
                  </td>
                  <td class="text-center">${doc.horasCatedra}h</td>
                  <td><small>${doc.email}</small></td>
                  <td class="text-center">
                    <a href="${pageContext.request.contextPath}/docentes/editar/${doc.id}" class="btn btn-sm btn-outline-success me-1" title="Editar"><i class="bi bi-pencil-fill"></i></a>
                    <a href="${pageContext.request.contextPath}/docentes/eliminar/${doc.id}" class="btn btn-sm btn-outline-danger"
                       onclick="return confirm('¿Eliminar al docente ${doc.nombreCompleto}?')" title="Eliminar"><i class="bi bi-trash-fill"></i></a>
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
