<%--
  Created by IntelliJ IDEA.
  User: маня
  Date: 25.05.2022
  Time: 14:32
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>SpaceX page</title>
</head>
<body>

    <section>
        <h2>Register</h2>
        <br>
        <hr/>
        <form name="registerForm" method="post" action="Controller">
            <input type="hidden" name="register" value=""> <br>

            <label for="firstname">First Name: </label><br>
            <input type="text" name="firstname" id="firstname" value=""><br>
            <label for="lastname">Last Name: </label><br>
            <input type="text" name="lastname" id="lastname" value="">
            <label for="email">Email: </label><br>
            <input type="text" name="email" id="email" value="">
            <label for="phone">Phone: </label><br>
            <input type="text" name="phone" id="phone" value="">
            <label for="password">Password: </label><br>
            <input type="text" name="password" id="password" value="">
        </form>
        <hr/>
    </section>

</body>
</html>
