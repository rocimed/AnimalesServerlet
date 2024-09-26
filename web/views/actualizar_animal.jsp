<%-- 
    Document   : actualizar_animal
    Created on : 24/09/2024, 01:16:39 PM
    Author     : romar
--%>

<%@page import="Config.ConnectionBD"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%> 

<%@ page import="java.sql.Connection, java.sql.DriverManager, java.sql.PreparedStatement, java.sql.ResultSet" %> 

<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8"> 
        <title>Actualizar animal</title>
    </head>
    <body>
        <h2>Actualizar animal</h2> 
        <%
            String id = request.getParameter("id");
            String color = "";
            String especie = "";
            String tipo_animal = "";
            String tipo_alimento = "";
            Double peso = null;
            String habitad = "";
            String altura = "";
            
            ConnectionBD conexion = new ConnectionBD();
            Connection connection = conexion.getConnectionBD();
            PreparedStatement statement = null;
            ResultSet resultSet = null;
            try {

                // Consulta para obtener los datos del usuario por ID 
                String sql = "SELECT color, especie, tipo_animal, tipo_alimento, peso, habitad, altura"
                        + " FROM animales WHERE id LIKE ?";
                statement = connection.prepareStatement(sql);
                statement.setString(1, id);
                resultSet = statement.executeQuery();
                if (resultSet.next()) {
                    color = resultSet.getString("color");
                    especie = resultSet.getString("especie");
                    tipo_animal = resultSet.getString("tipo_animal");
                    tipo_alimento = resultSet.getString("tipo_alimento");
                    peso = resultSet.getDouble("peso");
                    habitad = resultSet.getString("habitad");
                    altura = resultSet.getString("altura");
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (resultSet != null) {
                        resultSet.close();
                    }
                    if (statement != null) {
                        statement.close();
                    }
                    if (connection != null) {
                        connection.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        %> 

        <!-- Formulario con los datos del usuario para actualizar --> 
        <form id="formActualizarAnimal"> 
            ID <br>
            <input disabled type="number" id="txt_id" value="<%= id%>"><br>
            COLOR <br>
            <input type="text" id="txt_color" value="<%= color%>"><br>
            ESPECIE <br>
            <input type="text" id="txt_especie" value="<%= especie%>"><br>
            TIPO_ANIMAL <br>
            <input type="text" id="txt_tipo_animal" value="<%= tipo_animal%>"><br>
            TIPO_ALIMENTO <br>
            <input type="text" id="txt_tipo_alimento" value="<%= tipo_alimento%>"><br>
            PESO <br>
            <input type="number" step="0.01" id="txt_peso" value="<%= peso%>"><br>
            HABITAD <br>
            <input type="text" id="txt_habitad" value="<%= habitad%>"><br> 
            ALTURA <br>
            <input type="text" id="txt_altura" value="<%= altura%>"><br>
            <input type="button" value="Actualizar" onclick="actualizarAnimal()"><br>
            <a href="${pageContext.request.contextPath}/animal">Regresar a la tabla de animales</a>
        </form> 
        <div id="resultado"></div> 
        <script>
            function actualizarAnimal() {
                const id = document.getElementById("txt_id").value;
                const color = document.getElementById("txt_color").value;
                const especie = document.getElementById("txt_especie").value;
                const tipo_animal = document.getElementById("txt_tipo_animal").value;
                const tipo_alimento = document.getElementById("txt_tipo_alimento").value;
                const peso = document.getElementById("txt_peso").value;
                const habitad = document.getElementById("txt_habitad").value;
                const altura = document.getElementById("txt_altura").value;

                const datos = new URLSearchParams();
                datos.append("id", id);
                datos.append("color", color);
                datos.append("especie", especie);
                datos.append("tipo_animal", tipo_animal);
                datos.append("tipo_alimento", tipo_alimento);
                datos.append("peso", peso);
                datos.append("habitad",habitad);
                datos.append("altura", altura);
                console.log("Datos:" + datos );
                
                var urlParams = new URLSearchParams(window.location.search);
                var id22 = urlParams.get("id");
                console.log("ID  " + id22);
                
                fetch("${pageContext.request.contextPath}/animal?id="+id, {
                    method: "PUT",
                    body: datos,
                    headers: {
                        'Content-Type': 'application/json; charset=UTF-8'
                    }
                })
                        .then(response => {
                    if (response.ok) {
                        return response.text(); // O .json() si envías JSON
                    } else {
                        throw new Error('Error en la actualización');
                    }
                })
                .then(data => {
                    console.log(data);
                    document.getElementById("resultado").innerText =  "Animal actualizado exitosamente";
                    
                    //alert('Animal actualizado exitosamente');
                    // Redireccionar a la página /animalesServelet/animal
                    //window.location.href = "${pageContext.request.contextPath}/animal";
                })
                .catch(error => {
                    document.getElementById("resultado").innerText = "Error al actualizar usuario.";
                    console.error('Error:', error);
                    //alert('Ocurrió un error durante la actualización');
                });
            }

        </script> 
    </body>
</html>
