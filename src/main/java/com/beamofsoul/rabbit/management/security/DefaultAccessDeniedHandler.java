package com.beamofsoul.rabbit.management.security;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import com.beamofsoul.rabbit.management.util.HttpServletRequestUtils;

@Component
public class DefaultAccessDeniedHandler implements AccessDeniedHandler {

		/* (non-Javadoc) 
	     * @see org.springframework.security.web.access.AccessDeniedHandler#handle(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, org.springframework.security.access.AccessDeniedException) 
	     */
		private String errorPage;

		// ~ Methods
		// ========================================================================================================
		@Override
		public void handle(HttpServletRequest request, HttpServletResponse response,
				AccessDeniedException accessDeniedException) throws IOException,
				ServletException {
			
			if (!response.isCommitted()) {
			
				/**
				 * Determine whether current request is from AJax.
				 * Solve the wrong log print that "Request method 'DELETE/GET/PUT...' not supported" when authenticated user does not have enough permission to do things.
				 * Just make an 403 exception clear enough.
				 */
				if (HttpServletRequestUtils.isAjaxRequest(request)) {
					response.setStatus(HttpStatus.FORBIDDEN.value());
				} else {
					if (errorPage != null) {
						// Put exception into request scope (perhaps of use to a view)
						request.setAttribute(WebAttributes.ACCESS_DENIED_403,
								accessDeniedException);

						// Set the 403 status code.
						response.setStatus(HttpStatus.FORBIDDEN.value());

						// forward to error page.
						RequestDispatcher dispatcher = request.getRequestDispatcher(errorPage);
						dispatcher.forward(request, response);
					}
					else {
						response.sendError(HttpStatus.FORBIDDEN.value(),
							HttpStatus.FORBIDDEN.getReasonPhrase());
					}
				}
			}
		}

		/**
		 * The error page to use. Must begin with a "/" and is interpreted relative to the
		 * current context root.
		 *
		 * @param errorPage the dispatcher path to display
		 *
		 * @throws IllegalArgumentException if the argument doesn't comply with the above
		 * limitations
		 */
		public void setErrorPage(String errorPage) {
			if ((errorPage != null) && !errorPage.startsWith("/")) {
				throw new IllegalArgumentException("errorPage must begin with '/'");
			}

			this.errorPage = errorPage;
		}
}
