<%@ page import="org.mili.multisim.plugins.plinga.PlingaPlugin" %>
<%@ page import="org.mili.multisim.plugins.plinga.model.User" %>
<%@ page import="org.mili.multisim.plugins.plinga.servlet.PlingaServlet" %>
<html>

<head>
    <title>Plinga (sim)</title>
</head>

<body>

<h1>Sim-Plinga (Payment)</h1>

<%
    User user = PlingaServlet.getLoggedUser();
%>

<a href="<%=PlingaServlet.PLINGA_URL%>/?simaction=payment&id=12000&coins=75&price=5&type=SMS&userid=<%= user.getUid() %>">Bill 75 coins...</a>

</body>

</html>