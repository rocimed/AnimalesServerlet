<%-- 
    Document   : mostrar_animal
    Created on : 24/09/2024, 01:16:56 PM
    Author     : romar
--%>

<%@page import="java.util.ArrayList"%>
<%@page import="Model.AnimalModel"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <title>Lista de Animales</title>
        <style>
            table {
                width: 80%;
                border-collapse: collapse;
            }
            table, th, td {
                border: 1px solid black;
            }
            th, td {
                padding: 10px;
                text-align: left;
            }
            th {
                background-color: #f2f2f2;
            }
        </style>
        <script>
            function eliminarAnimal(id) {
                console.log(`eliminarAnimal?id=` + id);
                if (confirm("¿Estás seguro de que quieres eliminar este animal?")) {
                    fetch(`animal?id=` + id, {
                        method: 'DELETE'
                    }).then(response => {
                        if (response.ok) {
                            alert('Animal eliminado exitosamente');
                            location.reload();
                        } else {
                            alert('Error al eliminar animal');
                        }
                    }).catch(error => console.error('Error:', error));
                }
            }
        </script>

    </head>
    <body>
        <h2>Listado de los Animales</h2>
        <a href="views/registro_animal.jsp">Registrar un nuevo animal</a><h1></h1>
        <table>
            <thead>
                <tr>
                    <th>ID</th>
                    <th>COLOR</th>
                    <th>ESPECIE</th>
                    <th>TIPO DE ANIMAL</th>
                    <th>TIPO DE ALIMENTO</th>
                    <th>PESO</th>
                    <th>HABITAD</th>
                    <th>ALTURA</th>
                </tr>
            </thead>
            <tbody>
                <%
                    ArrayList<AnimalModel> listaAnimales = (ArrayList<AnimalModel>) request.getAttribute("animales");

                    if (listaAnimales != null && !listaAnimales.isEmpty()) {
                        for (AnimalModel animal : listaAnimales) {
                %>
                <tr>
                    <td><%= animal.getId()%></td>
                    <td><%= animal.getColor()%></td>
                    <td><%= animal.getEspecie()%></td>
                    <td><%= animal.getTipo_animal()%></td>
                    <td><%= animal.getTipo_alimento()%></td>
                    <td><%= animal.getPeso() %></td>
                    <td><%= animal.getHabitad() %></td>
                    <td><%= animal.getAltura() %></td>
                    
                    <td> <button onclick="eliminarAnimal(<%= animal.getId()%>)">Eliminar</button> </td>
                    <td>
                        <!-- Botón para actualizar, que redirige a actualizarUsuario.jsp con el ID del animal --> 
                        <form action="${pageContext.request.contextPath}/views/actualizar_animal.jsp" method="GET"> 
                            <input type="hidden" name="id" value="<%= animal.getId()%>"> 
                            <input type="submit" value="Actualizar"> 
                        </form> 
                    </td>

                </tr>
                <%
                    }
                } else {
                %>
                <tr>
                    <td colspan="7">No hay aniamles registrados.</td>
                </tr>
                <%
                    }
                %>
            </tbody>
        </table>
    </body>
</html>
