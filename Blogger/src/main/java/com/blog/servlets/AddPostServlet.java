package com.blog.servlets;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import com.blog.dao.PostDao;
import com.blog.entities.Post;
import com.blog.entities.User;
import com.blog.helper.ConnectionProvider;
import com.blog.helper.Helper;

import jakarta.servlet.ServletException;
import jakarta.servlet.ServletContext; // Import ServletContext
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;


@MultipartConfig
public class AddPostServlet extends HttpServlet {


    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {

            int cid = Integer.parseInt(request.getParameter("cid"));
            String pTitle = request.getParameter("pTitle");
            String pContent = request.getParameter("pContent");
            String pCode = request.getParameter("pCode");
            Part part = request.getPart("pic");
            
            // Getting current user
            HttpSession session = request.getSession();
            User user = (User) session.getAttribute("currentUser");

            // Create Post object
            Post p = new Post(pTitle, pContent, pCode, part.getSubmittedFileName(), null, cid, user.getId());
            PostDao dao = new PostDao(ConnectionProvider.getConnection());

            if (dao.savePost(p)) {
                // Corrected usage of getRealPath
                ServletContext context = request.getServletContext();
                String realPath = context.getRealPath("/");
                String uploadPath = realPath + "blog_pics" + File.separator + part.getSubmittedFileName();

                // Save the file
                boolean isFileSaved = Helper.saveFile(part.getInputStream(), uploadPath);
                if (isFileSaved) {
                    out.println("Post added successfully.");
                } else {
                    out.println("Failed to save the post image.");
                }
            } else {
                out.println("Error while adding the post.");
            }

        }
    }


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }


    @Override
    public String getServletInfo() {
        return "AddPostServlet handles adding new blog posts.";
    }
}
