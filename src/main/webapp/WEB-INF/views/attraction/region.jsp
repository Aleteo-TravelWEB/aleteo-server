<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <%@ include file="/WEB-INF/views/include/head.jsp" %>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
    <link rel="stylesheet" href="../assets/css/main.css">

	<style>
      .bg-nav {
      	background-color: #7895B2;
      }
  	</style>
</head>
<body>
    <%@ include file="/WEB-INF/views/include/nav.jsp" %>

<div class='container'>
    <!-- 관광지 검색 하는 영역 start -->
    <form id="search-form" class="d-flex my-3" onsubmit="return false;" role="search" method="POST">
    	<input type="hidden" id="sidoCode" name="sidoCode">
    	<input type="hidden" id="gugunCode" name="gugunCode">
    	<input type="hidden" id="contentTypeId" name="contentTypeId">
        <!-- 지역 리스트 -->
        <select id="search-sido" class="form-select me-2">
            <option value="0" selected>검색 할 지역 선택</option>
        <c:forEach var="sido" items="${sidos}">
            <option value="${sido.sidoCode}">${sido.sidoName}</option>
        </c:forEach>
        </select>
        <!-- 구/군 리스트 -->
        <select id="search-gugun-id" class="form-select me-2">
            <option value="0" >구/군</option>
        </select>
        <!-- 관광지 유형 리스트 -->
        <select id="search-content-id" class="form-select me-2">
            <option value="0" selected>관광지 유형</option>
            <option value="12">관광지</option>
            <option value="14">문화시설</option>
            <option value="15">축제공연행사</option>
            <option value="25">여행코스</option>
            <option value="28">레포츠</option>
            <option value="32">숙박</option>
            <option value="38">쇼핑</option>
            <option value="39">음식점</option>
        </select>
        <button id="btn-search" class="btn submit-btn" type="button" style='width: 10em'>검색</button>
    </form>
    <!-- kakao map start -->
    <div id="map" class="mt-3" style="width: 100%; height: 550px"></div>
    <!-- kakao map end -->
</div>
<!-- 관광지 검색 end -->


<script type="text/javascript"
        src="//dapi.kakao.com/v2/maps/sdk.js?appkey=31b399e99305a76bd610e0ad347aacd4&libraries=services,clusterer,drawing"></script>
<script type="text/javascript" src="../assets/js/key.js"></script>
<script type="text/javascript" src="../assets/js/region.js"></script>

<script>
	document.querySelector("#navbar").classList.add("navbar-dark");

	document.getElementById("btn-search").addEventListener("click", function() {
		let sido = document.querySelector("#search-sido");
		let gugun = document.querySelector("#search-gugun-id");
		let content = document.querySelector("#search-content-id")
		
		let sidoCode = sido.options[sido.selectedIndex].value;
		let gugunCode = gugun.options[gugun.selectedIndex].value;
		let contentTypeId = content.options[content.selectedIndex].value;

		console.log(sidoCode + " " + gugunCode + " " + contentTypeId);
		
		if(sidoCode == 0)
			alert("검색 지역을 선택하세요");
		else if(gugunCode == 0)
			alert("구/군을 선택하세요.");
		else if(contentTypeId == 0){
			let form = document.querySelector("#search-form");
			form.setAttribute("action", "${root}/attraction/listall");
			form.submit();
		}			
		else {
			let form = document.querySelector("#search-form");
			form.setAttribute("action", "${root}/attraction/list");
			form.submit();
		}
	});

	<c:if test="${not empty attractions}">
		$("#search-sido").val("${param.sidoCode}").prop("selected", true);
		$("#search-content-id").val("${param.contentTypeId}").prop("selected", true);
		initSubAreaOption("${param.sidoCode}", "${param.gugunCode}");
		
		let positions = [];
		<c:forEach var="area" items="${attractions}">
			markerInfo = {
				title: "${area.title}",
				latlng: new kakao.maps.LatLng("${area.latitude}", "${area.longitude}"),
				image: "${area.first_image}",
				addr: "${area.addr1}",
				zipcode: "${area.zipcode}",
				tel: "${area.tel}"
			};
			positions.push(markerInfo);
		</c:forEach>
		
		if(positions.length > 0)
			displayMarker(positions);
	</c:if>
	
	
</script>
<%@ include file="/WEB-INF/views/include/footer.jsp" %>
</body>
</html>
