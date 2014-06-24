<%@ page import="org.mili.multisim.plugins.plinga.model.User" %>
<%@ page import="org.mili.multisim.plugins.plinga.servlet.PlingaServlet" %>
<html>

<%
    User user = PlingaServlet.getLoggedUser();
%>

<head>
    <title>Plinga (sim)</title>

    <script type="text/javascript" src="//ajax.googleapis.com/ajax/libs/jquery/1.7.2/jquery.js"></script>

    <script type="text/javascript" src="<%= PlingaServlet.WEB_URL %>/plinga.js"></script>

</head>

<body>

<h1>Sim-Plinga (Game) <%= user != null ? user.getUid() : "NO USER LOGGED ON!" %></h1>

<%
%>

<iframe id="plinga" name="plinga" src="<%= PlingaServlet.getIFrameUrl(user) %>" width="970" height="800" marginwidth="0" marginheight="0" frameborder="0" scrolling="no" allowtransparency="true"></iframe>

</body>

</html>