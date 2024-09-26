<%-- 
    Document   : resultado
    Created on : 24/09/2024, 01:17:32 PM
    Author     : romar
--%>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%> 
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8"> 
        <title>Resultado del Registro</title> 
    </head>
    <body>
        <h2>Resultado</h2> 
        <p><%= request.getAttribute("mensaje")%></p> 
        <a href="views/registro_animal.jsp">Regresar al formulario</a> 
    </body>
</html>
