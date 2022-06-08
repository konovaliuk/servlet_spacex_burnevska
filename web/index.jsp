<%--
  Created by IntelliJ IDEA.
  User: маня
  Date: 24.05.2022
  Time: 14:27
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <title>SpaceX</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}components/main.css">

</head>
<body>
    <%@ include file="WEB-INF/jsp/components/head.jsp"%>
    <c:if test="${sessionScope.user ne null}">
        <div>
            <img src="${pageContext.request.contextPath}/WEB-INF/jsp/components/imgs/background.png" alt="">
            <a id="create mission button" href="${pageContext.request.contextPath}/WEB-INF/jsp/create_mission.jsp">
                CREATE MISSION
            </a>
        </div>
    </c:if>
    <div>
        <iframe width="1080" src="https://www.youtube.com/embed/TeVbYCIFVa8"
                title="YouTube video player" frameborder="0"
                allow="accelerometer; autoplay; clipboard-write;
                encrypted-media; gyroscope; picture-in-picture"
                allowfullscreen>
        </iframe>
    </div>
    <%@ include file="/WEB-INF/jsp/components/footer.jsp"%>
</body>
</html>
