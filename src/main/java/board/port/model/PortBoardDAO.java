package board.port.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class PortBoardDAO {
	
	DataSource dataSource;
	
	public PortBoardDAO() {
		try {
			Context context= new InitialContext();
			Context envContext= (Context) context.lookup("java:/comp/env");
			dataSource= (DataSource) envContext.lookup("jdbc/oracle");
			
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}// 생성자 //
	
	// 게시물 추가
	public void insertPort(PortBoardVO vo) {
		
		try {
			Connection conn= dataSource.getConnection();
			
			String sql= "INSERT INTO PORT_BOARD(PORT_NO, PORT_TITLE, PORT_CONTENT, USER_ID) VALUES(SEQ_PORT_NO.NEXTVAL,?,?,?)";
			
			PreparedStatement pstmt= conn.prepareStatement(sql);
			pstmt.setString(1, vo.getPortTitle());
			pstmt.setString(2, vo.getPortContent());
			pstmt.setString(3, vo.getUserId());
			
			pstmt.executeUpdate();
			pstmt.close();
			conn.close();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}// 게시물 추가 //
	
	
	// 게시물 이미지 저장
	public void insertPortImg(List<PortBoardImgVO> imgList) {
		
		try {
			Connection conn= dataSource.getConnection();
			
			String sql= "INSERT INTO PORT_BOARD_IMG VALUES(SEQ_IMG_NO.NEXTVAL, ?,?)";
			
			PreparedStatement pstmt= conn.prepareStatement(sql);
			
			for(PortBoardImgVO vo : imgList) {
				pstmt.setInt(1, vo.getPortNo());
				pstmt.setString(2, vo.getImgPath());
				
				pstmt.executeUpdate();
			}
			
			pstmt.close();
			conn.close();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	} // 게시물 이미지 저장 //
	
	
	// 전체 게시물 리스트 검색
	public List<PortBoardVO> getPortList() {
		
		List<PortBoardVO> boardList= new ArrayList<PortBoardVO>();
		
		try {
			Connection conn= dataSource.getConnection();
			
			String sql= "SELECT * FROM PORT_BOARD ORDER BY PORT_NO DESC";
			PreparedStatement pstmt= conn.prepareStatement(sql);
			ResultSet rs= pstmt.executeQuery();
			while(rs.next()) {
				PortBoardVO vo= new PortBoardVO();
				vo.setPortNo(rs.getInt("PORT_NO"));
				vo.setPortTitle(rs.getString("PORT_TITLE"));
				vo.setPortContent(rs.getString("PORT_CONTENT"));
				vo.setUserId(rs.getString("USER_ID"));
				vo.setPortDate(rs.getDate("PORT_DATE"));
				vo.setPortView(rs.getInt("PORT_VIEW"));
				
				boardList.add(vo);
				
			}
			rs.close();
			pstmt.close();
			conn.close();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return boardList;
	}// 전체 게시물 리스트 검색 //
	
	
	// 상세 게시물 검색
	public PortBoardVO getPort(PortBoardVO vo) {
		
		PortBoardVO port= null;
		
		try {
			Connection conn= dataSource.getConnection();
			
			String sql= "SELECT * FROM PORT_BOARD WHERE PORT_NO=?";
			PreparedStatement pstmt= conn.prepareStatement(sql);
			pstmt.setInt(1, vo.getPortNo());
			
			
			ResultSet rs= pstmt.executeQuery();
			if(rs.next()) {
				port= new PortBoardVO();
				port.setPortNo(rs.getInt("PORT_NO"));
				port.setPortTitle(rs.getString("PORT_TITLE"));
				port.setPortContent(rs.getString("PORT_CONTENT"));
				port.setUserId(rs.getString("USER_ID"));
				port.setPortDate(rs.getDate("PORT_DATE"));
				port.setPortView(rs.getInt("PORT_VIEW"));

			}
			rs.close();
			pstmt.close();
			conn.close();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return port;
	}// 상세 게시물 검색 //
	
	
	// 특정 회원의 게시물만 가져오기
	public List<PortBoardVO> getUserPort(PortBoardVO vo){
		
		List<PortBoardVO> boardList= new ArrayList<PortBoardVO>();
		
		try {
			Connection conn= dataSource.getConnection();
			
			String sql= "SELECT PB.*, PBI.IMG_NO, PBI.IMG_PATH "
					+ "FROM PORT_BOARD PB "
					+ "LEFT JOIN ( "
					+ "  SELECT PORT_NO, MIN(IMG_NO) AS MIN_IMG_NO "
					+ "  FROM PORT_BOARD_IMG "
					+ "  GROUP BY PORT_NO "
					+ ") MIN_IMG ON PB.PORT_NO = MIN_IMG.PORT_NO "
					+ "LEFT JOIN PORT_BOARD_IMG PBI ON MIN_IMG.MIN_IMG_NO = PBI.IMG_NO "
					+ "WHERE PB.USER_ID = ? "
					+ "ORDER BY PB.PORT_NO DESC";
			
			PreparedStatement pstmt= conn.prepareStatement(sql);
			pstmt.setString(1, vo.getUserId());
			
			ResultSet rs= pstmt.executeQuery();
			while(rs.next()) {
				PortBoardVO vo1= new PortBoardVO();
				vo1.setPortNo(rs.getInt("PORT_NO"));
				vo1.setPortTitle(rs.getString("PORT_TITLE"));
				vo1.setPortContent(rs.getString("PORT_CONTENT"));
				vo1.setUserId(rs.getString("USER_ID"));
				vo1.setPortDate(rs.getDate("PORT_DATE"));
				vo1.setPortView(rs.getInt("PORT_VIEW"));
				
				PortBoardImgVO vo2= new PortBoardImgVO();
				vo2.setImgNo(rs.getInt("IMG_NO"));
				vo2.setImgPath(rs.getString("IMG_PATH"));
				
				vo1.setPortImg(vo2);
				
				boardList.add(vo1);

				
			}
			rs.close();
			pstmt.close();
			conn.close();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return boardList;
	}
	
		
	
	// 해당 게시물의 이미지들 가져오기
	public List<PortBoardImgVO> getPortImgs(PortBoardVO vo){
		
		List<PortBoardImgVO> portImgList= new ArrayList<PortBoardImgVO>();
		
		try {
			Connection conn= dataSource.getConnection();
			
			String sql= "SELECT * FROM PORT_BOARD_IMG WHERE PORT_NO=? ORDER BY IMG_NO";
			
			PreparedStatement pstmt= conn.prepareStatement(sql);
			pstmt.setInt(1, vo.getPortNo());
			
			ResultSet rs= pstmt.executeQuery();
			while(rs.next()) {
				PortBoardImgVO img= new PortBoardImgVO();
				img.setImgNo(rs.getInt("IMG_NO"));
				img.setPortNo(rs.getInt("PORT_NO"));
				img.setImgPath(rs.getString("IMG_PATH"));
				
				portImgList.add(img);
			}
			
			rs.close();
			pstmt.close();
			conn.close();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return portImgList;
	}// 해당 게시물의 이미지들 가져오기 //
	
	
	// 해당 이미지 가져오기
	public PortBoardImgVO getImg(PortBoardImgVO vo) {
		
		PortBoardImgVO img= null;
		
		try {
			Connection conn= dataSource.getConnection();
			
			String sql= "SELECT * FROM PORT_BOARD_IMG WHERE IMG_NO=?";
			
			PreparedStatement pstmt= conn.prepareStatement(sql);
			pstmt.setInt(1, vo.getImgNo());
			
			ResultSet rs= pstmt.executeQuery();
			if(rs.next()) {
				img= new PortBoardImgVO();
				img.setImgNo(rs.getInt("IMG_NO"));
				img.setPortNo(rs.getInt("PORT_NO"));
				img.setImgPath(rs.getString("IMG_PATH"));
			}
			
			rs.close();
			pstmt.close();
			conn.close();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return img;
	}// 해당 이미지 가져오기 //
	
	
	
	
	
	
	
	
	
	
	
}
