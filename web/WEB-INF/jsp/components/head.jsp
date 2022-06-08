<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<link rel="stylesheet" href="${pageContext.request.contextPath}/WEB-INF/jsp/components/main.css">
<header>
    <div id="header">

        <a href="${pageContext.request.contextPath}/index.jsp" id="logo">
            <img src="${pageContext.request.contextPath}/WEB-INF/jsp/components/imgs/logo.png" alt="SpaceX"></a>
        
        <c:if test="${sessionScope.user eq null}">
            <nav>
                <a href="${pageContext.request.contextPath}/index.jsp">Home</a>
                <a href="${pageContext.request.contextPath}/WEB-INF/jsp/mission.jsp">Missions</a>
                <a href="${pageContext.request.contextPath}/WEB-INF/jsp/user.jsp">Profile</a>
            </nav>
            <div id=signIn><a href="${pageContext.request.contextPath}/signin.jsp">Sign in</a></div>
            <div id=signUp><a href="${pageContext.request.contextPath}/register.jsp"></a>Sign up</div>
        </c:if>
        <c:if test="${sessionScope.user ne null}">
            <nav>
                <a href="${pageContext.request.contextPath}/index.jsp">Home</a>
                <a href="${pageContext.request.contextPath}/WEB-INF/jsp/mission.jsp">Missions</a>
                <a href="${pageContext.request.contextPath}/WEB-INF/jsp/user.jsp">Profile</a>
            </nav>
            <div id=signOut><a href="${pageContext.request.contextPath}/index.jsp"></a>Sign out</div>

        </c:if>
        <c:if test="${sessionScope.user ne null and sessionScope.user.role == 2}">
                <div id=admin><a href="${pageContext.request.contextPath}/WEB-INF/jsp/admin.jsp"></a>Admin</div>
        </c:if>
    </div>
</header>
