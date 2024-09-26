/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controller;

import Config.ConnectionBD;
import Model.AnimalModel;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

@WebServlet("/animal")
public class Animal extends HttpServlet {

    Connection conn;
    PreparedStatement ps;
    Statement statement;
    ResultSet rs;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try ( PrintWriter out = response.getWriter()) {
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet Animal</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet Animal at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Configurar la codificación de la solicitud y la respuesta a UTF-8
        request.setCharacterEncoding("UTF-8");

        ConnectionBD conexion = new ConnectionBD();
        List<AnimalModel> listaAnimales = new ArrayList<>();
        String sql = "SELECT id, color, especie, tipo_animal, tipo_alimento, peso, habitad, altura FROM animales";

        try {
            conn = conexion.getConnectionBD();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                AnimalModel animal = new AnimalModel();
                int idFinal = Integer.parseInt(rs.getString("id"));
                animal.setId(idFinal);
                animal.setColor(rs.getString("color"));
                animal.setEspecie(rs.getString("especie"));
                animal.setTipo_animal(rs.getString("tipo_animal"));
                animal.setTipo_alimento(rs.getString("tipo_alimento"));
                Double pesoFinal = Double.parseDouble(rs.getString("peso"));
                animal.setPeso(pesoFinal);
                animal.setHabitad(rs.getString("habitad"));
                animal.setAltura(rs.getString("altura"));
                listaAnimales.add(animal);
            }

            request.setAttribute("animales", listaAnimales);
            request.getRequestDispatcher("/views/mostrar_animales.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error al obtener los usuarios" + e);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (conn != null && !conn.isClosed()) {
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Configurar la codificación de la solicitud y la respuesta a UTF-8
        request.setCharacterEncoding("UTF-8");

        ConnectionBD conexion = new ConnectionBD();
        String id = request.getParameter("txt_id");
        String color = request.getParameter("txt_color");
        String especie = request.getParameter("txt_especie");
        String tipo_animal = request.getParameter("txt_tipo_animal");
        String tipo_alimento = request.getParameter("txt_tipo_alimento");
        String peso = request.getParameter("txt_peso");
        String habitad = request.getParameter("txt_habitad");
        String altura = request.getParameter("txt_altura");

        int idFinal = Integer.parseInt(id);
        Double pesoFinal = Double.parseDouble(peso);

        try {
            String sql = "INSERT INTO animales (id, color, especie, tipo_animal, tipo_alimento, peso, habitad, altura) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            conn = conexion.getConnectionBD();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, idFinal);
            ps.setString(2, color);
            ps.setString(3, especie);
            ps.setString(4, tipo_animal);
            ps.setString(5, tipo_alimento);
            ps.setDouble(6, pesoFinal);
            ps.setString(7, habitad);
            ps.setString(8, altura);

            int filasInsertadas = ps.executeUpdate();
            if (filasInsertadas > 0) {
                request.setAttribute("mensaje", "Animal registrado con éxito!");
                request.getRequestDispatcher("/views/resultado.jsp").forward(request, response);
            } else {
                request.setAttribute("mensaje", "Error al registrar animal.");
                request.getRequestDispatcher("/views/resultado.jsp").forward(request, response);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("mensaje", "Ocurrió un error: " + e.getMessage());
            request.getRequestDispatcher("/views/resultado.jsp").forward(request, response);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Configurar la codificación de la solicitud y la respuesta a UTF-8
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        ConnectionBD conexion = new ConnectionBD();
        String id = request.getParameter("id");

        if (id == null || id.trim().isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        String sql = "DELETE FROM animales WHERE id like ?";

        try {
            conn = conexion.getConnectionBD();
            ps = conn.prepareStatement(sql);
            ps.setString(1, id);

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                response.setStatus(HttpServletResponse.SC_OK);
            } else {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (conn != null && !conn.isClosed()) {
                    conn.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    // Configurar la codificación de la solicitud y la respuesta a UTF-8
    request.setCharacterEncoding("UTF-8");
    response.setCharacterEncoding("UTF-8");

    ConnectionBD conexion = new ConnectionBD();
    String id = null;
    String color = null;
    String especie = null;
    String tipo_animal = null;
    String tipo_alimento = null;
    String pesoString = null;
    String habitad = null;
    String altura = null;

    try {
        BufferedReader reader = new BufferedReader(new InputStreamReader(request.getInputStream(), StandardCharsets.UTF_8));
        String line;
        StringBuilder sb = new StringBuilder();
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
        reader.close();

        // Decodificamos los parámetros correctamente
        String[] params = sb.toString().split("&");
        for (String param : params) {
            String[] pair = param.split("=");
            if (pair.length == 2) {
                String key = URLDecoder.decode(pair[0], StandardCharsets.UTF_8.name());
                String value = URLDecoder.decode(pair[1], StandardCharsets.UTF_8.name());

                switch (key) {
                    case "id":
                        id = value;
                        break;
                    case "color":
                        color = value;
                        break;
                    case "especie":
                        especie = value;
                        break;
                    case "tipo_animal":
                        tipo_animal = value;
                        break;
                    case "tipo_alimento":
                        tipo_alimento = value;
                        break;
                    case "peso":
                        pesoString = value;
                        break;
                    case "habitad":
                        habitad = value;
                        break;
                    case "altura":
                        altura = value;
                        break;
                }
            }
        }
    } catch (Exception e) {
        e.printStackTrace();
    }

    if (id == null || id.isEmpty()) {
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        response.getWriter().write("Id not found in request body");
        return;
    }

    try {
        conn = conexion.getConnectionBD();

        String sql = "UPDATE animales SET color=?, especie=?, tipo_animal=?, tipo_alimento=?, peso=?, habitad=?, altura=? WHERE id = ?";
        ps = conn.prepareStatement(sql);

        ps.setString(1, color);
        ps.setString(2, especie);
        ps.setString(3, tipo_animal);
        ps.setString(4, tipo_alimento);

        if (pesoString != null) {
            Double pesoFinal = Double.parseDouble(pesoString);
            ps.setDouble(5, pesoFinal);
        } else {
            ps.setNull(5, java.sql.Types.DOUBLE);
        }

        ps.setString(6, habitad);
        ps.setString(7, altura);
        ps.setString(8, id);

        int rowsAffected = ps.executeUpdate();
        if (rowsAffected > 0) {
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().write("Animal updated successfully");
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            response.getWriter().write("Animal not found");
        }
    } catch (Exception e) {
        e.printStackTrace();
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        response.getWriter().write("Error: " + e.getMessage());
    } finally {
        try {
            if (ps != null) {
                ps.close();
            }
            if (conn != null && !conn.isClosed()) {
                conn.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

    @Override
    public String getServletInfo() {
        return "Short description";
    }
}
