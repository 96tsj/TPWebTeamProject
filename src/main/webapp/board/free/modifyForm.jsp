<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>글수정</title>

<link type="text/css" rel="stylesheet" href="css/freeForm.css">

</head>
<body>
	<h1 class="title">글 수정</h1>
	<h4 class="message">자유롭게 글을 수정하실 수 있습니다. &nbsp;&nbsp; <a href="boardDetail.jsp">게시글로 돌아가기</a></h4>
	
	<form action="../../freeBoard" method="post" enctype="application/x-www-form-urlencoded">	
		<input type="hidden" name="method" value="PUT">
		<input type="hidden" name="free_no" value="${freeBoard.freeNo }">
		
		<table class="moditable">		
			<tr>
				<td class="align-left">
				  <p>카테고리 선택 (필수)</p>
        	   	  <select name="free_category" required="required">
            	  	<option value="자유게시판">자유게시판</option>
            	  	<option value="면접/취업">면접/취업</option>
            	  	<option value="이력서/자기소개서">자기소개서/이력서</option>
       		   	  </select>
				</td>
			</tr>
			<tr>
				<td><input type="text" placeholder="제목을 입력해주세요" id="title" name="free_title" required="required" value="${freeBoard.freeTitle }"></td>
			</tr>
			<tr>
				<td><textarea rows="30" cols="100" id="content" placeholder="내용을 입력해 주세요" name="free_content" required="required">${freeBoard.freeContent }</textarea></td>
			</tr>
		</table>
	
		<div class="button">
			<input type="submit" value="수정하기" >
			<input type="reset" value="다시작성">
		</div>
			
	</form>	


</body>
</html>