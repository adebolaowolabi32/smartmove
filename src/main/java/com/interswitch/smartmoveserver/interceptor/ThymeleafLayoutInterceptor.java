package com.interswitch.smartmoveserver.interceptor;

import com.interswitch.smartmoveserver.annotation.Layout;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author adebola.owolabi
 */
public class ThymeleafLayoutInterceptor extends HandlerInterceptorAdapter {

    private static final String DEFAULT_LAYOUT = "layouts/default";
    private static final String DEFAULT_VIEW_ATTRIBUTE_NAME = "view";

    private String defaultLayout = DEFAULT_LAYOUT;
    private String viewAttributeName = DEFAULT_VIEW_ATTRIBUTE_NAME;

    public void setDefaultLayout(String defaultLayout) {
        this.defaultLayout = defaultLayout;
    }

    public void setViewAttributeName(String viewAttributeName) {
        this.viewAttributeName = viewAttributeName;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        if (modelAndView == null || !modelAndView.hasView()) {
            return;
        }

        String originalViewName = modelAndView.getViewName();
        if (originalViewName == null || isRedirectOrForward(originalViewName)) {
            return;
        }

        Layout layout = getMethodOrTypeAnnotation(handler);
        if (layout == null) {
            return;
        }

        String layoutName = getLayoutName(layout);
        if (Layout.NONE.equals(layoutName)) {
            return;
        }

        if (layoutName.isEmpty()) {
            layoutName = defaultLayout;
        }

        modelAndView.setViewName(layoutName);
        modelAndView.addObject(this.viewAttributeName, originalViewName);
    }

    private boolean isRedirectOrForward(String viewName) {
        return viewName.startsWith("redirect:") || viewName.startsWith("forward:");
    }

    private String getLayoutName(Layout layout) {
        if (layout != null) {
            return layout.value();
        }
        return defaultLayout;
    }

    private Layout getMethodOrTypeAnnotation(Object handler) {
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Layout layout = handlerMethod.getMethodAnnotation(Layout.class);
            if (layout == null) {
                layout = handlerMethod.getBeanType().getAnnotation(Layout.class);
            }
            return layout;
        }
        return null;
    }
}
