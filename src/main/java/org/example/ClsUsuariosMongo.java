package org.example;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

public class ClsUsuariosMongo {
    private MongoClient mongoClient;
    private MongoDatabase database;
    private MongoCollection <Document> collection;

    public void conexion(){

        //Cadena de conexión, contiene la información de la instalación de mongodb
        ConnectionString connectionString = new ConnectionString("mongodb://localhost:27017");

        //Se crean las configuraciones especificas para conexion y manejo de la db
        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .build();

        //Crea la conexion y establece la comunicacion
        mongoClient = MongoClients.create(settings);

        //busca la base de datos y colección, si no existe, la crea
        database = mongoClient.getDatabase("MiBaseDatos");
        collection = database.getCollection("Usuarios");

        System.out.println("Conexión exitosa");

}
        // crea un usuario
    public void crearUsuario() {
        mdUsuario usuario = new mdUsuario();
        usuario.setNombre("Juan");
        usuario.setCorreo("juanpatito@example.com");
        usuario.setIdTelegram(1234567890);

        Document document = new Document("nombre", usuario.getNombre())
                .append("correo", usuario.getCorreo())
                .append("idTelegram", usuario.getIdTelegram());
        collection.insertOne(document);
        System.out.println("Usuario creado");
    }

        public void leerUsuarios(){
            List<mdUsuario> usuarios = new ArrayList<>();

            for (Document document : collection.find()) {
                mdUsuario u = new mdUsuario();
                u.setIdTelegram(document.getLong("idTelegram"));
                u.setNombre(document.getString("nombre"));
                u.setCorreo(document.getString("correo"));
                usuarios.add(u);

            }
            for (mdUsuario u : usuarios) {
                System.out.println("Nombre: " + u.getNombre());
                System.out.println("Correo: " + u.getCorreo());

                System.out.println("__________________________");
            }
        }
    private  void actualizarUsuario() {
        collection.updateOne(Filters.eq("idTelegram", 88745587),
                Updates.set("correo", "Nuevo@example.com"));
        System.out.println("Usuario actualizado");
    }

    private  void eliminarUsuario() {
        collection.deleteOne(Filters.eq("idTelegram", 9995587));
        System.out.println("Usuario eliminado");
    }

    private  void desconectarMongoDB() {
        // Cerrar la conexión
        mongoClient.close();
        System.out.println("Desconectado de MongoDB");

}



    //buscar usuario por correo
    //returna un registro

    //buscar usuario por correo
    //returna un registro
    private  void buscarUnUsuarioCorreo(String correo) {
        Document doc = collection.find(Filters.eq("correo", correo)).first();
        if (doc != null) {
            System.out.println("Usuario encontrado: " + doc.toJson());
        } else {
            System.out.println("Usuario no encontrado");
        }
    }




    //buscar usuario por correo
    //retorna una lista de registros
    private  List<mdUsuario> buscarUsuariosPorCorreo(String correo) {
        List<mdUsuario> usuarios = new ArrayList<>();
        MongoCursor<Document> cursor = collection.find(Filters.eq("correo", correo)).iterator();

        try {
            while (cursor.hasNext()) {
                Document doc = cursor.next();
                mdUsuario u = new mdUsuario();
                u.setIdTelegram(doc.getLong("idTelegram"));
                u.setNombre(doc.getString("nombre"));
                u.setCorreo(doc.getString("correo"));
                usuarios.add(u);
            }
        } finally {
            cursor.close();
        }

        return usuarios;
}
    }
