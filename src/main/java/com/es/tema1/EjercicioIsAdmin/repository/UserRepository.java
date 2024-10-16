package com.es.tema1.EjercicioIsAdmin.repository;

import com.es.tema1.EjercicioIsAdmin.Utils.EncryptedUtils;
import com.es.tema1.EjercicioIsAdmin.io.Console;
import com.es.tema1.EjercicioIsAdmin.io.IOutputInterface;
import com.es.tema1.EjercicioIsAdmin.model.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementación del repositorio de usuarios que simula una base de datos en memoria.
 * Proporciona métodos para insertar, obtener, actualizar y eliminar usuarios.
 */
public class UserRepository implements RepositoryInterface<String, User> {

    // Lista de usuarios que simula la base de datos.
    private ArrayList<User> bddUser = new ArrayList<>();
    private IOutputInterface consola = new Console();

    /**
     * Inserta un nuevo usuario en el repositorio.
     *
     * @param x El usuario que se desea insertar.
     * @return El usuario insertado.
     */
    @Override
    public User insert(User x) {
        bddUser.add(x);
        return x;
    }

    /**
     * Obtiene un usuario del repositorio por su clave (ID).
     *
     * @param key La clave del usuario que se desea obtener.
     * @return El usuario encontrado o null si no existe.
     */
    @Override
    public User get(String key) {
        for (User user : bddUser) {
            if (user.getId().equals(key)) {
                return user;
            }
        }
        return null;
    }

    /**
     * Obtiene todos los usuarios del repositorio.
     *
     * @return Una lista de todos los usuarios.
     */
    @Override
    public List<User> getAll() {
        return new ArrayList<>(bddUser);
    }

    /**
     * Actualiza un campo de un usuario específico.
     *
     * @param key             La clave del usuario a actualizar.
     * @param campoACambiar   El campo que se desea cambiar (1: admin, 2: correo, 3: contraseña).
     * @param valorCambiado   El nuevo valor para el campo especificado.
     * @return El usuario actualizado o null si no se encuentra.
     */
    @Override
    public User update(String key, int campoACambiar, String valorCambiado) {
        User user = get(key);
        // Coge al usuario y hace el cambio que haga falta.
        try {
            if (user != null) {
                switch (campoACambiar) {
                    case 1:
                        if (valorCambiado.isEmpty()) {
                            consola.Escribir("No se han cambiado nada");
                        } else {
                            String respuesta = consola.PreguntarString("Quieres ser administrador s/n").toLowerCase();
                            if (respuesta.equals("s")) {
                                user.setAdmin(true);
                                consola.Escribir("Ahora eres admin");
                            } else {
                                user.setAdmin(false);
                                consola.Escribir("Ahora no eres admin");
                            }
                        }
                        break;

                    case 2:
                        if (valorCambiado.isEmpty()) {
                            consola.Escribir("Error, no se ha cambiado nada.");
                        } else {
                            String emailComprobado = consola.ValidarEmail(valorCambiado);
                            if (emailComprobado == null) {
                                consola.Escribir("Error, no se ha cambiado nada");
                            } else {
                                user.setCorreo(emailComprobado);
                            }
                        }
                        break;

                    case 3:
                        if (valorCambiado.isEmpty()) {
                            consola.Escribir("No se ha cambiado nada debido a que se ha introducido un valor vacío.");
                        } else {
                            String pass = EncryptedUtils.encriptador(valorCambiado);
                            user.setEncriptedPassword(pass);
                            consola.Escribir("Contraseña cambiada correctamente");
                        }
                        break;
                }
                return user;
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null; // Si el usuario no existe
    }

    /**
     * Elimina un usuario del repositorio.
     *
     * @param key La clave del usuario que se desea eliminar.
     */
    @Override
    public void delete(String key) {
        User user = get(key);
        if (user != null) {
            bddUser.remove(user);
        }
    }
}
