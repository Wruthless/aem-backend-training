package com.adobe.backendtraining.core.servlets;


import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.day.cq.wcm.api.PageManagerFactory;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.apache.sling.servlets.annotations.SlingServletResourceTypes;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.IOException;

@Component(service = {Servlet.class})
@SlingServletResourceTypes(
        // Points to the component.
        resourceTypes = TitleSlingServlet.RESOURCE_TYPE,
        selectors = "foobar",
        extensions = "html")
// SlingSafeMethodsServlet is used for application with ready only data.
// This servlet will not support POST, PUT, or DELETE.
public class TitleSlingServlet extends SlingSafeMethodsServlet {

    // TO-DO -- see if optional since not implementing 'Serializable'.
    private static final long serialVersionUID = 1L;

    // resourceTypes = TitleSlingServlet.RESOURCE_TYPE.
    protected static final String RESOURCE_TYPE = "backendtrain/components/title";

    // Retrieve a PageManager instance from the factory Service.
    // PageManager provides methods for page level operations.
    @Reference
    private PageManagerFactory pageManagerFactory;

    @Override
    protected void doGet(
            SlingHttpServletRequest request,
            SlingHttpServletResponse response) throws ServletException, IOException {

        // ResourceResolver resolves incoming request to actual or virtual resources.
        // Gets resources for CRUD operations, and retrieves application managers for object manipulation.
        // Retrieves from request/response.
        PageManager pm = pageManagerFactory.getPageManager(request.getResourceResolver());

        // Added import manually.
        // Uses PageManager to find the containing page of the resource (component).
        Page curPage = pm.getContainingPage(request.getResource());

        // Verify the page exists.
        if (curPage != null && !curPage.getName().equals("master")) {
            String responseStr = "";
            response.setHeader("Content-Type", "text/html");
            responseStr = "<h1>Sling Servlet injected this title on the " + curPage.getName() + " page.</h1>";
            response.getWriter().print(responseStr);
            response.getWriter().close();
        }
    }
}
