package org.arojas.appmockito.ejemplos.repositories;

import org.arojas.appmockito.ejemplos.models.Examen;

import java.util.List;

public interface ExamenRepository {
    Examen guardar(Examen examen);
    List<Examen> findAll();
}
