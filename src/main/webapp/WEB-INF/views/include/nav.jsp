<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<header>
  <nav id="navbar" class="navbar navbar-expand-lg bg-nav shadow py-3">
    <div class="container-fluid mx-5">
      <a class="navbar-brand fw-bold fs-4 mr-5" href="${root}/">TTT</a>
      <button
        class="navbar-toggler"
        type="button"
        data-bs-toggle="collapse"
        data-bs-target="#navbarSupportedContent"
        aria-controls="navbarSupportedContent"
        aria-expanded="false"
        aria-label="Toggle navigation"
      >
        <span class="navbar-toggler-icon"></span>
      </button>

      <div class="collapse navbar-collapse" id="navbarSupportedContent">
        <ul class="navbar-nav me-auto mb-2 mb-lg-0" id="service-list"
        	<c:if test="${empty userInfo}">
        	  style="visibility: hidden;"
        	</c:if>
        >
          <li class="nav-item">
            <a class="nav-link active" aria-current="page" href="${root}/region/attraction">
                       지역별 관광지 검색
            </a>
          </li>
          <li class="nav-item">
            <a class="nav-link active" href="${root}/plan/list?pgno=1&key=&word=">
                       여행경로 보기
            </a>
          </li>
          <li class="nav-item">
            <a class="nav-link active" href="${root}/hotplace/list?pgno=1">
                       핫플레이스 보기
            </a>
          </li>
          <li class="nav-item">
            <a class="nav-link active" href="${root}/board/list?pgno=1&key=&word=">
                    자유 게시판
            </a>
          </li>   
          <li class="nav-item">
            <a class="nav-link active" href="${root}/notice/list?pgno=1&key=&word=">
                    공지
            </a>
          </li>      
        </ul>

		<c:if test="${not empty userInfo}">
		  <div class="navbar-nav mb-2 mb-lg-0">
		    <div>
		  	  <a class="nav-link active">
                ${userInfo.userName}님, 안녕하세요.
              </a>
            </div>
            <div>
              <a class="nav-link active" id="view-user" data-bs-toggle="modal" data-bs-target="#viewModal">
               	회원 정보
              </a>
            </div>
            <div>
		      <a class="nav-link active" href="${root}/user/logout">
                           로그아웃
              </a>
            </div>
		  </div>
		</c:if>
		
		<c:if test="${empty userInfo}">
		  <div class="navbar-nav mb-2 mb-lg-0">
		    <div>
          	  <!-- <a class="nav-link active" id="signin-user" data-bs-toggle="modal" data-bs-target="#signupModal"> -->
          	  <a class="nav-link active" href="${root}/user/login">
                           로그인
              </a>
            </div>
            <div>
		      <!-- <a class="nav-link active" id="signup-user" data-bs-toggle="modal" data-bs-target="#signupModal"> -->
		      <a class="nav-link active" href="${root}/user/join">
                           회원가입
              </a>
            </div>
		  </div>
		</c:if>

        
        <!-- 회원정보 모달 start -->
        <div
          class="modal fade mt-5"
          id="viewModal"
          tabindex="-1"
          aria-labelledby="exampleModalLabel"
          aria-hidden="true"
        >
          <div class="modal-dialog">
            <div class="modal-content">
              <div class="modal-header">
                <h5 class="modal-title" id="exampleModalLabel">회원 정보</h5>
                <button
                  type="button"
                  class="btn-close"
                  data-bs-dismiss="modal"
                  aria-label="Close"
                ></button>
              </div>
              <div class="modal-body">
                <div class="login-container text-center">
                  <div style="display: inline-block; text-align:left; width: 25rem">
                    <div class="row d-flex justify-content-center">
                      <div class="col-10 mb-3">
                        <span>아이디</span>
                        <input
                          type="text"
                          class="form-control my-3 px-3 py-2"
                          id="view-id"
                          name="view-id"
                          value="${userInfo.userId}"
                          readonly
                        />
                      </div>
                    </div>
                    <div class="row d-flex justify-content-center">
                      <div class="col-10 mb-3">
                        <span>이름</span>
                        <input
                          type="text"
                          class="form-control my-3 px-3 py-2"
                          id="view-name"
                          name="view-name"
                          value="${userInfo.userName}"
                          readonly
                        />
                      </div>
                    </div>
                    <div class="row d-flex justify-content-center">
                      <div class="col-10 mb-3">
                        <span>회원 등급</span>
                        <input
                          type="text"
                          class="form-control my-3 px-3 py-2"
                          id="view-grade"
                          name="view-grade"
                          value="${userInfo.grade}"
                          readonly
                        />
                      </div>
                    </div>
                    <div class="row d-flex justify-content-center">
                      <div class="col-10">
                        <span>이메일</span>
                        <input
                          type="text"
                          class="form-control my-3 px-3 py-2"
                          id="view-email-id"
                          name="view-email-id"
                          value="${userInfo.emailId}"
                          readonly
                        />
                        <span>@</span>
                        <input
                          type="text"
                          class="form-control my-3 px-3 py-2"
                          id="view-email-domain"
                          name="view-email-domain"
                          value="${userInfo.emailDomain}"
                          readonly
                        />
                      </div>
                    </div>
                  </div>
                </div>
              </div>
              <div class="modal-footer">
                <!-- <button type="button" class="btn btn-secondary"
                                      data-bs-dismiss="modal">Close</button> -->
                <button type="button" id="delete-btn" class="btn submit-btn me-2">회원 탈퇴</button>
                <button type="button" id="mvupdate-btn" class="btn submit-btn" data-bs-toggle="modal" data-bs-target="#updateModal">내 정보 수정</button>
              </div>
            </div>
          </div>
        </div>
        <!-- 회원정보 모달 end -->
        
        
        <!-- 회원정보 수정 모달 start -->
        <form class="d-flex" id="update-form" method="POST" role="search">
          <div
            class="modal fade mt-5"
            id="updateModal"
            tabindex="-1"
            aria-labelledby="exampleModalLabel"
            aria-hidden="true"
          >
            <div class="modal-dialog">
              <div class="modal-content">
                <div class="modal-header">
                  <h5 class="modal-title" id="exampleModalLabel">회원 정보 수정</h5>
                  <button
                    type="button"
                    class="btn-close"
                    data-bs-dismiss="modal"
                    aria-label="Close"
                  ></button>
                </div>
                <div class="modal-body">
                  <div class="login-container text-center">
                    <div style="display: inline-block; text-align:left; width: 25rem">
                      <input type="hidden" name="action" value="update">
                      <div class="row d-flex justify-content-center">
                        <div class="col-10 mb-3">
                          <span>아이디</span>
                          <input
                            type="text"
                            class="form-control my-3 px-3 py-2"
                            id="update-id"
                            name="update-id"
                            value="${userInfo.userId}"
                            readonly
                          />
                        </div>
                      </div>
                      <div class="row d-flex justify-content-center">
                        <div class="col-10 mb-3">
                          <span>비밀번호</span>
                          <input
                            type="password"
                            class="form-control my-3 px-3 py-2"
                            id="update-password"
                            name="update-password"
                            value=""
                            autoComplete="off"
                          />
                        </div>
                      </div>
                      <div class="row d-flex justify-content-center">
                        <div class="col-10 mb-3">
                          <span>이름</span>
                          <input
                            type="text"
                            class="form-control my-3 px-3 py-2"
                            id="update-name"
                            name="update-name"
                            value="${userInfo.userName}"
                            readonly
                          />
                        </div>
                      </div>
                      <div class="row d-flex justify-content-center">
	                      <div class="col-10 mb-3">
	                        <span>회원 등급</span>
	                        <input
	                          type="text"
	                          class="form-control my-3 px-3 py-2"
	                          id="update-grade"
	                          name="update-grade"
	                          value="${userInfo.grade}"
	                          readonly
	                        />
	                      </div>
             		  </div>
                      <div class="row d-flex justify-content-center">
                        <div class="col-10">
                        <span>이메일</span>
	                        <input
	                          type="text"
	                          class="form-control my-3 px-3 py-2"
	                          id="update-email-id"
	                          name="update-email-id"
	                          value="${userInfo.emailId}"
	                        />
	                        <span>@</span>
	                        <input
	                          type="text"
	                          class="form-control my-3 px-3 py-2"
	                          id="update-email-domain"
	                          name="update-email-domain"
	                          value="${userInfo.emailDomain}"
	                        />
                        </div>
                      </div>
                    </div>
                  </div>
                </div>
                <div class="modal-footer">
                  <!-- <button type="button" class="btn btn-secondary"
                                        data-bs-dismiss="modal">Close</button> -->
                  <button type="button" id="update-btn" class="btn submit-btn">내 정보 수정</button>
                </div>
              </div>
            </div>
          </div>
        </form>
        <!-- 회원정보 수정 모달 end -->
        
        
          
      </div>
    </div>
  </nav>
</header>