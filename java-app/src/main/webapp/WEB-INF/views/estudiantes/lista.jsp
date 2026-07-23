<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="es">
<head>
  <meta charset="UTF-8"><meta name="viewport" content="width=device-width, initial-scale=1">
  <title>Estudiantes — UniSistema</title>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
  <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css" rel="stylesheet">
  <style>
    body{background:#f0f4f8;font-family:'Segoe UI',sans-serif}
    .navbar{background:#1a3a6c !important}
    .page-header{background:#1a3a6c;color:#fff;border-radius:16px;padding:1.5rem 2rem}
    .table-card{border-radius:16px;border:none;box-shadow:0 4px 18px rgba(0,0,0,.07)}
    .table thead th{background:#1a3a6c;color:#fff;border:none;font-weight:600;font-size:.85rem}
    .table tbody tr:hover{background:#f0f7ff}
    .badge-cat{font-size:.72rem;border-radius:8px;padding:.3em .7em}
    .avatar-circle{width:36px;height:36px;border-radius:50%;display:flex;align-items:center;justify-content:center;font-weight:700;font-size:.85rem;color:#fff;background:#1a3a6c;flex-shrink:0}
  </style>
</head>
<body>
<%@ include file="/WEB-INF/views/partials/navbar.jspf" %>
<div class="container-lg py-4">

  <!-- Cabecera -->
  <div class="page-header mb-4 d-flex justify-content-between align-items-center flex-wrap gap-2">
    <div>
      <h5 class="fw-bold mb-1"><i class="bi bi-people-fill me-2"></i>Estudiantes</h5>
      <small class="opacity-75">Total registrados: <strong>${total}</strong></small>
    </div>
    <a href="${pageContext.request.contextPath}/estudiantes/nuevo" class="btn btn-light fw-semibold">
      <i class="bi bi-person-plus-fill me-1"></i>Nuevo Estudiante
    </a>
  </div>

  <!-- Mensajes -->
  <c:if test="${param.msg == 'creado'}">
    <div class="alert alert-success alert-dismissible fade show" role="alert">
      <i class="bi bi-check-circle-fill me-2"></i>Estudiante registrado correctamente.
      <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
    </div>
  </c:if>
  <c:if test="${param.msg == 'actualizado'}">
    <div class="alert alert-info alert-dismissible fade show">
      <i class="bi bi-pencil-fill me-2"></i>Estudiante actualizado correctamente.
      <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
    </div>
  </c:if>
  <c:if test="${param.msg == 'eliminado'}">
    <div class="alert alert-warning alert-dismissible fade show">
      <i class="bi bi-trash-fill me-2"></i>Estudiante eliminado.
      <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
    </div>
  </c:if>

  <!-- Buscador -->
  <div class="card table-card mb-3">
    <div class="card-body p-3">
      <form method="get" action="${pageContext.request.contextPath}/estudiantes" class="d-flex gap-2">
        <input type="search" name="q" value="${busqueda}" class="form-control" placeholder="Buscar por nombre, apellido, código o carrera…" style="border-radius:10px">
        <button type="submit" class="btn btn-primary px-4" style="border-radius:10px"><i class="bi bi-search"></i></button>
        <c:if test="${not empty busqueda}">
          <a href="${pageContext.request.contextPath}/estudiantes" class="btn btn-outline-secondary" style="border-radius:10px"><i class="bi bi-x-lg"></i></a>
        </c:if>
      </form>
    </div>
  </div>

  <!-- Tabla -->
  <div class="card table-card">
    <div class="table-responsive">
      <table class="table table-hover align-middle mb-0">
        <thead>
          <tr>
            <th>Estudiante</th>
            <th>Código</th>
            <th>Carrera</th>
            <th class="text-center">Sem.</th>
            <th>Email</th>
            <th class="text-center">Acciones</th>
          </tr>
        </thead>
        <tbody>
          <c:choose>
            <c:when test="${empty estudiantes}">
              <tr>
                <td colspan="6" class="text-center py-5 text-muted">
                  <i class="bi bi-inbox fs-2 d-block mb-2"></i>
                  <c:choose>
                    <c:when test="${not empty busqueda}">No se encontraron estudiantes para "<strong>${busqueda}</strong>"</c:when>
                    <c:otherwise>No hay estudiantes registrados aún.</c:otherwise>
                  </c:choose>
                </td>
              </tr>
            </c:when>
            <c:otherwise>
              <c:forEach var="est" items="${estudiantes}">
                <tr>
                  <td>
                    <div class="d-flex align-items-center gap-2">
                      <div class="avatar-circle">${est.iniciales}</div>
                      <div>
                        <div class="fw-semibold">${est.nombreCompleto}</div>
                        <small class="text-muted">${est.telefono}</small>
                      </div>
                    </div>
                  </td>
                  <td><span class="badge bg-primary bg-opacity-10 text-primary fw-semibold">${est.codigo}</span></td>
                  <td>${est.carrera}</td>
                  <td class="text-center"><span class="badge bg-secondary">${est.semestre}°</span></td>
                  <td><small>${est.email}</small></td>
                  <td class="text-center">
                    <a href="${pageContext.request.contextPath}/estudiantes/editar/${est.id}" class="btn btn-sm btn-outline-primary me-1" title="Editar">
                      <i class="bi bi-pencil-fill"></i>
                    </a>
                    <a href="${pageContext.request.contextPath}/estudiantes/eliminar/${est.id}"
                       class="btn btn-sm btn-outline-danger"
                       onclick="return confirm('¿Eliminar al estudiante ${est.nombreCompleto}?')"
                       title="Eliminar">
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
