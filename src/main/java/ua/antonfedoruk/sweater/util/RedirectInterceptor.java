package ua.antonfedoruk.sweater.util;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//this class fixes redirection trouble with Turbolinks
public class RedirectInterceptor extends HandlerInterceptorAdapter {
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        if (modelAndView != null) {
            //this string contain URL arguments which stand after the question mark; as arguments aren't obvious we should add check
            String args = request.getQueryString() != null ? ("?" + request.getQueryString()) : "";

            //get redirection url
            String url = request.getRequestURI().toString() + args;

            //add header with necessary  url
            response.setHeader("Turbolinks-Location", url);
        }
    }
}
