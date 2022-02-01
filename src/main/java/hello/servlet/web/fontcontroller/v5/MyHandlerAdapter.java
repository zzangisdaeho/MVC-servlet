package hello.servlet.web.fontcontroller.v5;

import hello.servlet.web.fontcontroller.ModelView;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface MyHandlerAdapter {

    // 어뎁터가 핸들러를 받고 자신이 핸들할 수 있는 객체인지 확인
    boolean supports(Object handler);

    // 어뎁터가 중간에서 핸들러를 사용. 후에 프론트 컨트롤러가 원하는 결과값으로 돌려준다.
    ModelView handle(HttpServletRequest request, HttpServletResponse response, Object handler) throws ServletException, IOException;
}
