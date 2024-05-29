package edu.unizg.foi.nwtis.dkermek.filter;

import java.io.IOException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebFilter(urlPatterns = "/")
public class RootRedirector extends HttpFilter {

  private static final long serialVersionUID = -7521137248331149073L;

  @Override
  protected void doFilter(final HttpServletRequest req, final HttpServletResponse res,
      final FilterChain chain) throws IOException {
    res.sendRedirect("mvc");
  }
}
