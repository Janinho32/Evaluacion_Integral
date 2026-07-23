package edu.universidad.model;

/**
 * Interfaz IEvaluable — define el contrato para entidades que pueden ser
 * evaluadas académicamente. Aplica el principio de abstracción mediante
 * interfaces.
 */
public interface IEvaluable {

    /**
     * Calcula el promedio de notas de la entidad.
     * @return promedio como double
     */
    double calcularPromedio();

    /**
     * Retorna la categoría académica según el promedio.
     * @return string con la categoría: Excelente, Bueno, Regular, Deficiente
     */
    String getCategoria();

    /**
     * Indica si la entidad aprueba (promedio >= umbral mínimo).
     * @return true si aprueba
     */
    boolean aprueba();
}
