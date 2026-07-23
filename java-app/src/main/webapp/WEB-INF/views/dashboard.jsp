<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt"  prefix="fmt" %>
<!DOCTYPE html>
<html lang="es">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>Dashboard — Sistema Universitario</title>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
  <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css" rel="stylesheet">
  <style>
    :root {
      --azul:   #1a3a6c;
      --verde:  #1b7a4a;
      --naranja:#c55a11;
      --morado: #6a2c8e;
    }
    body { background: #f0f4f8; font-family: 'Segoe UI', sans-serif; }
    .navbar { background: var(--azul) !important; }
    .navbar-brand { font-weight: 700; letter-spacing: .5px; }
    .stat-card { border-radius: 16px; border: none; transition: transform .2s; box-shadow: 0 4px 18px rgba(0,0,0,.08); }
    .stat-card:hover { transform: translateY(-4px); }
    .stat-icon { width: 60px; height: 60px; border-radius: 50%; display: flex; align-items: center; justify-content: center; font-size: 1.8rem; }
    .stat-val  { font-size: 2.2rem; font-weight: 700; line-height: 1; }
    .stat-lbl  { font-size: .85rem; color: #6c757d; margin-top: 4px; }
    .section-title { font-size: 1rem; font-weight: 700; color: var(--azul); border-left: 4px solid var(--azul); padding-left: 12px; margin-bottom: 1rem; }
    .quick-btn { border-radius: 12px; font-weight: 600; padding: .65rem 1.4rem; }
    .badge-estado { font-size: .75rem; }
  </style>
</head>
<body>

<%@ include file="/WEB-INF/views/partials/navbar.jspf" %>

<div class="container-lg py-4">
  <div class="mb-4">
    <h4 class="fw-bold text-dark mb-1"><i class="bi bi-bar-chart-line-fill me-2" style="color:var(--azul)"></i>Panel Principal</h4>
    <p class="text-muted mb-0">Resumen general del Sistema de Gestión Universitaria</p>
  </div>

  <!-- Tarjetas de estadísticas -->
  <div class="row g-3 mb-4">
    <div class="col-6 col-md-3">
      <div class="card stat-card p-3">
        <div class="d-flex align-items-center gap-3">
          <div class="stat-icon" style="background:#dbeafe"><i class="bi bi-person-fill" style="color:#1d4ed8"></i></div>
          <div>
            <div class="stat-val" style="color:#1d4ed8">${totalEstudiantes}</div>
            <div class="stat-lbl">Estudiantes</div>
          </div>
        </div>
      </div>
    </div>
    <div class="col-6 col-md-3">
      <div class="card stat-card p-3">
        <div class="d-flex align-items-center gap-3">
          <div class="stat-icon" style="background:#dcfce7"><i class="bi bi-person-workspace" style="color:#16a34a"></i></div>
          <div>
            <div class="stat-val" style="color:#16a34a">${totalDocentes}</div>
            <div class="stat-lbl">Docentes</div>
          </div>
        </div>
      </div>
    </div>
    <div class="col-6 col-md-3">
      <div class="card stat-card p-3">
        <div class="d-flex align-items-center gap-3">
          <div class="stat-icon" style="background:#fef9c3"><i class="bi bi-journal-bookmark-fill" style="color:#b45309"></i></div>
          <div>
            <div class="stat-val" style="color:#b45309">${totalCursos}</div>
            <div class="stat-lbl">Cursos</div>
          </div>
        </div>
      </div>
    </div>
    <div class="col-6 col-md-3">
      <div class="card stat-card p-3">
        <div class="d-flex align-items-center gap-3">
          <div class="stat-icon" style="background:#f3e8ff"><i class="bi bi-clipboard-check-fill" style="color:#7c3aed"></i></div>
          <div>
            <div class="stat-val" style="color:#7c3aed">${totalMatriculas}</div>
            <div class="stat-lbl">Matrículas</div>
          </div>
        </div>
      </div>
    </div>
  </div>

  <!-- Métricas académicas -->
  <div class="row g-3 mb-4">
    <div class="col-md-6">
      <div class="card border-0 shadow-sm" style="border-radius:16px">
        <div class="card-body p-4">
          <div class="section-title">Rendimiento Académico</div>
          <div class="d-flex justify-content-between align-items-end mb-2">
            <span class="text-muted">Promedio general</span>
            <span class="fs-3 fw-bold text-primary">${promedioGeneral}</span>
          </div>
          <div class="progress mb-2" style="height:10px; border-radius:99px">
            <div class="progress-bar bg-primary" style="width:${promedioGeneral * 5}%"></div>
          </div>
          <small class="text-muted">Calificación: <strong>${calificacion}</strong></small>
        </div>
      </div>
    </div>
    <div class="col-md-6">
      <div class="card border-0 shadow-sm" style="border-radius:16px">
        <div class="card-body p-4">
          <div class="section-title">Tasa de Aprobación</div>
          <div class="d-flex justify-content-between align-items-end mb-2">
            <span class="text-muted">${matriculasAprobadas} de ${totalMatriculas} matrículas</span>
            <span class="fs-3 fw-bold text-success">${pctAprobacion}%</span>
          </div>
          <div class="progress mb-2" style="height:10px; border-radius:99px">
            <div class="progress-bar bg-success" style="width:${pctAprobacion}%"></div>
          </div>
          <small class="text-muted">Nota mínima aprobatoria: <strong>11.00</strong></small>
        </div>
      </div>
    </div>
  </div>

  <!-- Acciones rápidas -->
  <div class="card border-0 shadow-sm mb-4" style="border-radius:16px">
    <div class="card-body p-4">
      <div class="section-title">Acciones Rápidas</div>
      <div class="d-flex flex-wrap gap-2">
        <a href="${pageContext.request.contextPath}/estudiantes/nuevo" class="btn btn-primary quick-btn">
          <i class="bi bi-person-plus-fill me-1"></i> Nuevo Estudiante
        </a>
        <a href="${pageContext.request.contextPath}/docentes/nuevo" class="btn btn-success quick-btn">
          <i class="bi bi-person-workspace me-1"></i> Nuevo Docente
        </a>
        <a href="${pageContext.request.contextPath}/cursos/nuevo" class="btn btn-warning text-dark quick-btn">
          <i class="bi bi-journal-plus me-1"></i> Nuevo Curso
        </a>
        <a href="${pageContext.request.contextPath}/matriculas/nueva" class="btn quick-btn text-white" style="background:var(--morado)">
          <i class="bi bi-clipboard-plus me-1"></i> Nueva Matrícula
        </a>
      </div>
    </div>
  </div>

  <!-- Footer informativo -->
  <div class="text-center text-muted mt-4 pb-3" style="font-size:.82rem">
    Sistema de Gestión Universitaria &mdash; Evaluación Final POO &bull;
    Java + Servlets + JSP + JDBC/H2
  </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
