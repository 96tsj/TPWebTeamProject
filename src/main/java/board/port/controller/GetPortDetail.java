package board.port.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import board.port.model.PortBoardService;
import board.port.model.PortBoardVO;

public class GetPortDetail extends HttpServlet{
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		req.setCharacterEncoding("utf-8");
		resp.setContentType("text/html; charset=utf-8");
		
		PortBoardVO vo= new PortBoardVO();
		vo.setPortNo(Integer.parseInt(req.getParameter("port_no")));
		
		PortBoardService service= new PortBoardService();
		PortBoardVO port= service.getPort(vo);
		List<PortBoardVO> userPort= service.getUserPort(vo);
		
		HttpSession session= req.getSession();
		session.setAttribute("port", port);
		session.setAttribute("userPort", userPort);
		
		System.out.println(userPort.get(0).getPortImg().getImgNo());
		
		resp.sendRedirect("board/port/portDetail.jsp");
	}
	

}
